package com.test.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import org.htmlparser.Parser;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.lexer.Page;
import org.htmlparser.util.DefaultParserFeedback;


/**
 * I/O操作类
 * @author aa
 *
 */
public class Utils {

	public static Date getDateFromString(String dtext,Date fileCreateDate) {
		Date date=null;
		int y,mm,se;  
		Calendar c = Calendar.getInstance();  
		c.setTime(fileCreateDate);
		y = c.get(Calendar.YEAR); //年  
		//d = c.get(Calendar.DAY_OF_MONTH); //日  
		mm = c.get(Calendar.MINUTE); //分
		se = c.get(Calendar.SECOND);//秒
		if(dtext.contains("秒前")){
			int end=0;
			for(int i=0;i<dtext.length();i++){
				if(dtext.charAt(i)>='0' && dtext.charAt(i)<='9'){
					end++;
				}else{
					break;
				}
			}
			dtext=dtext.substring(0,end);
			int second=Integer.parseInt(dtext);
			c.set(Calendar.SECOND, se-second);
			date=c.getTime();
		}
		else if(dtext.contains("分钟前")){
			int end=0;
			for(int i=0;i<dtext.length();i++){
				if(dtext.charAt(i)>='0' && dtext.charAt(i)<='9'){
					end++;
				}else{
					break;
				}
			}
			dtext=dtext.substring(0,end);
			int minute=Integer.parseInt(dtext);
			c.set(Calendar.MINUTE, mm-minute);
			date=c.getTime();
		}else if(dtext.contains("今天")){
			 dtext=dtext.replace("今天 ", "").trim();
			 String ss[]=dtext.split(":");
			 if(ss!=null && ss.length==2){
				 c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(ss[0]));
				 c.set(Calendar.MINUTE, Integer.parseInt(ss[1]));
				 date=c.getTime();
			 }
		}else if(dtext.contains("月")){
			dtext=y+"年".concat(dtext);
			SimpleDateFormat sf=new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
			try {
				date=sf.parse(dtext);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}else if(dtext.contains("-")){
			SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
			try {
				date=sf.parse(dtext);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return date;
	}
	public static void writeFileFromStream(String filename,InputStream in){
		if(filename==null || filename.trim().length()==0)
			return;
		File file=new File(filename);
    	if(!file.exists()){
    		try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
		FileOutputStream fou=null;
		try {
			fou = new FileOutputStream(file);
			byte []buffer=new byte[1024*4];
			int len=-1;
			while((len=in.read(buffer))!=-1){
				fou.write(buffer,0,len);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(in!=null)
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if(fou!=null)
				try {
					fou.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}	
	public static void writeFileFromString(String filename,String str){
		if(filename==null || filename.trim().length()==0)
			filename="tmp.txt";
    	File file=new File(filename);
    	if(!file.exists()){
    		try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
		BufferedWriter writer=null;
		BufferedReader reader=null;
		try {
			writer=new BufferedWriter(new FileWriter(file));
			reader=new BufferedReader(new StringReader(str));
			String tmp=null;
			StringBuffer buffer=new StringBuffer();
			while((tmp=reader.readLine())!=null)
				buffer.append(tmp+"\n");
			writer.write(buffer.toString());
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				reader.close();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    	
    }
	
	
	
    public static String getStringFromStream(InputStream in) {
    	BufferedReader reader=null;
		reader = new BufferedReader(new InputStreamReader(in));
		StringBuffer buffer=new StringBuffer();
		String str=null;
		try{
			while((str=reader.readLine())!=null){
				buffer.append(str+"\n");
			}	
			reader.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}			
		try {
			return new String(buffer.toString().getBytes(),"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "error:"+e.getMessage();
		}
    }
    //得到数据库的配置信息
    public static Properties getDBconfig(){
    	Properties properties=new Properties();
    	InputStream in = null;
		try {
			in = new FileInputStream(new File("config/dbconfig.ini"));
			properties.load(in);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(in!=null)
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return properties;
    }
    
    public static Parser createParser(String inputHTML) {
		Lexer mLexer = new Lexer(new Page(inputHTML));
		Parser parser = new Parser(mLexer, new DefaultParserFeedback(
				DefaultParserFeedback.QUIET));
		return parser;
	}
}
