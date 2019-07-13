



package com.ugc.controller;

import com.ugc.facade.StorageDomainFacade;
import com.ugc.facade.StreamObjectCacheFacade;
import com.ugc.facade.StreamRequestsFacade;
import com.ugc.view.ViewConstants;
import com.ugc.view.ViewDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


@Controller
public class CounterObjectController
{

    private StorageDomainFacade storageDomainFacade;
    private StreamObjectCacheFacade streamObjectCache;
    private StreamRequestsFacade streamRequestsFacade;
    private static Log log = LogFactory.getLog(CounterObjectController.class);

    @Autowired
    public CounterObjectController(StorageDomainFacade storageDomainFacade, StreamObjectCacheFacade streamObjectCache, StreamRequestsFacade streamRequestsFacade) {
        this.storageDomainFacade = storageDomainFacade;
        this.streamObjectCache = streamObjectCache;
        this.streamRequestsFacade = streamRequestsFacade;
    }


    @RequestMapping(value = "/counter/(*:clientKey)/(*:fileId)/(*:userName)", method = RequestMethod.POST)
    public synchronized String increment(@RequestParam("fileId")String fileId, @RequestParam("clientKey")String clientKey, @RequestParam("counts")String counts,
                            @RequestParam("objectKey")String objectKey, @RequestParam("userName")String userName, @RequestParam("category")String category,
                            ModelMap map) {

        streamRequestsFacade.countRequest(clientKey);
        String finalKey = clientKey + userName + fileId;
        String buket = SystemConstants.OBJECT_BUKET;
        if (!category.equals("")) {
            finalKey = clientKey + userName + category;
            buket = SystemConstants.CATEGORY_BUUKET;
            if (log.isInfoEnabled()){
                log.info("increment for category: " + category + " with object id " + fileId);
            }
        }
        CounterResult counterResult;
        boolean objectKeyExists = false;
        try {

            if (streamObjectCache.getObject(finalKey) == null) {
                if (!storageDomainFacade.isKeyExists(finalKey, buket)) {
                    ViewDTO view = new ViewDTO();
                    view.setTransferObject("this object key does not exist");
                    map.addAttribute(view);
                    return ViewConstants.DEFAULT;
                }
                String json = "";
                if (!category.equals("")) {
                    json = storageDomainFacade.retriveAsCategory(clientKey, userName, category, SystemConstants.CATEGORY_BUUKET);
                } else {
                    json = storageDomainFacade.get(fileId, clientKey, userName, SystemConstants.OBJECT_BUKET);
                }
                streamObjectCache.cacheObject(finalKey, json);
                counterResult = increment(counts, objectKey, category, finalKey, objectKeyExists, json, fileId);

            } else {
                String json = streamObjectCache.getObject(finalKey);
                counterResult = increment(counts, objectKey, category, finalKey, objectKeyExists, json, fileId);
            }

            if (!counterResult.isExists()) {
                ViewDTO view = new ViewDTO();
                view.setTransferObject("the object key does not exist");
                map.addAttribute(view);
                return ViewConstants.DEFAULT;
            }

        } catch (JSONException e) {
            ViewDTO view = new ViewDTO();
            view.setTransferObject("this object is not valid");
            map.addAttribute(view);
            return ViewConstants.DEFAULT;
        } catch (NumberFormatException e) {
            ViewDTO view = new ViewDTO();
            view.setTransferObject("the object key is not a number value");
            map.addAttribute(view);
            return ViewConstants.DEFAULT;
        }

       ViewDTO view = new ViewDTO();
       view.setTransferObject(String.valueOf(counterResult.getCount()));
       map.addAttribute(view);
       if (log.isInfoEnabled()){
           log.info("increment object id " + objectKey + " for app id " + clientKey + " with count results" + counterResult.getCount());
       }
       return ViewConstants.COUNTER_RESULT_VIEW;

    }

    private CounterResult increment(String counts, String objectKey, String category, String finalKey, boolean objectKeyExists, String json, String fileId) throws JSONException {
        String updatedJson = "";
        CounterResult counterResult;
        if (category.equals("")) {
            JSONObject jo = new JSONObject(json);
            JSONArray arrayData = jo.getJSONArray("data");
            counterResult = incrementStreamObjectInt(counts, objectKey, objectKeyExists, arrayData);
            JSONObject o = new JSONObject();
            o.put("data", arrayData);
            updatedJson = o.toString();
            streamObjectCache.updateCache(finalKey, updatedJson);
        } else {
            JSONObject jo = new JSONObject(json);
            JSONArray data = jo.getJSONArray("group");
            counterResult = incrementStreamCategoryobjectInt(counts, objectKey, objectKeyExists, data, fileId);
            JSONObject o = new JSONObject();
            o.put("group", data);
            updatedJson = o.toString();
            streamObjectCache.updateCategoryCache(finalKey, updatedJson);
        }
        return counterResult;
    }

    private CounterResult incrementStreamCategoryobjectInt(String counts, String objectKey, boolean objectKeyExists, JSONArray data, String fileId) throws JSONException {
        int size = data.length();
        int countValue = 0;
        for (int i = 0; i < size; i++) {
            JSONObject jobject = data.getJSONObject(i);
            String id = jobject.getString("id");
            JSONArray ja = jobject.getJSONArray("data");
            int length = ja.length();
            for (int j = 0; j < length; j++) {
                JSONObject jObject = ja.getJSONObject(j);
                String key = jObject.getString("key");
                if (key.equals(objectKey) && id.equals(fileId)) {
                    objectKeyExists = true;
                    String value = jObject.getString("value");
                    countValue = Integer.parseInt(value);
                    if (!counts.equals("")) {
                        Integer c = (Integer.parseInt(counts));
                        countValue = countValue + c.intValue();
                    } else {
                        countValue++;
                    }
                    jObject.put("value", countValue);
                    break;
                }
            }
        }
        return new CounterResult(countValue, objectKeyExists);
    }

    private CounterResult incrementStreamObjectInt(String counts, String objectKey, boolean objectKeyExists, JSONArray arrayData) throws JSONException {

        int size = arrayData.length();
        int countValue = 0;
        for (int i = 0; i < size; i++) {
            JSONObject jObject = arrayData.getJSONObject(i);
            String key = jObject.getString("key");
            if (key.equals(objectKey)) {
                objectKeyExists = true;
                String value = jObject.getString("value");
                countValue = Integer.parseInt(value);
                if (!counts.equals("")) {
                    Integer c = (Integer.parseInt(counts));
                    countValue = countValue + c.intValue();
                } else {
                    countValue++;
                }
                jObject.put("value", countValue);
                break;
            }
        }
        return new CounterResult(countValue, objectKeyExists);
    }

    private class CounterResult{
        private int count = 0;
        private boolean exists = false;

        public CounterResult(int count, boolean exists){
            this.count = count;
            this.exists = exists;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public boolean isExists() {
            return exists;
        }

        public void setExists(boolean exists) {
            this.exists = exists;
        }
    }

}
