package org.andy.common.cache;

/**
 * 
 * @ClassName CacheException
 * @Description TODO
 * @author andy.hu
 * @Date 2016年2月21日
 */
public class CacheException extends RuntimeException {

    public CacheException() {
        super();
    }

    public CacheException(String msg) {
        super(msg);
    }

    public CacheException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

    public CacheException(Throwable throwable) {
        super(throwable);
    }

}
