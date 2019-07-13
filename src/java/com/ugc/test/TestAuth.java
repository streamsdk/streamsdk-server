package com.ugc.test;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SignatureException;
import java.util.*;

public class TestAuth
{

    public static void main(String[]ar) throws SignatureException {
        long randomNum = new Random().nextLong();
        long timestamp = (long) System.currentTimeMillis()/1000;

        HashMap<String, String> args = new HashMap<String, String>();

        String str = "SELECT COUNT(DISTINCT ENTITY_KEY_NUM) AS NUM_CASES\n" +
                     "FROM WORKFLOW_WORKITEM_LINK\n" +
                     "WHERE LINKED_ENTITY_KEY = [CUSTOMER_ID]\n" +
                     "AND TEST_KEY = [CUSTOMER_ID]\n" +
                     "AND ENTITY_NAME = 'Case'\n" +
                     "AND LINKED_ENTITY_NAME = 'Customer'\n" +
                     "AND TO_TIMESTAMP = TO_TIMESTAMP('31/12/9999 00:00:00', 'DD/MM/YYYY HH24:MI:SS')";
        args.put("oauth_consumer_key", "1611b5b391294943a9481091c5ac8339");
        args.put("oauth_nonce", "hello");
        args.put("oauth_signature_method", "HMAC-SHA1");
        args.put("oauth_timestamp", "1248799716");
        args.put("oauth_version", "1.0");
        /* if (accessToken != null && accessToken.getKey() != null) // If an access token has been stored, automatically pass it
       args.put("oauth_token", accessToken.getKey());*/
        List<String> argList = new ArrayList<String>();
        for (String key : args.keySet()) {
            String arg = key+"="+encode(args.get(key));
            argList.add(arg);
        }
        Collections.sort(argList);
        StringBuilder part3 = new StringBuilder();
        for (int i = 0; i < argList.size(); i++) {
            part3.append(argList.get(i));
            if (i != argList.size()-1) {
                part3.append("&");
            }
        }
        String part1 = "GET";
        String part2 = "http://api.myspace.com/request_token";
        String baseString = encode(part1)+"&"+encode(part2)+"&"+encode(part3.toString());

        String combinedSecret = "9b08ba44e01844d3b9ba678ef2229a4a";
//System.out.println("CombinedSecret = '" + combinedSecret + "'");
        System.out.println("baseString: " + baseString);
        String sig = calculateRFC2104HMAC(combinedSecret, baseString);
        System.out.println("sig: " + sig);
        String result = part2+"?"+part3+"&oauth_signature="+encode(sig);
        System.out.println(result);


        /*byte b[] = Base64Util.decode("PlygldPibZopbrzhopx28Q/X4sE=");
        System.out.println(b[1]);*/

        byte by[] = Base64Util.decode("h5NcfS3bfwgXkrgPooZsAujdpbU=");
        System.out.println("h: " + by[0]);


//System.out.println(result);


/*
        GetMethod getMethod = new GetMethod(result);


        HttpClient client = new HttpClient();
        try {
            int statusCode = client.executeMethod(getMethod);
            byte[] responseBody = getMethod.getResponseBody();

            System.out.println(new String(responseBody));

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }*/

    }


    public static String encode(String value) {
           try {
               return URLEncoder.encode(value, "UTF-8");
           } catch (UnsupportedEncodingException e) {
               throw new RuntimeException(e);
           }
       }

       /**
        * Returns the HMAC-SHA1 signature of a given string.
        * @param key Key (or secret) to use in obtaining the signature.
        * @param data The string whose signature we want to compute.
        * @return The computed signature.
        */

       public static String calculateRFC2104HMAC(String key, String data)
               throws java.security.SignatureException
       {
           String result;
           try {

// get an hmac_sha1 key from the raw key bytes
               SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), "HmacSHA1");

// get an hmac_sha1 Mac instance and initialize with the signing key
               Mac mac = Mac.getInstance("HmacSHA1");
               mac.init(signingKey);

// compute the hmac on input data bytes
               byte[] rawHmac = mac.doFinal(data.getBytes());
               System.out.println("rawHmac: " + rawHmac[0]);

// base64-encode the hmac
               result = Base64Util.encodeBytes(rawHmac);

           } catch (Exception e) {
               throw new SignatureException("Failed to generate HMAC : " + e.getMessage());
           }
           return result;
       }



       protected static String getHMACSHA1(String key, String data) {
           try {
               // get an hmac_sha1 key from the raw key bytes
               SecretKeySpec signingKey = new SecretKeySpec(key.getBytes("UTF-8"), "HMAC-SHA1");

               // get an hmac_sha1 Mac instance and initialize with the signing key
               Mac mac = Mac.getInstance("HmacSHA1");
               mac.init(signingKey);

               // compute the hmac on input data bytes
               byte[] rawHmac = mac.doFinal(data.getBytes("UTF-8"));

               // base64-encode the hmac
               return Base64Util.encodeBytes(rawHmac);
           } catch (Exception e) {
               throw new RuntimeException("Unable to generate HMAC-SHA1", e);
           }
       }




}






