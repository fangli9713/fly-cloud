package com.fly.common.util.http;

import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

@Slf4j
public class HttpUtil {


	public static String doJsonRquest(String requestUrl, Map<String, ?> headMap,String postJson) throws Exception{
		return doRequest(requestUrl, headMap, null, null,postJson,new HttpResponseLineStringCallback<String>(){
            StringBuilder sbr=new StringBuilder();
			@Override
			public String onComplete() {
				return sbr.toString();
			}

			@Override
			public void onException(Throwable ex) {
				
			}

			@Override
			public boolean callback(String line) {
				if(line!=null&&(line=line.trim()).length()>0){
					sbr.append(line.trim());
				}
				return true;
			}
			
			@Override
			public boolean onHeaders(HttpResponse response) {
				return true;
			}
			
		},10000,20000);
	}
	
	public static String doXmlRquest(String requestUrl, Map<String, ?> headMap,String postXML) throws Exception{
		return doRequest(requestUrl, headMap, null, postXML,null,new HttpResponseLineStringCallback<String>(){
            StringBuilder sbr=new StringBuilder();
			@Override
			public String onComplete() {
				return sbr.toString();
			}

			@Override
			public void onException(Throwable ex) {
				
			}

			@Override
			public boolean callback(String line) {
				if(line!=null&&(line=line.trim()).length()>0){
					sbr.append(line.trim());
				}
				return true;
			}
			
			@Override
			public boolean onHeaders(HttpResponse response) {
				return true;
			}
			
		},10000,20000);
	}
	
	public static String doRequest(String requestUrl, Map<String, ?> headMap, 
			Map<String, ?> postParam) throws Exception{
		return doRequest(requestUrl, headMap, postParam, null,null,new HttpResponseLineStringCallback<String>(){
            StringBuilder sbr=new StringBuilder();
			@Override
			public String onComplete() {
				return sbr.toString();
			}

			@Override
			public void onException(Throwable ex) {
				
			}

			@Override
			public boolean callback(String line) {
				if(line!=null&&(line=line.trim()).length()>0){
					sbr.append(line.trim());
				}
			    return true;
			}

			@Override
			public boolean onHeaders(HttpResponse response) {
				return true;
			}
			
		},10000,20000);
				
	}
	
