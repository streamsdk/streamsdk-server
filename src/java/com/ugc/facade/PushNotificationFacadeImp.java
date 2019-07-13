package com.ugc.facade;

import com.ugc.controller.SystemConstants;
import com.ugc.helper.FileCreationHelper;
import com.ugc.utils.JsonUtils;
import javapns.Push;
import javapns.communication.exceptions.CommunicationException;
import javapns.communication.exceptions.KeystoreException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

@Service(value = "PushNotificationFacade")
public class PushNotificationFacadeImp implements PushNotificationFacade
{

    private StorageDomainFacade storageDomainFacade;
    private AndroidPushMessageFacade androidPushMessageFacade;
    private FileCreationHelper fch;
    private StreamRequestsFacade streamRequestsFacade;
    private static Log log = LogFactory.getLog(PushNotificationFacadeImp.class);
    private static final String BROADCAST = "broadcast";

    @Autowired
    public PushNotificationFacadeImp(StorageDomainFacade storageDomainFacade, FileCreationHelper fch, AndroidPushMessageFacade androidPushMessageFacade,
                                   StreamRequestsFacade streamRequestsFacade) {
        this.storageDomainFacade = storageDomainFacade;
        this.androidPushMessageFacade = androidPushMessageFacade;
        this.fch = fch;
        this.streamRequestsFacade = streamRequestsFacade;
    }

    public String addToken(String clientKey, String token, String deviceType) throws JSONException {

        String objectId = clientKey + "tokenonly";
        String json = storageDomainFacade.get("tokenonly", clientKey, "", SystemConstants.TOEKN_BUKET);
        if (json == null) {
            //first time to send token
            String result = createSingleTokenChannelData(token, deviceType);
            storageDomainFacade.saveObject(result, SystemConstants.TOEKN_BUKET, objectId);
            return result;
        }

        String clientTokenJson = JsonUtils.getjson(json, token);
        if (clientTokenJson == null) {
            String result = createSingleTokenChannelData(token, deviceType);
            String newJson = JsonUtils.updateOrAddObject(json, result);
            storageDomainFacade.saveObject(newJson, SystemConstants.TOEKN_BUKET, objectId);
            return result;
        } else {
            return clientTokenJson;
        }
    }

    public String subscribeChannel(String token, String clientKey, String channel) throws JSONException, TokenNotFoundException {

        String objectId = clientKey + "token";
        String json = storageDomainFacade.get("token", clientKey, "", SystemConstants.TOEKN_BUKET);
        if (json == null){
            // this is the first time the channel is subscribed
            json = storageDomainFacade.get("tokenonly", clientKey, "", SystemConstants.TOEKN_BUKET);
        }
         if (json == null)
             throw new TokenNotFoundException("The token (" +  token + ") does not exist in the system, please add this token first");

        String clientToken = JsonUtils.getjson(json, token);
        if (clientToken == null)
            throw new TokenNotFoundException("The token (" +  token + ") does not exist in the system, please add this token first");

        JSONObject dataObject = new JSONObject(clientToken);
        JSONArray dataArray = dataObject.getJSONArray("data");
        int length = dataArray.length();
        JSONArray channels = null;
        for (int x = 0; x < length; x++) {
            JSONObject data = dataArray.getJSONObject(x);
            String type = (String) data.get("type");
            if (type.equals("array")) {

                channels = (JSONArray) data.get("value");
                int size = channels.length();
                boolean exists = false;
                for (int i = 0; i < size; i++) {
                    String c = channels.getString(0);
                    if (c.equals(channel)) {
                        exists = true;
                    }
                }
                if (!exists)
                    channels.put(channel);

            }
        }

        String updatedJson = createSingleTokenChannelData(token, channels);
        String newJson = JsonUtils.updateOrAddObject(json, updatedJson);
        storageDomainFacade.saveObject(newJson, SystemConstants.TOEKN_BUKET, objectId);
        return newJson;

    }

    public String unsubscribeChannel(String token, String clientKey, String channel) throws JSONException {

       String objectId = clientKey + "token";
        String json = storageDomainFacade.get("token", clientKey, "", SystemConstants.TOEKN_BUKET);
        String clientToken = JsonUtils.getjson(json, token);
        JSONObject dataObject = new JSONObject(clientToken);
        JSONArray dataArray = dataObject.getJSONArray("data");
        JSONArray channels = null;
        int length = dataArray.length();
        JSONArray newChannels = new JSONArray();
        for (int x=0; x < length; x++){
            JSONObject data = dataArray.getJSONObject(x);
            String type = (String) data.get("type");
            if (type.equals("array")) {
                channels = (JSONArray) data.get("value");
                int size = channels.length();
                for (int i = 0; i < size; i++) {
                  if (!channels.get(i).equals(channel)) {
                     newChannels.put(channels.get(i));
                  }
                }
            }
        }


        String updatedJson = createSingleTokenChannelData(token, newChannels);
        String newJson = JsonUtils.updateOrAddObject(json, updatedJson);
        storageDomainFacade.saveObject(newJson, SystemConstants.TOEKN_BUKET, objectId);
        return newJson;

    }

