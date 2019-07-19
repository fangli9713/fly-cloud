package com.fly.common.netty.protobuf;

import com.alibaba.fastjson.JSON;
import com.fly.common.netty.NettyConfig;
import com.fly.common.netty.dto.BaseMsgOuterClass;
import com.fly.common.netty.dto.BaseResultOuterClass;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


@Service("protoBufServiceHandler")
public class ProtoBufServiceHandler {


	private static Map<String,Method> methods;
	private static final ProtoBufServiceHandler instance=new ProtoBufServiceHandler();

	public static class SingletonHolder{

		private static ProtoBufServiceHandler singleton = new ProtoBufServiceHandler();
	}
	public static ProtoBufServiceHandler newInstance() {
		return ProtoBufServiceHandler.SingletonHolder.singleton;
	}

	private ProtoBufServiceHandler(){
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
		BaseMsgOuterClass.BaseMsg msg = (BaseMsgOuterClass.BaseMsg)obj;
		Method method=methods.get(msg.getMethod());
		try {
			final Object invoke = method.invoke(ProtoBufServiceHandler.getInstance(), new Object[]{ctx, msg});
		}catch (Exception e){

		}
		System.out.println("handle 完毕");
	}
	
	public static ProtoBufServiceHandler getInstance(){
		return instance;
	}
	
	public static BaseResultOuterClass.BaseResult.Builder createBaseResult(int code, String info, String method){
	    BaseResultOuterClass.BaseResult.Builder newBuilder = BaseResultOuterClass.BaseResult.newBuilder();
        newBuilder.setCode(0);
        newBuilder.setData(info);
        newBuilder.setMethod(method);
        return newBuilder;
	}
	public static void writeChannel(Channel channel, int code, String info, String method){
		channel.writeAndFlush(createBaseResult(code, info, method));
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
