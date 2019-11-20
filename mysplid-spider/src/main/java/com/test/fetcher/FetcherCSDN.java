package com.test.fetcher;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class FetcherCSDN {

	public Document fetcherCSDN(String url, int i) {
		Document doc = null;
		try {			
			doc = Jsoup.connect(url+"/article/list/"+i+"?").get();			
		} catch (IOException e) {

			e.printStackTrace();
		}
		return doc;
	}

}
