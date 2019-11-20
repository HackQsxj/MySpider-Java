package com.test.util;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Hex;
import org.apache.http.HttpResponse;
import org.apache.http.cookie.Cookie;

import com.test.json.msg.PreLoginResponseMessage;


/**
 * 登陆新浪微博的类
 * @author aa
 *
 */
public class LoginSina {

	private String username;//用户名
	private String password;//密码
	private String rsakv;
	private String pubkey;
	private String servertime;//服务器的时间
	private String nonce;//一次性字符串
	private String userid;//用户微博ID
	private String pcid;//若需要输入验证码时用到
	private String userdomainname;//用户域名
	private String door;//验证码
	
	private Map<String,String> headers=null;
	
	private List<Cookie> cookies=null;
	
	public LoginSina(String username,String password){
		this.username=username;
		this.password=password;
		init();
	}
	
	public Map<String,String> getHeaders(){
		Map<String,String> hds=null;
		if(headers!=null && headers.keySet().size()>0){
			hds=new HashMap<String,String>();
			for(String key:headers.keySet()){
				hds.put(key,headers.get(key));
			}
		}
		return hds;
	}
	
	public List<Cookie> getCookies(){
		List<Cookie> cc=null;
		if(cookies!=null && cookies.size()>0){
			cc=new ArrayList<Cookie>();
			for(int i=0;i<cookies.size();i++){
				cc.add(cookies.get(i));
			}
		}
		return cc;
	}
	
	
	//登录微博
		public String dologinSina(){
			System.out.println("---do login ");
			String url="http://login.sina.com.cn/sso/login.php?client=ssologin.js(v1.4.5)";//v1.3.17
			Map<String,String> headers=new HashMap<String,String>();
			Map<String,String> params=new HashMap<String,String>();
			
			headers.put("Accept", "text/html, application/xhtml+xml, */*");
			headers.put("Referer", "http://weibo.com/");
			headers.put("Accept-Language", "zh-cn");
			headers.put("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0; BOIE9;ZHCN");
			headers.put("Host", "login.sina.com.cn");
			headers.put("Connection", "Keep-Alive");
			headers.put("Content-Type", "application/x-www-form-urlencoded");
			headers.put("Cache-Control", "no-cache");
		
			params.put("encoding", "UTF-8");
			params.put("entry", "weibo");
			params.put("from", "");
			params.put("prelt", "112");
			params.put("gateway", "1");
			params.put("nonce", nonce);
			params.put("pwencode", "rsa2");//wsse
			params.put("returntype", "META");
			params.put("pagerefer", "");
			params.put("savestate", "7");	
			params.put("servertime", servertime);
			params.put("rsakv", rsakv);
			params.put("service", "miniblog");
			params.put("sp", getEncryptedP());
			params.put("ssosimplelogin", "1");
			params.put("su", getEncodedU());
			params.put("url", "http://weibo.com/ajaxlogin.php?framelogin=1&callback=parent.sinaSSOController.feedBackUrlCallBack");
			params.put("useticket", "1");
			params.put("vsnf", "1");
			
			HttpResponse response=HttpUtils.doPost(url, headers, params);
			this.cookies=HttpUtils.getResponseCookies(response);
			this.headers=headers;
	    	String responseText=HttpUtils.getStringFromResponse(response);
			try {
				responseText=new String(responseText.getBytes(),"GBK");
				if(!responseText.contains("retcode=0")){
					downloadCheckImage();
					this.nonce=getnonce();
					Scanner s=new Scanner(System.in);
					if(responseText.contains("retcode=4049"))
						System.out.println("请输入验证码:");
					else if(responseText.contains("retcode=2070")){
						System.out.println("验证码不正确，请再次输入验证码:");
					}
					this.door=s.next();
					dologinSina();
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			System.out.println("login success!");
			return responseText;
		}
		//登录后重点向
		public String redirect(){
			String cookieValue=HttpUtils.setCookie2String(this.cookies);
			this.headers.clear();
			this.headers.put("Accept", "image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
			this.headers.put("Accept-Language", "zh-cn");
			this.headers.put("Connection", "Keep-Alive");
			this.headers.put("Host", "sina.com.cn");
			this.headers.put("Referer", "http://login.sina.com.cn/sso/login.php?client=ssologin.js(v1.4.15)");
			this.headers.put("User", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; QQDownload 691)");
			this.headers.put("Cookie", cookieValue);
			
			String ssosavestate="";
			String ticket = "";
			for(Cookie c:this.cookies){
				if(c.getName().equals("ALF")){
					ssosavestate=c.getValue();
				}else if(c.getName().equals("tgc")){
					ticket=c.getValue();
				}
			}
			String url="http://weibo.com/ajaxlogin.php?" +
					"framelogin=1&callback=parent.sinaSSOController.feedBackUrlCallBack&" +
					"sudaref=weibo.com";
			HttpResponse response=HttpUtils.doGet(url, this.headers);
			response=HttpUtils.doGet(url, this.headers);
			
			String responseText=HttpUtils.getStringFromResponse(response);
			return responseText;
		}
		//生成一次性的字符串 6位
		private String getnonce() {
	        String x = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	        String str = "";
	        for (int i = 0; i < 6; i++) {
	            str += x.charAt((int)Math.ceil(Math.random() * 1000000) % x.length());
	        }
	        return str;
		}
		//初始化得到服务区的时间和一次性字符串
		private void init(){
			String url=compositeUrl();
			Map<String,String> headers=new HashMap<String,String>();
			headers.put("Accept", "*/*");
			headers.put("Referer", "http://weibo.com/");
			headers.put("Accept-Language", "zh-cn");
			headers.put("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; QQDownload 691)");
			headers.put("Host", "login.sina.com.cn");
			headers.put("Connection", "Keep-Alive");
			
			HttpResponse response=HttpUtils.doGet(url, headers);
			String responseText=HttpUtils.getStringFromResponse(response);
			int begin=responseText.indexOf("{");
			int end=responseText.lastIndexOf("}");
			responseText=responseText.substring(begin,end+1);
			System.out.println(responseText);
			PreLoginResponseMessage plrmsg =JsonUtils.jsontoPreLoginResponseMessage(responseText);
			this.nonce=plrmsg.getNonce();
			this.servertime=plrmsg.getServertime()+"";
			this.pubkey=plrmsg.getPubkey();
			this.rsakv=plrmsg.getRsakv();
			this.pcid=plrmsg.getPcid();
		}
		//下载验证码
		private void downloadCheckImage() {
			if(pcid==null) return;
			this.headers.remove("Content-Type");
			this.cookies.clear();
			String cookieValue=HttpUtils.setCookie2String(this.cookies);
			this.headers.put("Cookie", cookieValue);
			String url="http://login.sina.com.cn/cgi/pin.php?r="+(long)(Math.random()*100000000)+"&s=0&p="+this.pcid;
			System.out.println(url);
			HttpResponse response=HttpUtils.doGet(url, headers);
			InputStream in=HttpUtils.getInputStreamFromResponse(response);
			Utils.writeFileFromStream("image/checkImage.jpeg", in);
		}
		//组合预登录时的URL
		private String compositeUrl(){
			StringBuilder builder=new StringBuilder();
//			builder.append("http://login.sina.com.cn/sso/prelogin.php?")
//				   .append("entry=weibo&callback=sinaSSOController.preloginCallBack&")
//				   .append("su="+getEncodedU())
//				   .append("&client=ssologin.js(v1.3.17)&_="+System.currentTimeMillis());
			builder.append("http://login.sina.com.cn/sso/prelogin.php?")
			   .append("entry=weibo&callback=sinaSSOController.preloginCallBack&")
			   .append("su="+getEncodedU())
			   .append("&rsakt=mod&checkpin=1&client=ssologin.js(v1.4.5)&_="+System.currentTimeMillis());
			return builder.toString();
		}
		//对用户名进行编码
		private String getEncodedU() {
			if(username!=null && username.length()>0){
				return Base64Encoder.encode(EncodeUtils.encodeURL(username,"utf-8").getBytes());
			}
			return "";
		}
		//对密码进行编码
		private String getEncryptedP(){
//			return EncodeSuAndSp.getEncryptedP(password, servertime, nonce);
			String data=servertime+"\t"+nonce+"\n"+password;
			String spT=rsaCrypt(pubkey, "10001", data);
			return spT;
		}
		
		public static String rsaCrypt(String pubkey, String exponentHex, String pwd,String servertime,String nonce) {
			  String data=servertime+"\t"+nonce+"\n"+pwd;
			  return rsaCrypt(pubkey,exponentHex,data);
		}
		/**
		 *e.rsakv = a.rsakv;
	      var f = new sinaSSOEncoder.RSAKey;
	      f.setPublic(a.rsaPubkey, "10001");
	      c = f.encrypt([a.servertime, a.nonce].join("\t") + "\n" + c)
	      */
		public static String rsaCrypt(String pubkey, String exponentHex, String messageg) {
				KeyFactory factory=null;
				try {
					factory = KeyFactory.getInstance("RSA");
				} catch (NoSuchAlgorithmException e1) {
					return "";
				}
				BigInteger publicExponent = new BigInteger(pubkey, 16); /* public exponent */
				BigInteger modulus = new BigInteger(exponentHex, 16); /* modulus */
				RSAPublicKeySpec spec = new RSAPublicKeySpec(publicExponent, modulus);
				RSAPublicKey pub=null;
				try {
					pub = (RSAPublicKey) factory.generatePublic(spec);
				} catch (InvalidKeySpecException e1) {
					return "";
				}
				Cipher enc=null;
				byte[] encryptedContentKey =null;
				try {
					enc = Cipher.getInstance("RSA");
					enc.init(Cipher.ENCRYPT_MODE, pub);
					encryptedContentKey = enc.doFinal(messageg.getBytes());
				} catch (NoSuchAlgorithmException e1) {
					System.out.println(e1.getMessage());
					return "";
				} catch (NoSuchPaddingException e1) {
					System.out.println(e1.getMessage());
					return "";
				} catch (InvalidKeyException e1) {
					System.out.println(e1.getMessage());
					return "";
				} catch (IllegalBlockSizeException e1) {
					System.out.println(e1.getMessage());
					return "";
				} catch (BadPaddingException e1) {
					System.out.println(e1.getMessage());
					return "";
				} 
				return new String(Hex.encodeHex(encryptedContentKey));
		}
		public void setUserid(String userid) {
			this.userid = userid;
		}

		public String getUserid() {
			return userid;
		}

		public void setUserdomainname(String userdomainname) {
			this.userdomainname = userdomainname;
		}

		public String getUserdomainname() {
			return userdomainname;
		}
}
