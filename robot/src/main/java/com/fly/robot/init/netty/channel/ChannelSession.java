package com.fly.robot.init.netty.channel;

import io.netty.channel.Channel;

/**
 * @author 
 * @date 2018-07-25 20:21
 * @description 通信channel数据
 */
public class ChannelSession {
    private String channelId;//channel的id，netty4把id从Long改成string
    private Channel channel;//通信的channel

    public ChannelSession(Channel channel) {
        this.channel = channel;
        channelId = channel.id().asLongText();
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
