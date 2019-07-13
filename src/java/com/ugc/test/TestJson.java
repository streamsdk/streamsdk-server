package com.ugc.test;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.Date;


public class TestJson
{
    public static void main(String[] args) throws JSONException {

         JSONObject application = new JSONObject();

        try {
            Date now = new Date();
            JSONArray array = new JSONArray();
            JSONObject requests1 = new JSONObject();
            requests1.put("requests", 10L);
            requests1.put("date", now.getTime());
            JSONObject requests2 = new JSONObject();
            requests2.put("requests", 20L);
            requests2.put("date", now.getTime());
            JSONObject requests3 = new JSONObject();
            requests3.put("requests", 20L);
            requests3.put("date", now.getTime());
            array.put(requests1);
            array.put(requests2);
            array.put(requests3);
            application.put("application", array);
            application.put("total", 50L);
            application.put("storage", 10L);

        } catch (JSONException e) {

        }

        String json = application.toString();
        System.out.println(json);
    }

    private static void someTests1() throws JSONException {

        JSONObject idChannel = new JSONObject();
        idChannel.put("id", "testchannel");

        JSONArray channelArray = new JSONArray();
        channelArray.put(idChannel);

        JSONObject channels = new JSONObject();
        channels.put("channels", channelArray);

        String s = "{\"operators\":[{\"value\":\"testvalue\",\"operator\":\"equal\",\"key\":\"testkey\"},{\"value\":10,\"operator\":\"greater\",\"key\":\"testkey1\"},{\"value\":\"testvalue1\",\"operator\":\"equal\",\"key\":\"testkey2\"},{\"value\":[\"a\",\"b\",\"c\"],\"operator\":\"contains\",\"key\":\"testkey3\"}],\"logic\":\"and\"}";
        System.out.println(channels.toString());


    }


    private static void createGroup() throws JSONException {


        JSONObject data = new JSONObject();
        data.put("type", "array");
        data.put("key", "channels");
        JSONArray ja = new JSONArray();
        data.put("value", ja);
        JSONArray dataArray = new JSONArray();
        dataArray.put(data);


        JSONObject groupObject = new JSONObject();
        groupObject.put("id", "testtoekn");
        groupObject.put("data", dataArray);
        JSONArray groupObjectArray = new JSONArray();
        groupObjectArray.put(groupObject);


        JSONObject group = new JSONObject();
        group.put("category", "pushChannels");
        group.put("group", groupObjectArray);

        System.out.println(group.toString());
    }


    private static void queryLogicTest() throws JSONException {

        String v[] = {"a", "b", "c"};

        JSONStringer jsonObject = new JSONStringer();
        jsonObject.array();

        jsonObject.object().key("operator").value("contains");
        jsonObject.key("key").value("testkey");
        jsonObject.key("value").value(v);
        jsonObject.endObject();

        jsonObject.object().key("operator").value("greater");
        jsonObject.key("key").value("testkey1");
        jsonObject.key("value").value(12);
        jsonObject.endObject();


        jsonObject.endArray();

        JSONArray array = new JSONArray(jsonObject.toString());
        JSONObject o = new JSONObject();
        o.put("opertors", array);
        o.put("logic", "and");

        String json = o.toString();

        System.out.println(json);
    }


