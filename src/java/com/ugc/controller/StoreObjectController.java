package com.ugc.controller;

import com.ugc.domain.ObjectListDTO;
import com.ugc.facade.*;
import com.ugc.view.ViewConstants;
import com.ugc.view.ViewDTO;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.List;

@Controller
public class StoreObjectController
{

    private StorageDomainFacade storageDomainFacade;
    private StreamObjectCacheFacade streamObjectCache;
    private StreamRequestsFacade streamRequestsFacade;
    private UpdateStreamObjectFacade updateStreamObjectFacade;
    private ExportImportFacade exportImportFacade;
    private static Log log = LogFactory.getLog(StoreObjectController.class);

    @Autowired
    public StoreObjectController(StorageDomainFacade storageDomainFacade, StreamObjectCacheFacade streamObjectCache,
                                 StreamRequestsFacade streamRequestsFacade, UpdateStreamObjectFacade updateStreamObjectFacade, ExportImportFacade exportImportFacade) {
        this.storageDomainFacade = storageDomainFacade;
        this.streamObjectCache = streamObjectCache;
        this.streamRequestsFacade = streamRequestsFacade;
        this.updateStreamObjectFacade = updateStreamObjectFacade;
        this.exportImportFacade = exportImportFacade;
    }

    @RequestMapping(value = "/store/object/(*:clientKey)/(*:userName)", method = RequestMethod.POST)
    public String createObject(
            @RequestParam("json")String json,
            @RequestParam("clientKey")String clientKey,
            @RequestParam("userName")String userName,
            @RequestParam("objectId")String id,

            ModelMap map) {

        streamRequestsFacade.countRequest(clientKey);
        String objectId = "";
        if (!id.equals("")) {
            String finalKey = clientKey + userName + id;
            if (storageDomainFacade.isKeyExists(finalKey, SystemConstants.OBJECT_BUKET)) {
                ViewDTO view = new ViewDTO();
                view.setTransferObject("This object id aleady exists, please create a uniqiue id");
                if (log.isInfoEnabled()){
                    log.info("object id " + id + " for client key " + clientKey + " already exists");
                }
                map.addAttribute(view);
                return ViewConstants.DEFAULT;
            }
            objectId = sendToStorage(json, clientKey, userName, SystemConstants.OBJECT_BUKET, id);
        } else {
            objectId = sendToStorage(json, clientKey, userName, SystemConstants.OBJECT_BUKET, "");
        }
        ViewDTO view = new ViewDTO();
        view.setTransferObject(objectId);
        map.addAttribute(view);
        map.addAttribute("isCreation", new Boolean(true));
        if (log.isInfoEnabled()){
            log.info("object id " + objectId + " for app id " + clientKey + " is created" + (userName.equals("") ? "" : " with username: " + userName));

        }
        return ViewConstants.OBJECT_CREATE;

    }

    @RequestMapping(value = "/store/object/removekey/(*:clientKey)/(*:fileId)/(*:userName)", method = RequestMethod.POST)
    public String deleteObjectKey( @RequestParam("key")String key,
            @RequestParam("userName")String userName,
            @RequestParam("clientKey")String clientKey,
            @RequestParam("fileId")String fileId,
            ModelMap map){


        streamRequestsFacade.countRequest(clientKey);
        try {
            if (key.indexOf("&&") != -1){
               String keys[] = key.split("&&");
               for (String sKey: keys)
                   updateStreamObjectFacade.deleteObjectKey(sKey, fileId, clientKey, userName, SystemConstants.OBJECT_BUKET);
            }else{
                   updateStreamObjectFacade.deleteObjectKey(key, fileId, clientKey, userName, SystemConstants.OBJECT_BUKET);
            }
        } catch (ObjectKeyNotExistException e) {
            ViewDTO view = new ViewDTO();
            view.setTransferObject(e.getMessage());
            map.addAttribute(view);
            return ViewConstants.DEFAULT;

        } catch (JSONException e) {
            ViewDTO view = new ViewDTO();
            view.setTransferObject("The data is corrupted, please send again");
            map.addAttribute(view);
            return ViewConstants.DEFAULT;
        }
        ViewDTO view = new ViewDTO();
        view.setTransferObject(fileId);
        map.addAttribute(view);
        map.addAttribute("isCreation", new Boolean(false));
        if (log.isInfoEnabled()) {
            log.info("update object id " + fileId + " for app id " + clientKey);
        }
        return ViewConstants.OBJECT_CREATE;

    }

    @RequestMapping(value = "/store/object/update/(*:clientKey)/(*:fileId)/(*:userName)", method = RequestMethod.POST)
    public String updateObject(
            @RequestParam("json")String json,
            @RequestParam("userName")String userName,
            @RequestParam("clientKey")String clientKey,
            @RequestParam("fileId")String fileId,
            ModelMap map) {

        streamRequestsFacade.countRequest(clientKey);
        try {
            updateStreamObjectFacade.updateObject(json, fileId, clientKey, userName, SystemConstants.OBJECT_BUKET);

        } catch (ObjectKeyNotExistException e) {
            ViewDTO view = new ViewDTO();
            view.setTransferObject(e.getMessage());
            map.addAttribute(view);
            return ViewConstants.DEFAULT;

        } catch (JSONException e) {
            ViewDTO view = new ViewDTO();
            view.setTransferObject("The data is corrupted, please send again");
            map.addAttribute(view);
            return ViewConstants.DEFAULT;
        }
        ViewDTO view = new ViewDTO();
        view.setTransferObject(fileId);
        map.addAttribute(view);
        map.addAttribute("isCreation", new Boolean(false));
        if (log.isInfoEnabled()) {
            log.info("update object id " + fileId + " for app id " + clientKey);
        }
        return ViewConstants.OBJECT_CREATE;

    }

