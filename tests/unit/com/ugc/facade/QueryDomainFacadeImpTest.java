package com.ugc.facade;

import com.ugc.utils.JsonUtils;
import junit.framework.Assert;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Test;

import java.util.Map;


public class QueryDomainFacadeImpTest
{

    @Test
    public void testGeoQueryWithOrQuery(){

        String json = "{\"group\":[{\"id\":\"london\",\"data\":[{\"type\":\"integer\",\"value\":1111,\"key\":\"history\"},{\"value\":\"51.511214,-0.119824\",\"type\":\"geo\",\"key\":\"location\"},{\"type\":\"string\",\"value\":\"\",\"key\":\"reserved.streamsdktoken\"}],\"modified\":1362654648963},{\"id\":\"dublinCity\",\"data\":[{\"type\":\"integer\",\"value\":999,\"key\":\"history\"},{\"value\":\"53.349805,-6.26031\",\"type\":\"geo\",\"key\":\"location\"},{\"type\":\"string\",\"value\":\"\",\"key\":\"reserved.streamsdktoken\"}],\"modified\":1362654648964},{\"id\":\"upperG\",\"data\":[{\"type\":\"integer\",\"value\":888,\"key\":\"history\"},{\"value\":\"53.351772,-6.256357\",\"type\":\"geo\",\"key\":\"location\"},{\"type\":\"string\",\"value\":\"\",\"key\":\"reserved.streamsdktoken\"}],\"modified\":1362654648965},{\"id\":\"lowerG\",\"data\":[{\"type\":\"integer\",\"value\":777,\"key\":\"history\"},{\"value\":\"53.357897,-6.260192\",\"type\":\"geo\",\"key\":\"location\"},{\"type\":\"string\",\"value\":\"\",\"key\":\"reserved.streamsdktoken\"}],\"modified\":1362654648965},{\"id\":\"shankill\",\"data\":[{\"type\":\"integer\",\"value\":666,\"key\":\"history\"},{\"value\":\"53.233266,-6.123758\",\"type\":\"geo\",\"key\":\"location\"},{\"type\":\"string\",\"value\":\"\",\"key\":\"reserved.streamsdktoken\"}],\"modified\":1362654648966},{\"id\":\"cork\",\"data\":[{\"type\":\"integer\",\"value\":555,\"key\":\"history\"},{\"value\":\"51.896892,-8.486316\",\"type\":\"geo\",\"key\":\"location\"},{\"type\":\"string\",\"value\":\"\",\"key\":\"reserved.streamsdktoken\"}],\"modified\":1362654648967},{\"id\":\"paris\",\"data\":[{\"type\":\"integer\",\"value\":444,\"key\":\"history\"},{\"value\":\"48.856614,2.352222\",\"type\":\"geo\",\"key\":\"location\"},{\"type\":\"string\",\"value\":\"\",\"key\":\"reserved.streamsdktoken\"}],\"modified\":1362654648968}],\"category\":\"gps\"}";
        String query = "{\"ids\":[],\"operators\":[{\"value\":\"53.357897,-6.260192,10\",\"operator\":\"near\",\"key\":\"location\"},{\"value\":900,\"operator\":\"greater\",\"key\":\"history\"}],\"logic\":\"or\"}";
        QueryDomainFacadeImp qdf = new QueryDomainFacadeImp();
        try {
            Map<String, JSONArray> arrayMap = qdf.findMatchingObjects(json, query);
            Assert.assertEquals(2, arrayMap.size());

        } catch (JSONException e) {

        }


    }


