package com.menu.controller.EcpayUtil;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Map;
import java.util.TreeMap;

public class EcpayUtil {

	
	private static final String HASH_KEY = "5294y06JbISpM5x9";
	private static final String HASH_IV = "v77hoKGq4kWxNNIS";
	
	public static String generateCheckMacValue(Map<String, String> params) {
		
		Map<String, String> sorted = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
		sorted.putAll(params);
		
		StringBuilder sb = new StringBuilder();
		sb.append("HashKey=").append(HASH_KEY);
		for(Map.Entry<String, String> entry : sorted.entrySet()) {
			sb.append("&").append(entry.getKey()).append("=").append(entry.getValue());
			
		}
		sb.append("&HashIV=").append(HASH_IV);
		
		try {
			String urlEncoded = URLEncoder.encode(sb.toString(), "UTF-8")
					.toLowerCase()
					.replaceAll("%21", "!")
					.replaceAll("%28", "(")
					.replaceAll("%29", ")")
					.replaceAll("%2a", "*")
					.replaceAll("%2d", "-")
					.replaceAll("%2e", ".")
					.replaceAll("%5f", "_");
					
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] digest = md.digest(urlEncoded.getBytes(StandardCharsets.UTF_8));
			
			
			StringBuilder hex = new StringBuilder();
			for (byte b : digest) {
				hex.append(String.format("%02X",b));
			}
			return hex.toString();
		}catch(Exception e) {
			e.printStackTrace();
			return "";
		}
	}
}
