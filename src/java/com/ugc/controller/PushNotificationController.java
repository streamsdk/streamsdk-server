package com.ugc.controller;

import com.ugc.facade.CertificateNotExistException;
import com.ugc.facade.PushNotificationFacade;
import com.ugc.facade.TokenNotFoundException;
import com.ugc.facade.StreamRequestsFacade;
import com.ugc.helper.FileCreationHelper;
import com.ugc.utils.FileMaster;
import com.ugc.utils.StringUtils;
import com.ugc.view.ViewConstants;
import com.ugc.view.ViewDTO;
import javapns.communication.exceptions.CommunicationException;
import javapns.communication.exceptions.KeystoreException;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Controller
public class PushNotificationController
{

    private PushNotificationFacade pushNotificationFacade;
    private FileCreationHelper fch;
    private StreamRequestsFacade streamRequestsFacade;
    private static Log log = LogFactory.getLog(PushNotificationController.class);

    @Autowired
    public PushNotificationController(PushNotificationFacade pushNotificationFacade, FileCreationHelper fch, StreamRequestsFacade streamRequestsFacade) {
        this.pushNotificationFacade = pushNotificationFacade;
        this.fch = fch;
        this.streamRequestsFacade = streamRequestsFacade;
    }

    @RequestMapping(value = "/notification/token/(*:clientKey)", method = RequestMethod.POST)
    public synchronized String addOrRetrieveToken(@RequestParam("token")String token,
                                                  @RequestParam("clientKey")String clientKey,
                                                  ModelMap map) {
        streamRequestsFacade.countRequest(clientKey);
        String json = "";
        try {
            if (token.startsWith("Device Token="))
                token = StringUtils.parseToken(token);
            json = pushNotificationFacade.addToken(clientKey, token, "ios");
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
            log.info("add Or Retrieve Token " + token + " for app id " + clientKey);
        }
        return ViewConstants.OBJECT_JSON_VIEW;
    }

    @RequestMapping(value = "/notification/subscribe/channel/(*:clientKey)", method = RequestMethod.POST)
    public synchronized String subscribe(@RequestParam("token")String token,
                                         @RequestParam("clientKey")String clientKey,
                                         @RequestParam("channel")String channel,
                                         ModelMap map) {

        streamRequestsFacade.countRequest(clientKey);
        try {
            if (token != null && !token.equals("")) {
                if (token.startsWith("Device Token="))
                    token = StringUtils.parseToken(token);
                pushNotificationFacade.subscribeChannel(token, clientKey, channel);
            }
            pushNotificationFacade.saveChannel(clientKey, channel);

        } catch (JSONException e) {
            ViewDTO view = new ViewDTO();
            view.setTransferObject("The data is corrupted, please send again");
            map.addAttribute(view);
            return ViewConstants.DEFAULT;
        } catch (TokenNotFoundException e) {
            ViewDTO view = new ViewDTO();
            view.setTransferObject(e.getMessage());
            map.addAttribute(view);
            return ViewConstants.DEFAULT;

        }
        ViewDTO view = new ViewDTO();
        view.setTransferObject("OK");
        map.addAttribute(view);
        if (log.isInfoEnabled()){
            log.info("subscribe channel " + channel + " for token " + token + " for client id " + clientKey);
        }
        return ViewConstants.DEFAULT;
    }


    @RequestMapping(value = "/notification/unsubscribe/channel/(*:clientKey)", method = RequestMethod.POST)
    public synchronized String unsubscribe(@RequestParam("token")String token,
                                           @RequestParam("clientKey")String clientKey,
                                           @RequestParam("channel")String channel,
                                           ModelMap map) {

        streamRequestsFacade.countRequest(clientKey);
        try {
            if (token != null && !token.equals("")) {
                if (token.startsWith("Device Token=")){
                    token = StringUtils.parseToken(token);
                }
             }
            pushNotificationFacade.unsubscribeChannel(token, clientKey, channel);
        } catch (JSONException e) {
            ViewDTO view = new ViewDTO();
            view.setTransferObject("The data is corrupted, please send again");
            map.addAttribute(view);
            return ViewConstants.DEFAULT;
        }

        ViewDTO view = new ViewDTO();
        view.setTransferObject("OK");
        map.addAttribute(view);
        if (log.isInfoEnabled()){
             log.info("unsubscribe channel " + channel + " for token " + token + " for client id " + clientKey);
        }
        return ViewConstants.DEFAULT;

    }


    @RequestMapping(value = "/notification/get/tokens/(*:clientKey)", method = RequestMethod.GET)
    public String getTokensForChannel(@RequestParam("channel")String channel,
                                                   @RequestParam("clientKey")String clientKey,
                                                   ModelMap map) {

        streamRequestsFacade.countRequest(clientKey);
        String tokenJson = "";
        try {
            tokenJson = pushNotificationFacade.getTokensForChannel(clientKey, channel);
        } catch (JSONException e) {
            ViewDTO view = new ViewDTO();
            view.setTransferObject("The data is corrupted, please send again");
            map.addAttribute(view);
            return ViewConstants.DEFAULT;
        }
        ViewDTO view = new ViewDTO();
        view.setTransferObject(tokenJson);
        map.addAttribute(view);
        if (log.isInfoEnabled()){
            log.info("get tokens for channel: " + channel);
        }
        return ViewConstants.OBJECT_JSON_VIEW;
    }