    private static void someTests() throws JSONException {
/*
          String res = "{\"data\":[{\"type\":\"integer\",\"value\":100,\"key\":\"testintkey\"},{\"type\":\"string\",\"value\":\"testvalue3\",\"key\":\"testkey3\"},{\"type\":\"string\",\"value\":\"testvalue2\",\"key\":\"testkey2\"},{\"type\":\"integer\",\"value\":100000,\"key\":\"testlongkey\"},{\"type\":\"string\",\"value\":\"testvalue1\",\"key\":\"testkey1\"},{\"value\":[\"a\",\"b\",\"c\"],\"type\":\"array\",\"key\":\"jObject2\"},{\"value\":{\"value\":\"a\",\"id\":\"1\"},\"type\":\"object\",\"key\":\"jObject1\"}]}";
          JSONObject jo = new JSONObject(res);
          JSONArray data = jo.getJSONArray("data");*/

        /*int size = data.length();
         for (int i=0; i < size; i++){
             JSONObject jObject = data.getJSONObject(i);
             String key = jObject.getString("key");
             System.out.println(key);
         }*/

        String json = "{\"group\":[{\"data\":[{\"type\":\"string\",\"value\":\"testvalue1\",\"key\":\"testkey1\"}]},{\"data\":[{\"type\":\"string\",\"value\":\"testvalue2\",\"key\":\"testkey2\"},{\"type\":\"integer\",\"value\":100,\"key\":\"testint\"}]}],\"category\":\"hellotest\"}";
        JSONObject jo = new JSONObject(json);
        JSONArray data = jo.getJSONArray("group");
        int size = data.length();
        for (int i = 0; i < size; i++) {
            JSONObject jobject = data.getJSONObject(i);
            JSONArray ja = jobject.getJSONArray("data");
            int length = ja.length();
            for (int j = 0; j < length; j++) {
                JSONObject jObject = ja.getJSONObject(j);
                String key = jObject.getString("key");
                //              System.out.println("key: "  +  key);
            }

        }

        String test = "{\"category\":\"hellotest\",\"group\":[{\"id\":\"idtest\",\"data\":[{\"value\":\"testvalue1\",\"type\":\"string\",\"key\":\"testkey1\"}]},{\"id\":\"idtest\",\"data\":[{\"value\":\"testvalue2\",\"type\":\"string\",\"key\":\"testkey2\"},{\"value\":100,\"type\":\"integer\",\"key\":\"testint\"}]}]}";
        for (int i = 0; i < size; i++) {
            JSONObject jobject = data.getJSONObject(i);
            jobject.put("id", "idtest");
        }

        String js = jo.toString();
        System.out.println(js);

        for (int i = 0; i < size; i++) {
            JSONObject jobject = data.getJSONObject(i);
            String str = jobject.getString("id");
            System.out.println(str);
        }


        String j = "{\"data\":[{\"type\":\"boolean\",\"value\":false,\"key\":\"testbool\"},{\"type\":\"string\",\"value\":\"testvalue3\",\"key\":\"testkey3\"},{\"type\":\"string\",\"value\":\"testvalue2\",\"key\":\"testkey2\"},{\"type\":\"string\",\"value\":\"testvalue1\",\"key\":\"testkey1\"},{\"value\":[\"a\",\"b\",\"c\"],\"type\":\"array\",\"key\":\"jObject2\"},{\"value\":{\"value\":\"a\",\"id\":\"1\"},\"type\":\"object\",\"key\":\"jObject1\"},{\"type\":\"integer\",\"value\":100,\"key\":\"testintkey\"},{\"type\":\"integer\",\"value\":100000,\"key\":\"testlongkey\"}]}";

    }


    private static void testAddNewObject() throws JSONException {

        String json = "{\"data\":[{\"name\":\"Sven Siorpaes\",\"id\":\"507554464\"},{\"name\":\"Ningning Shi\",\"id\":\"553322001\"},{\"name\":\"Hua Guo\",\"id\":\"1018223421\"},{\"name\":\"Karl Tiller\",\"id\":\"1140917794\"},{\"name\":\"Bob Testtest\",\"id\":\"1297489353\"},{\"name\":\"Heyuan Li\",\"id\":\"1315795231\"},{\"name\":\"Guo Du\",\"id\":\"1628168014\"}]}";
        JSONObject friends = new JSONObject(json);
        friends.put("userCreationTime", "now");
        System.out.println(friends.toString());
    }


