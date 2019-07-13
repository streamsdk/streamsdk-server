package com.ugc.facade;

import org.json.JSONException;


public interface UpdateStreamObjectFacade
{

    public void updateObject(String updatedJson, String fileId, String clientKey, String userName, String buketName) throws ObjectKeyNotExistException, JSONException;

    public void deleteObjectKey(String key, String fileId, String clientKey, String userName, String buketName) throws ObjectKeyNotExistException, JSONException;

}
