package com.ugc.test;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.GetMethod;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.jdom.Element;
import com.ugc.utils.XmlUtils;

import java.io.IOException;

public class ResumeUploadTest
{

   private HttpClient _client;

   @Before
   public void setUp()
   {
      _client = new HttpClient();
   }

   @Test
   public void testResumeUpload()
   {
      String data = "1234567890abcdefghij";
      String part1 = "1234567890";
      String part2 = "abcdefghij";
      String id = "";
      String response = "";

      //create media source
      PostMethod postMethod = new PostMethod("http://localhost:8081/print/media/file");
      postMethod.addParameter("fileSize", String.valueOf(data.length()));
      postMethod.addParameter("contentType", "image/jpg");
       try
       {
           _client.executeMethod(postMethod);
           String xmlRes = postMethod.getResponseBodyAsString();
           Element element = XmlUtils.createElement(xmlRes);
           id = element.getChildText("id");
           System.out.println("response: " + id);

       } catch (IOException e){}


       //upload part1
       PostMethod post = new PostMethod("http://localhost:8081/print/media/file/upload" + "/" + id + "/0");
       ByteArrayRequestEntity request = new ByteArrayRequestEntity(part1.getBytes());
       post.setRequestEntity(request);
       try {
           _client.executeMethod(post);
           response = post.getResponseBodyAsString();
           Element element = XmlUtils.createElement(response);
           String size = element.getChildText("filesize");
           String uploadedSize = element.getChildText("uploadedsize");
           Assert.assertEquals(size, String.valueOf(data.length()));
           Assert.assertEquals(String.valueOf(part1.length()), uploadedSize);
       } catch (IOException e) {
           e.printStackTrace();
       }


       //get how many uploaded
       GetMethod get = new GetMethod("http://localhost:8081/print/media/file");
       get.setQueryString("fileId=" + id);
       String alreayUploadedSize = "";
       try {
           _client.executeMethod(get);
           response = get.getResponseBodyAsString();
           Element element = XmlUtils.createElement(response);
           alreayUploadedSize = element.getChildText("uploaded");

           Assert.assertEquals(String.valueOf(part1.length()), alreayUploadedSize);
       } catch (IOException e) {
           e.printStackTrace();
       }


       // upload part2
       PostMethod post1 = new PostMethod("http://localhost:8081/print/media/file/upload" + "/" + id + "/" + alreayUploadedSize);
       ByteArrayRequestEntity request1 = new ByteArrayRequestEntity(part2.getBytes());
       post1.setRequestEntity(request1);
       try {
           _client.executeMethod(post1);
           response = post1.getResponseBodyAsString();
           Element element = XmlUtils.createElement(response);
           String uploadedSize = element.getChildText("uploadedsize");
           Assert.assertEquals(String.valueOf(data.length()), uploadedSize);
       } catch (IOException e) {
           e.printStackTrace();
       }
   }

}

