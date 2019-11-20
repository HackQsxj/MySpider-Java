package com.test.demo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class CaptureHTML {

	public static void main(String[] args) throws Exception {
		Scanner sc = new Scanner(System.in);
		System.out.println("输入一个ip:");
		String ip = sc.nextLine();
		captureHTML(ip);
	}

	private static void captureHTML(String ip) throws Exception{

		String strURL = "http://ip.chinaz.com/?IP="+ip;
		URL url = new URL(strURL);
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		InputStreamReader input = new InputStreamReader(httpConn.getInputStream(), "utf-8");
		BufferedReader br = new BufferedReader(input);
		String line = "";
		StringBuilder contentBuf = new StringBuilder();
		while((line = br.readLine()) != null){
			System.out.println(line);
			contentBuf.append(line);
		}
		String buf = contentBuf.toString();
		int beginIx = buf.indexOf("IP/域名"+ip+"的信息");
		int endIx = buf.indexOf("最近查询");
		String result = buf.substring(beginIx, endIx);
		System.out.println("captureHTML()的结果:\n" + result);
	}
}