    private boolean isChannelExists(JSONArray channels, String channel) throws JSONException {

        int size = channels.length();
        for (int i = 0; i < size; i++) {
            JSONObject jo = channels.getJSONObject(i);
            String c = jo.getString("id");
            if (c.equals(channel))
                return true;
        }
        return false;

    }

    public void createBroadcastChannel(String clientKey){

       String objectId = clientKey + "channel";
        String newJson = null;
        try {
            newJson = createSingleChannels(BROADCAST);
        } catch (JSONException e) {

        }
        storageDomainFacade.saveObject(newJson, SystemConstants.TOEKN_BUKET, objectId);

    }

    public String saveChannel(String clientKey, String channel) throws JSONException {

        String objectId = clientKey + "channel";
        String json = storageDomainFacade.get("channel", clientKey, "", SystemConstants.TOEKN_BUKET);
        if (json == null) {
            String newJson = createSingleChannels(channel);
            storageDomainFacade.saveObject(newJson, SystemConstants.TOEKN_BUKET, objectId);
        } else {
            JSONObject jObject = new JSONObject(json);
            JSONArray ja = (JSONArray) jObject.get("channels");
            if (!isChannelExists(ja, channel)) {
                JSONObject idChannel = new JSONObject();
                idChannel.put("id", channel);
                ja.put(idChannel);
                String newJson = createSingleChannels(ja);
                storageDomainFacade.saveObject(newJson, SystemConstants.TOEKN_BUKET, objectId);
            }
        }
        return "";
    }

    public String getsubscribedChannels(String clientKey, String token) throws JSONException {

        String json = storageDomainFacade.get("token", clientKey, "", SystemConstants.TOEKN_BUKET);
        String clientToken = JsonUtils.getjson(json, token);
        return clientToken;

    }

    public String getAllChannels(String clientKey) {

        String json = storageDomainFacade.get("channel", clientKey, "", SystemConstants.TOEKN_BUKET);
        return json;

    }

    public String getTokensForChannel(String clientKey, String channel) throws JSONException {

        if (!channel.equals(BROADCAST)) {
            Set<String> tokens = new HashSet<String>();
            String json = storageDomainFacade.get("token", clientKey, "", SystemConstants.TOEKN_BUKET);
            JSONObject jo = new JSONObject(json);
            JSONArray data = jo.getJSONArray("group");
            int size = data.length();
            for (int i = 0; i < size; i++) {
                JSONObject jobject = data.getJSONObject(i);
                JSONArray dataJsonObject = (JSONArray) jobject.get("data");
                int dLength = dataJsonObject.length();
                for (int x = 0; x < dLength; x++) {
                    JSONObject d = dataJsonObject.getJSONObject(x);
                    String type = (String) d.get("type");
                    if (type.equals("array")) {

                        JSONArray channels = (JSONArray) d.get("value");
                        int cSize = channels.length();
                        boolean found = false;
                        for (int y = 0; y < cSize; y++) {
                            String c = (String) channels.get(y);
                            if (c.equals(channel)) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            String id = jobject.getString("id");
                            tokens.add(id);
                        }

                    }
                }
            }
            String newJson = JsonUtils.deleteObjectsFromIdSet(json, tokens);
            return newJson;
        } else {
            String json = storageDomainFacade.get("tokenonly", clientKey, "", SystemConstants.TOEKN_BUKET);
            if (json == null)
               json = createDefaultChannelGroup();
            return json;
        }

    }

    public void sendMessages(String tokenJson, String message, String password, String clientKey, String soundFile) throws JSONException, CertificateNotExistException, CommunicationException, KeystoreException {

        JSONObject jo = new JSONObject(tokenJson);
        JSONArray data = jo.getJSONArray("group");
        int size = data.length();
        for (int i = 0; i < size; i++) {
            JSONObject jobject = data.getJSONObject(i);
            String id = jobject.getString("id");
            JSONArray jDataArray = (JSONArray) jobject.get("data");            
            if (log.isInfoEnabled()){
               log.info("send message " + message + " for app id " + clientKey + " with token " + tokenJson  + " with sound file " + soundFile);
            }

            streamRequestsFacade.countPushRequest(clientKey);
            if (isAndroid(jDataArray))
                androidPushMessageFacade.pushMessage(clientKey, id, message);
            else
                sendMessage(id, clientKey, message, password, soundFile);
        }

    }

