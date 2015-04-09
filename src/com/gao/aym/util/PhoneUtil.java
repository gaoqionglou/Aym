package com.gao.aym.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 检测手机号码的合法性 开发检测的手机号码有以下号段
 * 移动：134[0-8],135,136,137,138,139,150,151,157,158,159,182,187,188
 * 联通：130,131,132,152,155,156,185,186 电信：133,1349,153,180,189
 * 
 * @author gao
 * 
 */
public class PhoneUtil {

	/**
	 * 检测手机号码是否合法
	 * 
	 * @param phonenum
	 * @return
	 */
	public static boolean isValidMoblie(String phonenum) {
		String moblie = "^1(3[0-9]|5[0-35-9]|8[025-9])\\d{8}$";
		Pattern pattern = Pattern.compile(moblie);
		Matcher matcher = pattern.matcher(phonenum);
		return matcher.matches();
	}

	/**
	 * 检测是否是中国移动手机号码
	 * 
	 * @param phonenum
	 * @return
	 */
	public static boolean isChinaMoblie(String phonenum) {
		String CM = "^1(34[0-8]|(3[5-9]|5[017-9]|8[278])\\d)\\d{7}$";
		Pattern pattern = Pattern.compile(CM);
		Matcher matcher = pattern.matcher(phonenum);
		return matcher.matches();
	}

	/**
	 * 检测是否是中国联通手机号码
	 * 
	 * @param phonenum
	 * @return
	 */
	public static boolean isChinaUnicom(String phonenum) {
		String CU = "^1(3[0-2]|5[256]|8[56])\\d{8}$";
		Pattern pattern = Pattern.compile(CU);
		Matcher matcher = pattern.matcher(phonenum);
		return matcher.matches();

	}

	/**
	 * 检测是否是中国电信手机号码
	 * 
	 * @param phonenum
	 * @return
	 */
	public static boolean isChinaTelecom(String phonenum) {
		String CT = "^1((33|53|8[09])[0-9]|349)\\d{7}$";
		Pattern pattern = Pattern.compile(CT);
		Matcher matcher = pattern.matcher(phonenum);
		return matcher.matches();
	}
}
