package com.ugc.controller;

import com.ugc.domain.AndroidPushMessage;
import com.ugc.facade.AndroidPushMessageFacade;
import com.ugc.facade.PushNotificationFacade;
import com.ugc.facade.StreamRequestsFacade;
import com.ugc.helper.FileCreationHelper;
import com.ugc.view.ViewConstants;
import com.ugc.view.ViewDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedList;

@Controller
public class AndroidPushNotificationController
{

    private PushNotificationFacade pushNotificationFacade;
    private FileCreationHelper fch;
    private StreamRequestsFacade streamRequestsFacade;
    private AndroidPushMessageFacade androidPushMessageFacade;

    private static Log log = LogFactory.getLog(AndroidPushNotificationController.class);

    @Autowired
    public AndroidPushNotificationController(PushNotificationFacade pushNotificationFacade, FileCreationHelper fch,
                                       AndroidPushMessageFacade androidPushMessageFacade,      StreamRequestsFacade streamRequestsFacade){

        this.pushNotificationFacade = pushNotificationFacade;
        this.fch = fch;
        this.streamRequestsFacade = streamRequestsFacade;
        this.androidPushMessageFacade = androidPushMessageFacade;

    }

    @RequestMapping(value = "/notificationandroid/token/(*:clientKey)", method = RequestMethod.POST)
    public synchronized String addOrRetrieveToken(@RequestParam("token")String token,
                                                  @RequestParam("clientKey")String clientKey,
                                                  ModelMap map) {
        streamRequestsFacade.countRequest(clientKey);
        String json = "";
        try {

            json = pushNotificationFacade.addToken(clientKey, token, "android");

        } catch (JSONException e) {
            ViewDTO view = new ViewDTO();
            view.setTransferObject("The data is corrupted, please send again");
            map.addAttribute(view);
            return ViewConstants.DEFAULT;
        }

        ViewDTO view = new ViewDTO();
        view.setTransferObject(json);
        map.addAttribute(view);
        if (log.isInfoEnabled()){
            log.info("android add Or Retrieve Token " + token + " for app id " + clientKey);
        }
        return ViewConstants.OBJECT_JSON_VIEW;
    }


    @RequestMapping(value = "/notificationandroid/send/token/(*:clientKey)", method = RequestMethod.POST)
    public String sendAndroidPushMessage(@RequestParam("token")String token,  @RequestParam("message")String message,
                                 @RequestParam("clientKey")String clientKey, ModelMap map){

            streamRequestsFacade.countPushRequest(clientKey);
            ViewDTO view = new ViewDTO();
            pushNotificationFacade.sendAndroidMessage(token, clientKey, message);
            view.setTransferObject("OK");
            map.addAttribute(view);
            return ViewConstants.DEFAULT;

    }

     @RequestMapping(value = "/notificationandroid/consume/token/(*:clientKey)", method = RequestMethod.POST)
     public String consumeMessage(@RequestParam("token")String token,  @RequestParam("hash")String hash,
                                 @RequestParam("clientKey")String clientKey, @RequestParam("time")String time, ModelMap map){

         streamRequestsFacade.countRequest(clientKey);
         ViewDTO view = new ViewDTO();
         androidPushMessageFacade.consumeMessage(clientKey, token, hash, Long.parseLong(time));
         view.setTransferObject("OK");
         map.addAttribute(view);
         return ViewConstants.DEFAULT;

     }

    @RequestMapping(value = "/notificationandroid/getmessages/token/(*:clientKey)", method = RequestMethod.POST)
    public String getMessages(@RequestParam("token")String token, @RequestParam("clientKey")String clientKey, ModelMap map){

        streamRequestsFacade.countRequest(clientKey);
        ViewDTO view = new ViewDTO();
        LinkedList<AndroidPushMessage> messages = androidPushMessageFacade.getMessages(clientKey, token);
        if (messages != null)
           view.setCollections(messages);
        map.addAttribute(view);
        return ViewConstants.ANDROID_PUSH_MESSAGES;


    }


}
