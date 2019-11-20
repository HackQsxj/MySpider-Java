package com.test.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.test.json.group.JsonGroup;
import com.test.json.hotweibo.HotWeiboJson;
import com.test.json.member.JsonMemberInfo;
import com.test.json.member.JsonMemberTopic;
import com.test.json.msg.PreLoginResponseMessage;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * 解析JSON格式的数据工具类
 * @author aa
 *
 */
public class JsonUtils {

	public static HotWeiboJson toHotWeiboJson(String jsonString){
		JSONObject jsonobj=JSONObject.fromObject(jsonString);
		HotWeiboJson hotWeiboJson=(HotWeiboJson) JSONObject.toBean(jsonobj, HotWeiboJson.class);
		return hotWeiboJson;
	}
	
	//将预登录时的json字符串转换为对应的bean
	public static PreLoginResponseMessage jsontoPreLoginResponseMessage(String jsondata){
		JSONObject jsonobj=JSONObject.fromObject(jsondata);
		PreLoginResponseMessage message=(PreLoginResponseMessage) JSONObject.toBean(jsonobj, PreLoginResponseMessage.class);
		return message;
	} 
	//按分类得到微群信息列表信息;---;----;
	public static JsonGroup jsontoGroupInfo(String jsondata){
		JsonGroup message=null;
		JSONObject jsonobj;
		try{
			jsonobj=JSONObject.fromObject(jsondata);
			message=(JsonGroup) JSONObject.toBean(jsonobj, JsonGroup.class);
		}catch(Exception ex){
			System.out.println(jsondata);
			ex.printStackTrace();
			return null;
		}
		return message;
	}
	//得到用户微博信息
	public static JsonMemberTopic jsontoMemberTopicInfo(String jsondata){
		JsonMemberTopic message=null;
		JSONObject jsonobj;
		try{
			jsonobj=JSONObject.fromObject(jsondata);
			message=(JsonMemberTopic) JSONObject.toBean(jsonobj, JsonMemberTopic.class);
		}catch(Exception ex){
			System.out.println(jsondata);
			ex.printStackTrace();
			return null;
		}
		return message;
	}
	//得到用户基本信息;
	public static JsonMemberInfo jsontoMemberInfo(String jsondata){
		JsonMemberInfo message=null;
		JSONObject jsonobj;
		try{
			jsonobj=JSONObject.fromObject(jsondata);
			message=(JsonMemberInfo) JSONObject.toBean(jsonobj, JsonMemberInfo.class);
		}catch(Exception ex){
			System.out.println(jsondata);
			ex.printStackTrace();
		}
		return message;
	}
	
	public static List<JsonMemberInfo> pretreatment(String htmlcode){
		List<JsonMemberInfo> jmis=new ArrayList<JsonMemberInfo>();
		Pattern p=Pattern.compile("(<script>).+(</script>)");
		Matcher m=p.matcher(htmlcode);
		List<String> scriptStr=new ArrayList<String>();
		List<String> JsonStr=new ArrayList<String>();
		while(m.find()){
			scriptStr.add(m.group());
		}
		
		for(int i=0;i<scriptStr.size();i++){
			String ss=scriptStr.get(i);
			ss=ss.replace("<script>STK && STK.pageletM && STK.pageletM.view(", " ")
			  .replace(")</script>", " ");
			JsonStr.add(ss);
		}
		for(int i=0;i<JsonStr.size();i++){
			String s=JsonStr.get(i);
			JsonMemberInfo jsmi=JsonUtils.jsontoMemberInfo(s);
			jmis.add(jsmi);
		}
		return jmis;
	}
	
	
	//把一个数据转换为json字符串
	public static String beantojsonArray(Object obj){
		JSONArray jsonarray=JSONArray.fromObject(obj);
		return jsonarray.toString();
	}
	//把一个bean对象转换为json字符串
	public static String beantojson(Object obj){
		JSONObject jsonobj=JSONObject.fromObject(obj);
		return jsonobj.toString();
	}
}
