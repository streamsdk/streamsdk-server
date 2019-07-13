package com.ugc.facade;

import com.ugc.domain.AndroidPushMessage;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service(value = "AndroidPushMessageFacade")
public class AndroidPushMessageFacadeImp implements AndroidPushMessageFacade
{

    Map<String, Map<String, LinkedList<AndroidPushMessage>>> pushMessages = new ConcurrentHashMap<String, Map<String, LinkedList<AndroidPushMessage>>>();

    public void pushMessage(String clientKey, String token, String message) {

        AndroidPushMessage apm = new AndroidPushMessage();
        long time = System.currentTimeMillis();
        apm.setMessageCreationTime(time);
        apm.setMessage(message);

        if (pushMessages.containsKey(clientKey)){

            if (pushMessages.get(clientKey).get(token) != null){
                pushMessages.get(clientKey).get(token).push(apm);
            }else{
                //new token for the client key
                LinkedList<AndroidPushMessage> am = new LinkedList<AndroidPushMessage>();
                am.push(apm);
                pushMessages.get(clientKey).put(token, am);
            }
                
        }else{
           //new application ID
           LinkedList<AndroidPushMessage> am = new LinkedList<AndroidPushMessage>();
           am.push(apm);
           Map<String, LinkedList<AndroidPushMessage>> ams = new ConcurrentHashMap<String, LinkedList<AndroidPushMessage>>();
           ams.put(token, am);
           pushMessages.put(clientKey, ams);

        }
    }

    public void consumeMessage(String clientKey, String token, String hash, long messageCreationTime) {

        AndroidPushMessage apm = new AndroidPushMessage();
        apm.setMessageCreationTime(messageCreationTime);
        apm.setMessageHash(hash);
        try {
            pushMessages.get(clientKey).get(token).remove(apm);
        }catch(Exception e){

        }
    }

    public LinkedList<AndroidPushMessage> getMessages(String clientKey, String token) {
        
         if (pushMessages.containsKey(clientKey)){
             Map<String, LinkedList<AndroidPushMessage>> map = pushMessages.get(clientKey);
             if (map != null)
                 return map.get(token);
         }
         return null;
    }
}
