package com.test;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

import com.test.model.SpiderParams;
import com.test.util.UrlQueue;
import com.test.work.SpiderWorker;


public class SpiderStarter {

	public static void main(String[] args) {
//		首先初始化参数
		initializeParams();
//		初始化爬取队列
		initializeQueue();
//		创建worker线程并启动
		for(int i = 1; i <= SpiderParams.WORKER_NUM; i++){
			new Thread(new SpiderWorker(i)).start();
		}
	}

	private static void initializeQueue() {
		System.out.println("请输入你要爬取的网站:");
		Scanner sc = new Scanner(System.in);
		while(sc.hasNext()){
			String url = sc.nextLine();
			if("END".equals(url)){
				return;
			}
			UrlQueue.addElement(url);
		}
	}

	private static void initializeParams() {
	
		InputStream is;
		try{
			is = new BufferedInputStream(new FileInputStream("conf/spider.properties"));
			Properties properties = new Properties();
			properties.load(is);
			
			// 从配置文件中读取参数
			SpiderParams.WORKER_NUM = Integer.parseInt(properties.getProperty("spider.threadNum"));
			SpiderParams.DEYLAY_TIME = Integer.parseInt(properties.getProperty("spider.fetchDelay"));

			is.close();
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
