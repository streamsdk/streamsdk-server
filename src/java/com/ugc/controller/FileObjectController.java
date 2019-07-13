package com.ugc.controller;

import com.ugc.domain.ObjectListDTO;
import com.ugc.facade.ImageHashSearchFacade;
import com.ugc.facade.StorageDomainFacade;
import com.ugc.facade.StreamRequestsFacade;
import com.ugc.helper.FileCreationHelper;
import com.ugc.utils.FileMaster;
import com.ugc.utils.StreamUtils;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.Map;

@Controller
public class FileObjectController
{
    private FileCreationHelper fch;
    private StorageDomainFacade storageDomainFacade;
    private StreamRequestsFacade streamRequestsFacade;
    private ImageHashSearchFacade imageHashSearchFacade;
    private static Log log = LogFactory.getLog(FileObjectController.class);

    @Autowired
    public FileObjectController(FileCreationHelper fch, StorageDomainFacade storageDomainFacade,
            ImageHashSearchFacade imageHashSearchFacade, StreamRequestsFacade streamRequestsFacade) {
        this.fch = fch;
        this.storageDomainFacade = storageDomainFacade;
        this.streamRequestsFacade = streamRequestsFacade;
        this.imageHashSearchFacade = imageHashSearchFacade;
    }

    @RequestMapping(value = "/store/(*:from)/file", method = RequestMethod.POST)
    public String postFile(HttpServletRequest req, @RequestParam("from")String from,
                           ModelMap map) throws Exception {

        String objectId = storeCurrentUploadedFile(req, from);
        ViewDTO view = new ViewDTO();
        view.setTransferObject(objectId);
        map.addAttribute(view);
        map.addAttribute("isCreation", new Boolean(true));
        if (log.isInfoEnabled()){
            log.info("posting file: " + objectId);
        }
        return ViewConstants.OBJECT_CREATE;

    }


    @RequestMapping(value = "/send/file/(*:clientKey)/(*:fileId)/(*:userName)", method = RequestMethod.POST)
    public String postFileWithMetaData(@RequestParam("userName")String userName, @RequestParam("clientKey")String clientKey,
                                       @RequestParam("fileId")String fileId,
                                       @RequestParam("json")String json, ModelMap map) throws Exception {

        streamRequestsFacade.countRequest(clientKey);
        streamRequestsFacade.countRequest(clientKey);
        File file = fch.getFile(fileId);
        long length = file.length();
        System.out.println(length);
        if (imageHashSearchFacade.isTestMode())
            imageHashSearchFacade.searchImageAndSendPush(json, clientKey, file);
        sendToStorage(fileId, clientKey, userName, new FileInputStream(file), json, length, SystemConstants.FILE_BUKET);
        ViewDTO view = new ViewDTO();
        view.setTransferObject("ok");
        map.addAttribute(view);
        if (log.isInfoEnabled()){
            log.info("posting file with metadata: " + fileId);
        }
        return ViewConstants.DEFAULT;

    }

    @RequestMapping(value = "/get/file/(*:clientKey)/(*:fileId)/(*:userName)", method = RequestMethod.GET)
    public void getObject(
            @RequestParam("fileId")String fileId, @RequestParam("userName")String userName,
            @RequestParam("clientKey")String clientKey, ModelMap map, HttpServletResponse res) {

        // TODO: add proper error response message, this cache solution might create problem when scale is a concern, add clean up thread for these files
        streamRequestsFacade.countRequest(clientKey);
        InputStream in = null;
        File file = fch.getFileNotThrownException(fileId);
        if (file != null && file.exists()) {
            try {
                in = new FileInputStream(file);
                StreamUtils.writeStream(in, res.getOutputStream());

            } catch (FileNotFoundException e) {

            } catch (IOException e) {
                
            }
            if (log.isInfoEnabled()){
                 log.info("downloading file: " + fileId + " streaming file from file system");
             }
        } else {
            in = getInputStreamFromStorage(fileId, clientKey, userName, SystemConstants.FILE_BUKET);
            try {
                File fileToCache = fch.createFileWithFileId(fileId);
                FileOutputStream fOut = new FileOutputStream(fileToCache);
                ServletOutputStream to = res.getOutputStream();
              //  StreamUtils.writeDoubleStream(in, fOut, to);
                StreamUtils.writeSingleStream(in, to);
                in = getInputStreamFromStorage(fileId, clientKey, userName, SystemConstants.FILE_BUKET);
                StreamUtils.writeSingleStream(in, fOut);

                if (log.isInfoEnabled()){
                    log.info("downloading file: " + fileId + " streaming file from amazon s3");
                }
            } catch (IOException e) {

            }
        }
    }


