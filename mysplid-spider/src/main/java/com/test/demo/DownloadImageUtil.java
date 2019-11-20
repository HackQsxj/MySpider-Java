package com.test.demo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * Java实现爬虫三 功能: 从一个网址上爬取图片,然后重命名保存到本地路径中
 * 
 * @author aa
 *
 */
public class DownloadImageUtil {

	// 图片保存路径
	public static final String IMAGE_SAVE_PATH = "F:\\image\\DownloadImageUtil";
	// 获取img标签正则表达式
	public static final String IMGURL_REG = "<img.*src=(.*?)[^>]*?>";
	// 获取src路径的正则表达式
	public static final String IMGSRC_REG = "(http|https):\"?(.*?)(\"|>|\\s+)";

	public static final String[] IMAGE_TYPE_SUFFIX = new String[] { "=png", "=jpg", "=jpeg", ".png", ".jpg", "jpeg" };

	// 默认生成的图片名称从1开始递增
	public static Integer imageIndex = 1;

	public static void main(String[] args) {
		// 爬取的网页url
		String rquestURL = null;
		Scanner sc = new Scanner(System.in);
		System.out.println("请输入一个URL地址:");
		rquestURL = sc.nextLine();
		// 通过请求url解析出相应内容
		String htmlContent = ParserContext(rquestURL);
		// 通过正则表达式匹配,取出data-src的图片链接放入到list中
		List<String> imageUrlList = getImageSrc(htmlContent);

		for (String imageUrl : imageUrlList) {
			try {
				download(imageUrl, IMAGE_SAVE_PATH);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		System.out.println("从{" + rquestURL + "]网站,共抓取[" + (imageIndex - 1) + "]张图片");
	}

	/**
	 * 解析图片url路径,保存到对应目录下
	 * 
	 * @param imageUrl
	 * @param imageSavePath
	 * @throws IOException
	 */
	private static void download(String imageUrl, String imageSavePath) throws IOException {

		String imageType = "";
		boolean flag = false;
		for (String suffix : IMAGE_TYPE_SUFFIX) {
			if (imageUrl.lastIndexOf(suffix) > -1 || imageUrl.lastIndexOf(suffix.toUpperCase()) > -1) {
				flag = true;
				imageType = suffix.replace("=", ".");
				break;
			}
		}
		// 存在该图片类型
		if (flag) {
			String filename = String.valueOf(imageIndex) + imageType;
			download(imageUrl, filename, imageSavePath);
			imageIndex++;
		}
	}

	private static void download(String imageUrl, String filename, String imageSavePath) throws IOException {

		if (StringUtils.isEmpty(imageUrl) || StringUtils.isEmpty(filename) || StringUtils.isEmpty(imageSavePath)) {
			throw new IllegalArgumentException("方法入参不能为空");
		}

		// 如果目录不存在,就新增一个
		File dir = new File(imageSavePath);
		if (!dir.exists() && dir.isDirectory()) {
			dir.mkdir();
		}

		// 构造URL
		URL url = new URL(imageUrl);
		// 打开连接
		URLConnection urlConn = url.openConnection();
		// 设置请求超时为5s
		urlConn.setConnectTimeout(5 * 1000);
		// 输入流
		InputStream is = urlConn.getInputStream();

		// 创建缓冲区
		byte[] bs = new byte[1024];
		// 读取数据长度
		int len;
		// 输出的文件流
		OutputStream os = new FileOutputStream(dir.getPath() + "/" + filename);

		while ((len = is.read(bs)) != -1) {
			os.write(bs, 0, len);
		}
		// 资源释放
		os.close();
		is.close();
	}

	/**
	 * 通过正则表达式的匹配,获取ImageSrc的地址
	 * 
	 * @param htmlContent
	 * @return
	 */
	private static List<String> getImageSrc(String htmlContent) {

		if (htmlContent == null || htmlContent.equals("")) {
			throw new IllegalArgumentException("html请求内容不能为空.");
		}
		List<String> listImageUrl = getImageUrl(htmlContent);

		List<String> listImageSrc = new ArrayList<>();

		for (String imageContent : listImageUrl) {
			Matcher matcher = Pattern.compile(IMGSRC_REG).matcher(imageContent);
			while (matcher.find()) {
				listImageSrc.add(matcher.group().substring(0, matcher.group().length() - 1));
			}
		}
		return listImageSrc;
	}

	/**
	 * 通过正则表达式获取ImageURL的地址
	 * 
	 * @param htmlContent
	 * @return
	 */
	private static List<String> getImageUrl(String htmlContent) {

		if (htmlContent == null || htmlContent.equals("")) {
			throw new IllegalArgumentException("html请求内容不能为空.");
		}

		List<String> listImgUrl = new ArrayList<String>();
		Matcher matcher = Pattern.compile(IMGURL_REG).matcher(htmlContent);

		while (matcher.find()) {
			listImgUrl.add(matcher.group().replaceAll("'", ""));
		}
		
		/*for (String string : listImgUrl) {
			System.out.println("listImgUrl : "+string);
		}*/
		return listImgUrl;
	}

	/**
	 * 读取url中的相应内容并返回
	 * 
	 * @param requestUrl
	 * @return
	 */
	private static String ParserContext(String requestUrl) {

		if (requestUrl == null || requestUrl.equals("")) {
			throw new IllegalArgumentException("url不能为空.");
		}

		String html = null;
		// 创建httpClient对象
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			// 创建httpget对象
			HttpGet httpGet = new HttpGet(requestUrl);
			// 执行get请求
			CloseableHttpResponse response = httpclient.execute(httpGet);
			try {
				// 获取响应实体
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					html = EntityUtils.toString(entity);
				}
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭连接
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return html;
	}
}
