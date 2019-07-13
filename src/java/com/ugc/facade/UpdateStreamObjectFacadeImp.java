package com.ugc.facade;

import com.ugc.controller.SystemConstants;
import com.ugc.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.json.JSONException;

@Service(value = "UpdateStreamObjectFacade")
public class UpdateStreamObjectFacadeImp implements UpdateStreamObjectFacade
{

    private StorageDomainFacade storageDomainFacade;
    private StreamObjectCacheFacade streamObjectCache;

    @Autowired
    public UpdateStreamObjectFacadeImp(StorageDomainFacade storageDomainFacade, StreamObjectCacheFacade streamObjectCache) {
        this.storageDomainFacade = storageDomainFacade;
        this.streamObjectCache = streamObjectCache;
    }

   public void updateObject(String updatedJson, String fileId, String clientKey, String userName, String buketName) throws ObjectKeyNotExistException, JSONException {

        String finalKey = clientKey + userName + fileId;
        String objectJson = streamObjectCache.getObject(finalKey);
        if (objectJson == null) {
            objectJson = getFromStorage(fileId, clientKey, userName, SystemConstants.OBJECT_BUKET);
            if (objectJson == null)
                 throw new ObjectKeyNotExistException("object " + fileId + " does not exist, please create parent category object first");
        }
        String newJson = JsonUtils.updateStreamObject(updatedJson, objectJson);
        streamObjectCache.updateCache(finalKey, newJson);
   }

    public void deleteObjectKey(String  key, String fileId, String clientKey, String userName, String buketName) throws ObjectKeyNotExistException, JSONException {

        String finalKey = clientKey + userName + fileId;
        String objectJson = streamObjectCache.getObject(finalKey);
        if (objectJson == null) {
            objectJson = getFromStorage(fileId, clientKey, userName, SystemConstants.OBJECT_BUKET);
            if (objectJson == null)
                throw new ObjectKeyNotExistException("object " + fileId + " does not exist, please create parent category object first");
        }
        String newJson = JsonUtils.deleteStreamObjectKey(key, objectJson);
        streamObjectCache.updateCache(finalKey, newJson);

    }

    public String getFromStorage(String fileId, String clientKey, String userName, String buketName) {
        return storageDomainFacade.get(fileId, clientKey, userName, buketName);
    }
}
