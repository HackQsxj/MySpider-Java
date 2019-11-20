package com.test.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;


/**
 * http操作相关的类
 * @author aa
 *
 */
public class HttpUtils {
	/*
	 * params :
	 * url:  地址
	 * headers：请求头部信息
	 * return : httpresponse响应
	 */
	public static HttpResponse doGet(String url,Map<String,String> headers){
		HttpClient client=createHttpClient();
		HttpGet getMethod=new HttpGet(url);
		HttpResponse response=null;
		try {
			if(headers!=null && headers.keySet().size()>0){
				for(String key:headers.keySet()){
					getMethod.addHeader(key, headers.get(key));
				}
			}	
			response=client.execute(getMethod);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			String msg=e.getMessage();
			if(msg.contains("Truncated chunk")){
				System.out.println(e.getMessage() +" 数据获取不完整。需要重新获取。");
			}else{
				System.out.println(e.getMessage() +" 连接 被拒绝。需要降低爬取频率。");
			}
		} catch(Exception e){
		}
		return response;		
	}
	
	/*
	 * params :
	 * url:  地址
	 * headers：请求头部信息
	 * params：post的请求数据
	 * return : httpresponse响应
	 */
	
	public static HttpResponse doPost(String url,Map<String,String> headers,Map<String,String> params){
		HttpClient client=createHttpClient();
		HttpPost postMethod=new HttpPost(url);
		HttpResponse response=null;
		try {
			if(headers!=null && headers.keySet().size()>0){
				for(String key:headers.keySet()){
					postMethod.addHeader(key, headers.get(key));
				}
			}	
			List<NameValuePair> p=null;
			if(params!=null && params.keySet().size()>0){
				p=new ArrayList<NameValuePair>();
				for(String key:params.keySet()){
					p.add(new BasicNameValuePair(key,params.get(key)));
				}
			}
			if(p!=null)
				postMethod.setEntity(new UrlEncodedFormEntity(p,HTTP.UTF_8));
			response=client.execute(postMethod);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return response;			
	}
	
	//上传一个文件
	public static HttpResponse doPost(String url,Map<String,String> headers,String fileName){
		HttpClient client=createHttpClient();
		HttpPost postMethod=new HttpPost(url);
		String boundary = "";
		HttpResponse response=null;
		try {
			if(headers!=null && headers.keySet().size()>0){
				for(String key:headers.keySet()){
					postMethod.addHeader(key, headers.get(key));
					if(key.equals("Content-Type")){
						String tmp=headers.get(key);
						boundary=tmp.substring(tmp.indexOf("=")+1);
					}
				}
			}	
			File file=new File(fileName);
			InputStream in=new FileInputStream(file);
			
			StringBuffer buffer=new StringBuffer();
			buffer.append(boundary).append("\n")
				  .append("Content-Disposition: form-data; name=\"pic1\"; filename=\""+file.getName()).append("\"\n")
				  .append("Content-Type: image/pjpeg").append("\n")
				  .append("\n");
			
			System.out.println(buffer.toString());
			
			String tmpstr=Utils.getStringFromStream(in);
			tmpstr=Base64Encoder.encode(tmpstr.getBytes());
			buffer.append(tmpstr).append("\n");
			buffer.append(boundary+"--").append("\n");
			
			System.out.println(buffer.toString());
			
			in=new ByteArrayInputStream(buffer.toString().getBytes());
			
			InputStreamEntity ise=new InputStreamEntity(in,buffer.toString().getBytes().length);  
			
		    postMethod.setEntity(ise);  
		    
			response=client.execute(postMethod);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return response;			
	}
	/*
	 * params :
	 * httpresponse
	 * return : 响应的头部信息
	 */
	
	public static List<Header> getReponseHeaders(HttpResponse response){
		List<Header> headers=null;
		Header[] hds=response.getAllHeaders();
		if(hds!=null && hds.length>0){
			headers=new ArrayList<Header>();
			for(int i=0;i<hds.length;i++){
				headers.add(hds[i]);
			}
		}		
		return headers;
	}
	
	/*
	  * params :
	  * headers:头部信息 
	  * request：请求
	 */
	public static void setHeaders(Map<String,String> headers,HttpUriRequest request){
		if(headers!=null && headers.keySet().size()>0){
			for(String key:headers.keySet()){
				request.addHeader(key, headers.get(key));			}
		}
	}
	
	/*
	 * params :
	 * httpresponse
	 * return : 响应的cookies值
	 */
	
	public static List<Cookie> getResponseCookies(HttpResponse response){
		List<Cookie> cookies=null;
		Header[] hds=response.getAllHeaders();
		if(hds!=null && hds.length>0){
			for(int i=0;i<hds.length;i++){
				if(hds[i].getName().equalsIgnoreCase("Set-Cookie")){
					if(cookies==null){
						cookies=new ArrayList<Cookie>();
					}					 
					String cookiestring[]=hds[i].getValue().split(";");
					String ss[]=cookiestring[0].split("=",2);
					String cookiename=ss[0];
					String cookievalue=ss[1];
					Cookie cookie=new BasicClientCookie(cookiename,cookievalue);
					cookies.add(cookie);
				}
			}
		}		
		return cookies;
	}
	/*
	 * params :
	 * cookies数组
	 * return : cookies数组组成的字符串
	 */
	public static String setCookie2String(List<Cookie> cookies){
		StringBuilder builder=null; 
		if(cookies!=null && cookies.size()>0){
			builder=new StringBuilder();
			for(int j=0;j<cookies.size();j++){
				Cookie c=cookies.get(j);
				builder.append(c.getName()+"="+c.getValue());
				if(j!=cookies.size()-1)
					builder.append("; ");
			 }
			return builder.toString();
		}		
		return null;
	}
	
	/*
	 * 从响应中得到输入流
	 */
	public static InputStream getInputStreamFromResponse(HttpResponse response){
		if(response==null){
			return null;
		}
		HttpEntity entity=response.getEntity();
		InputStream in=null;
		try {
			in = entity.getContent();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return  in;
	}
	
	/*
	 * 从响应中得到字符串
	 */
	public static String getStringFromResponse(HttpResponse response){
		if(response==null){
			return null;
		}
		InputStream in=getInputStreamFromResponse(response);
		String responseText="";
		if(in!=null){
			responseText=Utils.getStringFromStream(in);
		}
		return responseText;
	}
	
	/**
	 * 创建支持多线程并发连接的HTTPCLIENT
	 */
	private final static HttpClient createHttpClient() {
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, "UTF-8");
		
		ThreadSafeClientConnManager clientmanager = new ThreadSafeClientConnManager();
		clientmanager.setMaxTotal(20);
		HttpClient client = new DefaultHttpClient(clientmanager, params);
		return client;
	}
	
	/**
	 * 加入代理的功能
	 * @return HttpClient 对象
	 */
	public static HttpClient getDefaultHttpClientByProxy() {
		HttpClient httpclient =createHttpClient();
		String filePath = "proxy.properties";
		HttpHost proxy = null;
		Map<String, String> map = ReadIni.getDbini(filePath);
		if (map.size() == 0) {
			throw new RuntimeException("无可用代理");
		} else {
			Set<String> set = map.keySet();
			String[] array = (String[]) set.toArray(new String[set.size()]);
			Random r = new Random();
			int rnum = r.nextInt(array.length);
			String ip = array[rnum];
			String port = map.get(ip);
			proxy = new HttpHost(ip, Integer.parseInt(port));
		}
		httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,proxy);
		httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		return httpclient;
	}
}
