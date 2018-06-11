package org.andy.common.cache;

/**
 * 
 * @ClassName LruObject
 * @Description TODO
 * @author andy.hu
 * @Date 2016年2月22日
 */
public class LruObject {

    public LruObject(int currentCount, Object object) {
        this.currentCount = currentCount;
        this.object = object;
    }

    public LruObject(Object object) {
        this.object = object;
    }

    /** 当前访问数 */
    private int currentCount;
    /** 缓存的实际数据对象 */
    private Object object;

    public int getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

}
