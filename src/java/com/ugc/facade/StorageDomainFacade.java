package com.ugc.facade;

import com.ugc.domain.ObjectListDTO;
import com.ugc.domain.UserDTO;
import com.ugc.domain.UserUser;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface StorageDomainFacade
{

    public void saveObject(String json, String buketName, String key);

    public String send(String json, String clientKey, String userName, String buketName, String id);

    public String saveAsCategory(String json, String clientKey, String userName, String categoryName, String buketName);

    public String retriveAsCategory(String clientKey, String userName, String category, String buketName);

    public String update(String fileId, String json, String clientKey, String userName, String buketName);

    public String get(String fileId, String clientKey, String userName, String buketName);

    public void send(String buketName, String key, String clientKey, String userName, InputStream in, String json, long length) throws IOException;

    public InputStream getInputStream(String fileId, String clientKey, String userName, String buketName);

    public Map<String, String> getObjectMetaData(String fileId, String clientKey, String userName, String buketName);

    public List<ObjectListDTO> getObjectList(String buketName, String userName, String clientKey);

    public UserUser getUserObject(String buketName, String userName);

    public void persistUser(String key, String json, String password, long creationTime, long loginTime, boolean userExists, String buketName);

    public boolean authenticateSystemUser(String clientKey, String secretKey, String buketName);

    void delete(String fileId, String clientKey, String userName, String buketName);

    public boolean isKeyExists(String key, String buketName);

    public void deleteObject(String key, String buketName);

     public List<UserDTO> getUserList(String buketName, String clientKey);

     public UserDTO getClientKey(String userName);

}
