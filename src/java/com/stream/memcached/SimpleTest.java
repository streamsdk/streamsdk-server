package com.stream.memcached;

import com.alisoft.xplatform.asf.cache.memcached.CacheUtil;
import com.alisoft.xplatform.asf.cache.memcached.MemcachedCacheManager;
import com.alisoft.xplatform.asf.cache.IMemcachedCache;
import com.alisoft.xplatform.asf.cache.ICacheManager;


public class SimpleTest
{

    public static void main(String[] args) {

        ICacheManager<IMemcachedCache> manager = CacheUtil.getCacheManager(IMemcachedCache.class, MemcachedCacheManager.class.getName());
        manager.setConfigFile("memcached1.xml");
        manager.setResponseStatInterval(5 * 1000);
        manager.start();


        IMemcachedCache cache1 = manager.getCache("mclient0");
        IMemcachedCache cache2 = manager.getCache("mclient1");

        cache1.put("key1", "value1");
        cache1.put("key1", "value2");



        Object o = cache2.get("key1");
        Object o1 = cache2.get("key2");

        System.out.println(o);
        System.out.println(o1);



    }

}
