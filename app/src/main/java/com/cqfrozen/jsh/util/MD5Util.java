package com.cqfrozen.jsh.util;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MD5Util {

	public static String encodeMD5(String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] bytes = md.digest(password.getBytes());
			StringBuffer buffer = new StringBuffer();
			for(byte b : bytes){
				int i = b & 0xff;
				String str = Integer.toHexString(i);
				if(str.length() < 2){
					str = "0" + str;
				}
				buffer.append(str);
			}
			return buffer.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}

}
