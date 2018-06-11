package org.andy.common;

/**
 * 
 * @ClassName EscapeUtil
 * @Description 前端 escape函数解码/与编码
 * @author andy.hu
 * @Date 2016年1月19日
 */
public class EscapeUtil {

	/**
	 * escape编码方法
	 * 
	 * @param src
	 *            编码的字符串
	 * @return
	 */
	public static String escape(String src) {
		int i;
		char j;
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length() * 6);
		for (i = 0; i < src.length(); i++) {
			j = src.charAt(i);
			if (Character.isDigit(j) || Character.isLowerCase(j) || Character.isUpperCase(j))
				tmp.append(j);
			else if (j < 256) {
				tmp.append("%");
				if (j < 16)
					tmp.append("0");
				tmp.append(Integer.toString(j, 16));
			} else {
				tmp.append("%u");
				tmp.append(Integer.toString(j, 16));
			}
		}
		return tmp.toString();
	}

	/**
	 * unescape解码方法
	 * 
	 * @param src
	 *            解码的字符串
	 * @return
	 */
	public final static String unescape(String src) {
		if (StringUtil.isNotBlank(src)) {
			StringBuffer tmp = new StringBuffer();
			tmp.ensureCapacity(src.length());
			int lastPos = 0, pos = 0;
			char ch;
			while (lastPos < src.length()) {
				pos = src.indexOf("%", lastPos);
				if (pos == lastPos) {
					if (src.charAt(pos + 1) == 'u') {
						ch = (char) Integer.parseInt(src.substring(pos + 2, pos + 6), 16);
						tmp.append(ch);
						lastPos = pos + 6;
					} else {
						ch = (char) Integer.parseInt(src.substring(pos + 1, pos + 3), 16);
						tmp.append(ch);
						lastPos = pos + 3;
					}
				} else {
					if (pos == -1) {
						tmp.append(src.substring(lastPos));
						lastPos = src.length();
					} else {
						tmp.append(src.substring(lastPos, pos));
						lastPos = pos;
					}
				}
			}
			return tmp.toString();
		}
		return null;
	}
}
