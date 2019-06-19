package com.fly.common.util.http;


import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Pattern;

@Slf4j
public class HttpProxy {
	
	private Proxy inetProxy;
    private CopyOnWriteArrayList<Pattern> wildcardNoproxyHosts =new CopyOnWriteArrayList<Pattern>();
	private ConcurrentHashMap<String,Object> noproxyHosts=new ConcurrentHashMap<String,Object>();
	
	private static final HttpProxy httpProxy=new HttpProxy();
	private static final HttpProxy httpsProxy=new HttpProxy();

	public static final HttpProxy getHttpProxy(){
		return httpProxy;
	}
	
	public static final HttpProxy getHttpsProxy(){
		return httpsProxy;
	}
	
	public void setProxy(InetSocketAddress proxyAddress){
		if(proxyAddress!=null){
			inetProxy=new Proxy(Proxy.Type.HTTP,proxyAddress);
		}
	}
	
	public Proxy getProxy(){
		return inetProxy;
	}
	
	public Set<String> getNoProxyHosts(){
		Set<String> set1=new HashSet<String>();
		set1.addAll(noproxyHosts.keySet());
	    for(Pattern p:wildcardNoproxyHosts){
	    	set1.add(p.pattern());
	    }
	    return set1;
	}
	
	 public void removeNoProxyHost(String host){
    	if(host==null){
			return;
		}
		host=host.trim();
		noproxyHosts.remove(host);
		for(Pattern p:wildcardNoproxyHosts){
			host.equals(p.pattern());
			wildcardNoproxyHosts.remove(p);
			break;
	    }

	}
	
	 public void addNoProxyHost(String host){
			if(host==null){
				return;
			}
			host=host.trim();
			if(host.length()==0){
				return;
			}
			if(host.indexOf('*')==-1){
				noproxyHosts.putIfAbsent(host, Boolean.TRUE);
			}else{
				synchronized(wildcardNoproxyHosts){
					for(Pattern p:wildcardNoproxyHosts){
						if(host.equals(p.pattern())){
							return;
						}
					}
					wildcardNoproxyHosts.add(Pattern.compile(host));
				}
				
			}
		}
	
	
		public boolean isNoProxy(String host){
			if(host==null){
				return true;
			}
			if(noproxyHosts.containsKey(host)){
				return true;
			}
			
			if(!wildcardNoproxyHosts.isEmpty()){
			    for(Pattern p:wildcardNoproxyHosts){
			    	if(p.matcher(host).matches()){
			    		return true;
			    	}
			    }
				
			}
			
		    return false;
		}
		
		
		public HttpProxy loadNoProxyFormConfig(String configValue){
			if(configValue!=null&&(configValue=configValue.trim()).length()>0){
				String[] a1=configValue.split("\\|");
				if(a1!=null){
					for(String aa:a1){
						aa=aa.trim();
						if(aa.length()>0){
							addNoProxyHost(aa);
						}
					}
				}
			}
			return this;
		}
		
		public HttpProxy setProxyFormConfig(String hostValue,String portValue){
			if(hostValue!=null&&(hostValue=hostValue.trim()).length()>0
					&&portValue!=null&&(portValue=portValue.trim()).length()>0){
				try{
				    setProxy(new InetSocketAddress(hostValue,Integer.parseInt(portValue)));
				}catch(Exception ex){
					log.warn("无效代理host或port! host:"+hostValue+", port:"+portValue+", 忽略此代理设置!");
				}
				
			}
			return this;
		}
	
	
	/**
	 * 得到http代理
	 * @param url
	 * @return
	 */
	public static Proxy getProxy(String url){
		if(url==null){
			return Proxy.NO_PROXY;
		}else if(url.startsWith("http://")){
			String host=getHost(url);
			if(httpProxy.isNoProxy(host)){
				return Proxy.NO_PROXY;
			}
			
			if(httpProxy.inetProxy==null){
				return null;
			}
			
			return httpProxy.inetProxy;
		}else if(url.startsWith("https://")){
			String host=getHost(url);
			if(httpsProxy.isNoProxy(host)){
				return Proxy.NO_PROXY;
			}
			if(httpsProxy.inetProxy==null){
				return null;
			}
			
			
			return httpsProxy.inetProxy;
		}else{
			return Proxy.NO_PROXY;
		}
	}
	
	public static String getHost(String url){

		String vla="//";
		int len=url.indexOf(vla);
		if(len==-1){
			return null;
		}
		String a=url.substring(len+vla.length());
		
		int i=a.indexOf('/');
		String hostport=a;
		if(i!=-1){
			hostport=a.substring(0,i);
		}
		String host=hostport;
		i=hostport.indexOf(':');
		if(i!=-1){
			host=hostport.substring(0, i);
		}
		
		return host;
	}
	
	
	
}