	/**
	 * 同步Http请求
	 * @param requestUrl https或者http url，不能为null
	 * @param headMap 请求的头信息 ,可以为null , map.entry.value 可以是Collection,String[],String
	 * @param postParam POST 参数 此参数如果不为null，postJson 或postXML 必须为null，3者之间选一，或者都为null get
	 * @param postXML xmlpost，此参数如果不为null，postParam 或postJson 必须为null，3者之间选一，或者都为null get
	 * @param postJson json post，此参数如果不为null，postParam 或postXML 必须为null，3者之间选一，或者都为null get
	 * @param callback 如果为null 返回null
	 * @param connectTimout 单位毫秒,连接超时 如果<=0使用默认
	 * @param readTimeout 单位毫秒,读超时 如果<=0使用默认
	 * @return 该方法阻塞
	 */
	public static <R> R doRequest(String requestUrl, Map<String, ?> headMap, 
			Map<String, ?> postParam, String postXML,String postJson, 
			HttpResponseCallback<R> callback,
			int connectTimout,
			int readTimeout) throws Exception{
		log.info(requestUrl+"?"+postJson);
		HttpURLConnection httpUrlConn=null;
		R result=null;
		try{
			
			if(requestUrl.startsWith("https://")){
				TrustManager[] tm = { new SimpleX509TrustManager() };
				SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
				sslContext.init(null, tm, new java.security.SecureRandom());
				SSLSocketFactory ssf = sslContext.getSocketFactory();
				URL url = new URL(requestUrl);
				Proxy proxy=HttpProxy.getProxy(requestUrl);
				
				HttpsURLConnection httpUrlConn1;
				if(proxy==null){
					httpUrlConn1= (HttpsURLConnection) url.openConnection();
				}else{
					httpUrlConn1= (HttpsURLConnection) url.openConnection(proxy);
				}
				
				httpUrlConn1.setSSLSocketFactory(ssf);
				httpUrlConn=httpUrlConn1;
			}else{
				URL url = new URL(requestUrl);
				Proxy proxy=HttpProxy.getProxy(requestUrl);
				if(proxy==null){
					httpUrlConn = (HttpURLConnection) url.openConnection();
				}else{
					httpUrlConn = (HttpURLConnection) url.openConnection(proxy);
				}
				
			}
			
			if(headMap!=null&&!headMap.isEmpty()){
				for(Map.Entry<String, ?> e:headMap.entrySet()){
					Object ao=e.getValue();
					if(ao==null){
						continue;
					}
					
					if(ao instanceof String[]){
						String[] a=(String[])ao;
						for(String a1:a){
							httpUrlConn.addRequestProperty(e.getKey(), a1);
						}
					}else if(ao instanceof Collection){
						Collection<?> a=(Collection<?>)ao;
						for(Object a1:a){
							httpUrlConn.addRequestProperty(e.getKey(), a1.toString());
						}
					}else{
						httpUrlConn.setRequestProperty(e.getKey(), ao.toString());
					}
				}
			}
			
			if(connectTimout>0){
				httpUrlConn.setConnectTimeout(connectTimout);
			}
			
			if(readTimeout>0){
				httpUrlConn.setReadTimeout(readTimeout);
			}
			
			httpUrlConn.setUseCaches(false);
			String charset="UTF-8";
			if(postParam!=null&&!postParam.isEmpty()){
				httpUrlConn.setRequestMethod("POST");
				httpUrlConn.setDoOutput(true);
				PrintWriter out=new PrintWriter(httpUrlConn.getOutputStream());
				boolean first=true;
				for(Map.Entry<String, ?> pair:postParam.entrySet()){
					Object a0=pair.getValue();
					if(a0==null){
						continue;
					}
					
					if(a0 instanceof String[]){
						String[] a=(String[])a0;
						for(String a1:a){
							if(first){
								first=false;
							}else{ 
								out.print('&');
							}
							
							out.print(pair.getKey());
							out.print('=');
							out.print(URLEncoder.encode(a1, charset));
						}
					}else if(a0 instanceof Collection){
						Collection<?> a=(Collection<?>)a0;
						for(Object a1:a){
							if(first){
								first=false;
							}else{ 
								out.print('&');
							}
							
							out.print(pair.getKey());
							out.print('=');
							out.print(URLEncoder.encode(a1.toString(), charset));
						}
					}else{
						if(first){
							first=false;
						}else{ 
							out.print('&');
						}
						
						out.print(pair.getKey());
						out.print('=');
						out.print(URLEncoder.encode(a0.toString(), charset));
					}
				}
				out.close();
			}else if(postXML!=null){
				httpUrlConn.setRequestProperty("Content-Type", "text/xml");
				httpUrlConn.setRequestMethod("POST");
				httpUrlConn.setDoOutput(true);
				OutputStream out=httpUrlConn.getOutputStream();
				out.write(postXML.getBytes("UTF-8"));
				out.close();
			}else if(postJson!=null){
				httpUrlConn.setRequestProperty("Content-Type", "application/json");
				httpUrlConn.setRequestMethod("POST");
				httpUrlConn.setDoOutput(true);
				OutputStream out=httpUrlConn.getOutputStream();
				//System.out.println(postJson);
				out.write(postJson.getBytes("UTF-8"));
				out.close();
			}
			
			httpUrlConn.connect();
			
			boolean obtainContext=true;
			if(callback!=null){
				obtainContext=callback.onHeaders(new DefaultHttpResponse(httpUrlConn.getHeaderFields(), httpUrlConn.getResponseCode(), httpUrlConn.getResponseMessage(), httpUrlConn.getContentLengthLong(),httpUrlConn.getContentEncoding(),httpUrlConn.getContentType()));
			}
			
			if(obtainContext){
				InputStream inputStream =null;
				try{
					inputStream = httpUrlConn.getInputStream();
					if(callback instanceof HttpResponseDataCallback){
						HttpResponseDataCallback<R> ak=(HttpResponseDataCallback<R>)callback;
						byte[] data=new byte[1024];
						int len;
						while((len = inputStream.read(data)) != -1){
							if(len>0){
								if(!ak.callback(data, 0, len)){
									break;
								}
							}
						}
					}else if(callback instanceof HttpResponseLineStringCallback){
						HttpResponseLineStringCallback<R> a1=(HttpResponseLineStringCallback<R>)callback;
						InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
						BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
						String str = null;
						while ((str = bufferedReader.readLine()) != null) {
							if(!a1.callback(str)){
								break;
							}
						}
					}else{
						while((inputStream.read()) != -1){
							
						}
					}
					
				}finally{
					if(inputStream!=null){
						inputStream.close();
						inputStream = null;
					}
					
				}
			}
			
			
		}catch(Throwable ex){
			if(callback!=null){
				callback.onException(ex);
			}
			throw ex;
		}finally{
			if(httpUrlConn!=null){
				try{
					httpUrlConn.disconnect();
				}catch(Exception ex){
					
				}
			}
		}
		
		if(callback!=null){
			result=callback.onComplete();
		}
		
		return result;
	}
	