    private static void testParse() throws JSONException {

        String res = "{\"data\":[{\"name\":\"Sven Siorpaes\",\"id\":\"507554464\"},{\"name\":\"Nagappan Arunachalam\",\"id\":\"509741059\"},{\"name\":\"Nilesh Joshi\",\"id\":\"510584401\"},{\"name\":\"Robert Sinnott\",\"id\":\"523808151\"},{\"name\":\"Judy Niuniu\",\"id\":\"537196370\"},{\"name\":\"Ningning Shi\",\"id\":\"553322001\"},{\"name\":\"Ray Gallagher\",\"id\":\"556964973\"},{\"name\":\"Victor Issel Olvera-Smith\",\"id\":\"568753059\"},{\"name\":\"Yang Song\",\"id\":\"592797708\"},{\"name\":\"Jinesh Krishnan\",\"id\":\"612163266\"},{\"name\":\"Declan Meaney\",\"id\":\"614406920\"},{\"name\":\"Guy Granger\",\"id\":\"617340057\"},{\"name\":\"Yaou Li\",\"id\":\"625989399\"},{\"name\":\"John Stanley\",\"id\":\"626434424\"},{\"name\":\"Alex Waskiewicz\",\"id\":\"629791184\"},{\"name\":\"Tongkun Cui\",\"id\":\"631328218\"},{\"name\":\"John Fitzgerald\",\"id\":\"632117896\"},{\"name\":\"Salvatore Fanara\",\"id\":\"684080126\"},{\"name\":\"Ray Gallagher\",\"id\":\"702426655\"},{\"name\":\"Mick Morrissey\",\"id\":\"759133535\"},{\"name\":\"Darren Redmond\",\"id\":\"771968845\"},{\"name\":\"Han Zhe\",\"id\":\"805089043\"},{\"name\":\"Hua Guo\",\"id\":\"1018223421\"},{\"name\":\"Karl Tiller\",\"id\":\"1140917794\"},{\"name\":\"Bob Testtest\",\"id\":\"1297489353\"},{\"name\":\"Heyuan Li\",\"id\":\"1315795231\"},{\"name\":\"Prasad Prabha\",\"id\":\"1315798293\"},{\"name\":\"Hao Gao\",\"id\":\"1321914968\"},{\"name\":\"Rob Sin\",\"id\":\"1356276285\"},{\"name\":\"Trevor-anderson Felicity Candice\",\"id\":\"1385867307\"},{\"name\":\"Sebastian G\\u0142owacki\",\"id\":\"1390090116\"},{\"name\":\"Guo Du\",\"id\":\"1628168014\"}]}";


        String response = "{\"data\":[{\"id\":\"10150089124974465\",\"from\":{\"name\":\"Sven Siorpaes\",\"id\":\"507554464\"},\"name\":\"Profile Pictures\",\"link\":\"http:\\/\\/www.facebook.com\\/album.php?aid=276409&id=507554464\",\"count\":1,\"type\":\"profile\",\"created_time\":\"2010-08-24T15:37:36+0000\"},{\"id\":\"69586354464\",\"from\":{\"name\":\"Sven Siorpaes\",\"id\":\"507554464\"},\"name\":\"wedding\",\"location\":\"munich\",\"link\":\"http:\\/\\/www.facebook.com\\/album.php?aid=81187&id=507554464\",\"count\":12,\"type\":\"normal\",\"created_time\":\"2009-01-09T06:05:01+0000\",\"updated_time\":\"2009-01-09T06:29:59+0000\"},{\"id\":\"24829149464\",\"from\":{\"name\":\"Sven Siorpaes\",\"id\":\"507554464\"},\"name\":\"St. Patricks Day Munich\",\"link\":\"http:\\/\\/www.facebook.com\\/album.php?aid=30395&id=507554464\",\"count\":9,\"type\":\"normal\",\"created_time\":\"2008-03-18T20:06:56+0000\",\"updated_time\":\"2008-03-18T20:30:50+0000\"},{\"id\":\"18982564464\",\"from\":{\"name\":\"Sven Siorpaes\",\"id\":\"507554464\"},\"name\":\"random\",\"link\":\"http:\\/\\/www.facebook.com\\/album.php?aid=13266&id=507554464\",\"count\":14,\"type\":\"normal\",\"created_time\":\"2007-09-27T16:07:35+0000\",\"updated_time\":\"2009-01-05T12:53:48+0000\"},{\"id\":\"17466349464\",\"from\":{\"name\":\"Sven Siorpaes\",\"id\":\"507554464\"},\"name\":\"My first album\",\"description\":\"impressions\",\"location\":\"ireland\",\"link\":\"http:\\/\\/www.facebook.com\\/album.php?aid=167&id=507554464\",\"count\":3,\"type\":\"normal\",\"created_time\":\"2006-11-02T17:30:04+0000\",\"updated_time\":\"2006-11-02T17:31:48+0000\"}],\"paging\":{\"previous\":\"https:\\/\\/graph.facebook.com\\/507554464\\/albums?format=json&access_token=jTmZkNApT-ndCaHRdwFCIZuo3JqjJvEb0Qmn82Qt3ow.eyJpdiI6ImdGUnRueHIxQ0tfUDVfakFRRjgwZUEifQ.yKaRZ7HCGInpkOS9eiKd_CZzVObrPRwSCbnrO9vG15pDHzJqZJwIEgFNGTz5EslSk_dQCNiP2OSzIZAcpuXPJaUkxhXJxbugI8TWMy8phMUolTBd5tVrDgvvSv9KTyQBEhlwyPsAHeCBAhZvxk6O7Q&limit=25&since=2010-08-24T15%3A37%3A36%2B0000\",\"next\":\"https:\\/\\/graph.facebook.com\\/507554464\\/albums?format=json&access_token=jTmZkNApT-ndCaHRdwFCIZuo3JqjJvEb0Qmn82Qt3ow.eyJpdiI6ImdGUnRueHIxQ0tfUDVfakFRRjgwZUEifQ.yKaRZ7HCGInpkOS9eiKd_CZzVObrPRwSCbnrO9vG15pDHzJqZJwIEgFNGTz5EslSk_dQCNiP2OSzIZAcpuXPJaUkxhXJxbugI8TWMy8phMUolTBd5tVrDgvvSv9KTyQBEhlwyPsAHeCBAhZvxk6O7Q&limit=25&until=2006-11-02T17%3A30%3A03%2B0000\"}}";
        JSONObject friends = new JSONObject(res);
        JSONArray data = friends.getJSONArray("data");
        int size = data.length();
        for (int i = 0; i < size; i++) {
            JSONObject friend = data.getJSONObject(i);
            String name = friend.getString("name");
            String id = friend.getString("id");
            System.out.println("name: " + name);
            System.out.println("id: " + id);


        }

    }


