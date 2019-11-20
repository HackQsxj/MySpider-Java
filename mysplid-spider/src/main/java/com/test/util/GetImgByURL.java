package com.test.util;

import java.util.Scanner;

public class GetImgByURL {

	public static void getImgByURL(String content, String url) {
		System.out.println("\n++++++++++++++++++++++++++++++++++++++++");
		System.out.println("++++++++++++++++++++++++++++++++++++++++\n");
		System.out.println("是否要爬取发布微博中的图片?");
		System.out.println("如果需要下载图片,请输入[YES],否则输入[NO]");
		System.out.println("\n++++++++++++++++++++++++++++++++++++++++");
		System.out.println("++++++++++++++++++++++++++++++++++++++++\n");
		Scanner sc = new Scanner(System.in);
		String answer = sc.next();
		if ("YES".equals(answer)) {
			System.out.println("\n++++++++++++++++++++++++++++++++++++++++\n");
			System.out.println("正在下载图片");
			System.out.println("\n++++++++++++++++++++++++++++++++++++++++\n");
			new DownLoadImgByURL().downloadImgByURL(content, url);
			
		}
	}
}
