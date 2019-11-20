package com.test.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.test.model.JSONData;

import net.sf.json.JSONObject;

public class ParserWeiBo {

	public static String parserWeiBo(String content, int i, String url) {
		System.out.println("\n==================================================\n");
		System.out.println("正在爬取第 " + i + "页");
		System.out.println("\n==================================================\n");
		JSONObject jsonObject = JSONObject.fromObject(content);
		JSONData jsonData = (JSONData) JSONObject.toBean(jsonObject, JSONData.class);
		Document doc = Jsoup.parse(jsonData.getData());
		Elements detail = doc.getElementsByClass("WB_detail");
		for (Element element : detail) {
			Elements from = element.getElementsByClass("WB_from");
			Elements text = element.getElementsByClass("WB_text");
			Elements info = element.getElementsByClass("WB_info");
			String title = info.select("div a").attr("title") + info.text();
			if (title == null || "".equals(title)) {

				System.out.println("+++++++++++++++++++++++++++");
				System.out.println("该微博为原创微博:");
				System.out.println("微博内容为:   " + text.text());
				System.out.println("发表时间为:    " + from.text());
			} else {
				System.out.println("+++++++++++++++++++++++++++");
				System.out.println("该微博为转发微博:");
				System.out.println("微博内容为:   " + text.text());
				System.out.println("转发自:    " + info.select("div a").attr("title") + info.text());
				System.out.println("发表时间为:    " + from.text());
			}
		}
		return detail.toString();
	}

}
