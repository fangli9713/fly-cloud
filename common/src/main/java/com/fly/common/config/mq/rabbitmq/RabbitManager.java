package com.fly.common.config.mq.rabbitmq;

import com.fly.common.config.mq.rabbitmq.model.RabbitMessage;
import com.fly.common.config.mq.rabbitmq.model.SenderType;
import com.fly.common.core.convert.DataResultBuild;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.util.Assert;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Base64;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created at 2018/12/04 by Yun
 */
@Slf4j
public class RabbitManager {
    private RabbitAdmin admin;
    private CachingConnectionFactory connectionFactory;
    private RabbitProperties properties;
    private RabbitTemplate template;
    @Getter
    private MessageCache messageCache;
    private RetryStrategy retryStrategy;

    private RabbitTemplate.ConfirmCallback confirmCallback;
    private RabbitTemplate.ReturnCallback returnCallback;
    private ConcurrentHashMap<String, MessageSender> senderHolder = new ConcurrentHashMap<>();

    public RabbitManager(RabbitProperties properties, MessageCache messageCache, RetryStrategy retryStrategy) {
        Assert.notNull(properties, "'Properties' must not be null");
        Assert.notNull(messageCache, "'MessageCache' must not be null");
        Assert.notNull(retryStrategy, "'RetryStrategy' must not be null");
        this.properties = properties;
        this.messageCache = messageCache;
        this.retryStrategy = retryStrategy;
        this.connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses(properties.getAddresses());
        connectionFactory.setHost(properties.getHost());
        connectionFactory.setPort(properties.getPort());
        connectionFactory.setUsername(properties.getUsername());
        connectionFactory.setPassword(properties.getPassword());
        initial();
    }

    public RabbitManager(RabbitProperties properties, MessageCache messageCache, RetryStrategy retryStrategy,
                         CachingConnectionFactory connectionFactory) {
        Assert.notNull(properties, "'Properties' must not be null");
        Assert.notNull(messageCache, "'MessageCache' must not be null");
        Assert.notNull(retryStrategy, "'RetryStrategy' must not be null");
        Assert.notNull(connectionFactory, "'ConnectionFactory' must not be null");
        this.properties = properties;
        this.connectionFactory = connectionFactory;
        this.messageCache = messageCache;
        this.retryStrategy = retryStrategy;
        initial();
    }

    public RabbitManager(RabbitProperties properties, MessageCache messageCache, RetryStrategy retryStrategy,
                         CachingConnectionFactory connectionFactory, RabbitTemplate.ConfirmCallback confirmCallback) {
        Assert.notNull(properties, "'Properties' must not be null");
        Assert.notNull(messageCache, "'MessageCache' must not be null");
        Assert.notNull(retryStrategy, "'RetryStrategy' must not be null");
        Assert.notNull(connectionFactory, "'ConnectionFactory' must not be null");
        this.properties = properties;
        this.messageCache = messageCache;
        this.retryStrategy = retryStrategy;
        this.connectionFactory = connectionFactory;
        this.confirmCallback = confirmCallback;

        initial();
    }

    private void initial() {
        connectionFactory.setPublisherConfirms(true);
        admin = new RabbitAdmin(connectionFactory);
        this.template = admin.getRabbitTemplate();
        this.template.setMandatory(true);

        if (!(this.properties.isPublisherConfirms() && this.confirmCallback != null))
            this.confirmCallback = getDefaultConfirmCallback();

        if (!(this.properties.getTemplate().getMandatory() == Boolean.TRUE && this.returnCallback != null))
            this.returnCallback = getDefaultReturnCallback();

        template.setConfirmCallback(confirmCallback);
        template.setReturnCallback(returnCallback);

    }

    public RabbitAdmin getRabbitAdmin() {
        return this.admin;
    }

    public RabbitTemplate getRabbitTemplate() {
        return this.template;
    }

    RabbitTemplate.ConfirmCallback getDefaultConfirmCallback() {
        return (correlationData, ack, cause) -> {
            if (!ack) {
                if (correlationData != null)
                    log.error("send message failed: " + cause + correlationData.toString());
                else
                    log.error("send message failed: " + cause);
                // TODO: retry
            } else {
                if (correlationData != null)
                    messageCache.confirmedMessage(correlationData.getId());
            }
        };
    }

