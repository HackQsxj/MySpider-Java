package com.test.fetcher;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class FetcherCNBlog {

	public Document fetcherCNBlog(String url, int i) {
		Document doc = null;
		try {
			doc = Jsoup.connect(url+"/default.html?page="+i).get();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		return doc;
	}

}
