package com.ugc.facade;

public interface StreamObjectCacheFacade
{

     public String getObject(String key);

     public void cacheObject(String key, String value);

     public void updateCache(String key, String value);

     public void updateCategoryCache(String key, String value);

     public void removeObjectFromCache(String key);

     public String getUpdatedCategoryObjectJson(String key);

     public String getUpdatedObjectJson(String key);

     public String getKey(int index);
}
