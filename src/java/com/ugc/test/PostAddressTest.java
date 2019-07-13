package com.ugc.test;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class PostAddressTest
{
    private HttpClient _client;

   @Before
   public void setUp()
   {
      _client = new HttpClient();
   }

  @Test
   public void postAddressTest(){

      String address = "to dublin";
      String userId = "test2";
      String fileId = "59b63268c9634ecca9e51febd32cce57";
      String url = "http://localhost:8080/print/send/file/"+ userId + "/" + fileId;
      PostMethod post = new PostMethod(url);
      post.addParameter("json", address);
    /*  ByteArrayRequestEntity request = null;

          String addressInfo = sender + address;
          byte b[] = addressInfo.getBytes();
          request = new ByteArrayRequestEntity(b);

      post.setRequestEntity(request);*/
      try {
          _client.executeMethod(post);
          String response = post.getResponseBodyAsString();
          System.out.println("res: " + response);
      } catch (IOException e) {
          e.printStackTrace();
      }
  }
}
