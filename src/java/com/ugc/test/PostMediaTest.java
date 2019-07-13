package com.ugc.test;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.FileRequestEntity;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;

public class PostMediaTest
{
    private HttpClient _client;

   @Before
   public void setUp()
   {
      _client = new HttpClient();
   }

  @Test
   public void createMediaSource() throws FileNotFoundException {
      /*File file = new File("/home/shuai/uploadtest/test.jpg");
      PostMethod postMethod = new PostMethod("http://localhost:8080/print/media/file");
      postMethod.addParameter("fileSize", String.valueOf(file.length()));
      postMethod.addParameter("contentType", "image/jpeg");
      String id = "";
       try {
           _client.executeMethod(postMethod);
           String xmlRes = postMethod.getResponseBodyAsString();
           Element element = XmlUtils.createElement(xmlRes);
           id = element.getChildText("id");
           System.out.println("response: " + id);
           Assert.assertNotNull(id);

       } catch (IOException e) {
         e.printStackTrace();
       }*/

       PostMethod post = new PostMethod("http://localhost:8080/print/store/file/test2");
       File file = new File("/Users/wangshuai/Desktop/projects/test/sample.jpg");

       InputStreamRequestEntity inEntity = new InputStreamRequestEntity(new FileInputStream(file));
       FileRequestEntity fr = new FileRequestEntity(file, "image/jpeg");
       post.setRequestEntity(inEntity);
       //post.addParameter("json", "test");

       try {
           _client.executeMethod(post);
           String res = post.getResponseBodyAsString();
           System.out.println(res);

       } catch (IOException e) {
           e.printStackTrace();
       }


   }
}

