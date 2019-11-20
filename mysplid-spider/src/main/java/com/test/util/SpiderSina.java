package com.test.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;

/**
 * 爬取新浪微博的主程序
 * 
 * @author aa
 *
 */
public class SpiderSina {

	private LoginSina lg;
	private Map<String, String> headers;

	public SpiderSina(LoginSina lg) {
		this.lg = lg;
		this.headers = new HashMap<String, String>();
		headers.put("Accept", "text/html, application/xhtml+xml, */*");
		headers.put("Accept-Language", "zh-cn");
		headers.put("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0; BOIE9;ZHCN");
		headers.put("Connection", "Keep-Alive");
		headers.put("Cache-Control", "no-cache");
		String cookieValue = HttpUtils.setCookie2String(lg.getCookies());
		headers.put("Cookie", cookieValue);
	}

	// 用户基本信息 主要是将要解析用户主页下方经过编码后的内容
	public String getMemberInfo(String memberID) {
		String url = "http://weibo.com/" + memberID + "/info";
		this.headers.put("Host", "weibo.com");
		this.headers.put("Referer", "http://weibo.com/u/" + memberID);
		HttpResponse response = HttpUtils.doGet(url, headers);
		String responseText = HttpUtils.getStringFromResponse(response);
		return responseText;
	}

	/*
	 * @params memberID:是用户ID max_id:每次AJAX获得数据时上面一次的最后一个ID值
	 * end_id：用户最新的一条微博的ID值 k：一个随机数 page：页号 pre_page：前一页 count：每次返回的数值
	 * 当max_id为null是 count=50 否则为15 pagebar：ajax时，第一次为0，第二次为1 注意: 1
	 * 用此请求，每次获得的数据格式都一样，用同样的解析方法来进行解析。 2 每次一页可以获得总共45条记录，需要三次请求。每次请求可获得15条记录。 3
	 * max_id可以不用到，直接等于 end_id就可以了. 4
	 * 第一次请求时可以将end_id设置为NUll,即为第一次时翻页时的请求后边的滚动时必须有end_id参数，end_id为第一页的第一条ID即可。
	 */
	// 获得用户发布的微博信息 json格式的数据
	public String getMemberReleaseTopic(String memberID, String end_id, Integer page, Integer pagebar) {
		String url = "";
		Integer pre_page = 1;
		Integer count = 0;
		String k = System.currentTimeMillis() + "" + (int) (Math.random() * 100000) % 100;
		if (end_id == null) {
			count = 50;
			if (page == 1) {
				pre_page = 2;
			} else {
				pre_page = page - 1;
			}
			url = "http://weibo.com/aj/mblog/mbloglist?" + "page=" + page + "&count=" + count + "&pre_page=" + pre_page
					+ "&" + "_k=" + k + "&uid=" + memberID + "&_t=0&__rnd=" + System.currentTimeMillis();
		} else {
			count = 15;
			pre_page = page;
			url = "http://weibo.com/aj/mblog/mbloglist?" + "page=" + page + "&count=" + count + "&max_id=" + end_id
					+ "&" + "pre_page=" + pre_page + "&end_id=" + end_id + "&" + "pagebar=" + pagebar + "&_k=" + k + "&"
					+ "uid=" + memberID + "&_t=0&__rnd=" + System.currentTimeMillis();
		}

		this.headers.put("Referer", "http://weibo.com/u/" + memberID);
		this.headers.put("Host", "weibo.com");
		this.headers.put("Content-Type", "application/x-www-form-urlencoded");
		this.headers.put("x-requested-with", "XMLHttpRequest");

		HttpResponse response = HttpUtils.doGet(url, headers);
		if (response == null) {
			return "";
		}
		return HttpUtils.getStringFromResponse(response);
	}

	// 用户粉丝用户信息 html页面，每次20个
	public String getMemberFans(String memberID, int page) {
		String url = "http://weibo.com/" + memberID + "/fans?&uid=1689219395&tag=&page=" + page;
		this.headers.put("Host", "weibo.com");
		this.headers.put("Referer", "http://weibo.com/" + memberID + "/fans");
		HttpResponse response = HttpUtils.doGet(url, headers);
		String responseText = HttpUtils.getStringFromResponse(response);
		return responseText;
	}

	// 用户关注的用户信息 html页面
	public String getMemberFollowing(String memberID, int page) {
		String url = "http://weibo.com/" + memberID + "/follow?page=" + page;
		this.headers.put("Host", "weibo.com");
		this.headers.put("Referer", "http://weibo.com/" + memberID + "/follow");
		HttpResponse response = HttpUtils.doGet(url, headers);
		String responseText = HttpUtils.getStringFromResponse(response);
		return responseText;
	}
}
