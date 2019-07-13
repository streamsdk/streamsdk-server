package com.ugc.facade;

import org.json.JSONException;

public interface UpdateStreamCategoryObjectFacade
{

    public String updateStreamObjects(String category, String clientKey, String userName, String json) throws ObjectKeyNotExistException, JSONException;

    public String deleteStreamObjects(String ids, String category, String clientKey, String userName) throws JSONException, ObjectKeyNotExistException;

    public String removeField(String id, String category, String clientKey, String userName, String fieldName) throws ObjectKeyNotExistException, JSONException;


}
