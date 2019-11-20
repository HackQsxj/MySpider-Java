package com.test.fetcher;

import com.test.util.LoginSina;
import com.test.util.SpiderSina;

public class FetcherWeiBo {
	
	public static String fetcherWeiBo(String url, LoginSina lg, int i) {
		SpiderSina ss = new SpiderSina(lg);
		String userID = url.replace("https://weibo.com/", "");
		String content = ss.getMemberReleaseTopic(userID, null, i, null);
		return content;
	}

}
