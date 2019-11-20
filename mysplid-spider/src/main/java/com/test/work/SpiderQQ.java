package com.test.work;

import com.test.fetcher.FetchedQQ;

public class SpiderQQ {

	public void spiderQQ(String url) {
		System.out.println("\n++++++++++++++++++++++++++++++++++++++++\n");
		System.out.println("现在正在爬取的网站是:" + url);
		System.out.println("\n++++++++++++++++++++++++++++++++++++++++\n");
		// 爬取页面,并解析页面
		new FetchedQQ().getQQWeiBoDesc(url);
		
		System.out.println("\n++++++++++++++++++++++++++++++++++++++++");
		System.out.println("++++++++++++++++++++++++++++++++++++++++\n");
		System.out.println("该网站爬取完毕.....");
		System.out.println("\n++++++++++++++++++++++++++++++++++++++++");
		System.out.println("++++++++++++++++++++++++++++++++++++++++\n");
		// 6. 关闭连接

	}
}
