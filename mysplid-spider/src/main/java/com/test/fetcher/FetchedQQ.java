package com.test.fetcher;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class FetchedQQ {

	public void getQQWeiBoDesc(String url) {
		
		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
			
			Element talkList = doc.getElementById("talkList");
			Elements msgList = talkList.getElementsByClass("msgBox");
			for (Element msg : msgList) {
				String source = msg.select(".userName").text();
				String desc = msg.select(".msgCnt").text();
				String date = msg.select(".pubInfo").text();
				date = date.replace("转播|评论|收藏", "");
				String zhuanfa = null;
				zhuanfa = msg.select(".replyBox").text();
				
				if(zhuanfa == null||"".equals(zhuanfa)){
					System.out.println("+++++++++++++++++++++++++++");
					System.out.println("该微博为原创微博:");
					System.out.println(source);
					System.out.println(desc);
					System.out.println(date);
				}else{
					System.out.println("+++++++++++++++++++++++++++");
					System.out.println("该微博为转发微博:");
					System.out.println(source);
					System.out.println(desc);
					System.out.println(date);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
