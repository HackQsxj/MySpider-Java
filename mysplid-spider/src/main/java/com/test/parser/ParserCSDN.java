package com.test.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ParserCSDN {

	public void getArticleDesc(Document doc, int i) {

		System.out.println("\n==================================================\n");
		System.out.println("正在爬取第 "+i+"页");
		System.out.println("\n==================================================\n");
		
		Elements article_list = doc.getElementsByClass("article-item-box");

		for (Element article : article_list) {
			String source = article.select("div h4 a span").text();
			String title = article.select("div h4 a").text();
			String desc = article.select("div p a").text();
			if(desc.contains("&gt;&gt;&gt; ...")){
				desc = desc.replace("&gt;&gt;&gt; ...", "");
			}
			
			System.out.println("+++++++++++++++++++++++++++");
			System.out.println("这是一个" + source + "文章");
			System.out.println("文章标题:" + title);
			System.out.println("文章简介:" + desc);

			Elements other = article.select("div div p span");
			for (Element oth : other) {
				System.out.println(oth.text());
			}
		}
	}

}
