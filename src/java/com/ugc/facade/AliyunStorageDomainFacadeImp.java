package com.ugc.facade;

import com.aliyun.openservices.ClientException;
import com.aliyun.openservices.oss.OSSClient;
import com.aliyun.openservices.oss.OSSException;
import com.aliyun.openservices.oss.model.OSSObject;
import com.aliyun.openservices.oss.model.OSSObjectSummary;
import com.aliyun.openservices.oss.model.ObjectListing;
import com.aliyun.openservices.oss.model.ObjectMetadata;
import com.ugc.controller.AliyunSystemConstants;
import com.ugc.domain.ObjectListDTO;
import com.ugc.domain.UserDTO;
import com.ugc.domain.UserUser;
import com.ugc.utils.ConversionUtils;
import com.ugc.utils.StreamUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service(value = "StorageDomainFacade")
public class AliyunStorageDomainFacadeImp implements StorageDomainFacade
{
    private static String kSec = "U2iQVwuvLOCrqpqxkM7nmTwylrugqV";
    private static String kAcc = "YCLXkQ2vZ7EMbbaN";

    public void saveObject(String json, String buketName, String key) {
        OSSClient client = new OSSClient(kAcc, kSec);
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
        OSSClient client = new OSSClient(kAcc, kSec);
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
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength(json.length());
        client.putObject(buketName, finalKey, in, meta);
        return key;
    }

    public String saveAsCategory(String json, String clientKey, String userName, String categoryName, String buketName) {
        String key = clientKey + userName + categoryName;
        OSSClient client = new OSSClient(kAcc, kSec);
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
        OSSClient client = new OSSClient(kAcc, kSec);
        String str = "";
        try {
            OSSObject object = client.getObject(buketName, key);
            InputStream in = object.getObjectContent();
            byte b[] = StreamUtils.readByteArray(in);
            str = new String(b, "UTF-8");
        } catch (OSSException e) {
            return null;
        } catch (ClientException e) {
            return null;
        }
        catch (UnsupportedEncodingException e) {

        }
        return str;
    }