    RabbitTemplate.ReturnCallback getDefaultReturnCallback() {
        return (Message message, int replyCode, String replyText, String exchange, String routingKey) -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                log.error("Default return callback thread sleep error: " + e.getMessage());
            }

            RabbitMessage originMsg = (RabbitMessage) deserialize(message.getBody());

            if (retryStrategy.tryAgain(originMsg.getId())) {
                messageCache.needConfirmMessage(originMsg);
                template.convertAndSend(exchange, routingKey, message,
                        new CorrelationData(originMsg != null ? originMsg.getId() : null));
            } else {
                messageCache.sendFailedMessage(originMsg);
                log.error("Send message failed: " + replyCode + " " + replyText + ", to ex: " + exchange + ", rk: "
                        + routingKey + ", msgId: " + originMsg.getId());
            }
        };
    }

    public MessageSender buildMessageSender(final String exchange, final String routingKey) {
        return (message) -> {
            String id = generateCorId();
            long time = System.currentTimeMillis();
            RabbitMessage msg = new RabbitMessage(id, time, exchange, routingKey, message);
            messageCache.needConfirmMessage(msg);

            try {
                template.convertAndSend(exchange, routingKey, msg, new CorrelationData(id));
            } catch (AmqpException e) {
                log.error("RabbitMessage send failed: " + e.getMessage());
                return DataResultBuild.fail("RabbitMessage send failed");
            }
            return DataResultBuild.success("RabbitMessage send success");
        };
    }

    /**
     * declare queue and exchange, and binding
     *
     * @param type       exchange type
     * @param exchange   exchange name
     * @param routingKey route key
     * @param queue      String[] queue name
     */
    public void bindExchangeAndQueue(SenderType type, String exchange, String routingKey, String... queue) {
        if (SenderType.TOPIC.equals(type)) {
            TopicExchange te = new TopicExchange(exchange, true, false, null);
            admin.declareExchange(te);

            for (String q : queue) {
                Queue qo = new Queue(q, true, false, false, null);
                admin.declareQueue(qo);
                admin.declareBinding(BindingBuilder.bind(qo).to(te).with(routingKey));
            }
        } else if (SenderType.FANOUT.equals(type)) {
            FanoutExchange fe = new FanoutExchange(exchange, true, false, null);

            for (String q : queue) {
                Queue qo = new Queue(q, true, false, false, null);
                admin.declareQueue(qo);
                admin.declareBinding(BindingBuilder.bind(qo).to(fe));
            }

        } else if (SenderType.HEADERS.equals(type)) {
            HeadersExchange he = new HeadersExchange(exchange, true, false, null);
            admin.declareExchange(he);

            for (String q : queue) {
                Queue qo = new Queue(q, true, false, false, null);
                admin.declareQueue(qo);
                admin.declareBinding(BindingBuilder.bind(qo).to(he).where(routingKey).exists());
            }
        } else {
            DirectExchange de = new DirectExchange(exchange, true, false, null);
            admin.declareExchange(de);

            for (String q : queue) {
                Queue qo = new Queue(q, true, false, false, null);
                admin.declareQueue(qo);
                admin.declareBinding(BindingBuilder.bind(qo).to(de).with(routingKey));
            }
        }
    }

    /**
     * 根据线程ID + 时间戳 + [0, 123456789)内随机数，生成唯一值
     *
     * @return String id
     */
    public String generateCorId() {
        String random = String.valueOf(Thread.currentThread().getId()) + System.currentTimeMillis() + new Random()
                .nextInt(123456789);
        return Base64.getEncoder().encodeToString(random.getBytes());
    }

    private Object deserialize(byte[] bytes) {
        ByteArrayInputStream bais = null;
        try {
            // 反序列化
            bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("bytes Could not deserialize:" + e.toString());
            return null;
        } finally {
            try {
                if (bais != null) {
                    bais.close();
                }
            } catch (IOException ex) {
                System.out.println("LogManage Could not serialize:" + ex.toString());
            }
        }
    }

    /**
     * 获取消息发送者
     *
     * @param exchange exchange name
     * @param routeKey route key
     * @return
     */
    public MessageSender getSender(String exchange, String routeKey) {
        MessageSender ms;
        // key = exchange.routeKey
        ms = senderHolder.get(exchange + "." + routeKey);
        if (ms != null)
            return ms;

        ms = this.buildMessageSender(exchange, routeKey);
        senderHolder.put(exchange + "." + routeKey, ms);
        return ms;
    }
}
