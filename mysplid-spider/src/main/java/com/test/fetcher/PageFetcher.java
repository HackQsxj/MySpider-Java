package com.test.fetcher;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import com.test.model.FetchedPage;
import com.test.util.UrlQueue;

public class PageFetcher {
	
	private HttpClient client;

	public PageFetcher() {
		super();
//		设置超时时间
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, 10 * 1000);
	    HttpConnectionParams.setSoTimeout(params, 10 * 1000);	    
		client = new DefaultHttpClient(params);
	}

	/**
	 * 主动关闭HttpClient连接
	 */
	public void close(){
		client.getConnectionManager().shutdown();
	}
	
	/**
	 * 爬取url的网页内容
	 * @param url
	 * @return
	 */
	public FetchedPage getContentFromUrl(String url) {
//		定义参数初始值
		String content = null;
		int statusCode = 500;
		
//		创建Get请求,并设置请求头
		HttpGet getHttp = new HttpGet(url);
		getHttp.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; rv:16.0) Gecko/20100101 Firefox/16.0");
		HttpResponse response;
		
		try{
//			获得信息载体
			response = client.execute(getHttp);
			HttpEntity entity = response.getEntity();
			statusCode = response.getStatusLine().getStatusCode();
			
			if(entity != null){
//				不为空,则转化为文本信息,并设置爬取网页的字符集
				content = EntityUtils.toString(entity, "GB2312");
				System.out.println(content);
//				System.out.println(statusCode);
			}
		}catch(Exception e){
			e.printStackTrace();
			
			// 因请求超时等问题产生的异常，将URL放回待抓取队列，重新爬取
			UrlQueue.addFirstElement(url);
		}
		
		return new FetchedPage(url, content, statusCode);
	}

}
