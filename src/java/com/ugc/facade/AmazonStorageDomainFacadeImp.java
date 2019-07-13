package com.ugc.facade;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.ugc.controller.SystemConstants;
import com.ugc.domain.ObjectListDTO;
import com.ugc.domain.UserDTO;
import com.ugc.domain.UserUser;
import com.ugc.utils.ConversionUtils;
import com.ugc.utils.StreamUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.*;


public class AmazonStorageDomainFacadeImp implements StorageDomainFacade
{
   // private static final String ACCESS_KEY = "AKIAI2LYYTHPW4PMUQDA";
   // private static final String SECRET_KEY = "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";
                                            

    private static final String ACCESS_KEY = "AKIAJT2CA7N7TG7QSKQA";
    private static final String SECRET_KEY = "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";
                                              

    public void saveObject(String json, String buketName, String key) {
        AWSCredentials myCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        AmazonS3 client = new com.amazonaws.services.s3.AmazonS3Client(myCredentials);
        ByteArrayInputStream in = null;
        try {
            in = new ByteArrayInputStream(json.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            
        }
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(json.length());
        client.putObject(buketName, key, in, objectMetadata);
    }

    public String send(String json, String clientKey, String userName, String buketName, String id) {
        AWSCredentials myCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        AmazonS3 client = new com.amazonaws.services.s3.AmazonS3Client(myCredentials);
        ByteArrayInputStream in = null;
        try {
            in = new ByteArrayInputStream(json.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {

        }
        String key = "";
        if (id.equals("")) {
            String uid = UUID.randomUUID().toString().replace("-", "");
            key = getHashValue(uid);
        } else {
            key = id;
        }
        //save the object in the format of current userName + unique UUID
        String finalKey = clientKey + userName + key;
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(json.length());
        client.putObject(buketName, finalKey, in, objectMetadata);
        return key;
    }

    public String saveAsCategory(String json, String clientKey, String userName, String categoryName, String buketName) {

        String key = clientKey + userName + categoryName;
        AWSCredentials myCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        AmazonS3 client = new com.amazonaws.services.s3.AmazonS3Client(myCredentials);
        ByteArrayInputStream in = null;
        try {
            in = new ByteArrayInputStream(json.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {

        }
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(json.length());
        client.putObject(buketName, key, in, objectMetadata);
        return key;
    }

    public String retriveAsCategory(String clientKey, String userName, String category, String buketName) {

        String key = clientKey + userName + category;
        AWSCredentials myCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        AmazonS3 client = new com.amazonaws.services.s3.AmazonS3Client(myCredentials);
        //get the object in the format of systemUsername + objectid
        String str = "";
        try {
            S3Object object = client.getObject(buketName, key);
            InputStream in = object.getObjectContent();
            byte b[] = StreamUtils.readByteArray(in);
            str = new String(b, "UTF-8");
        } catch (AmazonClientException e) {
            return null;
        } catch (UnsupportedEncodingException e) {

        }
        return str;
    }

    public String update(String fileId, String json, String clientKey, String userName, String buketName) {
        AWSCredentials myCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        AmazonS3 client = new com.amazonaws.services.s3.AmazonS3Client(myCredentials);
        ByteArrayInputStream in = null;
        try {
            in = new ByteArrayInputStream(json.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {

        }
        // this method is for updating existing object
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(json.length());
        client.putObject(buketName, clientKey + userName + fileId, in, objectMetadata);
        return fileId;
    }

    public String get(String fileId, String clientKey, String userName, String buketName) {
        AWSCredentials myCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        AmazonS3 client = new com.amazonaws.services.s3.AmazonS3Client(myCredentials);
        //get the object in the format of systemUsername + username + objectid
        String key = clientKey + userName + fileId;
        String str = "";
        try {
            S3Object object = client.getObject(buketName, key);
            InputStream in = object.getObjectContent();
            byte b[] = StreamUtils.readByteArray(in);
            str = new String(b, "UTF-8");
        } catch (AmazonClientException e) {
            return null;
        } catch (UnsupportedEncodingException e) {
            
        }
        return str;
    }

    public boolean isKeyExists(String key, String buketName) {
        AWSCredentials myCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        AmazonS3 client = new com.amazonaws.services.s3.AmazonS3Client(myCredentials);
        try {
            S3Object object = client.getObject(buketName, key);
        } catch (AmazonClientException e) {
            String message = e.getMessage();
            return false;
        }

        return true;
    }


    public void send(String buketName, String key, String clientKey, String userName, InputStream in, String json, long length) throws IOException {

        AWSCredentials myCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        AmazonS3 client = new com.amazonaws.services.s3.AmazonS3Client(myCredentials);
        //save the file in the format of userName + unqiue key
        ObjectMetadata omd = new ObjectMetadata();
        omd.addUserMetadata("json", json);
        omd.setContentLength(length);
        PutObjectResult objectResult = client.putObject(new PutObjectRequest(buketName, clientKey + userName + key, in, omd));
        System.out.println(objectResult.getETag());
    }

    public InputStream getInputStream(String fileId, String clientKey, String userName, String buketName) {

        AWSCredentials myCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        AmazonS3 client = new com.amazonaws.services.s3.AmazonS3Client(myCredentials);
        //get the file in the format of systemUserName + fileId
        S3Object object = client.getObject(buketName, clientKey + userName + fileId);
        return object.getObjectContent();
    }

    public Map<String, String> getObjectMetaData(String fileId, String clientKey, String userName, String buketName) {
        AWSCredentials myCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        AmazonS3 client = new com.amazonaws.services.s3.AmazonS3Client(myCredentials);
        //get the file in the format of systemUserName + fileId

        //S3Object object = client.getObject(buketName, clientKey + userName + fileId);
       // ObjectMetadata metaData = object.getObjectMetadata();
       // Map<String, String> map = metaData.getUserMetadata();

       ObjectMetadata metaData = client.getObjectMetadata(buketName, clientKey + userName + fileId);
       Map<String, String> map = metaData.getUserMetadata();
       return map;
    }

    public List<ObjectListDTO> getObjectList(String buketName, String userName, String clientKey) {

        AWSCredentials myCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        AmazonS3 client = new com.amazonaws.services.s3.AmazonS3Client(myCredentials);
        String startWith = clientKey + userName;
        ObjectListing ol = client.listObjects(buketName, startWith);
        List<S3ObjectSummary> summaryList = ol.getObjectSummaries();
        List<ObjectListDTO> objects = new ArrayList<ObjectListDTO>();
        for (S3ObjectSummary s3Object : summaryList) {
            long size = s3Object.getSize();
            String key = s3Object.getKey();
      //      String id = key.substring(userName.length());
            Date modified = s3Object.getLastModified();
            ObjectListDTO dto = new ObjectListDTO(key, size, modified, modified.getTime());
            if (buketName.equals(SystemConstants.FILE_BUKET)){
               //  S3Object object = client.getObject(buketName, key);
                // String json = object.getObjectMetadata().getUserMetadata().get("json");
                 ObjectMetadata metaData = client.getObjectMetadata(buketName, key);
                 Map<String, String> map = metaData.getUserMetadata();
                 dto.setFileMetadataJson(map.get("json"));
            }
            objects.add(dto);
        }
        return objects;
    }

    public List<UserDTO> getUserList(String buketName, String clientKey) {

        AWSCredentials myCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        AmazonS3 client = new com.amazonaws.services.s3.AmazonS3Client(myCredentials);
        List<UserDTO> users = new ArrayList<UserDTO>();
        ObjectListing ol = client.listObjects(buketName, clientKey);
        List<S3ObjectSummary> summaryList = ol.getObjectSummaries();
        for (S3ObjectSummary s3ObjectSummary : summaryList) {
            String key = s3ObjectSummary.getKey();
            String userName = key.replace(clientKey, "");
           // S3Object object = client.getObject(buketName, key);
          //  InputStream in = object.getObjectContent();
            ObjectMetadata metaData = client.getObjectMetadata(buketName, key);
           /* byte b[] = StreamUtils.readByteArray(in);
            String json = null;
            try {
                json = new String(b, "UTF-8");
            } catch (UnsupportedEncodingException e) {

            }*/
            UserDTO user = new UserDTO();
            String loginTime = metaData.getUserMetadata().get("logintime");
            String creationTime = metaData.getUserMetadata().get("signuptime");
            String json = metaData.getUserMetadata().get("json");
            user.setLoginTime(loginTime);
            user.setCreationTime(creationTime);
            user.setMetaDataJson(json);
            user.setUserName(userName);
            users.add(user);
        }
        return users;

    }

    public UserUser getUserObject(String buketName, String userName) {
        AWSCredentials myCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        AmazonS3 client = new com.amazonaws.services.s3.AmazonS3Client(myCredentials);
        UserUser user = new UserUser(userName);
        try {
           // S3Object object = client.getObject(buketName, userName);
            ObjectMetadata metaData = client.getObjectMetadata(buketName, userName);
            String password = metaData.getUserMetadata().get("password");
            String creationTime = metaData.getUserMetadata().get("signuptime");
            String json = metaData.getUserMetadata().get("json");
            //InputStream in = object.getObjectContent();
           // byte b[] = StreamUtils.readByteArray(in);
            //String json = new String(b, "UTF-8");
            user.setMetadataJson(json);
            user.setPassword(password);
            user.setLoginTime(System.currentTimeMillis());
            user.setCreationTime(Long.parseLong(creationTime));
        } catch (AmazonClientException e) {
            //key/user does not exist
            return null;
        }
        return user;
    }

    public void persistUser(String key, String json, String password, long creationTime, long loginTime, boolean userExists, String buketName) {
        AWSCredentials myCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        AmazonS3 client = new com.amazonaws.services.s3.AmazonS3Client(myCredentials);
        ByteArrayInputStream in = null;
        try {
           if (json == null)
              json = "{\"data\":[]}";
            in = new ByteArrayInputStream(json.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {

        }
        ObjectMetadata objectMetadata = new ObjectMetadata();
        ObjectMetadata newObjectMetadata = new ObjectMetadata();


        if (userExists) {
            //S3Object object = client.getObject(buketName, key);
            //objectMetadata = object.getObjectMetadata();
            objectMetadata = client.getObjectMetadata(buketName, key);
            String oldPassword = objectMetadata.getUserMetadata().get("password");
            String oldSignUpTime = objectMetadata.getUserMetadata().get("signuptime");
            String oldLoginTime = objectMetadata.getUserMetadata().get("logintime");
            newObjectMetadata.addUserMetadata("password", oldPassword);
            newObjectMetadata.addUserMetadata("signuptime", oldSignUpTime);
            if (oldLoginTime != null)
               newObjectMetadata.addUserMetadata("logintime", oldLoginTime);
        }
        if (!password.equals(""))
            newObjectMetadata.addUserMetadata("password", password);
        if (creationTime != -1)
            newObjectMetadata.addUserMetadata("signuptime", String.valueOf(creationTime));
        if (loginTime != -1)
            newObjectMetadata.addUserMetadata("logintime", String.valueOf(loginTime));

        newObjectMetadata.addUserMetadata("json", json);
        newObjectMetadata.setContentLength(json.length());

        if (userExists){
             AWSCredentials myCredentials1 = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
             AmazonS3 client1 = new com.amazonaws.services.s3.AmazonS3Client(myCredentials1);
             client1.putObject(buketName, key, in, newObjectMetadata);
        }else{
             client.putObject(buketName, key, in, newObjectMetadata);
        }

    }

    public boolean authenticateSystemUser(String clientKey, String secretKey, String buketName) {
        AWSCredentials myCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        AmazonS3 client = new com.amazonaws.services.s3.AmazonS3Client(myCredentials);
        try {
            ObjectMetadata metaData = client.getObjectMetadata(buketName, clientKey);
            String password = metaData.getUserMetadata().get("password");
            if (!password.equals(secretKey))
                return false;
        } catch (AmazonClientException e) {
            //client key does not exists
            return false;
        }
        return true;
    }

    public UserDTO getClientKey(String userName){

        AWSCredentials myCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        AmazonS3 client = new com.amazonaws.services.s3.AmazonS3Client(myCredentials);
        try {
            S3Object object = client.getObject(SystemConstants.SYSTEM_WEBUSER_BUKET, userName);
            InputStream in = object.getObjectContent();
            byte b[] = StreamUtils.readByteArray(in);
            String json = new String(b, "UTF-8");
            JSONObject j = new JSONObject(json);
            String key = (String) j.get("clientKey");
            String secretKey = (String)j.get("secretKey");
            UserDTO user = new UserDTO();
            user.setClientKey(key);
            user.setSecretKey(secretKey);
            return user;

          } catch (AmazonClientException e) {

          } catch (JSONException e) {

          } catch (UnsupportedEncodingException e) {

        }
        return null;
    }

    public void delete(String fileId, String clientKey, String userName, String buketName) {
        AWSCredentials myCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        AmazonS3 client = new com.amazonaws.services.s3.AmazonS3Client(myCredentials);
        //get the object in the format of systemUsername + username + objectid
        String key = clientKey + userName + fileId;
        client.deleteObject(buketName, key);
    }

    public void deleteObject(String key, String buketName) {
        AWSCredentials myCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        AmazonS3 client = new com.amazonaws.services.s3.AmazonS3Client(myCredentials);
        client.deleteObject(buketName, key);
    }

    private static String getHashValue(String uid) {

        try {
            return ConversionUtils.createMD5Hash(uid, true);
        } catch (NoSuchAlgorithmException e) {

        }
        return "";
    }
}
