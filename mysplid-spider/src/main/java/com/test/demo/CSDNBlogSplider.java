package com.test.demo;

import org.jsoup.nodes.Document;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

public class CSDNBlogSplider implements PageProcessor{

	private Site site = Site.me().setDomain("https://blog.csdn.net").setSleepTime(3000)
							.setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.110 Safari/537.36");
	
	
	public static void main(String[] args) {
		
//		构建爬虫
		Spider spider = Spider.create(new CSDNBlogSplider());
//		博客共4页,我们添加4次列表页url
		for(int i = 1; i < 5; i++){
			spider.addUrl("https://blog.csdn.net/HackQ_sxj/article/list/"+i+"?");
		}
//		执行爬虫
		spider.run();
	}

	@Override
	public Site getSite() {
		
		return site;
	}

	@Override
	public void process(Page page) {
		
//		具体博客页面解析
		if(page.getUrl().get()
				.startsWith("https://blog.csdn.net/HackQ_sxj/article")){
//			这里进行标签解析,根据需要的字段的位置进行标签定位
			
			Document doc = page.getHtml().getDocument();
			System.out.println(doc);
			
			/*String subject = page.getHtml().xpath("//h6[@class='title-article']")
					.get().replace("<h6 class=\"title-article\">", "").replace("</h6>", "");
			String time = page.getHtml().xpath("//div[@class='article-bar-top d-flex']/span")
					.replace("<span class=\"time\">", "").replace("</span>", "").toString();
			String readNum = page.getHtml().xpath("//div[@class='article-bar-top d-flex']/div[1]/span")
					.replace("<span class=\" read-count\">", "").replace("</span>", "").toString();
			
			System.out.println(subject);
			System.out.println(time);
			System.out.println(readNum);*/
			
			try{
				Thread.sleep(1000);
			}catch(InterruptedException e){
				System.err.println("线程出错...");
			}
	
		}else{
//			列表页面解析,当前页所有博客url添加到解析列表
			page.addTargetRequests(page.getHtml()
					.xpath("//div[@class='article-item-box csdn-tracking-statistics']/h4/a")
					.links().all());
		}
	}
}
