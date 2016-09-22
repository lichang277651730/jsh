package com.cqfrozen.jsh.util;


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
}
