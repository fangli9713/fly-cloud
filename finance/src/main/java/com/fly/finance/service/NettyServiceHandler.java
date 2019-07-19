package com.fly.finance.service;

import com.alibaba.fastjson.JSON;
import com.fly.common.netty.NettyConfig;
import com.fly.common.netty.client.NettyClientProtoBufHandler;
import com.fly.common.netty.dto.BaseMsgOuterClass;
import com.fly.common.netty.protobuf.ProtoBufServiceHandler;
import com.fly.finance.util.SpringContext;
import com.fly.finance.vo.RecommendVO;
import io.netty.channel.ChannelHandlerContext;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NettyServiceHandler {
    private static Map<String, Method> methods;

    private static final NettyServiceHandler instance=new NettyServiceHandler();

    public static class SingletonHolder{

        private static NettyServiceHandler singleton = new NettyServiceHandler();
    }
    public static NettyServiceHandler getInstance() {
        return NettyServiceHandler.SingletonHolder.singleton;
    }


    public NettyServiceHandler() {
        methods = new HashMap<String, Method>();
        Method[] ms = getClass().getMethods();
        for (Method m : ms) {
            Class<?>[] parc = m.getParameterTypes();
            if (parc != null && parc.length == 2
                    && parc[0].equals(ChannelHandlerContext.class)
                    && parc[1].equals(BaseMsgOuterClass.BaseMsg.class)
                    && boolean.class.equals(m.getReturnType())) {
                methods.put(m.getName(), m);
            }
        }
    }

    public static void handle(ChannelHandlerContext ctx, Object obj) throws Exception {
        BaseMsgOuterClass.BaseMsg msg = (BaseMsgOuterClass.BaseMsg) obj;
        String token = msg.getToken();
        Method method = methods.get(msg.getMethod());
        if(method == null){
            return;
        }
        String channelKey = "";
        NettyConfig.channelMap.put(channelKey, ctx);
        try {
            final Object invoke = method.invoke(ProtoBufServiceHandler.getInstance(), new Object[]{ctx, msg});

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("handle 完毕");
    }

    /**
     * 查询今日推荐
     * @param ctx
     * @param baseMsg
     * @return
     */
    public static boolean today(ChannelHandlerContext ctx, BaseMsgOuterClass.BaseMsg baseMsg) {
        AshareRecommendService service = SpringContext.getBean(AshareRecommendService.class);
        final List<RecommendVO> list = service.selectTodayRecommend();
        ProtoBufServiceHandler.writeChannel(ctx.channel(),0, JSON.toJSONString(list),baseMsg.getMethod());
        return true;
    }
}
