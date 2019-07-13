package com.ugc.facade;

import com.ugc.domain.ObjectListDTO;
import com.ugc.domain.UserDTO;
import com.ugc.domain.UserUser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MockStorageDomainFacade implements StorageDomainFacade
{
    Map<String, Map<String, String>> store = new HashMap<String, Map<String, String>>();

    public void saveObject(String json, String buketName, String key) {

        if (!store.containsKey(buketName)){
            Map<String, String> keyValues = new HashMap<String, String>();
            keyValues.put(key, json);
            store.put(buketName, keyValues);
        }else{
            Map<String, String> keyValues = store.get(buketName);
            keyValues.put(key, json);
            store.put(buketName, keyValues);
        }
    }

    public void saveTokenObject(String json, String key) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public String send(String json, String clientKey, String userName, String buketName, String id) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String saveAsCategory(String json, String clientKey, String userName, String categoryName, String buketName) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String retriveAsCategory(String clientKey, String userName, String category, String buketName) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String update(String fileId, String json, String clientKey, String userName, String buketName) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String get(String fileId, String clientKey, String userName, String buketName) {

        String objectKey = clientKey + fileId;
        Map<String, String> keyValues = store.get(buketName);
        if (keyValues != null)
           return keyValues.get(objectKey);
        return null;
    }

    public void send(String buketName, String key, String clientKey, String userName, InputStream in, String json, long length) throws IOException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void send(String buketName, String key, String clientKey, String userName, InputStream in, String json) throws IOException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void send(String buketName, String key, String clientKey, String userName, InputStream in, Map<String, String> metaData) throws IOException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public InputStream getInputStream(String fileId, String clientKey, String userName, String buketName) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Map<String, String> getObjectMetaData(String fileId, String clientKey, String userName, String buketName) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<ObjectListDTO> getObjectList(String buketName, String userName, String clientKey) {
       return new ArrayList<ObjectListDTO>();
    }

    public UserUser getUserObject(String buketName, String userName) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void persistUser(String key, String json, String password, long creationTime, long loginTime, boolean userExists, String buketName) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void persistUser(String key, String json, String password, String buketName) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean authenticateSystemUser(String clientKey, String secretKey, String buketName) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void delete(String fileId, String clientKey, String userName, String buketName) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isKeyExists(String key, String buketName) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void deleteObject(String key, String buketName) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<UserDTO> getUserList(String buketName, String clientKey) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public UserDTO getClientKey(String userName) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
