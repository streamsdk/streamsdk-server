package com.ugc.test;

import com.aliyun.openservices.ClientException;
import com.aliyun.openservices.oss.OSSClient;
import com.aliyun.openservices.oss.OSSException;
import com.aliyun.openservices.oss.model.OSSObject;
import com.aliyun.openservices.oss.model.OSSObjectSummary;
import com.aliyun.openservices.oss.model.ObjectListing;
import com.aliyun.openservices.oss.model.ObjectMetadata;
import com.ugc.controller.SystemConstants;
import com.ugc.domain.ObjectListDTO;
import com.ugc.utils.ConversionUtils;
import com.ugc.utils.StreamUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class AliyunTest
{
    private static String kSec = "U2iQVwuvLOCrqpqxkM7nmTwylrugqV";
    private static String kAcc = "YCLXkQ2vZ7EMbbaN";
    private static String bName = "com-streamsdk-object";

    public static void main(String[] args) {

         // send("{\"data\":[]}", "clientkey", "username", bName, "");
        //update("CA29B36CDCBE61F7AD8F73A5DF2D9E26", "updated", "cleintkey", "username", bName);
       // get("CA29B36CDCBE61F7AD8F73A5DF2D9E26", "cleintkey", "username", bName);
       // delete("CA29B36CDCBE61F7AD8F73A5DF2D9E26", "cleintkey", "username", bName);

       //  getObjectList(bName, "username", "clientkey");
    }

    public static List<ObjectListDTO> getObjectList(String buketName, String userName, String clientKey) {

        OSSClient client = new OSSClient(kAcc, kSec);
        String startWith = clientKey + userName;
        ObjectListing listing = client.listObjects(bName, startWith);
        List<OSSObjectSummary> ossObjectSummaries = listing.getObjectSummaries();
        List<ObjectListDTO> objects = new ArrayList<ObjectListDTO>();

        for (OSSObjectSummary objectSummary : ossObjectSummaries) {

            long size = objectSummary.getSize();
            String key = objectSummary.getKey();
            Date modified = objectSummary.getLastModified();
            ObjectListDTO dto = new ObjectListDTO(key, size, modified, modified.getTime());
            if (buketName.equals(SystemConstants.FILE_BUKET)){
                 ObjectMetadata metaData = client.getObjectMetadata(buketName, key);
                 Map<String, String> map = metaData.getUserMetadata();
                 dto.setFileMetadataJson(map.get("json"));
            }
            objects.add(dto);

        }

        return objects;

    }

    public static void  delete(String fileId, String clientKey, String userName, String buketName){

        OSSClient client = new OSSClient(kAcc, kSec);
        String key = clientKey + userName + fileId;
        client.deleteObject(buketName, key);
    }

    public static String update(String fileId, String json, String clientKey, String userName, String buketName) {

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

    public static String get(String fileId, String clientKey, String userName, String buketName) {

        OSSClient client = new OSSClient(kAcc, kSec);
        String key = clientKey + userName + fileId;
        String str = "";
        try {
           OSSObject ossObject = client.getObject(bName, key);
           InputStream in = ossObject.getObjectContent();
           byte b[] = StreamUtils.readByteArray(in);
           str = new String(b, "UTF-8");
        }catch (UnsupportedEncodingException e) {
           System.out.println(e.getMessage());
        }catch(OSSException e){
            return null;
        }catch(ClientException e){
            return null;
        }

        return str;
    }

    public static String send(String json, String clientKey, String userName, String buketName, String id) {


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

    private static String getHashValue(String uid) {

        try {
            return ConversionUtils.createMD5Hash(uid, true);
        } catch (NoSuchAlgorithmException e) {

        }
        return "";
    }

}
