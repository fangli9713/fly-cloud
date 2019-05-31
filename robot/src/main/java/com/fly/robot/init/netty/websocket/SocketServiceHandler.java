package com.fly.robot.init.netty.websocket;

import com.alibaba.fastjson.JSON;
import com.fly.robot.init.netty.NettyConfig;
import com.fly.robot.init.netty.dto.BaseMsg;
import com.fly.robot.init.netty.dto.BaseMsgOuterClass;
import com.fly.robot.init.netty.dto.BaseResult;
import com.fly.robot.init.netty.dto.BaseResultOuterClass;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


@Service("socketServiceHandler")
public class SocketServiceHandler {


	private static Map<String,Method> methods;
	private static final SocketServiceHandler instance=new SocketServiceHandler();

	public static class SingletonHolder{

		private static SocketServiceHandler singleton = new SocketServiceHandler();
	}
	public static SocketServiceHandler newInstance() {
		return SocketServiceHandler.SingletonHolder.singleton;
	}

	private SocketServiceHandler(){
		methods=new HashMap<String,Method>();
		Method[] ms=getClass().getMethods();
		for(Method m:ms){
			Class<?>[] parc=m.getParameterTypes();
			if(parc!=null&&parc.length==2
					&&parc[0].equals(ChannelHandlerContext.class)
					&&parc[1].equals(BaseMsgOuterClass.BaseMsg.class)
					&&boolean.class.equals(m.getReturnType())){
				methods.put(m.getName(), m);
			}
		}
	}
	
	public static void handle(ChannelHandlerContext ctx, Object obj) throws Exception{
		BaseMsg msg = (BaseMsg)obj;
		String token = msg.getToken();
		Method method=methods.get(msg.getMethod());

	    String channelKey = "";
		NettyConfig.channelMap.put(channelKey, ctx);
		try {
			final Object invoke = method.invoke(SocketServiceHandler.getInstance(), new Object[]{ctx, msg});

		}catch (Exception e){

		}
		System.out.println("handle 完毕");
	}
	
	public static SocketServiceHandler getInstance(){
		return instance;
	}
	
	public static BaseResult createBaseResult(int code, String info, String method){
		BaseResult newBuilder = new BaseResult();
        newBuilder.setCode(0);
        newBuilder.setData(info);
        newBuilder.setMethod(method);
        return newBuilder;
	}

	public static TextWebSocketFrame finalResult(int code, String info, String method){
		return new TextWebSocketFrame(JSON.toJSONString(createBaseResult(code,info,method)));
	}
	public static void writeChannel(Channel channel, int code, String info, String method){
		channel.writeAndFlush(finalResult(code, info, method));
	}
	
	/**-----------------------------------------具体的业务实现-------------------------------------------**/
	
	/**
	 * 接收合作方上传的入场记录
	 * @return
	 */
	public static boolean uploadEnterInfo(ChannelHandlerContext ctx, BaseMsgOuterClass.BaseMsg msg){
		Map<String,Object> returnMap = new HashMap<String,Object>();
		returnMap.put("return_code", 0);
		returnMap.put("return_des", "成功");
			String jsonData = msg.getInfo();
			BaseResultOuterClass.BaseResult.Builder newBuilder = BaseResultOuterClass.BaseResult.newBuilder();
			newBuilder.setCode(0);
			newBuilder.setData(JSON.toJSONString(returnMap));
			newBuilder.setMethod(msg.getMethod());
			ctx.writeAndFlush(newBuilder);
			return true;
	}
	
	
	
	
	
	
	
}
