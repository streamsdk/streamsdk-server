package com.ugc.controller;

import com.ugc.domain.ObjectListDTO;
import com.ugc.facade.*;
import com.ugc.view.ViewConstants;
import com.ugc.view.ViewDTO;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
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
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

@Controller
public class StoreObjectListController
{

    private StorageDomainFacade storageDomainFacade;
    private StreamObjectCacheFacade streamObjectCache;
    private UpdateStreamCategoryObjectFacade updateStreamCategoryObjectFacade;
    private StreamRequestsFacade streamRequestsFacade;
    private ExportImportFacade exportImportFacade;
    private static Log log = LogFactory.getLog(StoreObjectListController.class);

    @Autowired
    public StoreObjectListController(StorageDomainFacade storageDomainFacade, StreamObjectCacheFacade streamObjectCache,ExportImportFacade exportImportFacade,
                                     UpdateStreamCategoryObjectFacade updateStreamCategoryObjectFacade,  StreamRequestsFacade streamRequestsFacade) {
        this.storageDomainFacade = storageDomainFacade;
        this.streamObjectCache = streamObjectCache;
        this.updateStreamCategoryObjectFacade = updateStreamCategoryObjectFacade;
        this.streamRequestsFacade = streamRequestsFacade;
        this.exportImportFacade = exportImportFacade;
    }

    @RequestMapping(value = "/store/object/category/(*:clientKey)/(*:userName)", method = RequestMethod.POST)
    public String createCategoryObject(
            @RequestParam("json")String json,
            @RequestParam("userName")String userName,
            @RequestParam("clientKey")String clientKey,
            @RequestParam("category")String categoryName,
            ModelMap map) {

        streamRequestsFacade.countRequest(clientKey);
        String finalKey = clientKey + userName + categoryName;
        if (log.isInfoEnabled()){
            log.info("create new category " + categoryName + "  for app id " + clientKey);
        }
        if (storageDomainFacade.isKeyExists(finalKey, SystemConstants.CATEGORY_BUUKET)) {
            ViewDTO view = new ViewDTO();
            view.setTransferObject("the category " + categoryName + " aleady exists, please create a uniqiue category name");
            map.addAttribute(view);
            return ViewConstants.DEFAULT;
        } else {
            String key = sendToStorage(json, clientKey, userName, categoryName, SystemConstants.CATEGORY_BUUKET);
            ViewDTO view = new ViewDTO();
            view.setTransferObject(key);
            map.addAttribute(view);
            map.addAttribute("isCreation", new Boolean(true));
            return ViewConstants.OBJECT_CREATE;
        }
    }

    @RequestMapping(value = "/get/category/object/(*:clientKey)/(*:category)/(*:userName)", method = RequestMethod.GET)
    public String getObjects(
            @RequestParam("category")String categoryName,
            @RequestParam("clientKey")String clientKey,
            @RequestParam("userName")String userName, ModelMap map) {

        streamRequestsFacade.countRequest(clientKey);
        String finalKey = clientKey + userName + categoryName;
        String json = streamObjectCache.getObject(finalKey);
        if (json == null) {
            json = getFromStorage(clientKey, userName, categoryName, SystemConstants.CATEGORY_BUUKET);
            if (json == null) {
                ViewDTO view = new ViewDTO();
                view.setTransferObject("This category " + categoryName + " does not exist, please create the category first");
                map.addAttribute(view);
                return ViewConstants.DEFAULT;
            } else {
                streamObjectCache.cacheObject(finalKey, json);
            }
        }
        ViewDTO view = new ViewDTO();
        view.setTransferObject(json);
        map.addAttribute(view);
        if (log.isInfoEnabled()){
           log.info("get objects within category " + categoryName);
        }
        return ViewConstants.OBJECT_JSON_VIEW;

    }

    @RequestMapping(value = "/category/import/(*:clientKey)", method = RequestMethod.POST)
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
                    exportImportFacade.importData(in, clientKey, SystemConstants.CATEGORY_BUUKET);
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