    @Test
    public void testGeoQueryWithOtherQueryAnd(){

        String json = "{\"group\":[{\"id\":\"london\",\"data\":[{\"type\":\"integer\",\"value\":1111,\"key\":\"history\"},{\"value\":\"51.511214,-0.119824\",\"type\":\"geo\",\"key\":\"location\"},{\"type\":\"string\",\"value\":\"\",\"key\":\"reserved.streamsdktoken\"}],\"modified\":1362654648963},{\"id\":\"dublinCity\",\"data\":[{\"type\":\"integer\",\"value\":999,\"key\":\"history\"},{\"value\":\"53.349805,-6.26031\",\"type\":\"geo\",\"key\":\"location\"},{\"type\":\"string\",\"value\":\"\",\"key\":\"reserved.streamsdktoken\"}],\"modified\":1362654648964},{\"id\":\"upperG\",\"data\":[{\"type\":\"integer\",\"value\":888,\"key\":\"history\"},{\"value\":\"53.351772,-6.256357\",\"type\":\"geo\",\"key\":\"location\"},{\"type\":\"string\",\"value\":\"\",\"key\":\"reserved.streamsdktoken\"}],\"modified\":1362654648965},{\"id\":\"lowerG\",\"data\":[{\"type\":\"integer\",\"value\":777,\"key\":\"history\"},{\"value\":\"53.357897,-6.260192\",\"type\":\"geo\",\"key\":\"location\"},{\"type\":\"string\",\"value\":\"\",\"key\":\"reserved.streamsdktoken\"}],\"modified\":1362654648965},{\"id\":\"shankill\",\"data\":[{\"type\":\"integer\",\"value\":666,\"key\":\"history\"},{\"value\":\"53.233266,-6.123758\",\"type\":\"geo\",\"key\":\"location\"},{\"type\":\"string\",\"value\":\"\",\"key\":\"reserved.streamsdktoken\"}],\"modified\":1362654648966},{\"id\":\"cork\",\"data\":[{\"type\":\"integer\",\"value\":555,\"key\":\"history\"},{\"value\":\"51.896892,-8.486316\",\"type\":\"geo\",\"key\":\"location\"},{\"type\":\"string\",\"value\":\"\",\"key\":\"reserved.streamsdktoken\"}],\"modified\":1362654648967},{\"id\":\"paris\",\"data\":[{\"type\":\"integer\",\"value\":444,\"key\":\"history\"},{\"value\":\"48.856614,2.352222\",\"type\":\"geo\",\"key\":\"location\"},{\"type\":\"string\",\"value\":\"\",\"key\":\"reserved.streamsdktoken\"}],\"modified\":1362654648968}],\"category\":\"gps\"}";
        String query = "{\"ids\":[],\"operators\":[{\"value\":\"53.357897,-6.260192,10\",\"operator\":\"near\",\"key\":\"location\"},{\"value\":700,\"operator\":\"greater\",\"key\":\"history\"}],\"logic\":\"and\"}";
        QueryDomainFacadeImp qdf = new QueryDomainFacadeImp();
        try {
            Map<String, JSONArray> arrayMap = qdf.findMatchingObjects(json, query);
            Assert.assertEquals(3, arrayMap.size());

        } catch (JSONException e) {

        }
     }

