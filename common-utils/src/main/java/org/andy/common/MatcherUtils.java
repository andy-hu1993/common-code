package org.andy.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 该util用于匹配正则表达式
 * @author LIUYANGBING
 *
 */
public class MatcherUtils {

	public final static String REGEX = "[^0-9]";//
	public final static String NUMBER_MATCHER = "^[0-9]*$";//匹配只能是数字
	public final static String NUMBER_UNDERLINE_MATCHER = "^(?!_)(?!.*?_$)[0-9_]{1,}+$";//匹配只能是数字加下划线
	public final static String PHONE_MATCHER = "^1(3|4|5|7|8)[0-9]\\d{8}$";//对手机号正则匹配验证
	public final static String CHINESE_MATCHER = "^[\u4e00-\u9fa5]+$";//只能匹配中文
	public final static String MATCHER_NAME = "^.{2,40}$";//匹配任意字符串长度在2到40之间
	public final static String MATCHER_ANYTHING_PHONE = "^1\\d{10}$";//匹配任意手机格式的验证
	
	public static Boolean matcher(String parameter, String pattenrParam) {
		if (StringUtil.isBlank(parameter)) {
			return false;
		}
		Pattern pattern = Pattern.compile(pattenrParam);
		Matcher matcher = pattern.matcher(parameter);
		Boolean find = matcher.find();
		return find;
	}
	
	public static String getNumberFromStr(String parameter, String patternParam) {
		Pattern p = Pattern.compile(patternParam);   
		Matcher m = p.matcher(parameter);  
		return m.replaceAll("").trim();
	}
	
}
