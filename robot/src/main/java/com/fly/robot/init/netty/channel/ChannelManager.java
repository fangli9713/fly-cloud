package com.fly.robot.init.netty.channel;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 我叫大宝
 * @date 2018-07-25 21:00
 * @description 通信的所有channel的管理器
 */
public enum ChannelManager {
    instance;
    private Map<String, ChannelSession> sessionMap = new ConcurrentHashMap<>();

    public static ChannelManager getInstance() {
        return instance;
    }

    public void addSession(Channel channel) {
        if (channel == null) return;
        ChannelSession session = new ChannelSession(channel);
        ChannelSession oldSession = sessionMap.get(session.getChannelId());
        if (oldSession != null) {
            sessionMap.remove(session.getChannelId());
        }
        sessionMap.put(session.getChannelId(), session);
    }

    public ChannelSession getChannelBySessionId(String sessionId) {
        ChannelSession session = sessionMap.get(sessionId);
        return session;
    }

    public void removeSession(String sessionId) {
        sessionMap.remove(sessionId);
    }

}
