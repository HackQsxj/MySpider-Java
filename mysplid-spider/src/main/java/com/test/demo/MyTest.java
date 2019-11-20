package com.test.demo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MyTest {

	public static void main(String[] args) {
		String ip = null;
		System.out.println("请输入一个IP:");
		Scanner sc = new Scanner(System.in);
		ip = sc.nextLine();
		String strURL = "http://ip.chinaz.com/?IP="+ip;
		URL url;
		try {
			url = new URL(strURL);
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "utf-8"));
			
			StringBuilder contentBuf = new StringBuilder();
			while(br.readLine() != null){
				contentBuf.append(br.readLine());
			}
//			System.out.println(contentBuf);
			String content = contentBuf.toString();
			Document doc = Jsoup.parse(content);
			Element body = doc.body();
			Elements elementsByClass = body.getElementsByClass("IcpMain02");
			Elements span = elementsByClass.select("div p span");
			HashMap<String, String> map = new HashMap<>();
			map.put(span.get(0).text(), span.get(2).text());
			map.put(span.get(1).text(), span.get(3).text());
			for (String key : map.keySet()) {
				System.out.println(key+"-----"+map.get(key));
			}
		} catch (Exception e) {
			e.printStackTrace();	
		}
	}
}
