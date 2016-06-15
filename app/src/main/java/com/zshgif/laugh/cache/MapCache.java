package com.zshgif.laugh.cache;

import android.support.v4.util.LruCache;




/**
 * Created by sh on 2015/9/17.
 */
public class MapCache {
    /**
     *缓存
     */
    public static LruCache<String,Object> cacheMap = new LruCache<String,Object>(20);

    public static void putObject(String key,Object object){
        cacheMap.put(key,object);
    }
    public static Object getObjectForKey(String key){
       return cacheMap.get(key);
    }

}
