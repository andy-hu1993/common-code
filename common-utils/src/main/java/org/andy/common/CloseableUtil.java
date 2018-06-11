package org.andy.common;

import java.io.Closeable;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 工具类 - Closeable
 * @author luCheng
 */
public class CloseableUtil {
	
	private static final Logger LOG = LoggerFactory.getLogger(CloseableUtil.class);
	
	private CloseableUtil(){}
	/**
	 * 关闭
	 * @param closeables
	 */
	public static void close(Closeable... closeables) {
		if(closeables==null){
			return;
		}
		
		for(Closeable closeable:closeables){
			close(closeable);
		}
	}
	
	/**
	 * 关闭
	 * @param closeable
	 */
	public static void close(Closeable closeable) {
		if(closeable==null){
			return;
		}
		
		try {
			closeable.close();
		} catch (IOException e) {
			LOG.error(e.getMessage(),e);
		}
	}
}
