package com.fly.common.util.http;

public interface HttpResponseDataCallback<R> extends HttpResponseCallback<R> {

	/**
	 * 
	 * @param data
	 * @param offset
	 * @param length
	 * @return true 继续处理，后面的响应字节，false不在继续处理后续的数据
	 */
	public boolean callback(byte[] data, int offset, int length);

	
}