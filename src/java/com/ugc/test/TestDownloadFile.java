package com.ugc.test;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

import java.io.*;

public class TestDownloadFile
{

    private static final int BUFFER_SIZE = 1024 * 10;
    public static void main(String[]args){

            testDownload();

    }


    private static void testDownload(){

        GetMethod get = new GetMethod("http://localhost:8018/print/get/file/C06B58F6700462E5597E94723D7B105C/8f8d2c6d9fc644e2b27871e79a42d423");
        HttpClient client = new HttpClient();
        try {
            client.executeMethod(get);
            InputStream bodyAsStream = get.getResponseBodyAsStream();
            File file = new File("c:\\issues\\download.jpg");
            file.createNewFile();
            long count = copy(bodyAsStream, new FileOutputStream(file));
            System.out.println("hello");

        } catch (IOException e) {
             System.out.println("hello");
        } catch (Exception e) {
             System.out.println("hello");
        }

    }

    public static long copy(InputStream input, OutputStream output) throws Exception, IOException {
          byte[] buffer = new byte[BUFFER_SIZE];

          BufferedInputStream in = new BufferedInputStream(input, BUFFER_SIZE);
          BufferedOutputStream out = new BufferedOutputStream(output, BUFFER_SIZE);
          int count = 0, n = 0;
          try {
                  while ((n = in.read(buffer, 0, BUFFER_SIZE)) != -1) {
                          out.write(buffer, 0, n);
                          count += n;
                  }
                  out.flush();
          } finally {
                  try {
                          out.close();
                  } catch (IOException e) {
                  }
                  try {
                          in.close();
                  } catch (IOException e) {
                  }
          }
          return count;
  }

}
