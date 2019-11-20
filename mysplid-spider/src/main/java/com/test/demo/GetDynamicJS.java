package com.test.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class GetDynamicJS {

	public static void main(String[] args) throws IOException {
		URL url = new URL("http://blog.csdn.net/");
		URLConnection urlConn = url.openConnection();
		urlConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.110 Safari/537.36");
		urlConn.connect();
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		String html = null;
		try{
			is = urlConn.getInputStream();
			isr = new InputStreamReader(is, "utf-8");
			br = new BufferedReader(isr);
			html = null;
			while((html = br.readLine()) != null){
				System.out.println(html);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			br.close();
			isr.close();
			is.close();
		}
	}
	
}