    @RequestMapping(value = "/get/object/(*:clientKey)/(*:fileId)/(*:userName)", method = RequestMethod.GET)
    public String getObject(
            @RequestParam("fileId")String fileId, @RequestParam("clientKey")String clientKey, @RequestParam("userName")String userName, ModelMap map) {

        streamRequestsFacade.countRequest(clientKey);
        String finalKey = clientKey + userName + fileId;
        String objectJson = streamObjectCache.getObject(finalKey);
        if (objectJson == null) {
            objectJson = getFromStorage(fileId, clientKey, userName, SystemConstants.OBJECT_BUKET);
            if (objectJson == null) {
                ViewDTO view = new ViewDTO();
                view.setTransferObject("This object id " + fileId + " does not exist, please create the object first");
                map.addAttribute(view);
                return ViewConstants.DEFAULT;
            } else {
                streamObjectCache.cacheObject(finalKey, objectJson);
            }
        }
        ViewDTO view = new ViewDTO();
        view.setTransferObject(objectJson);
        map.addAttribute(view);
         if (log.isInfoEnabled()){
            log.info("get object id " + fileId + " for app id " + clientKey);
        }
        return ViewConstants.OBJECT_JSON_VIEW;

    }

    @RequestMapping(value = "/delete/object/(*:clientKey)/(*:fileId)/(*:userName)", method = RequestMethod.DELETE)
    public String deleteObject(@RequestParam("fileId")String fileId, @RequestParam("clientKey")String clientKey, @RequestParam("userName")String userName, ModelMap map) {

        streamRequestsFacade.countRequest(clientKey);
        String finalKey = clientKey + userName + fileId;
        deleteFromStorage(fileId, clientKey, userName, SystemConstants.OBJECT_BUKET);
        streamObjectCache.removeObjectFromCache(finalKey);
        ViewDTO view = new ViewDTO();
        view.setTransferObject("OK");
        map.addAttribute(view);
         if (log.isInfoEnabled()){
            log.info("delete object id " + fileId + " for app id " + clientKey);
        }
        return ViewConstants.DEFAULT;
    }


    @RequestMapping(value = "/get/objectlist/(*:clientKey)/(*:userName)", method = RequestMethod.GET)
    public String getObjectList(@RequestParam("userName")String userName, @RequestParam("clientKey")String clientKey, ModelMap map) {

        streamRequestsFacade.countRequest(clientKey);
        List<ObjectListDTO> objects = storageDomainFacade.getObjectList(SystemConstants.OBJECT_BUKET, userName, clientKey);
        if (log.isInfoEnabled()){
            log.info("get object list for app id: " + clientKey);
        }
        ViewDTO view = new ViewDTO();
        view.setCollections(objects);
        view.setTransferObject(clientKey + userName);
        map.addAttribute(view);
        return ViewConstants.OBJECT_LIST_VIEW;

    }

    @RequestMapping(value = "/export/(*:clientKey)/(*:userName)", method = RequestMethod.GET)
    public void export(@RequestParam("userName")String userName, @RequestParam("clientKey")String clientKey, HttpServletResponse res){

        try {
            //export counts as 10 requests
            for (int i=0; i < 10; i++)
                streamRequestsFacade.countRequest(clientKey);

            File file =  exportImportFacade.export(clientKey, userName, SystemConstants.OBJECT_BUKET);
            InputStream in = new FileInputStream(file);
             if (in != null) {
                res.setContentType("application/octet-stream");
                String name = clientKey + ".zip";
                res.setHeader("Content-Disposition", "attachment;filename=" + name);
                OutputStream output = res.getOutputStream();
                IOUtils.copy(in, output);
                output.close();
                in.close();
                file.delete();
            }

        } catch (IOException e) {

        }

    }


    @RequestMapping(value = "/import/(*:clientKey)", method = RequestMethod.POST)
    public void importData(HttpServletRequest req, @RequestParam("clientKey")String clientKey) {

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

                    in = item.getInputStream();
                    exportImportFacade.importData(in, clientKey, SystemConstants.OBJECT_BUKET);
                }
            }
        }
        catch (FileUploadException e) {
        }
        catch (IOException e) {
        }
        finally {

        }
        if (log.isInfoEnabled()){
            log.info("");
        }


    }


    public void deleteFromStorage(String fileId, String clientKey, String userName, String buketName) {
        storageDomainFacade.delete(fileId, clientKey, userName, buketName);
    }

    public String getFromStorage(String fileId, String clientKey, String userName, String buketName) {
        return storageDomainFacade.get(fileId, clientKey, userName, buketName);
    }

    public String sendToStorage(String json, String clientKey, String userName, String buketName, String id) {
        return storageDomainFacade.send(json, clientKey, userName, buketName, id);
    }

    public String updateStorage(String fileId, String json, String clientKey, String userName, String buketName) {
        return storageDomainFacade.update(fileId, json, clientKey, userName, buketName);
    }

}
