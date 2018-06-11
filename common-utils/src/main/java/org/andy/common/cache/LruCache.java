package org.andy.common.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * @ClassName LruCache
 * @Description cache默认实现，备并发控制,保证线程之间的共享互斥 简单的lru实现
 * @author andy.hu
 * @Date 2016年2月21日
 */
public class LruCache implements Cache<Long, LruObject> {

    // 缓存最大个数
    private static final int MAX = 70;

    private static int count = -1;

    // 用于存放cache数据
    private final Map<Long, LruObject> dataMap = new ConcurrentHashMap<Long, LruObject>();

    @Override
    public LruObject get(Long key) {
        if (key == null) {
            throw new NullPointerException("错误：参数key不能为空！");
        }
        dataMap.get(key).setCurrentCount(++count);
        ;
        return dataMap.get(key);
    }

    @Override
    public void put(Long key, LruObject value) {
        if (key == null) {
            throw new NullPointerException("错误：参数key不能为空！");
        }
        if (value == null) {
            throw new NullPointerException("错误：参数value不能为空！");
        }
        // 放入数据之前检查map容量，移除最近最少使用的缓存对象
        if (dataMap.size() >= MAX) {
            Long tempKey = null;
            int i = count;
            // 遍历缓存map获取缓存对象currentCount最小的
            for (Long k : dataMap.keySet()) {
                LruObject lruObject = dataMap.get(k);
                int j = lruObject.getCurrentCount();
                if (j < i) {
                    i = j;
                    tempKey = k;
                }
            }
            if (tempKey != null) {
                dataMap.remove(tempKey);
            }
        }
        value.setCurrentCount(++count);
        dataMap.put(key, value);
    }

    @Override
    public void update(Long key, LruObject value) {
        if (key == null) {
            throw new NullPointerException("错误：参数key不能为空！");
        }
        if (value == null) {
            throw new NullPointerException("错误：参数value不能为空！");
        }
        dataMap.remove(key);
        value.setCurrentCount(++count);
        dataMap.put(key, value);
    }

    @Override
    public boolean remove(Long key) {
        if (key == null) {
            throw new NullPointerException("错误：参数key不能为空！");
        }
        return dataMap.remove(key) != null;
    }

    @Override
    public void clear() {
        dataMap.clear();
    }

    @Override
    public boolean containKey(Long key) {
        return dataMap.containsKey(key);
    }

}
