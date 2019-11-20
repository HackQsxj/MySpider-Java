package com.test.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ParserCNBlog {

	public void getArticleDesc(Document doc, int i) {
		
		System.out.println("\n==================================================\n");
		System.out.println("正在爬取第 "+i+"页");
		System.out.println("\n==================================================\n");
		
		Elements article_list = doc.getElementsByClass("day");
		for (Element article : article_list) {
			String title = article.select("div div a").text();
			String desc = article.select("div div div").text();
			title= title.replace("编辑", "");
			System.out.println("+++++++++++++++++++++++++++");
			System.out.println(title);
			System.out.println(desc);
		}
	}

}
