package com.fly.common.util.http;

import java.util.List;
import java.util.Map;

public class DefaultHttpResponse implements HttpResponse{
	private Map<String, List<String>> headers;
	private int responseCode;
	private String responseMessage;
	private long contentLength;
	private String contentEncoding;
	private String contentType;
	public DefaultHttpResponse(Map<String, List<String>> headers,int responseCode,String responseMessage,
			long contentLength,String contentEncoding,String contentType){
		this.headers=headers;
		this.responseCode=responseCode;
		this.responseMessage=responseMessage;
		this.contentLength=contentLength;
		this.contentEncoding=contentEncoding;
		this.contentType=contentType;
	}

	@Override
	public Map<String, List<String>> getHeaders() {
		return headers;
	}

	@Override
	public int getResponseCode() {
		return responseCode;
	}

	@Override
	public String getResponseMessage() {
		return responseMessage;
	}

	@Override
	public long getContentLength() {
		return contentLength;
	}

	@Override
	public String getContentEncoding() {
		return contentEncoding;
	}

	@Override
	public String getContentType() {
		return contentType;
	}
	
	
	
}
