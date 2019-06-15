package com.fly.robot.init.netty.websocket;

import com.alibaba.fastjson.JSON;
import com.fly.common.core.convert.DataResultBuild;
import com.fly.common.dto.BaseMsg;
import com.fly.common.dto.BaseResult;
import com.fly.common.util.http.HttpUtil;
import com.fly.robot.init.netty.NettyConfig;
import com.fly.robot.init.netty.dto.BaseMsgOuterClass;
import com.fly.robot.init.netty.dto.BaseResultOuterClass;
import com.fly.robot.vo.Result;
import com.fly.robot.vo.RobotRequest;
import com.fly.robot.vo.RobotResponse;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
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

	public static final String apiUrl = "http://openapi.tuling123.com/openapi/api/v2";

	public static String talk(String word) {
		if(StringUtils.isEmpty(word)){
			return null;
		}
		RobotRequest request = new RobotRequest();
		final RobotRequest.Perception perception = request.getPerception();
		perception.inputText.put("text",word);
		try {

			final String s = HttpUtil.doJsonRquest(apiUrl, null, JSON.toJSONString(request));
			System.out.println("收到消息"+s);
			RobotResponse robotResponse = JSON.parseObject(s, RobotResponse.class);
			List<Result> results = robotResponse.getResults();
			if(!CollectionUtils.isEmpty(results)){
				Result result = results.get(0);
				Object res = result.getValues().get("text");
				if(!StringUtils.isEmpty(res)){
					return res.toString();
				}

			}
//            {"emotion":{"robotEmotion":{"a":0,"d":0,"emotionId":0,"p":0},"userEmotion":{"a":0,"d":0,"emotionId":21500,"p":0}},"intent":{"actionName":"","code":10004,"intentName":""},"results":[{"groupType":1,"resultType":"text","values":{"text":"特朗普是最爱发推特的总统。"}}]}
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;

	}

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