    private boolean isAndroid(JSONArray dataArray) throws JSONException {

        int length = dataArray.length();
        for (int i=0; i < length; i++){
            JSONObject object = dataArray.getJSONObject(i);
            String key = (String) object.get("key");
            if (key.equals("deviceType")){
                String value = (String) object.get("value");
                if (value.equals("android"))
                    return true;
            }
        }

        return false;
    }

    public void sendMessage(String token, String clientKey, String message, String certPassword, String soundFile) throws CommunicationException, KeystoreException, CertificateNotExistException {

         //TODO: CHANAGE TO BATCH
         String certFolder = fch.getCertFolder();
         File certFile = new File(certFolder + "/" + clientKey);
         if (!certFile.exists())
             throw new CertificateNotExistException("Please Upload Certificate First");

         File theCert[] = certFile.listFiles();
         if (certFile != null && theCert.length == 0)
             throw new CertificateNotExistException("Please Upload Certificate First");

         final String certFilePath = theCert[0].getAbsolutePath();

         if (!soundFile.equals(""))
             Push.combined(message, -1, soundFile,certFilePath, certPassword, false, token);
         else
             Push.alert(message, certFilePath, certPassword, false, token);
         
    }

    public void sendAndroidMessage(String token, String clientKey, String message) {
         androidPushMessageFacade.pushMessage(clientKey, token, message);
    }

    private String createSingleChannels(JSONArray ja) throws JSONException {
        JSONObject channels = new JSONObject();
        channels.put("channels", ja);
        return channels.toString();
    }

    private String createSingleChannels(String channel) throws JSONException {

        // this method only called when first time the channel is crated (1. create applcaition 2. from somewhere like unit test)
        if (!channel.equals(BROADCAST)) {
            JSONObject idChannel = new JSONObject();
            idChannel.put("id", channel);

            JSONObject broadCastChannel = new JSONObject();
            broadCastChannel.put("id", BROADCAST);

            JSONArray channelArray = new JSONArray();
            channelArray.put(idChannel);
            channelArray.put(broadCastChannel);

            JSONObject channels = new JSONObject();
            channels.put("channels", channelArray);
            return channels.toString();
        } else {
            JSONObject broadCastChannel = new JSONObject();
            broadCastChannel.put("id", BROADCAST);
            JSONArray channelArray = new JSONArray();
            channelArray.put(broadCastChannel);
            JSONObject channels = new JSONObject();
            channels.put("channels", channelArray);
            return channels.toString();
        }
    }


    private String createSingleTokenChannelData(String token, JSONArray channels) throws JSONException {

        JSONObject data = new JSONObject();
        data.put("type", "array");
        data.put("key", "channels");
        data.put("value", channels);
        JSONArray dataArray = new JSONArray();
        dataArray.put(data);

        JSONObject groupObject = new JSONObject();
        groupObject.put("id", token);
        groupObject.put("data", dataArray);
        JSONArray groupObjectArray = new JSONArray();
        groupObjectArray.put(groupObject);

        JSONObject group = new JSONObject();
        group.put("category", "pushChannels");
        group.put("group", groupObjectArray);

        return group.toString();

    }

    private String createSingleTokenChannelData(String token, String deviceType) throws JSONException {
        JSONObject data = new JSONObject();
        data.put("type", "array");
        data.put("key", "channels");
        JSONArray ja = new JSONArray();
        data.put("value", ja);


        JSONObject type = new JSONObject();
        type.put("key", "deviceType");
        type.put("type", "string");
        type.put("value", deviceType);

        JSONArray dataArray = new JSONArray();
        dataArray.put(data);
        dataArray.put(type);

        JSONObject groupObject = new JSONObject();
        groupObject.put("id", token);
        groupObject.put("data", dataArray);
        JSONArray groupObjectArray = new JSONArray();
        groupObjectArray.put(groupObject);

        JSONObject group = new JSONObject();
        group.put("category", "pushChannels");
        group.put("group", groupObjectArray);

        return group.toString();
    }

    private String createDefaultChannelGroup() throws JSONException {

        JSONArray groupObjectArray = new JSONArray();
        JSONObject group = new JSONObject();
        group.put("category", "pushChannels");
        group.put("group", groupObjectArray);
        return group.toString();
    }
}