    private static void createArrayOfJson() throws JSONException {

        String res = "{\"data\":[{\"name\":\"Sven Siorpaes\",\"id\":\"507554464\"},{\"name\":\"Ningning Shi\",\"id\":\"553322001\"},{\"name\":\"Hua Guo\",\"id\":\"1018223421\"},{\"name\":\"Karl Tiller\",\"id\":\"1140917794\"},{\"name\":\"Bob Testtest\",\"id\":\"1297489353\"},{\"name\":\"Heyuan Li\",\"id\":\"1315795231\"},{\"name\":\"Guo Du\",\"id\":\"1628168014\"}]}";
        JSONStringer jsonObject = new JSONStringer();
        jsonObject.array();
        jsonObject.object().key("object").value(res);
        jsonObject.endObject();
        jsonObject.object().key("object").value(res);
        jsonObject.endObject();
        jsonObject.endArray();
        String response = jsonObject.toString();
        System.out.println(response);


    }

    private static void createJson() throws JSONException {

        JSONStringer jsonObject = new JSONStringer();
        jsonObject.array();

        jsonObject.object().key("fileName").value("name1");
        jsonObject.key("link").value("link1");
        jsonObject.key("version").value("v1");
        jsonObject.endObject();

        jsonObject.object().key("fileName").value("name2");
        jsonObject.key("link").value("link2");
        jsonObject.key("version").value("v2");
        jsonObject.endObject();

        jsonObject.endArray();
        String response = jsonObject.toString();
        System.out.println(response);

    }
}
