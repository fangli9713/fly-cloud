package com.fly.common.netty.protobuf;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.Date;

/**
  * @description: 状态监听 心跳由客户端发起,因此服务端不需要用到这个
  * @author fanglinan
  * @date 2019/5/30
  */
@Deprecated
public class ProtoBufServerIdleStateTrigger extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                // 在规定时间内没有收到客户端的上行数据, 主动断开连接
                System.out.println(JSON.toJSONString(new Date())+"准备发送心跳"+ctx.name());
                ProtoBufServiceHandler.writeChannel(ctx.channel(),0,"心跳","heart");
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
    
}
