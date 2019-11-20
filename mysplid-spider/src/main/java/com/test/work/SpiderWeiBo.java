package com.test.work;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

import com.test.fetcher.FetcherWeiBo;
import com.test.parser.ParserWeiBo;
import com.test.util.GetImgByURL;
import com.test.util.LoginSina;

public class SpiderWeiBo {

	public void spiderWeiBo(String url) {

		// 1. 模拟登陆
		String username = getUserInfo("username");
		String password = getUserInfo("password");
		LoginSina lg = new LoginSina(username, password);
		lg.dologinSina();
		lg.redirect();
		// 2. 开启连接,进行页面爬取
		int page = getPageNum(url);
		;
		String detailAll = null;
		for (int i = 1; i <= page; i++) {
			String content = new FetcherWeiBo().fetcherWeiBo(url, lg, i);
			// 3. 对页面合法性进行检查,查看是否反爬
			/*
			 * if(!handler.check(fetchedPage)){ // 如果被反爬,需要进行切换IP等操作
			 * 
			 * Log.info("Spider-" + threadIndex + ": switch IP to "); continue;
			 * }
			 */

			// 4. 解析页面数据
			String detail = new ParserWeiBo().parserWeiBo(content, i, url);
			detailAll += detail;
			// 5. 对页面数据进行存储

		}
		new GetImgByURL().getImgByURL(detailAll, url);
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

	private String getUserInfo(String string) {

		InputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream("conf/loginInfo.properties"));
			Properties properties = new Properties();
			properties.load(in);
			// 从配置文件中读取参数
			if ("username".equals(string)) {
				return properties.getProperty("weibo.username");
			} else if ("password".equals(string)) {
				return properties.getProperty("weibo.password");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		return null;
	}
}
