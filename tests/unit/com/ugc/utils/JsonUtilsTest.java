package com.ugc.utils;

import com.ugc.domain.Application;
import com.ugc.domain.QueryObject;
import com.ugc.domain.QueryObjectList;
import com.ugc.domain.Request;
import junit.framework.Assert;
import org.json.JSONException;
import org.junit.Test;

import java.util.List;

public class JsonUtilsTest
{

    @Test
    public void testUpdateStreamObject() throws JSONException {

        //just update test
        String oldJson = "{\"data\":[{\"type\":\"long\",\"value\":\"10\",\"key\":\"key2\"},{\"type\":\"string\",\"value\":\"value1\",\"key\":\"key1\"}]}";
        String updatedJson = "{\"data\":[{\"type\":\"long\",\"value\":\"12\",\"key\":\"key2\"},{\"type\":\"string\",\"value\":\"updatedvalue1\",\"key\":\"key1\"}]}";
        String newJson = JsonUtils.updateStreamObject(updatedJson, oldJson);
        Assert.assertEquals("{\"data\":[{\"value\":\"12\",\"type\":\"long\",\"key\":\"key2\"},{\"value\":\"updatedvalue1\",\"type\":\"string\",\"key\":\"key1\"}]}", newJson);

        //update and add test
        oldJson = "{\"data\":[{\"type\":\"long\",\"value\":\"10\",\"key\":\"key2\"},{\"type\":\"string\",\"value\":\"value1\",\"key\":\"key1\"}]}";
        updatedJson = "{\"data\":[{\"type\":\"long\",\"value\":\"12\",\"key\":\"key2\"},{\"type\":\"string\",\"value\":\"updatedvalue1\",\"key\":\"key1\"}, {\"type\":\"string\",\"value\":\"newvalue\",\"key\":\"newkey\"}]}";
        newJson = JsonUtils.updateStreamObject(updatedJson, oldJson);

        Assert.assertEquals("{\"data\":[{\"value\":\"12\",\"type\":\"long\",\"key\":\"key2\"},{\"value\":\"updatedvalue1\",\"type\":\"string\",\"key\":\"key1\"},{\"value\":\"newvalue\",\"type\":\"string\",\"key\":\"newkey\"}]}", newJson);

        //test delete
        newJson = JsonUtils.deleteStreamObjectKey("key2", newJson);
        Assert.assertEquals("{\"data\":[{\"value\":\"updatedvalue1\",\"type\":\"string\",\"key\":\"key1\"},{\"value\":\"newvalue\",\"type\":\"string\",\"key\":\"newkey\"}]}", newJson);
    }
    

    @Test
    public void testConvertFromJsonToAppData() throws JSONException {

        String str = "{\"totalPush\":50, \"total\":50,\"application\":[{\"push\":11, \"requests\":10,\"date\":1340030353936},{\"push\":11, \"requests\":20,\"date\":1340030353936},{\"push\":11, \"requests\":20,\"date\":1340030353936}],\"storage\":10}";
        Application app = JsonUtils.convertAppDataToApplication(str);
        long total = app.getTotal();
        long totalPush = app.getTotalPush();
        long sTotal = app.getStorage();
        List<Request> requests = app.getRequests();
        Assert.assertEquals(50L, total);
        Assert.assertEquals(50L, totalPush);
        Assert.assertEquals(10, sTotal);
        Assert.assertEquals(3, requests.size());
    }

    @Test
    public void testAdd(){

        String oldJson = "{\"category\":\"hellotest\",\"group\":[{\"id\":\"idtest1\",\"data\":[{\"value\":\"testvalue1\",\"type\":\"string\",\"key\":\"testkey1\"}]},{\"id\":\"idtest2\",\"data\":[{\"value\":\"testvalue2\",\"type\":\"string\",\"key\":\"testkey2\"},{\"value\":100,\"type\":\"integer\",\"key\":\"testint\"}]}]}";
        String newJson= "{\"category\":\"hellotest\",\"group\":[{\"id\":\"idtest3\",\"data\":[{\"value\":\"testvalue2\",\"type\":\"string\",\"key\":\"testkey2\"},{\"value\":100,\"type\":\"integer\",\"key\":\"testint\"}]}]}";

        String expectedJson = "{\"category\":\"hellotest\",\"group\":[{\"id\":\"idtest1\",\"data\":[{\"value\":\"testvalue1\",\"type\":\"string\",\"key\":\"testkey1\"}]},{\"id\":\"idtest2\",\"data\":[{\"value\":\"testvalue2\",\"type\":\"string\",\"key\":\"testkey2\"},{\"value\":100,\"type\":\"integer\",\"key\":\"testint\"}]},{\"id\":\"idtest3\",\"data\":[{\"value\":\"testvalue2\",\"type\":\"string\",\"key\":\"testkey2\"},{\"value\":100,\"type\":\"integer\",\"key\":\"testint\"}]}]}";

        try {
            String newJsonString = JsonUtils.updateOrAddObject(oldJson, newJson);
            Assert.assertEquals(expectedJson, newJsonString);

        } catch (JSONException e) {
            
        }

    }