    public String update(String fileId, String json, String clientKey, String userName, String buketName) {
        OSSClient client = new OSSClient(kAcc, kSec);
        ByteArrayInputStream in = null;
        try {
            in = new ByteArrayInputStream(json.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {

        }
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(json.length());
        client.putObject(buketName, clientKey + userName + fileId, in, objectMetadata);
        return fileId;
    }

    public String get(String fileId, String clientKey, String userName, String buketName) {

        OSSClient client = new OSSClient(kAcc, kSec);
        String key = clientKey + userName + fileId;
        String str = "";
        try {
            OSSObject ossObject = client.getObject(buketName, key);
            InputStream in = ossObject.getObjectContent();
            byte b[] = StreamUtils.readByteArray(in);
            str = new String(b, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
        } catch (OSSException e) {
            return null;
        } catch (ClientException e) {
            return null;
        }

        return str;
    }

    public void send(String buketName, String key, String clientKey, String userName, InputStream in, String json, long length) throws IOException {
        OSSClient client = new OSSClient(kAcc, kSec);
        ObjectMetadata omd = new ObjectMetadata();
        omd.addUserMetadata("json", json);
        omd.setContentLength(length);
        client.putObject(buketName, clientKey + userName + key, in, omd);
    }

    public InputStream getInputStream(String fileId, String clientKey, String userName, String buketName) {
        OSSClient client = new OSSClient(kAcc, kSec);
        OSSObject object = client.getObject(buketName, clientKey + userName + fileId);
        return object.getObjectContent();
    }

    public Map<String, String> getObjectMetaData(String fileId, String clientKey, String userName, String buketName) {
        OSSClient client = new OSSClient(kAcc, kSec);
        ObjectMetadata metaData = client.getObjectMetadata(buketName, clientKey + userName + fileId);
        Map<String, String> map = metaData.getUserMetadata();
        return map;
    }

    public List<ObjectListDTO> getObjectList(String buketName, String userName, String clientKey) {

        OSSClient client = new OSSClient(kAcc, kSec);
        String startWith = clientKey + userName;
        ObjectListing listing = client.listObjects(buketName, startWith);
        List<OSSObjectSummary> ossObjectSummaries = listing.getObjectSummaries();
        List<ObjectListDTO> objects = new ArrayList<ObjectListDTO>();

        for (OSSObjectSummary objectSummary : ossObjectSummaries) {

            long size = objectSummary.getSize();
            String key = objectSummary.getKey();
            Date modified = objectSummary.getLastModified();
            ObjectListDTO dto = new ObjectListDTO(key, size, modified, modified.getTime());
            if (buketName.equals(AliyunSystemConstants.FILE_BUKET)) {
                ObjectMetadata metaData = client.getObjectMetadata(buketName, key);
                Map<String, String> map = metaData.getUserMetadata();
                dto.setFileMetadataJson(map.get("json"));
            }
            objects.add(dto);
        }

        return objects;

    }

    public UserUser getUserObject(String buketName, String userName) {

        OSSClient client = new OSSClient(kAcc, kSec);
        UserUser user = new UserUser(userName);
        try {
            ObjectMetadata metaData = client.getObjectMetadata(buketName, userName);
            String password = metaData.getUserMetadata().get("password");
            String creationTime = metaData.getUserMetadata().get("signuptime");
            String json = metaData.getUserMetadata().get("json");
            user.setMetadataJson(json);
            user.setPassword(password);
            user.setLoginTime(System.currentTimeMillis());
            user.setCreationTime(Long.parseLong(creationTime));
        } catch (OSSException e) {
            return null;
        } catch (ClientException e) {
            return null;
        }
        return user;
    }

    public void persistUser(String key, String json, String password, long creationTime, long loginTime, boolean userExists, String buketName) {

        OSSClient client = new OSSClient(kAcc, kSec);
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

        if (userExists) {
            OSSClient client1 = new OSSClient(kAcc, kSec);
            client1.putObject(buketName, key, in, newObjectMetadata);
        } else {
            client.putObject(buketName, key, in, newObjectMetadata);
        }

    }

    public boolean authenticateSystemUser(String clientKey, String secretKey, String buketName) {
        OSSClient client = new OSSClient(kAcc, kSec);
        try {
            ObjectMetadata metaData = client.getObjectMetadata(buketName, clientKey);
            String password = metaData.getUserMetadata().get("password");
            if (!password.equals(secretKey))
                return false;
        } catch (OSSException e) {
            return false;
        } catch (ClientException e) {
            return false;
        }
        return true;
    }

    public void delete(String fileId, String clientKey, String userName, String buketName) {

        OSSClient client = new OSSClient(kAcc, kSec);
        String key = clientKey + userName + fileId;
        client.deleteObject(buketName, key);

    }

    public boolean isKeyExists(String key, String buketName) {

        OSSClient client = new OSSClient(kAcc, kSec);
        try {
            OSSObject ossObject = client.getObject(buketName, key);
        } catch (OSSException e) {
            return false;
        } catch (ClientException e) {
            return false;
        }

        return true;
    }

    public void deleteObject(String key, String buketName) {
        OSSClient client = new OSSClient(kAcc, kSec);
        client.deleteObject(buketName, key);
    }

    public List<UserDTO> getUserList(String buketName, String clientKey) {
        OSSClient client = new OSSClient(kAcc, kSec);
        List<UserDTO> users = new ArrayList<UserDTO>();
        ObjectListing ol = client.listObjects(buketName, clientKey);
        List<OSSObjectSummary> summaryList = ol.getObjectSummaries();
        for (OSSObjectSummary objectSummary : summaryList) {
            String key = objectSummary.getKey();
            String userName = key.replace(clientKey, "");
            ObjectMetadata metaData = client.getObjectMetadata(buketName, key);
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

    public UserDTO getClientKey(String userName) {
        OSSClient client = new OSSClient(kAcc, kSec);
        try {
            OSSObject ossObject = client.getObject(AliyunSystemConstants.SYSTEM_WEBUSER_BUKET, userName);
            InputStream in = ossObject.getObjectContent();
            byte b[] = StreamUtils.readByteArray(in);
            String json = new String(b, "UTF-8");
            JSONObject j = new JSONObject(json);
            String key = (String) j.get("clientKey");
            String secretKey = (String) j.get("secretKey");
            UserDTO user = new UserDTO();
            user.setClientKey(key);
            user.setSecretKey(secretKey);
            return user;

        } catch (OSSException e) {

        } catch (JSONException e) {

        } catch (UnsupportedEncodingException e) {

        } catch (ClientException e) {

        }
        return null;
    }

    private static String getHashValue(String uid) {

        try {
            return ConversionUtils.createMD5Hash(uid, true);
        } catch (NoSuchAlgorithmException e) {

        }
        return "";
    }
}
