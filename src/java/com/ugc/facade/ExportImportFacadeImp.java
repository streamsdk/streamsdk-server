package com.ugc.facade;

import com.ugc.domain.ObjectListDTO;
import com.ugc.helper.FileCreationHelper;
import com.ugc.controller.SystemConstants;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;


@Service(value = "ExportImportFacade")
public class ExportImportFacadeImp implements ExportImportFacade
{

    private StorageDomainFacade storageDomainFacade;
    private FileCreationHelper fch;
    private static Log log = LogFactory.getLog(PushNotificationFacadeImp.class);

    @Autowired
    public ExportImportFacadeImp(StorageDomainFacade storageDomainFacade, FileCreationHelper fch) {
        this.storageDomainFacade = storageDomainFacade;
        this.fch = fch;
    }

    public String getExportFilePath(String clientKey, String userName) {

        String exportFolder = fch.getExportFolder();
        File finalZip = new File(exportFolder, clientKey);
        return finalZip.getAbsolutePath();
    }


    public File export(String clientKey, String userName, String buketName) {

        List<ObjectListDTO> objects = storageDomainFacade.getObjectList(buketName, userName, clientKey);
        String prefix = clientKey + userName;
        String exportFolder = fch.getExportFolder();
        String tempExportPath = fch.getExportFolder() + File.separator + prefix;
        File tempExport = new File(tempExportPath);
        tempExport.mkdir();
        for (ObjectListDTO s3Object : objects) {
            String fullKey = s3Object.getId();
            String id = "";
            if (fullKey.contains(SystemConstants.STREAMSDK)){
               id = fullKey;
            }else{
               id = fullKey.replace(prefix, "");
            }
            InputStream in;
            if (id.equals(fullKey)){
                in = storageDomainFacade.getInputStream(id, "", userName, buketName);
            }else{
                in = storageDomainFacade.getInputStream(id, clientKey, userName, buketName);
            }

            File tempExportFolder = new File(tempExport, id);
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(tempExportFolder);
                IOUtils.copy(in, fileOutputStream);

            } catch (FileNotFoundException e) {
                log.error(e.getMessage());
            } catch (IOException e) {
                log.error(e.getMessage());
            } finally {
                if (in != null)
                    try {
                        in.close();
                    } catch (IOException e) {
                        log.error(e.getMessage());
                    }
                if (fileOutputStream != null)
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        log.error(e.getMessage());
                    }
            }
        }

        String parentFile = exportFolder + File.separator + clientKey + userName;
        File zipDir = new File(parentFile);
        String tempZipFileFolder = exportFolder + File.separator + "tmp";
        File finalZipTemp = new File(tempZipFileFolder, clientKey + userName + ".zip");
        try {
            finalZipTemp.createNewFile();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(finalZipTemp);
        } catch (FileNotFoundException e) {
            log.error(e.getMessage());
        }
        ZipOutputStream zos = new ZipOutputStream(out);
        try {
            zipDir(zipDir, zos);
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            try {
                zos.close();
                out.close();
                FileUtils.deleteDirectory(zipDir);
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        return finalZipTemp;

    }

    public void importData(InputStream in, String clientKey, String buketName) throws IOException {

        String importFolder = fch.getImportFolder();
        String tempImportFolder = importFolder + File.separator + clientKey;
        File importDataFolder = new File(tempImportFolder);
        importDataFolder.mkdir();
        File zipFile = new File(importFolder + File.separator + "tmp" + File.separator + clientKey);
        zipFile.createNewFile();
        FileOutputStream out = new FileOutputStream(zipFile);
        //copy the zip file's inputstream to disk
        try {
            IOUtils.copy(in, out);
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }

        unzipToDisk(zipFile, importDataFolder);
        zipFile.delete();
        doImport(importDataFolder, clientKey, buketName);
        FileUtils.deleteDirectory(importDataFolder);

    }


    private void doImport(File importDataFolder, String clientKey, String buketName) throws IOException {

        File fileIds[] = importDataFolder.listFiles();
        for (File fileId : fileIds) {
            String id = fileId.getName();
            String json = FileUtils.readFileToString(fileId, "UTF-8");
            if (buketName.equals(SystemConstants.CATEGORY_BUUKET)){
                if (id.contains(SystemConstants.STREAMSDK))
                   storageDomainFacade.saveAsCategory(json, "", "", id, SystemConstants.CATEGORY_BUUKET);
                else
                   storageDomainFacade.saveAsCategory(json, clientKey, "", id, SystemConstants.CATEGORY_BUUKET);
            }
            if (buketName.equals(SystemConstants.OBJECT_BUKET)){
               if (id.contains(SystemConstants.STREAMSDK))
                   storageDomainFacade.send(json, "", "", SystemConstants.OBJECT_BUKET, id);
                else
                   storageDomainFacade.send(json, clientKey, "", SystemConstants.OBJECT_BUKET, id);
            }
        }
    }

    private void unzipToDisk(File tempZipFile, File importDataFolder) throws IOException {
        ZipFile zipFile = new ZipFile(tempZipFile);
        Enumeration<?> enumeration = zipFile.entries();
        while (enumeration.hasMoreElements()) {
            ZipEntry zipEntry = (ZipEntry) enumeration.nextElement();
            BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(zipEntry));
            int size;
            byte[] buffer = new byte[2048];
            String name = zipEntry.getName();
            File currentFile = new File(importDataFolder, name);
            File parent = currentFile.getParentFile();
            if (!parent.exists()) {
                parent.mkdir();
            }
            currentFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(currentFile);
            while ((size = bis.read(buffer, 0, buffer.length)) != -1) {
                fos.write(buffer, 0, size);
            }
            fos.close();
            bis.close();
        }
        zipFile.close();
        if (log.isDebugEnabled()) {
            log.debug("Temp export zip file " + zipFile.getName() + " created");
        }
    }


    private void zipDir(File zipDir, ZipOutputStream zos) throws IOException {

        String[] dirList = zipDir.list();
        byte[] readBuffer = new byte[2156];
        int bytesIn = 0;
        for (int i = 0; i < dirList.length; i++) {
            File f = new File(zipDir, dirList[i]);
            FileInputStream fis = new FileInputStream(f);
            String path = f.getName();
            ZipEntry anEntry = new ZipEntry(path);
            zos.putNextEntry(anEntry);
            while ((bytesIn = fis.read(readBuffer)) != -1) {
                zos.write(readBuffer, 0, bytesIn);
            }
            if (log.isDebugEnabled())
                log.debug("Compressing " + anEntry.getName());
            fis.close();
        }
    }
}
