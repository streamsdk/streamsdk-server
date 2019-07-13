package com.ugc.facade;

import com.ugc.controller.SystemConstants;
import com.ugc.utils.JsonUtils;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "UpdateStreamCategoryObjectFacade")
public class UpdateStreamCategoryObjectFacadeImp implements UpdateStreamCategoryObjectFacade
{

    private StorageDomainFacade storageDomainFacade;
    private StreamObjectCacheFacade streamObjectCache;

    @Autowired
    public UpdateStreamCategoryObjectFacadeImp(StorageDomainFacade storageDomainFacade, StreamObjectCacheFacade streamObjectCache) {
        this.storageDomainFacade = storageDomainFacade;
        this.streamObjectCache = streamObjectCache;
    }


    public String updateStreamObjects(String category, String clientKey, String userName, String json) throws ObjectKeyNotExistException, JSONException {

        String finalKey = clientKey + userName + category;
        String jsonString = "";
        if (streamObjectCache.getObject(finalKey) != null) {
            jsonString = streamObjectCache.getObject(finalKey);
        } else {
            if (!storageDomainFacade.isKeyExists(finalKey, SystemConstants.CATEGORY_BUUKET)) {
                throw new ObjectKeyNotExistException("category " + category + " does not exist, please create parent category object first");
            } else {
                jsonString = storageDomainFacade.retriveAsCategory(clientKey, userName, category, SystemConstants.CATEGORY_BUUKET);
                streamObjectCache.cacheObject(finalKey, jsonString);
            }
        }

        String newJson = JsonUtils.updateOrAddObject(jsonString, json);
        streamObjectCache.updateCategoryCache(finalKey, newJson);
        return newJson;

    }

    public String deleteStreamObjects(String ids, String category, String clientKey, String userName) throws JSONException, ObjectKeyNotExistException {
        String finalKey = clientKey + userName + category;
        String jsonString = "";
        if (streamObjectCache.getObject(finalKey) != null) {
            jsonString = streamObjectCache.getObject(finalKey);
        } else {
            if (!storageDomainFacade.isKeyExists(finalKey, SystemConstants.CATEGORY_BUUKET)) {
                throw new ObjectKeyNotExistException("category " + category + " does not exist, please create parent category object first");
            } else {
                jsonString = storageDomainFacade.retriveAsCategory(clientKey, userName, category, SystemConstants.CATEGORY_BUUKET);
                streamObjectCache.cacheObject(finalKey, jsonString);
            }
        }
        String newJson = JsonUtils.deleteObjects(jsonString, ids);
        streamObjectCache.updateCategoryCache(finalKey, newJson);
        return "OK";
    }

    public String removeField(String id, String category, String clientKey, String userName, String fieldName) throws ObjectKeyNotExistException, JSONException {

        String finalKey = clientKey + userName + category;
        String jsonString = "";

        if (streamObjectCache.getObject(finalKey) != null) {
            jsonString = streamObjectCache.getObject(finalKey);
        } else {
            if (!storageDomainFacade.isKeyExists(finalKey, SystemConstants.CATEGORY_BUUKET)) {
                throw new ObjectKeyNotExistException("category " + category + " does not exist, please create parent category object first");
            } else {
                jsonString = storageDomainFacade.retriveAsCategory(clientKey, userName, category, SystemConstants.CATEGORY_BUUKET);
                streamObjectCache.cacheObject(finalKey, jsonString);
            }
        }        

        String newJson = JsonUtils.deleteField(jsonString, id, fieldName);
        streamObjectCache.updateCategoryCache(finalKey, newJson);
        return newJson;
    }


}