    @Test
    public void deleteField(){

        String oldJson = "{\"category\":\"hellotest\",\"group\":[{\"id\":\"idtest1\",\"data\":[{\"value\":\"testvalue1\",\"type\":\"string\",\"key\":\"testkey1\"}]},{\"id\":\"idtest2\",\"data\":[{\"value\":\"testvalue2\",\"type\":\"string\",\"key\":\"testkey2\"},{\"value\":100,\"type\":\"integer\",\"key\":\"testint\"}]}]}";
        String newJson = "{\"category\":\"hellotest\",\"group\":[{\"id\":\"idtest1\",\"data\":[{\"value\":\"testvalue1\",\"type\":\"string\",\"key\":\"testkey1\"}]},{\"id\":\"idtest2\",\"data\":[{\"value\":100,\"type\":\"integer\",\"key\":\"testint\"}]}]}";
        try {
            String expectedJson = JsonUtils.deleteField(oldJson, "idtest2", "testkey2");
            Assert.assertEquals(newJson, expectedJson);

            oldJson = "{\"group\":[{\"id\":\"idtest1\",\"data\":[{\"value\":\"testvalue1\",\"type\":\"string\",\"key\":\"testkey1\"}]},{\"id\":\"idtest2\",\"data\":[{\"value\":\"testvalue2\",\"type\":\"string\",\"key\":\"testkey2\"},{\"value\":100,\"type\":\"integer\",\"key\":\"testint\"}]},{\"id\":\"idtest3\",\"data\":[{\"value\":\"testvalue2\",\"type\":\"string\",\"key\":\"testkey2\"},{\"value\":100,\"type\":\"integer\",\"key\":\"testint\"}]}]}";
            newJson = "{\"group\":[{\"id\":\"idtest1\",\"data\":[{\"value\":\"testvalue1\",\"type\":\"string\",\"key\":\"testkey1\"}]},{\"id\":\"idtest2\",\"data\":[{\"value\":\"testvalue2\",\"type\":\"string\",\"key\":\"testkey2\"},{\"value\":100,\"type\":\"integer\",\"key\":\"testint\"}]},{\"id\":\"idtest3\",\"data\":[{\"value\":\"testvalue2\",\"type\":\"string\",\"key\":\"testkey2\"}]}]}";
            expectedJson = JsonUtils.deleteField(oldJson, "idtest3", "testint");
           Assert.assertEquals(expectedJson, newJson);

        } catch (JSONException e) {

        }


    }

    @Test
    public void testUpdateOrAdd() throws JSONException {
        String oldJson = "{\"group\":[{\"id\":\"id1\",\"data\":[{\"type\":\"string\",\"value\":\"testvalue1\",\"key\":\"testkey1\"}]},{\"id\":\"id2\",\"data\":[{\"type\":\"integer\",\"value\":11,\"key\":\"testkey2\"}]},{\"id\":\"id3\",\"data\":[{\"type\":\"string\",\"value\":\"testvalue\",\"key\":\"testkey3\"}]},{\"id\":\"id4\",\"data\":[{\"type\":\"string\",\"value\":\"testvalue4\",\"key\":\"testkey4\"}]},{\"id\":\"id5\",\"data\":[{\"type\":\"string\",\"value\":\"testvalue7\",\"key\":\"testkey7\"},{\"type\":\"string\",\"value\":\"testvalue6\",\"key\":\"testkey6\"},{\"type\":\"string\",\"value\":\"testvalue5\",\"key\":\"testkey5\"}]}],\"category\":\"hellohello\"}";

        String update = "{\"group\":[{\"id\":\"id2\",\"data\":[{\"type\":\"string\",\"value\":\"testvalue\",\"key\":\"testkey3\"}]}],\"category\":\"hellohello\"}";

       String newJson = JsonUtils.updateOrAddObject(oldJson, update);
        String expectedJson = "{\"category\":\"hellohello\",\"group\":[{\"id\":\"id1\",\"data\":[{\"value\":\"testvalue1\",\"type\":\"string\",\"key\":\"testkey1\"}]},{\"id\":\"id2\",\"data\":[{\"value\":\"testvalue\",\"type\":\"string\",\"key\":\"testkey3\"},{\"value\":11,\"type\":\"integer\",\"key\":\"testkey2\"}]},{\"id\":\"id3\",\"data\":[{\"value\":\"testvalue\",\"type\":\"string\",\"key\":\"testkey3\"}]},{\"id\":\"id4\",\"data\":[{\"value\":\"testvalue4\",\"type\":\"string\",\"key\":\"testkey4\"}]},{\"id\":\"id5\",\"data\":[{\"value\":\"testvalue7\",\"type\":\"string\",\"key\":\"testkey7\"},{\"value\":\"testvalue6\",\"type\":\"string\",\"key\":\"testkey6\"},{\"value\":\"testvalue5\",\"type\":\"string\",\"key\":\"testkey5\"}]}]}";
        Assert.assertEquals(expectedJson, newJson);


    }

