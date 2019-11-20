package com.test.work;

import java.util.Scanner;

import org.jsoup.nodes.Document;

import com.test.fetcher.FetcherCNBlog;
import com.test.parser.ParserCNBlog;

public class SpiderCNBlog {

	public void spiderCNBlog(String url) {

		int page = getPageNum(url);
		String detailAll = null;
		for (int i = 1; i <= page; i++) {
			// 爬取页面,并解析页面
			Document doc = new FetcherCNBlog().fetcherCNBlog(url, i);
			detailAll += doc.toString();
			new ParserCNBlog().getArticleDesc(doc, i);

		}
		System.out.println("\n++++++++++++++++++++++++++++++++++++++++");
		System.out.println("++++++++++++++++++++++++++++++++++++++++\n");
		System.out.println("该网站爬取完毕.....");
		System.out.println("\n++++++++++++++++++++++++++++++++++++++++");
		System.out.println("++++++++++++++++++++++++++++++++++++++++\n");
		// 6. 关闭连接

	}

	private int getPageNum(String url) {
		System.out.println("\n++++++++++++++++++++++++++++++++++++++++\n");
		System.out.println("现在正在爬取的网站是:" + url);
		System.out.println("\n++++++++++++++++++++++++++++++++++++++++\n");
		System.out.println("请输入你要爬取的页数:");
		Scanner sc = new Scanner(System.in);
		return sc.nextInt();
	}

}
