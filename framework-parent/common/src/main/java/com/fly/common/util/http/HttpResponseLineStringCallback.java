package com.fly.common.util.http;

public interface HttpResponseLineStringCallback<R> extends HttpResponseCallback<R>{

	/**
	 * 当读取到一行数据返回
	 * @param line
	 * @return true 继续处理，后面的响应字节，false不在继续处理后续的数据
	 */
	public boolean callback(String line);
	
	
}
