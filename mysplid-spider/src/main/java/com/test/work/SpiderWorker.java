package com.test.work;

import java.util.logging.Logger;

import com.test.fetcher.PageFetcher;
import com.test.handler.ContentHandler;
import com.test.parser.ContentParser;
import com.test.util.LoginSina;
import com.test.util.UrlQueue;

public class SpiderWorker implements Runnable {

	private static final Logger Log = Logger.getLogger(SpiderWorker.class.getName());
	private PageFetcher fetcher;
	private ContentHandler handler;
	private ContentParser parser;
	private int threadIndex;

	private static LoginSina lg;

	public SpiderWorker(int threadIndex) {
		this.threadIndex = threadIndex;
		this.fetcher = new PageFetcher();
		this.handler = new ContentHandler();
		this.parser = new ContentParser();
	}

	@Override
	public void run() {

		// 当抓取队列不为空时,执行爬取任务
		int i = 1;
		while (!UrlQueue.isEmpty()) {
			String url = UrlQueue.outElement();
			if (url.contains("https://weibo.com")) {
				System.out.println("\n++++++++++++++++++++++++++++++++++++++++");
				System.out.println("++++++++++++++++++++++++++++++++++++++++\n");
				System.out.println("            任务: "+ i++);
				System.out.println("        这是一个新浪微博网站,开始爬取.....");
				System.out.println("\n++++++++++++++++++++++++++++++++++++++++");
				System.out.println("++++++++++++++++++++++++++++++++++++++++\n");
				SpiderWeiBo sw = new SpiderWeiBo();
				sw.spiderWeiBo(url);
			}else if(url.contains("https://blog.csdn.net")){
				System.out.println("\n++++++++++++++++++++++++++++++++++++++++");
				System.out.println("++++++++++++++++++++++++++++++++++++++++\n");
				System.out.println("            任务: "+ i++);
				System.out.println("        这是一个CSDN网站,开始爬取.....");
				System.out.println("\n++++++++++++++++++++++++++++++++++++++++");
				System.out.println("++++++++++++++++++++++++++++++++++++++++\n");
				SpiderCSDN sc = new SpiderCSDN();
				sc.spiderCSDN(url);
			}else if(url.contains("https://www.cnblogs.com")){
				System.out.println("\n++++++++++++++++++++++++++++++++++++++++");
				System.out.println("++++++++++++++++++++++++++++++++++++++++\n");
				System.out.println("            任务: "+ i++);
				System.out.println("        这是一个博客园网站,开始爬取.....");
				System.out.println("\n++++++++++++++++++++++++++++++++++++++++");
				System.out.println("++++++++++++++++++++++++++++++++++++++++\n");
				SpiderCNBlog sb = new SpiderCNBlog();
				sb.spiderCNBlog(url);
			}else if(url.contains("http://t.qq.com")){
				System.out.println("\n++++++++++++++++++++++++++++++++++++++++");
				System.out.println("++++++++++++++++++++++++++++++++++++++++\n");
				System.out.println("            任务: "+ i++);
				System.out.println("        这是一个QQ微博网站,开始爬取.....");
				System.out.println("\n++++++++++++++++++++++++++++++++++++++++");
				System.out.println("++++++++++++++++++++++++++++++++++++++++\n");
				SpiderQQ sb = new SpiderQQ();
				sb.spiderQQ(url);
			}
			else{
				System.out.println("\n++++++++++++++++++++++++++++++++++++++++");
				System.out.println("++++++++++++++++++++++++++++++++++++++++\n");
				System.out.println("            任务: "+ i++);
				System.out.println("        抱歉该网站目前不能爬取,更新中......");
				System.out.println("\n++++++++++++++++++++++++++++++++++++++++");
				System.out.println("++++++++++++++++++++++++++++++++++++++++\n");
			}
		}
		Log.info("Spider-" + threadIndex + ": stop...");
	}

	

}