    @RequestMapping(value = "/get/file/metadata/(*:clientKey)/(*:fileId)/(*:userName)", method = RequestMethod.GET)
    public String getObjectMetaData(
            @RequestParam("fileId")String fileId, @RequestParam("userName")String userName, @RequestParam("clientKey")String clientKey,
            ModelMap map, HttpServletResponse res) throws IOException {

        streamRequestsFacade.countRequest(clientKey);
        Map<String, String> metaData = getObjectMetaData(fileId, clientKey, userName, SystemConstants.FILE_BUKET);
        ViewDTO view = new ViewDTO();
        view.setTransferObject(metaData);
        map.addAttribute(view);
        if (log.isInfoEnabled()){
            log.info("getting file metadata: " + fileId + " for app id " + clientKey);
        }
        return ViewConstants.FILE_METADATA_VIEW;

    }

    @RequestMapping(value = "/delete/file/(*:clientKey)/(*:fileId)/(*:userName)", method = RequestMethod.DELETE)
    public String deleteObject(@RequestParam("fileId")String fileId, @RequestParam("clientKey")String clientKey, @RequestParam("userName")String userName, ModelMap map) {

        streamRequestsFacade.countRequest(clientKey);
        deleteFromStorage(fileId, clientKey, userName, SystemConstants.FILE_BUKET);
        ViewDTO view = new ViewDTO();
        view.setTransferObject("OK");
        map.addAttribute(view);
        if (log.isInfoEnabled()){
            log.info("deleting file: " + fileId + " for app id " + clientKey);
        }
        return ViewConstants.DEFAULT;

    }


    @RequestMapping(value = "/get/filelist/(*:clientKey)/(*:userName)", method = RequestMethod.GET)
    public String getObjectList(@RequestParam("userName")String userName, @RequestParam("clientKey")String clientKey, ModelMap map) {

        streamRequestsFacade.countRequest(clientKey);
        List<ObjectListDTO> objects = storageDomainFacade.getObjectList(SystemConstants.FILE_BUKET, userName, clientKey);
        ViewDTO view = new ViewDTO();
        view.setCollections(objects);
        view.setTransferObject(clientKey + userName);
        map.addAttribute(view);
        if (log.isInfoEnabled()){
            log.info("getting file list for app id " + clientKey);
        }
        return ViewConstants.OBJECT_LIST_VIEW;

    }

    public void deleteFromStorage(String fileId, String clientKey, String userName, String buketName) {
        storageDomainFacade.delete(fileId, clientKey, userName, buketName);
    }

    public Map<String, String> getObjectMetaData(String fileId, String clientKey, String userName, String buketName) {
        return storageDomainFacade.getObjectMetaData(fileId, clientKey, userName, buketName);
    }

    public InputStream getInputStreamFromStorage(String fileId, String clientKey, String userName, String buketName) {
        return storageDomainFacade.getInputStream(fileId, clientKey, userName, buketName);
    }

    public void sendToStorage(String key, String clientKey, String userName, InputStream in, String json, long length, String buketName) throws IOException {
        storageDomainFacade.send(buketName, key, clientKey, userName, in, json, length);
    }

    private String storeCurrentUploadedFile(HttpServletRequest req, String from) throws IOException {
        File file = fch.createFile();
        FileMaster master = new FileMaster(file);
        master.setCurrentChannelPosition(0);

        if (!from.equals("android")) {
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
        } else {
            master.writeToFileSystem(req.getInputStream());

        }

        return file.getName();
    }

}
