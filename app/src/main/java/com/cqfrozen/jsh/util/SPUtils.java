package com.cqfrozen.jsh.util;


import android.text.TextUtils;

import com.cqfrozen.jsh.main.MyApplication;

public class SPUtils {

	public static boolean getFirst(){
		return MyApplication.userSp.getBoolean("isFirst", true);
	}
	
	public static void setFirst(boolean isFirst){
		MyApplication.userSp.edit().putBoolean("isFirst", isFirst).commit();
	}

	/**获取分类数据写入缓存时间
	 * @return
	 */
	public static Long getClassifyTime() {
		return MyApplication.userSp.getLong("classifyTime", 0);
	}

	/**设置分类数据写入缓存时间
	 * @param currentTimeMillis
	 */
	public static void setClassifyTime(long currentTimeMillis) {
		MyApplication.userSp.edit()
		.putLong("classifyTime", currentTimeMillis).commit();
	}
	
	/**获取分类数据 json
	 * @return
	 */
	public static String getClassifyData() {
		return MyApplication.userSp.getString("classifyData", "");
	}

	/**保存分类数据 json
	 * @param beanStr
	 */
	public static void setClassifyData(String beanStr) {
		MyApplication.userSp.edit()
		.putString("classifyData", beanStr).commit();
	}

	
	/**保存Token验证
	 * @param token
	 */
	public static void setToken(String token) {
		MyApplication.userSp.edit().putString("token", token).commit();
	}
	
	public static String getToken(){
		return MyApplication.userSp.getString("token", "");
	}

	/**保存用户名
	 * @param userName
	 */
	public static void setUserName(String userName) {
		MyApplication.userSp.edit().putString("userName", userName).commit();
	}
	
	public static String getUserName(){
		return MyApplication.userSp.getString("userName", "");
	}

	/**保存电话号码
	 * @param phoneStr
	 */
	public static void setPhoneNum(String phoneStr) {
		MyApplication.userSp.edit().putString("phoneNum", phoneStr).commit();
	}

	public static String getPhoneNum(){
		return MyApplication.userSp.getString("phoneNum", "");
	}


	/**
	 * 保存服务器的热门搜索关键字
	 */
	public static void setServerSearchKwd(String kwdJson) {
		MyApplication.userSp.edit().putString("server_search_kwd", kwdJson).commit();
	}

	public static String getServerSearchKwd(){
		return MyApplication.userSp.getString("server_search_kwd", "");
	}

	/**
	 * 保存搜索过的关键字
	 */
	public static void setSearchKwd(String keyword){
		String[] oldAry = getSearchKwd();
		if(oldAry == null){
			MyApplication.userSp.edit().putString("search_keyword", keyword).commit();
		}else {
			boolean isContains = false;
			for(int i = 0; i < oldAry.length; i++){
				if(oldAry[i].equals(keyword)){
					isContains = true;
				}
			}
			if(!isContains){
				String kwdStr = MyApplication.userSp.getString("search_keyword", "");
				kwdStr = kwdStr + "," + keyword;
				MyApplication.userSp.edit().putString("search_keyword", kwdStr).commit();
			}
		}
	}

	public static String[] getSearchKwd(){
		String search_keyword = MyApplication.userSp.getString("search_keyword", "");
		if(!TextUtils.isEmpty(search_keyword)){
			return parseToStringAry(search_keyword);
		}
		return null;
	}

	public static void clearSearchKwd(){
		MyApplication.userSp.edit().putString("search_keyword", "").commit();
	}

	private static String[] parseToStringAry(String data) {
		String[] ary = data.split(",");
		return ary;
	}


	/**
	 * 保存购物车数据
	 * @param cart_json
	 */
	public static void setCartData(String cart_json){
		MyApplication.userSp.edit().putString("cart_json", cart_json).commit();
	}

	public static String getCartData(){
		return  MyApplication.userSp.getString("cart_json", "");
	}

	/**
	 * 设置token失效时间
	 */
	public static void setExpireTime(long expire_time) {
		MyApplication.userSp.edit().putLong("expire_time", expire_time).commit();
	}

	public static Long getExpireTime(){
		return MyApplication.userSp.getLong("expire_time", 0L);
	}

}
