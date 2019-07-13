package com.ugc.facade;


import com.ugc.controller.SystemConstants;
import com.ugc.domain.FlushObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

@Service(value = "StreamObjectCacheFacade")
public class StreamObjectCacheFacadeImp implements StreamObjectCacheFacade
{
    private Map<String, String> cached = new ConcurrentHashMap<String, String>();
    private List<String> cachedKey = Collections.synchronizedList(new Vector<String>());
    private Map<String, String> updatedObjectJson = new ConcurrentHashMap<String, String>();
    private Map<String, String> updatedCategoryObjectJson = new ConcurrentHashMap<String, String>();
    private FlushQueueFacade flushQueueFacade;

    @Autowired
    public StreamObjectCacheFacadeImp(FlushQueueFacade flushQueueFacade){
        this.flushQueueFacade = flushQueueFacade;
    }

    public String getKey(int index) {
        return cachedKey.get(index);
    }

    public String getUpdatedCategoryObjectJson(String key) {
        return updatedCategoryObjectJson.get(key);
    }

    public String getUpdatedObjectJson(String key) {
        return updatedObjectJson.get(key);
    }

    @PreDestroy
    public void cleanUp() throws Exception {

    }

    public String getObject(String key) {

        if (cached.containsKey(key))
            return cached.get(key);
        return null;

    }

    public void cacheObject(String key, String value) {

        if (cachedKey.size() > 50000) {
            String lowest = cachedKey.get(0);
            String categoryJsonValue = updatedCategoryObjectJson.get(lowest);
            String objectJsonValue = updatedObjectJson.get(lowest);
            if (categoryJsonValue != null){
                FlushObject fo = new FlushObject(categoryJsonValue, key, SystemConstants.CATEGORY_BUUKET);
                flushQueueFacade.addObjectToFlushQueue(fo);
            }
            if (objectJsonValue != null){
                FlushObject fo = new FlushObject(objectJsonValue, key, SystemConstants.OBJECT_BUKET);
                flushQueueFacade.addObjectToFlushQueue(fo);
            }
            removeObjectFromCache(lowest);
        }
        addObjectToCache(key, value);
    }

    public void updateCache(String key, String value) {
        if (cached.containsKey(key)) {
            updatedObjectJson.put(key, value);
            cached.put(key, value);
        } else {
            updatedObjectJson.put(key, value);
            cacheObject(key, value);
        }
    }

    public void updateCategoryCache(String key, String value) {
        if (cached.containsKey(key)) {
            updatedCategoryObjectJson.put(key, value);
            cached.put(key, value);
        } else {
            updatedCategoryObjectJson.put(key, value);
            cacheObject(key, value);
        }
    }

    public void removeObjectFromCache(String key) {
        cachedKey.remove(key);
        cached.remove(key);
        updatedCategoryObjectJson.remove(key);
        updatedObjectJson.remove(key);
    }

    private void addObjectToCache(String key, String value) {
        cachedKey.add(key);
        cached.put(key, value);
    }


}