    @Test
    public void testUpdate(){

       String oldJson = "{\"group\":[{\"id\":\"idtest1\",\"data\":[{\"value\":\"testvalue1\",\"type\":\"string\",\"key\":\"testkey1\"}]},{\"id\":\"idtest2\",\"data\":[{\"value\":\"testvalue2\",\"type\":\"string\",\"key\":\"testkey2\"},{\"value\":100,\"type\":\"integer\",\"key\":\"testint\"}]},{\"id\":\"idtest3\",\"data\":[{\"value\":\"testvalue2\",\"type\":\"string\",\"key\":\"testkey2\"},{\"value\":100,\"type\":\"integer\",\"key\":\"testint\"}]}]}";
       String newJson= "{\"category\":\"hellotest\",\"group\":[{\"id\":\"idtest3\",\"data\":[{\"value\":\"testvalue2\",\"type\":\"string\",\"key\":\"testkey2\"},{\"value\":101,\"type\":\"integer\",\"key\":\"testint\"}]}]}";

        try {
            String newJsonString = JsonUtils.updateOrAddObject(oldJson, newJson);
           // System.out.println(newJsonString);
            
        } catch (JSONException e) {

        }

    }

    @Test
    public void testDelete(){

        String oldJson = "{\"group\":[{\"id\":\"idtest1\",\"data\":[{\"value\":\"testvalue1\",\"type\":\"string\",\"key\":\"testkey1\"}]},{\"id\":\"idtest2\",\"data\":[{\"value\":\"testvalue2\",\"type\":\"string\",\"key\":\"testkey2\"},{\"value\":100,\"type\":\"integer\",\"key\":\"testint\"}]},{\"id\":\"idtest3\",\"data\":[{\"value\":\"testvalue2\",\"type\":\"string\",\"key\":\"testkey2\"},{\"value\":100,\"type\":\"integer\",\"key\":\"testint\"}]}]}";
       // String jsonIdList = "{\"ids\":[\"a\",\"b\",\"c\"]}";
        String jsonIdList = "{\"ids\":[\"idtest3\"]}";
        String expectedString = "{\"group\":[{\"id\":\"idtest1\",\"data\":[{\"value\":\"testvalue1\",\"type\":\"string\",\"key\":\"testkey1\"}]},{\"id\":\"idtest2\",\"data\":[{\"value\":\"testvalue2\",\"type\":\"string\",\"key\":\"testkey2\"},{\"value\":100,\"type\":\"integer\",\"key\":\"testint\"}]}]}";

        try {
            String newJsonString = JsonUtils.deleteObjects(oldJson, jsonIdList);
            Assert.assertEquals(newJsonString, expectedString);



        } catch (JSONException e) {
           e.printStackTrace();
        }
    }


    @Test
    public void testParseQueryList(){
        String json = "{\"logic\":\"and\",\"operators\":[{\"value\":\"testvalue\",\"key\":\"testkey\",\"operator\":\"equal\"},{\"value\":12,\"key\":\"testkey1\",\"operator\":\"greater\"}]}";
        try {
            QueryObjectList qol = JsonUtils.convertToQueryObjectList(json);
            Assert.assertEquals(2, qol.size());
            Assert.assertEquals("and", qol.getLogic());

            List<QueryObject> qos = qol.getQueryObject("testkey");
            QueryObject qo1 = qos.get(0);
            Assert.assertEquals("testvalue", qo1.getValue());

            List<QueryObject> qos1 = qol.getQueryObject("testkey1");
            QueryObject qo2 = qos1.get(0);

            Assert.assertEquals("12", qo2.getValue());



        } catch (JSONException e) {

        }

    }

    @Test
    public void testParseLimitedKeys() throws JSONException {

        String json = "{\"logic\":\"and\",\"operators\":[], \"ids\": [\"id1\", \"id2\", \"id3\"]}";
        QueryObjectList qol = JsonUtils.convertToQueryObjectList(json);
        Assert.assertEquals(0, qol.size());
        Assert.assertEquals(3, qol.limitedKeysSize());

    }




}
