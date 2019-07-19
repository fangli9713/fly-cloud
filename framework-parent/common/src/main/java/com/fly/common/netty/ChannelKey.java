package com.fly.common.netty;


import org.springframework.util.StringUtils;

public class ChannelKey {

    
    private String uid;
    private String role;
    private String os;
    
    public static final String ROLE_USER = "1"; 
    public static final String ROLE_PROVIDER = "2"; 
    public static final String ROLE_DRIVER = "3"; 
    
    public ChannelKey(String uid,String role,String os) {
        if(StringUtils.isEmpty(uid) || StringUtils.isEmpty(role) || StringUtils.isEmpty(os)){
            throw new NullPointerException("param is invaild");
        }
        this.uid = uid;
        this.role = role;
        this.os = os;
    }
    
    public String getChannelKey(){
        return uid+","+role+","+os;
    }
    
    
    @Override
    public String toString() {
        return getChannelKey();
    }

    public boolean checkChannelKey(String channelKey){
        if(StringUtils.isEmpty(channelKey)){
            return false;
        }
        String [] list = channelKey.split(",");
        if(list.length != 3){
            return false;
        }
        return true;
    }
    
    public String getUid(String channelKey){
        if(!checkChannelKey(channelKey)){
            return null;
        }
        String [] list = channelKey.split(",");
        return list[0];
    }
    
    public String getRole(String channelKey){
        if(!checkChannelKey(channelKey)){
            return null;
        }
        String [] list = channelKey.split(",");
        return list[1];
    }
}
