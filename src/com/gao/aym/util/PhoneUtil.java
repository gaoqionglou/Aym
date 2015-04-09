package com.gao.aym.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ����ֻ�����ĺϷ��� ���������ֻ����������ºŶ�
 * �ƶ���134[0-8],135,136,137,138,139,150,151,157,158,159,182,187,188
 * ��ͨ��130,131,132,152,155,156,185,186 ���ţ�133,1349,153,180,189
 * 
 * @author gao
 * 
 */
public class PhoneUtil {

	/**
	 * ����ֻ������Ƿ�Ϸ�
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
	 * ����Ƿ����й��ƶ��ֻ�����
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
	 * ����Ƿ����й���ͨ�ֻ�����
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
	 * ����Ƿ����й������ֻ�����
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
