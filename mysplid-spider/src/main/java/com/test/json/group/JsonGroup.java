package com.test.json.group;
/**
 * @author zc
 * 得到微群的基本信息列表
 */
public class JsonGroup {
	private String code;//返回码 10000时正常
	private Data data;//数据信息
	private String msg;//返回的消息值 ：操作成功时表示成功获取
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Data getData() {
		return data;
	}
	public void setData(Data data) {
		this.data = data;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
