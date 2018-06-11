package org.andy.common;

import com.yitai.common.constant.Constants;
import com.yitai.common.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExceptionUtil {
	
	private static Logger logger = LoggerFactory.getLogger(ExceptionUtil.class.getSimpleName());
	
	/**
	 * 输入异常信息
	 * @param ex
	 */
	public static String printExceptionTrace(Exception ex) {
		StringBuffer buff = new StringBuffer();
		buff.append(ex.getMessage());
		StackTraceElement[] stackTrace = ex.getStackTrace();
		if (stackTrace != null && stackTrace.length > 0) {
			for (int i = 0; i < stackTrace.length; i++) {
				buff.append(stackTrace[i]).append(Constants.Separator.CRLF);
			}
		}
		logger.error("错误信息：Exception【{}】", buff.toString());
		return buff.toString();
	}
	
	/**
	 * 输出异常栈以及结果信息。
	 * @param ex
	 * @param result
	 * void
	 *
	 */
	public static String printExceptionTrace(RuntimeException ex, Result result) {
		StringBuffer buff = new StringBuffer();
		buff.append(ex.getMessage());
		StackTraceElement[] stackTrace = ex.getStackTrace();
		if (stackTrace != null && stackTrace.length > 0) {
			for (int i = 0; i < stackTrace.length; i++) {
				buff.append(stackTrace[i]).append(Constants.Separator.CRLF);
			}
		}
		logger.error("错误信息：code【{}】,message【{}】Exception【{}】", result.getCode(), result.getMessage(), buff.toString());
		return buff.toString();
	}

}