	public static String updLoadFile(String urlStr,Map<String, ?> headMap, 
			Map<String, String> formMap, 
			Map<String,String> filemap,
			int connectTimoutMills,
			int readTimeoutMills)
			throws Exception {
		byte[] rbytes = new byte[6];
		Random r = new Random();
		r.nextBytes(rbytes);
		String res=null;
		String BOUNDARY = "---------------------------71" + BytesTool.byteArrayToHexString(rbytes, 0, rbytes.length);
		HttpURLConnection conn;
		if(urlStr.startsWith("https://")){
			TrustManager[] tm = { new SimpleX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			URL url = new URL(urlStr);
			
			Proxy proxy=HttpProxy.getProxy(urlStr);
			
			HttpsURLConnection httpUrlConn1;
			if(proxy==null){
				httpUrlConn1= (HttpsURLConnection) url.openConnection();
			}else{
				httpUrlConn1= (HttpsURLConnection) url.openConnection(proxy);
			}
			
			httpUrlConn1.setSSLSocketFactory(ssf);
			conn=httpUrlConn1;
		}else{
			URL url = new URL(urlStr);
			Proxy proxy=HttpProxy.getProxy(urlStr);
			if(proxy==null){
				conn = (HttpURLConnection) url.openConnection();
			}else{
				conn = (HttpURLConnection) url.openConnection(proxy);
			}
			
		}
		String charset="UTF-8";
		try {
			if(connectTimoutMills>0){
				conn.setConnectTimeout(connectTimoutMills);
			}
			
			if(readTimeoutMills>0){
				conn.setReadTimeout(readTimeoutMills);
			}
			//conn.setReadTimeout(60000);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
			conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

			if(headMap!=null&&!headMap.isEmpty()){
				for(Map.Entry<String, ?> e:headMap.entrySet()){
					Object ao=e.getValue();
					if(ao==null){
						continue;
					}
					
					if(ao instanceof String[]){
						String[] a=(String[])ao;
						for(String a1:a){
							conn.addRequestProperty(e.getKey(), a1);
						}
					}else if(ao instanceof Collection){
						Collection<?> a=(Collection<?>)ao;
						for(Object a1:a){
							conn.addRequestProperty(e.getKey(), a1.toString());
						}
					}else{
						conn.setRequestProperty(e.getKey(), ao.toString());
					}
				}
			}
			
			OutputStream out = conn.getOutputStream();

			if (formMap != null && !formMap.isEmpty()) {
				StringBuffer strBuf = new StringBuffer();
				Iterator<Map.Entry<String, String>> iter = formMap.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry<String, String> entry = iter.next();
					String inputName = (String) entry.getKey();
					String inputValue = (String) entry.getValue();
					if (inputValue == null) {
						continue;
					}
					strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"\r\n\r\n");
					strBuf.append(URLEncoder.encode(inputValue,charset));
				}
				out.write(strBuf.toString().getBytes());
			}

			if (filemap != null && !filemap.isEmpty()) {
				for (Map.Entry<String, String> e : filemap.entrySet()) {
					File file=new File(e.getValue());
					if(!file.exists()){
						throw new IOException("file path:"+e.getValue()+" not found!");
					}
					String filename = file.getName();
					String contentType = MimeType.getByFilename(filename);

					StringBuffer strBuf = new StringBuffer();
					strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\"" + e.getKey() + "\"; filename=\"" + filename
							+ "\"\r\n");
					strBuf.append("Content-Type:" + contentType + "\r\n\r\n");

					out.write(strBuf.toString().getBytes());

					FileInputStream in = new FileInputStream(file);
					int bytes = 0;
					byte[] bufferOut = new byte[1024];
					while ((bytes = in.read(bufferOut)) != -1) {
						out.write(bufferOut, 0, bytes);
					}
					in.close();

				}
			}

			byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
			out.write(endData);
			out.flush();
			out.close();

			// 读取返回数据
			StringBuffer strBuf = new StringBuffer();
			BufferedReader reader = null;
			try{
				reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),charset));
				String line = null;
				while ((line = reader.readLine()) != null) {
					line=line.trim();
					if(line.length()>0){
						strBuf.append(line);
					}
				}
				res = strBuf.toString();
			}finally{
				if(reader!=null){
					reader.close();
				}
			}
		} finally {
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		}
		return res;

	}
	
	
//	public static void main(String[] args) throws Exception{
//		//String xml="<xml><fee_type>CNY</fee_type><nonce_str>vVOHlV33GiqeJkOIaGuY</nonce_str><order_id>10001092012018031201012200001386</order_id><order_status>SUCCESS</order_status><pay_fee>1</pay_fee><pay_time>20180312195813</pay_time><provider_id>1900000901</provider_id><provider_order_id>9918031200001000240000000001</provider_order_id><sign>15e6c198e86e0d1a7bf5d6d0056309d01bcf826a656968d7b6b8f3cff4aff96f</sign><timestamp>1520855896</timestamp><total_fee>1</total_fee></xml>";
//		//String url="http://test0.beckparking.com/core/public/weixin/7b2265223a2264222c2268223a2231302e312e332e313032222c226f223a2235227d/weixinNoSensePayNotify.do";
//		String xml="<xml><car_number>%E6%96%B0B78945</car_number><contract_status>YES</contract_status><nonce_str>KfWlQZJSV1WKRq0FGsK9</nonce_str><pay_leave_status>SUPPORT</pay_leave_status><provider_id>1900000901</provider_id><provider_parking_id>D0C24237383934350F6C1F5C</provider_parking_id><sign>833158b2aa25ab638b459d44ca587e84694763ec6886852f329f8d1ad76b3ede</sign><timestamp>1521019473</timestamp></xml>";
//		//String xml="<xml><car_number>新B78945</car_number><contract_status>YES</contract_status><nonce_str>KfWlQZJSV1WKRq0FGsK9</nonce_str><pay_leave_status>SUPPORT</pay_leave_status><provider_id>1900000901</provider_id><provider_parking_id>D0C24237383934350F6C1F5C</provider_parking_id><sign>833158b2aa25ab638b459d44ca587e84694763ec6886852f329f8d1ad76b3ede</sign><timestamp>1521019473</timestamp></xml>";
//		String url="http://127.0.0.1:8080/core/public/weixin/nosensepay/carStateCallback.do";
//		String v1=doXmlRquest(url, null, xml);
//		System.out.println(v1);	
////		System.out.println(java.net.URLDecoder.decode("新B78945", "UTF-8"));
//		//System.out.println(doRequest("https://issues.apache.org/",null, null));
////		System.out.println(doRequest("http://www.baidu.com", null));
//	}
	
}