    @RequestMapping(value = "/notification/get/channels/(*:clientKey)", method = RequestMethod.GET)
    public String getSubscribedChannel(@RequestParam("token")String token,
                                                    @RequestParam("clientKey")String clientKey,
                                                    ModelMap map) {

        streamRequestsFacade.countRequest(clientKey);
        String json = "";
        try {
            if (token != null && !token.equals("")) {
                if (token.startsWith("Device Token=")){
                    token = StringUtils.parseToken(token);
                }
            }
            json = pushNotificationFacade.getsubscribedChannels(clientKey, token);
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
            log.info("get Subscribed Channel for token: " + token + " for app id: " + clientKey);
        }
        return ViewConstants.OBJECT_JSON_VIEW;
    }

    @RequestMapping(value = "/notification/getall/channels/(*:clientKey)", method = RequestMethod.GET)
    public String getAllChannels(@RequestParam("clientKey")String clientKey,
                                              ModelMap map) {

        streamRequestsFacade.countRequest(clientKey);
        String json = pushNotificationFacade.getAllChannels(clientKey);
        if (log.isInfoEnabled()){
            log.info("get all channel for app id " + clientKey);
        }
        if (json == null) {
            ViewDTO view = new ViewDTO();
            view.setTransferObject("No channels have been created yet");
            map.addAttribute(view);
            return ViewConstants.DEFAULT;
        } else {
            ViewDTO view = new ViewDTO();
            view.setTransferObject(json);
            map.addAttribute(view);
            return ViewConstants.OBJECT_JSON_VIEW;
        }

    }

    @RequestMapping(value = "/notification/send/token/(*:clientKey)", method = RequestMethod.POST)
    public String sendMessage(@RequestParam("token")String token,
                              @RequestParam("password")String password,
                              @RequestParam("message")String message,
                              @RequestParam("clientKey")String clientKey,
                              @RequestParam("soundFile")String soundFile,
                              ModelMap map) {


          streamRequestsFacade.countPushRequest(clientKey);
          ViewDTO view = new ViewDTO();
          try {
              if (token != null && !token.equals("")) {
                  if (token.startsWith("Device Token=")) {
                      token = StringUtils.parseToken(token);
                  }
              }
             pushNotificationFacade.sendMessage(token, clientKey, message, password, soundFile);
          } catch (CommunicationException e) {
               view.setTransferObject(e.getMessage());
               map.addAttribute(view);
               return ViewConstants.DEFAULT;
          } catch (KeystoreException e) {
               view.setTransferObject(e.getMessage());
               map.addAttribute(view);
               return ViewConstants.DEFAULT;
          } catch (CertificateNotExistException e) {
               view.setTransferObject(e.getMessage());
               map.addAttribute(view);
               return ViewConstants.DEFAULT;
          }
           view.setTransferObject("OK");
           map.addAttribute(view);

           return ViewConstants.DEFAULT;
    }

    @RequestMapping(value = "/notification/send/channels/(*:clientKey)", method = RequestMethod.POST)
    public String sendMessages(@RequestParam("channel")String channel,
                              @RequestParam("password")String password,
                              @RequestParam("message")String message,
                              @RequestParam("clientKey")String clientKey,
                              @RequestParam("soundFile")String soundFile,
                              ModelMap map) {

        streamRequestsFacade.countRequest(clientKey);
        ViewDTO view = new ViewDTO();

        String tokenJson = "";
        try {

            tokenJson = pushNotificationFacade.getTokensForChannel(clientKey, channel);
            pushNotificationFacade.sendMessages(tokenJson, message, password, clientKey, soundFile);
           
        } catch (JSONException e) {
            view.setTransferObject("The data is corrupted, please send again");
            map.addAttribute(view);
            return ViewConstants.DEFAULT;
        } catch (CertificateNotExistException e) {
            view.setTransferObject(e.getMessage());
            map.addAttribute(view);
            return ViewConstants.DEFAULT;
        } catch (CommunicationException e) {
            view.setTransferObject(e.getMessage());
            map.addAttribute(view);
            return ViewConstants.DEFAULT;
        } catch (KeystoreException e) {
            view.setTransferObject(e.getMessage());
            map.addAttribute(view);
            return ViewConstants.DEFAULT;
        }

        view.setTransferObject("OK");
        map.addAttribute(view);

        return ViewConstants.DEFAULT;
    }


    @RequestMapping(value = "/notification/upload/cert/(*:clientKey)", method = RequestMethod.POST)
    public void uploadCertificate(HttpServletRequest req, @RequestParam("clientKey")String clientKey) {

        streamRequestsFacade.countRequest(clientKey);
        String certFolder = fch.getCertFolder();
        //todo: add logging

        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        String itemName = "";
        InputStream in = null;
        try {
            List<FileItem> items = upload.parseRequest(req);
            for (FileItem item : items) {
                if (item.getName() != null) {
                    itemName = item.getName();
                }
                if (!item.isFormField()) {
                    if (itemName.contains("\\")) {
                        int index = itemName.lastIndexOf("\\");
                        itemName = itemName.substring(index + 1);
                    }

                    File file = new File(certFolder + "/" + clientKey);
                    if (file.exists()) {
                        FileUtils.deleteDirectory(file);
                    }
                    file.mkdir();

                    File certFile = new File(file, itemName);
                    certFile.createNewFile();

                    FileMaster master = new FileMaster(certFile);
                    master.setCurrentChannelPosition(0);

                    in = item.getInputStream();
                    master.writeToFileSystem(in);
                }
            }
        }
        catch (FileUploadException e) {
        }
        catch (IOException e) {
        }
        finally {
            IOUtils.closeQuietly(in);
        }
        if (log.isInfoEnabled()){
            log.info("upload certificate for app id " + clientKey);
        }
    }
}
