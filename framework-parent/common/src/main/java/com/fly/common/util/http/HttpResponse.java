package com.fly.common.util.http;

import java.util.List;
import java.util.Map;

public interface HttpResponse {

	public Map<String,List<String>> getHeaders();
	
	public int getResponseCode();
	
	public String getResponseMessage();
	
	public long getContentLength();
	
	public String getContentEncoding();
	
	public String getContentType();
	
	
	
		
}
