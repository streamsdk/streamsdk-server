package com.ugc.facade;

import junit.framework.Assert;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;


public class StreamObjectCacheFacadeImpTest
{

    @Test
    public void testCacheObjectPutGet(){

        StreamObjectCacheFacadeImp socf = new StreamObjectCacheFacadeImp(new MockFlushQueueFacade());

        for (int i=0; i < 50001; i++){
            socf.cacheObject(String.valueOf(i), "json" + String.valueOf(i));
        }

        String value = socf.getObject("0");
        Assert.assertEquals("json0", value);

        socf.cacheObject("50001", "json5001");


        value = socf.getObject("0");
        Assert.assertNull(value);


    }

    @Test
    public void testCacheJsonObject() throws JSONException {

       String res = "{\"data\":[{\"type\":\"integer\",\"value\":100,\"key\":\"testintkey\"},{\"type\":\"string\",\"value\":\"testvalue3\",\"key\":\"testkey3\"},{\"type\":\"string\",\"value\":\"testvalue2\",\"key\":\"testkey2\"},{\"type\":\"integer\",\"value\":100000,\"key\":\"testlongkey\"},{\"type\":\"string\",\"value\":\"testvalue1\",\"key\":\"testkey1\"},{\"value\":[\"a\",\"b\",\"c\"],\"type\":\"array\",\"key\":\"jObject2\"},{\"value\":{\"value\":\"a\",\"id\":\"1\"},\"type\":\"object\",\"key\":\"jObject1\"}]}";
       JSONObject jo = new JSONObject(res);
       StreamObjectCacheFacadeImp socf = new StreamObjectCacheFacadeImp(new MockFlushQueueFacade());

       socf.cacheObject("testcache1", res);

       String json = socf.getUpdatedObjectJson("testcache1");
       Assert.assertNull(json);

       String j = socf.getObject("testcache1");
       Assert.assertNotNull(j);

       res = "{\"data\":[{\"type\":\"integer\",\"value\":100,\"key\":\"testintkey\"},{\"type\":\"string\",\"value\":\"testvalue4\",\"key\":\"testkey3\"},{\"type\":\"string\",\"value\":\"testvalue2\",\"key\":\"testkey2\"},{\"type\":\"integer\",\"value\":100000,\"key\":\"testlongkey\"},{\"type\":\"string\",\"value\":\"testvalue1\",\"key\":\"testkey1\"},{\"value\":[\"a\",\"b\",\"c\"],\"type\":\"array\",\"key\":\"jObject2\"},{\"value\":{\"value\":\"a\",\"id\":\"1\"},\"type\":\"object\",\"key\":\"jObject1\"}]}";
       socf.updateCache("testcache1", res);

       json = socf.getUpdatedObjectJson("testcache1");
       Assert.assertNotNull(json);
       
    }


}