    @Test
    public void testGeoQuery(){

        String json = "{\"group\":[{\"id\":\"london\",\"data\":[{\"value\":\"51.511214,-0.119824\",\"type\":\"geo\",\"key\":\"location\"},{\"type\":\"string\",\"value\":\"\",\"key\":\"reserved.streamsdktoken\"}],\"modified\":1362564242914},{\"id\":\"dublinCity\",\"data\":[{\"value\":\"53.349805,-6.26031\",\"type\":\"geo\",\"key\":\"location\"},{\"type\":\"string\",\"value\":\"\",\"key\":\"reserved.streamsdktoken\"}],\"modified\":1362564242914},{\"id\":\"upperG\",\"data\":[{\"value\":\"53.351772,-6.256357\",\"type\":\"geo\",\"key\":\"location\"},{\"type\":\"string\",\"value\":\"\",\"key\":\"reserved.streamsdktoken\"}],\"modified\":1362564242915},{\"id\":\"lowerG\",\"data\":[{\"value\":\"53.357897,-6.260192\",\"type\":\"geo\",\"key\":\"location\"},{\"type\":\"string\",\"value\":\"\",\"key\":\"reserved.streamsdktoken\"}],\"modified\":1362564242916},{\"id\":\"shankill\",\"data\":[{\"value\":\"53.233266,-6.123758\",\"type\":\"geo\",\"key\":\"location\"},{\"type\":\"string\",\"value\":\"\",\"key\":\"reserved.streamsdktoken\"}],\"modified\":1362564242918},{\"id\":\"cork\",\"data\":[{\"value\":\"51.896892,-8.486316\",\"type\":\"geo\",\"key\":\"location\"},{\"type\":\"string\",\"value\":\"\",\"key\":\"reserved.streamsdktoken\"}],\"modified\":1362564242919},{\"id\":\"paris\",\"data\":[{\"value\":\"48.856614,2.352222\",\"type\":\"geo\",\"key\":\"location\"},{\"type\":\"string\",\"value\":\"\",\"key\":\"reserved.streamsdktoken\"}],\"modified\":1362564242919}],\"category\":\"gps\"}";
        String query = "{\"ids\":[],\"operators\":[{\"value\":\"53.357897,-6.260192,10\",\"operator\":\"near\",\"key\":\"location\"}]}";
        QueryDomainFacadeImp qdf = new QueryDomainFacadeImp();
        try {
            Map<String, JSONArray> arrayMap = qdf.findMatchingObjects(json, query);
            Assert.assertEquals(6, arrayMap.size());

        } catch (JSONException e) {

        }
    }

    @Test
    public void testLimitedKeys() throws JSONException {

        String json = "{\"group\":[{\"id\":\"id1\",\"data\":[{\"type\":\"string\",\"value\":\"a\",\"key\":\"testkey\"}, {\"type\":\"integer\",\"value\":13,\"key\":\"testkey2\"},  {\"type\":\"integer\",\"value\":90,\"key\":\"testkey90\"}]}," +
                            "{\"id\":\"id2\",\"data\":[{\"type\":\"integer\",\"value\":11,\"key\":\"testkey2\"}]}," +
                            "{\"id\":\"id5\",\"data\":[{\"type\":\"integer\",\"value\":30,\"key\":\"testkey2\"}, {\"type\":\"string\",\"value\":\"testvalue\",\"key\":\"testkey\"}]}," +
                            "{\"id\":\"id3\",\"data\":[{\"type\":\"string\",\"value\":\"b\",\"key\":\"testkey3\"}]}," +
                            "{\"id\":\"id4\",\"data\":[{\"type\":\"string\",\"value\":\"c\",\"key\":\"testkey\"}]}],\"category\":\"hellohello\"}";

        String query = "{\"logic\":\"and\",\"operators\":[], \"ids\": [\"id1\", \"id2\", \"id3\"]}";
        QueryDomainFacadeImp qdf = new QueryDomainFacadeImp();
        Map<String, JSONArray> results = qdf.findMatchingObjects(json, query);
        Assert.assertEquals(3, results.size());
        Assert.assertTrue(results.containsKey("id1"));
        Assert.assertTrue(results.containsKey("id2"));
        Assert.assertTrue(results.containsKey("id3"));

    }

