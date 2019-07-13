package com.ugc.test;

import com.ugc.utils.ConversionUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class UserTest
{
     private HttpClient _client;

   @Before
   public void setUp()
   {
      _client = new HttpClient();

       String res = "{\"readAccessList\":[\"a\",\"b\",\"c\"],\"group\":[{\"data\":[{\"type\":\"string\",\"value\":\"testvalue1\",\"key\":\"testkey1\"}]},{\"data\":[{\"type\":\"string\",\"value\":\"testvalue2\",\"key\":\"testkey2\"}]}],\"category\":\"hello\",\"writeAccessList\":[\"d\",\"e\",\"f\"]}";
       
   }

   @Test
   public void testCreateUser()
   {
       String userName = "";
       String systemUserName = "";
       String json = "{\"data\":[{\"name\":\"Sven Siorpaes\",\"id\":\"507554464\"},{\"name\":\"Ningning Shi\",\"id\":\"553322001\"},{\"name\":\"Hua Guo\",\"id\":\"1018223421\"},{\"name\":\"Karl Tiller\",\"id\":\"1140917794\"},{\"name\":\"Bob Testtest\",\"id\":\"1297489353\"},{\"name\":\"Heyuan Li\",\"id\":\"1315795231\"},{\"name\":\"Guo Du\",\"id\":\"1628168014\"}]}";


       PostMethod postMetaData = new PostMethod("http://localhost:8018/print/useruser/create");
       postMetaData.addParameter("userName", userName);
       postMetaData.addParameter("systemUserName", systemUserName);
       postMetaData.addParameter("password", "password");
       postMetaData.addParameter("json", json);
     
       try {
           _client.executeMethod(postMetaData);
           String respones = postMetaData.getResponseBodyAsString();
           Assert.assertNotNull(respones);
       } catch (IOException e) {
           e.printStackTrace();
       }
   }

     private static String getHashValue(String uid) {
       try {
            return ConversionUtils.createMD5Hash(uid, true);
           } catch (NoSuchAlgorithmException e) {

           }
        return "";
    }




}