    @RequestMapping(value = "/category/export/(*:clientKey)/(*:userName)", method = RequestMethod.GET)
    public void export(@RequestParam("userName")String userName, @RequestParam("clientKey")String clientKey, HttpServletResponse res){

        try {

             //export counts as 10 requests
            for (int i=0; i < 10; i++)
                streamRequestsFacade.countRequest(clientKey);

             File file =  exportImportFacade.export(clientKey, userName, SystemConstants.CATEGORY_BUUKET);
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

    @RequestMapping(value = "/delete/categoryobject/(*:clientKey)/(*:category)/(*:userName)", method = RequestMethod.DELETE)
    public String deleteCategoryObject(@RequestParam("category")String categoryName,
                                       @RequestParam("clientKey")String clientKey,
                                       @RequestParam("userName")String userName, ModelMap map) {

        streamRequestsFacade.countRequest(clientKey);
        String finalKey = clientKey + userName + categoryName;
        if (!storageDomainFacade.isKeyExists(finalKey, SystemConstants.CATEGORY_BUUKET)) {
            ViewDTO view = new ViewDTO();
            view.setTransferObject("this category does not exist, please create the category first");
            map.addAttribute(view);
            return ViewConstants.DEFAULT;
        } else {
            storageDomainFacade.deleteObject(finalKey, SystemConstants.CATEGORY_BUUKET);
            streamObjectCache.removeObjectFromCache(finalKey);
        }
        ViewDTO view = new ViewDTO();
        map.addAttribute(view);
        view.setTransferObject("OK");
        if (log.isInfoEnabled()){
            log.info("delete category " + categoryName);
        }
        return ViewConstants.DEFAULT;
    }

    @RequestMapping(value = "/delete/category/object/(*:clientKey)/(*:userName)", method = RequestMethod.POST)
    public String deleteStreamObjects(@RequestParam("category")String categoryName,
                                      @RequestParam("clientKey")String clientKey,
                                      @RequestParam("json")String json,
                                      @RequestParam("userName")String userName, ModelMap map) {

        streamRequestsFacade.countRequest(clientKey);
        try {
            updateStreamCategoryObjectFacade.deleteStreamObjects(json, categoryName, clientKey, userName);
        } catch (JSONException e) {
            ViewDTO view = new ViewDTO();
            view.setTransferObject("The data is corrupted, please send again");
            map.addAttribute(view);
            return ViewConstants.DEFAULT;
        } catch (ObjectKeyNotExistException e) {
            ViewDTO view = new ViewDTO();
            view.setTransferObject(e.getMessage());
            map.addAttribute(view);
            return ViewConstants.DEFAULT;
        }
        ViewDTO view = new ViewDTO();
        map.addAttribute(view);
        view.setTransferObject("OK");
        if (log.isInfoEnabled()){
            log.info("delete stream objects within category " + categoryName);
        }
        return ViewConstants.DEFAULT;

    }

    @RequestMapping(value = "/remove/category/fields/(*:clientKey)/(*:userName)", method = RequestMethod.POST)
       public String removeFields(@RequestParam("category")String categoryName,
                                 @RequestParam("clientKey")String clientKey,
                                 @RequestParam("objectId")String objectId,
                                 @RequestParam("keys")String keys,
                                 @RequestParam("userName")String userName, ModelMap map) {

          streamRequestsFacade.countRequest(clientKey);
          try {
               String keyList[] = keys.split(",");
               for (String key : keyList)
                    updateStreamCategoryObjectFacade.removeField(objectId, categoryName, clientKey, userName, key);
           } catch (ObjectKeyNotExistException e) {
               ViewDTO view = new ViewDTO();
               view.setTransferObject("The data is corrupted, please send again");
               map.addAttribute(view);
               return ViewConstants.DEFAULT;

           } catch (JSONException e) {
               ViewDTO view = new ViewDTO();
               view.setTransferObject(e.getMessage());
               map.addAttribute(view);
               return ViewConstants.DEFAULT;
           }
           ViewDTO view = new ViewDTO();
           map.addAttribute(view);
           view.setTransferObject("OK");
           if (log.isInfoEnabled()){
               log.info("remove object " + objectId + " with keys " + keys + " for category " + categoryName + " of app id " + clientKey);
           }
           return ViewConstants.DEFAULT;
       }



     @RequestMapping(value = "/remove/category/field/(*:clientKey)/(*:userName)", method = RequestMethod.POST)
     public String removeField(@RequestParam("category")String categoryName,
                              @RequestParam("clientKey")String clientKey,
                              @RequestParam("id")String id,
                              @RequestParam("fieldName")String fieldName,
                              @RequestParam("userName")String userName, ModelMap map) {

        streamRequestsFacade.countRequest(clientKey);
        try {
            updateStreamCategoryObjectFacade.removeField(id, categoryName, clientKey, userName, fieldName);
        } catch (ObjectKeyNotExistException e) {
            ViewDTO view = new ViewDTO();
            view.setTransferObject("The data is corrupted, please send again");
            map.addAttribute(view);
            return ViewConstants.DEFAULT;

        } catch (JSONException e) {
            ViewDTO view = new ViewDTO();
            view.setTransferObject(e.getMessage());
            map.addAttribute(view);
            return ViewConstants.DEFAULT;
        }
        ViewDTO view = new ViewDTO();
        map.addAttribute(view);
        view.setTransferObject("OK");
        if (log.isInfoEnabled()){
            log.info("remove object " + id + " for category " + categoryName + " of app id " + clientKey);
        }
        return ViewConstants.DEFAULT;
    }

    @RequestMapping(value = "/get/categoryobjectlist/(*:clientKey)/(*:userName)", method = RequestMethod.GET)
    public String getObjectList(@RequestParam("userName")String userName, @RequestParam("clientKey")String clientKey,   ModelMap map) {

        streamRequestsFacade.countRequest(clientKey);
        List<ObjectListDTO> objects = storageDomainFacade.getObjectList(SystemConstants.CATEGORY_BUUKET, userName, clientKey);
        ViewDTO view = new ViewDTO();
        view.setCollections(objects);
        view.setTransferObject(clientKey + userName);
        map.addAttribute(view);
        if (log.isInfoEnabled()){
           log.info("get list of categories for app id " + clientKey);
        }
        return ViewConstants.OBJECT_LIST_VIEW;

    }

   @RequestMapping(value = "/update/category/object/(*:clientKey)/(*:userName)", method = RequestMethod.POST)
    public String updateCategory(@RequestParam("category")String categoryName,
                                 @RequestParam("clientKey")String clientKey,
                                 @RequestParam("json")String json,
                                 @RequestParam("userName")String userName, ModelMap map) {

        streamRequestsFacade.countRequest(clientKey);
        try {
            updateStreamCategoryObjectFacade.updateStreamObjects(categoryName, clientKey, userName, json);
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
        map.addAttribute(view);
        view.setTransferObject("OK");
        if (log.isInfoEnabled()){
           log.info("update category " + categoryName + " for app id " + clientKey);
        }

        return ViewConstants.DEFAULT;

    }


    public String getFromStorage(String clientKey, String userName, String category, String buketName) {
        return storageDomainFacade.retriveAsCategory(clientKey, userName, category, buketName);
    }

    private String sendToStorage(String json, String clientKey, String userName, String category, String buketName) {
        return storageDomainFacade.saveAsCategory(json, clientKey, userName, category, buketName);
    }

}