    @Test
    public void testContainsIn(){

        String json = "{\"group\":[{\"id\":\"id1\",\"data\":[{\"type\":\"string\",\"value\":\"a\",\"key\":\"testkey\"}, {\"type\":\"integer\",\"value\":13,\"key\":\"testkey2\"},  {\"type\":\"integer\",\"value\":90,\"key\":\"testkey90\"}]}," +
                      "{\"id\":\"id2\",\"data\":[{\"type\":\"integer\",\"value\":11,\"key\":\"testkey2\"}]}," +
                      "{\"id\":\"id5\",\"data\":[{\"type\":\"integer\",\"value\":30,\"key\":\"testkey2\"}, {\"type\":\"string\",\"value\":\"testvalue\",\"key\":\"testkey\"}]}," +
                      "{\"id\":\"id3\",\"data\":[{\"type\":\"string\",\"value\":\"b\",\"key\":\"testkey3\"}]}," +
                      "{\"id\":\"id4\",\"data\":[{\"type\":\"string\",\"value\":\"c\",\"key\":\"testkey\"}]}],\"category\":\"hellohello\"}";

        String query = "{\"logic\":\"and\",\"operators\":[{\"value\":[\"a\",\"b\",\"c\"],\"key\":\"testkey\",\"operator\":\"contains\"}]}";
        QueryDomainFacadeImp qdf = new QueryDomainFacadeImp();
        try {
            Map<String, JSONArray> results = qdf.findMatchingObjects(json, query);
            Assert.assertEquals(2, results.size());

            String j = JsonUtils.convertMatchingMapToString(results, "categorytest");
            System.out.println(j);  
        } catch (JSONException e) {
            System.out.println(e.getMessage());
        }

        String j = "{\"operators\":[{\"value\":[\"a\",\"b\",\"c\"],\"operator\":\"contains\",\"key\":\"testkey3\"},{\"value\":\"testvalue1\",\"operator\":\"equal\",\"key\":\"testkey2\"},{\"value\":10,\"operator\":\"greater\",\"key\":\"testkey1\"},{\"value\":\"testvalue\",\"operator\":\"equal\",\"key\":\"testkey\"}],\"logic\":\"and\"}";

    }


    @Test
    public void testFindMatchingResultsOr(){

        String json = "{\"group\":[{\"id\":\"id1\",\"data\":[{\"type\":\"string\",\"value\":\"testvalue\",\"key\":\"testkey\"}, {\"type\":\"integer\",\"value\":13,\"key\":\"testkey2\"},  {\"type\":\"integer\",\"value\":90,\"key\":\"testkey90\"}]}," +
                              "{\"id\":\"id2\",\"data\":[{\"type\":\"integer\",\"value\":11,\"key\":\"testkey2\"}]}," +
                              "{\"id\":\"id5\",\"data\":[{\"type\":\"integer\",\"value\":30,\"key\":\"testkey2\"}, {\"type\":\"string\",\"value\":\"testvalue\",\"key\":\"testkey\"}]}," +
                              "{\"id\":\"id3\",\"data\":[{\"type\":\"string\",\"value\":\"testvalue\",\"key\":\"testkey3\"}]}," +
                              "{\"id\":\"id4\",\"data\":[{\"type\":\"string\",\"value\":\"testvalue\",\"key\":\"testkey\"}]}],\"category\":\"hellohello\"}";
        String query = "";
        QueryDomainFacadeImp qdf = new QueryDomainFacadeImp();

       try{

           query = "{\"logic\":\"or\",\"operators\":[{\"value\":\"testvalue\",\"key\":\"testkey\",\"operator\":\"equal\"},{\"value\":\"testvalue3\",\"key\":\"testkey3\",\"operator\":\"equal\"}, {\"value\":10,\"key\":\"testkey2\",\"operator\":\"greater\"}]}";
           Map<String, JSONArray> results = qdf.findMatchingObjects(json, query);
           Assert.assertEquals(4, results.size());
           Assert.assertTrue(results.containsKey("id1"));
           Assert.assertTrue(results.containsKey("id2"));
           Assert.assertTrue(results.containsKey("id4"));
           Assert.assertTrue(results.containsKey("id5"));

          String j = JsonUtils.convertMatchingMapToString(results, "categorytest");
          System.out.println(j);


        } catch (JSONException e) {
             System.out.println(e.getMessage());
        } catch (NumberFormatException e){
             System.out.println(e.getMessage());
        }

    }



