package com.fly.common.util.http;

public interface HttpResponseCallback<R> {

	/**
	 * 接收内容信息前调用
	 * @param response 
	 * @return true,接收响应内容, false 不接收收响应内容
	 */
	public boolean onHeaders(HttpResponse response);
	
	/**
	 * 当响应完成没有异常时调用
	 */
	public R onComplete();
	
	/**
	 * 当响应异常时调用
	 * @param ex
	 */
	public void onException(Throwable ex);

	
}
