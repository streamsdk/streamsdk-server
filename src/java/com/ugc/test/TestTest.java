package com.ugc.test;

import com.ugc.utils.ConversionUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class TestTest
{
    public static void main(String[] args) {

        //    createSystemUser();
        //   String res = "{\"data\":\"[{\\\"id\\\":\\\"\\\",\\\"size\\\":303,\\\"modified\\\":\\\"Thu Feb 09 15:48:11 GMT 2012\\\"},{\\\"id\\\":\\\"05A69F4FAA1143B9D20FB48AD30AEEDC\\\",\\\"size\\\":1378,\\\"modified\\\":\\\"Tue Jan 24 22:52:54 GMT 2012\\\"},{\\\"id\\\":\\\"113F1496D660DC0C7DCE2F9B8073EFBD\\\",\\\"size\\\":300,\\\"modified\\\":\\\"Wed Feb 08 18:02:06 GMT 2012\\\"},{\\\"id\\\":\\\"2D1DD0AD752D496CCA7BE91A406E3211\\\",\\\"size\\\":300,\\\"modified\\\":\\\"Wed Feb 08 18:01:14 GMT 2012\\\"},{\\\"id\\\":\\\"67461022DD86AB10681B1EBAFE6DE756\\\",\\\"size\\\":1378,\\\"modified\\\":\\\"Tue Jan 24 22:56:59 GMT 2012\\\"},{\\\"id\\\":\\\"C27099D22A2D0BCFC01F283A401710A1\\\",\\\"size\\\":359,\\\"modified\\\":\\\"Thu Feb 09 15:52:21 GMT 2012\\\"},{\\\"id\\\":\\\"FA87C68BA24E33A480A85C70F3A29C79\\\",\\\"size\\\":300,\\\"modified\\\":\\\"Thu Feb 09 12:32:30 GMT 2012\\\"}]\"}";

        /*String d = "Wed Apr 18 16:23:49 BST 2012";
        DateUtils du = new DateUtils();
        try {
            System.out.println(du.parseRfc822Date(d));
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }*/
        //createSystemUser();
        //getCategoryObjectList();
        //addToken();
        //subscribe();
        //getSubChannles();
        //getAllChannels();
        //unsub();
        //getTokensForChannel();
        //getAllUsers();
        //testCreateApp();
        //loadApps();
        //createSystemUser();
        //authFromWeb();
        //testDownload();
        String data = "{\"id\":\"9fdd148f94db413e8979549a43dcf700\",\"data\":[{\"value\":\"android\",\"type\":\"string\",\"key\":\"deviceType\"},{\"value\":[\"test1\",\"message\"],\"type\":\"array\",\"key\":\"channels\"}]}";
        final int ran[] = new int[10];
        for (int i=0; i < 10; i++){
            ran[i] = genereateId();
        }

        System.out.println("ran[0]: " + ran[0]);
        System.out.println("ran[1]: " + ran[1]);
        System.out.println("ran[2]: " + ran[2]);
        System.out.println("ran[3]: " + ran[3]);

        /*testAddToken(String.valueOf(ran[0]));
        subsc(String.valueOf(ran[0]));*/
       // subsc(String.valueOf(731563218));

        for (int i=0; i < 8; i++){

            final int i1 = i;
            Thread t = new Thread(new Runnable(){
                  public void run() {
                      testAddToken(String.valueOf(ran[i1]));
                }
            });
            t.start();
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {

        }


       Thread s1 = new Thread(new Runnable(){
            public void run() {
                subsc(String.valueOf(1387760129));
            }
        });

        Thread s2 = new Thread(new Runnable(){
            public void run() {
                subsc(String.valueOf(1509917905));
            }
        });

        Thread s3 = new Thread(new Runnable(){
            public void run() {
                subsc(String.valueOf(1109186601));
            }
        });

        Thread s4 = new Thread(new Runnable(){
            public void run() {
                subsc(String.valueOf(1580126917));
            }
        });

        Thread s5 = new Thread(new Runnable(){
            public void run() {
                subsc(String.valueOf(1234622405));
            }
        });

        Thread s6 = new Thread(new Runnable(){
            public void run() {
                unsubsc(String.valueOf(1580126917));
            }
        });

        Thread s7 = new Thread(new Runnable(){
            public void run() {
                unsubsc(String.valueOf(1421716180));
            }
        });


       Thread s8 = new Thread(new Runnable(){
            public void run() {
                unsubsc(String.valueOf(2096958162));
            }
        });

        s1.start();
        s2.start();
        s3.start();
        s4.start();
        s5.start();
        //s6.start();
        //s7.start();
        //s8.start();



        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {

        }


    }

    private static int genereateId() {
        Random foo = new Random();
        int randomNumber = foo.nextInt(Integer.MAX_VALUE);
        return randomNumber;
    }

    private static void testAddToken(String token){


        PostMethod post = new PostMethod("http://localhost:8018/print/notificationandroid/token/B1F983D26BFC33F7168A992A69411A0D");
        post.addParameter("token", token);
        HttpClient client = new HttpClient();
        try {
            client.executeMethod(post);
            String respones = post.getResponseBodyAsString();
            System.out.println(respones);
        } catch (IOException e) {

        }

    }

     private static void unsubsc(String token){

       PostMethod post = new PostMethod("http://localhost:8018/print/notification/unsubscribe/channel/B1F983D26BFC33F7168A992A69411A0D");
        post.addParameter("token", token);
        post.addParameter("channel", "testchannel");
        HttpClient client = new HttpClient();
        try {
            client.executeMethod(post);
            String respones = post.getResponseBodyAsString();
            System.out.println(respones);
        } catch (IOException e) {

        }
    }

    private static void subsc(String token){

       PostMethod post = new PostMethod("http://localhost:8018/print/notification/subscribe/channel/B1F983D26BFC33F7168A992A69411A0D");
        post.addParameter("token", token);
        post.addParameter("channel", "testchannel");
        HttpClient client = new HttpClient();
        try {
            client.executeMethod(post);
            String respones = post.getResponseBodyAsString();
            System.out.println(respones);
        } catch (IOException e) {

        }
    }

    private static void testDownload(){

        GetMethod get = new GetMethod("http://localhost:8018/print/export/3FF513E1FC2888988C20F78A528F0F41");
        HttpClient client = new HttpClient();
        try {
            client.executeMethod(get);
          
        } catch (IOException e) {

        }

    }


    private static void authFromWeb(){

        PostMethod post = new PostMethod("http://localhost:8018/print/systemuser/signin/web");
        post.addParameter("userName", "test");
        post.addParameter("password", "password");
        HttpClient client = new HttpClient();
        try {
            client.executeMethod(post);
            String respones = post.getResponseBodyAsString();
            System.out.println(respones);
        } catch (IOException e) {

        }
    }

    private static void loadApps(){

        GetMethod get = new GetMethod("http://localhost:8018/print/systemuser/load/application/fsakauiowaetrnaiosviaew");
        HttpClient client = new HttpClient();
        try {
            client.executeMethod(get);
            String respones = get.getResponseBodyAsString();
            System.out.println(respones);
        } catch (IOException e) {

        }

    }


    private static void testCreateApp(){

        PostMethod post = new PostMethod("http://localhost:8018/print/systemuser/create/application/fsakauiowaetrnaiosviaew");
        HttpClient client = new HttpClient();
        try {
            client.executeMethod(post);
            String respones = post.getResponseBodyAsString();
            System.out.println(respones);
        } catch (IOException e) {

        }

    }

    private static void subStrTest() {
        System.out.println(dateConvert(1347612239052L));
        System.out.println(dateConvert(1346612239052L));
        System.out.println(dateConvert(1349612239052L));
        System.out.println(dateConvert(1357612239052L));
        System.out.println(dateConvert(1307612239052L));
        System.out.println(dateConvert(1327612239052L));

        Object valueObject = "1327612239052";
        String qo = "1337637458";
        String currentStringValue = "";
        if (valueObject.toString().length() > qo.length())
            currentStringValue = valueObject.toString().substring(0, qo.length());
        else
            currentStringValue = valueObject.toString();

        System.out.println(currentStringValue);

    }


    private static String dateConvert(long millionSeconds) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy HH:mm");
        Date resultdate = new Date(millionSeconds);
        String str = sdf.format(resultdate);
        return str;
    }


    private static void getAllUsers() {

        GetMethod get = new GetMethod("http://localhost:8018/print/useruser/allusers/3FF513E1FC2888988C20F78A528F0F41");
        HttpClient client = new HttpClient();
        try {
            client.executeMethod(get);
            String respones = get.getResponseBodyAsString();
            System.out.println(respones);
        } catch (IOException e) {

        }


    }


    private static void unsub() {
        PostMethod post = new PostMethod("http://localhost:8080/print/notification/unsubscribe/channel/3FF513E1FC2888988C20F78A528F0F41");
        post.addParameter("token", "testtoken4");
        post.addParameter("channel", "testchannelA");
        HttpClient client = new HttpClient();
        try {
            client.executeMethod(post);
            String respones = post.getResponseBodyAsString();
            System.out.println(respones);
        } catch (IOException e) {

        }
    }

    private static void getTokensForChannel() {

        GetMethod get = new GetMethod("http://localhost:8080/print/notification/get/tokens/3FF513E1FC2888988C20F78A528F0F41?channel=testchannelA");
        HttpClient client = new HttpClient();
        try {
            client.executeMethod(get);
            String respones = get.getResponseBodyAsString();
            System.out.println(respones);
        } catch (IOException e) {

        }

    }

    private static void getAllChannels() {

        GetMethod get = new GetMethod("http://localhost:8080/print/notification/getall/channels/3FF513E1FC2888988C20F78A528F0F41");
        HttpClient client = new HttpClient();
        try {
            client.executeMethod(get);
            String respones = get.getResponseBodyAsString();
            System.out.println(respones);
        } catch (IOException e) {

        }

    }

    private static void getSubChannles() {

        GetMethod get = new GetMethod("http://localhost:8080/print/notification/get/channels/3FF513E1FC2888988C20F78A528F0F41?token=testtoken2");
        HttpClient client = new HttpClient();
        try {
            client.executeMethod(get);
            String respones = get.getResponseBodyAsString();
            System.out.println(respones);
        } catch (IOException e) {

        }

    }

    private static void subscribe() {

        PostMethod post = new PostMethod("http://localhost:8080/print/notification/subscribe/channel/3FF513E1FC2888988C20F78A528F0F41");
        post.addParameter("token", "testtoken1");
        post.addParameter("channel", "testchannelC");
        HttpClient client = new HttpClient();
        try {
            client.executeMethod(post);
            String respones = post.getResponseBodyAsString();
            System.out.println(respones);
        } catch (IOException e) {

        }
    }

    private static void addToken() {
        PostMethod post = new PostMethod("http://localhost:8080/print/notification/token/3FF513E1FC2888988C20F78A528F0F41");
        post.addParameter("token", "testtoken4");
        HttpClient client = new HttpClient();
        try {
            client.executeMethod(post);
            String respones = post.getResponseBodyAsString();
            System.out.println(respones);
        } catch (IOException e) {

        }


    }

    private static void sub() {

    }

    private static String covert(String uid) {


        try {
            return ConversionUtils.createMD5Hash(uid, true);
        } catch (NoSuchAlgorithmException e) {

        }
        return "";
    }

    public static void createSystemUser() {
        PostMethod post = new PostMethod("http://localhost:8018/print/systemuser/create");
        post.addParameter("userName", "test");
        post.addParameter("password", "password");
        HttpClient client = new HttpClient();
        try {
            client.executeMethod(post);
            String respones = post.getResponseBodyAsString();
            System.out.println(respones);
        } catch (IOException e) {

        }
    }

    private static void testDeleteAll() {
        GetMethod get = new GetMethod("http://localhost:8080/print/get/deleteobject/test");
        HttpClient client = new HttpClient();
        try {
            client.executeMethod(get);
            String respones = get.getResponseBodyAsString();
            System.out.println(respones);
        } catch (IOException e) {

        }


    }

    private static void getCategoryObjectList() {
        GetMethod get = new GetMethod("http://localhost:8080/print/get/categoryobjectlist/3FF513E1FC2888988C20F78A528F0F41");
        HttpClient client = new HttpClient();
        try {
            client.executeMethod(get);
            String respones = get.getResponseBodyAsString();
            System.out.println(respones);
        } catch (IOException e) {

        }

    }

    private static void getObjectList() {

        GetMethod get = new GetMethod("http://localhost:8080/print/get/filelist/test2");
        HttpClient client = new HttpClient();
        try {
            client.executeMethod(get);
            String respones = get.getResponseBodyAsString();
            System.out.println(respones);
        } catch (IOException e) {

        }


    }


    private static void getObject() {
        GetMethod get = new GetMethod("http://localhost:8080/print/get/object/05A69F4FAA1143B9D20FB48AD30AEEDC/test1");
        HttpClient client = new HttpClient();
        try {
            client.executeMethod(get);
            String respones = get.getResponseBodyAsString();
            System.out.println(respones);
        } catch (IOException e) {

        }

    }


    private static void sendPost() {

        PostMethod post = new PostMethod("http://localhost:8080/print/store/object/test1");
        String json = "{\"data\":[{\"name\":\"Sven Siorpaes wang shuai\",\"id\":\"507554463\"},{\"name\":\"Nagappan Arunachalam\",\"id\":\"509741059\"},{\"name\":\"Nilesh Joshi\",\"id\":\"510584401\"},{\"name\":\"Robert Sinnott\",\"id\":\"523808151\"},{\"name\":\"Judy Niuniu\",\"id\":\"537196370\"},{\"name\":\"Ningning Shi\",\"id\":\"553322001\"},{\"name\":\"Ray Gallagher\",\"id\":\"556964973\"},{\"name\":\"Victor Issel Olvera-Smith\",\"id\":\"568753059\"},{\"name\":\"Yang Song\",\"id\":\"592797708\"},{\"name\":\"Jinesh Krishnan\",\"id\":\"612163266\"},{\"name\":\"Declan Meaney\",\"id\":\"614406920\"},{\"name\":\"Guy Granger\",\"id\":\"617340057\"},{\"name\":\"Yaou Li\",\"id\":\"625989399\"},{\"name\":\"John Stanley\",\"id\":\"626434424\"},{\"name\":\"Alex Waskiewicz\",\"id\":\"629791184\"},{\"name\":\"Tongkun Cui\",\"id\":\"631328218\"},{\"name\":\"John Fitzgerald\",\"id\":\"632117896\"},{\"name\":\"Salvatore Fanara\",\"id\":\"684080126\"},{\"name\":\"Ray Gallagher\",\"id\":\"702426655\"},{\"name\":\"Mick Morrissey\",\"id\":\"759133535\"},{\"name\":\"Darren Redmond\",\"id\":\"771968845\"},{\"name\":\"Han Zhe\",\"id\":\"805089043\"},{\"name\":\"Hua Guo\",\"id\":\"1018223421\"},{\"name\":\"Karl Tiller\",\"id\":\"1140917794\"},{\"name\":\"Bob Testtest\",\"id\":\"1297489353\"},{\"name\":\"Heyuan Li\",\"id\":\"1315795231\"},{\"name\":\"Prasad Prabha\",\"id\":\"1315798293\"},{\"name\":\"Hao Gao\",\"id\":\"1321914968\"},{\"name\":\"Rob Sin\",\"id\":\"1356276285\"},{\"name\":\"Trevor-anderson Felicity Candice\",\"id\":\"1385867307\"},{\"name\":\"Sebastian G\\u0142owacki\",\"id\":\"1390090116\"},{\"name\":\"Guo Du\",\"id\":\"1628168014\"}]}";
        post.addParameter("json", json);
        HttpClient client = new HttpClient();
        try {
            client.executeMethod(post);
            String respones = post.getResponseBodyAsString();
            System.out.println(respones);
        } catch (IOException e) {

        }


    }

}