    @Test
    public void testFindMatchingResults(){

        String json = "{\"group\":[{\"id\":\"id1\",\"data\":[{\"type\":\"string\",\"value\":\"testvalue\",\"key\":\"testkey\"}, {\"type\":\"integer\",\"value\":13,\"key\":\"testkey2\"},  {\"type\":\"integer\",\"value\":90,\"key\":\"testkey90\"}]}," +
                      "{\"id\":\"id2\",\"data\":[{\"type\":\"integer\",\"value\":11,\"key\":\"testkey2\"}]}," +
                      "{\"id\":\"id5\",\"data\":[{\"type\":\"integer\",\"value\":30,\"key\":\"testkey2\"}, {\"type\":\"string\",\"value\":\"testvalue\",\"key\":\"testkey\"}]}," +
                      "{\"id\":\"id3\",\"data\":[{\"type\":\"string\",\"value\":\"testvalue\",\"key\":\"testkey3\"}]}," +
                      "{\"id\":\"id4\",\"data\":[{\"type\":\"string\",\"value\":\"testvalue\",\"key\":\"testkey\"}]}],\"category\":\"hellohello\"}";
        String query = "{\"logic\":\"and\",\"operators\":[{\"value\":\"testvalue\",\"key\":\"testkey\",\"operator\":\"equal\"},{\"value\":10,\"key\":\"testkey2\",\"operator\":\"greater\"}]}";

        QueryDomainFacadeImp qdf = new QueryDomainFacadeImp();

        try {
            Map<String, JSONArray> results = qdf.findMatchingObjects(json, query);

            Assert.assertEquals(2, results.size());
            Assert.assertTrue(results.containsKey("id1"));

            query = "{\"logic\":\"and\",\"operators\":[{\"value\":\"testvalue\",\"key\":\"testkey\",\"operator\":\"equal\"},{\"value\":15,\"key\":\"testkey2\",\"operator\":\"greater\"}]}";
            results = qdf.findMatchingObjects(json, query);
            Assert.assertEquals(1, results.size());

            query = "{\"logic\":\"and\",\"operators\":[{\"value\":\"testvalue\",\"key\":\"testkey\",\"operator\":\"equal\"}]}";
            results = qdf.findMatchingObjects(json, query);
            Assert.assertEquals(3, results.size());

            query = "{\"operators\":[{\"value\":\"testvalue1\",\"key\":\"testkey\",\"operator\":\"equal\"}]}";
            results = qdf.findMatchingObjects(json, query);
            Assert.assertEquals(0, results.size());

           query = "{\"logic\":\"or\",\"operators\":[{\"value\":\"testvalue\",\"key\":\"testkey\",\"operator\":\"equal\"},{\"value\":10,\"key\":\"testkey2\",\"operator\":\"greater\"}]}";
           results = qdf.findMatchingObjects(json, query);
           Assert.assertEquals(4, results.size());
           Assert.assertTrue(results.containsKey("id1"));
           Assert.assertTrue(results.containsKey("id2"));
           Assert.assertTrue(results.containsKey("id4")); 
           Assert.assertTrue(results.containsKey("id5")); 


           query = "{\"logic\":\"or\",\"operators\":[{\"value\":\"testvalue\",\"key\":\"testkey\",\"operator\":\"equal\"},{\"value\":10,\"key\":\"testkey2\",\"operator\":\"greater\"}], \"ids\": [\"id1\", \"id2\"]}";
           results = qdf.findMatchingObjects(json, query);
           Assert.assertEquals(2, results.size());
             Assert.assertTrue(results.containsKey("id1"));
           Assert.assertTrue(results.containsKey("id2"));

          String j = JsonUtils.convertMatchingMapToString(results, "categorytest");
          System.out.println(j);  


        } catch (JSONException e) {
             System.out.println(e.getMessage());
        } catch (NumberFormatException e){
             System.out.println(e.getMessage());
        }
    }


}
