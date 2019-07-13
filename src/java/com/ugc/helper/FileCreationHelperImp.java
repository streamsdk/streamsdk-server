package com.ugc.helper;

import com.ugc.utils.ConversionUtils;
import com.ugc.utils.PlaceholderProperty;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileCreationHelperImp implements FileCreationHelper {

    private String _rootFolder = "";
    private String _certFolder = "";
    private String _calendarFolder = "";
    private String _mediaStorePath;
    private String _streamObjectCacheSchedule = "";
    private String _streamRequestSchedule = "";
    private String _exportFolder = "";
    private String _importFolder = "";
    private String _redirectUrl = "";
    private String _jsonFileText = "";

    public FileCreationHelperImp() {
    }

    @PlaceholderProperty(key = "exportfolder")
    public void setExportFolder(String export){
        _exportFolder = export;
    }

    @PlaceholderProperty(key = "importfolder")
    public void setImportFolder(String importF){
        _importFolder = importF;
    }

    @PlaceholderProperty(key = "redirectUrl")
    public void setRedirectUrl(String url){
        _redirectUrl = url;
    }

    public String getRedirectUrl(){
        return _redirectUrl;
    }

    public String getExportFolder(){
        return _exportFolder;
    }

    public String getImportFolder(){
        return _importFolder;
    }

    @PlaceholderProperty(key = "rootfolder")
    public void setRootFolder(String rootFolder) {
        _rootFolder = rootFolder;
    }
    
    @PlaceholderProperty(key = "certfolder")
    public void setCertFolder(String rootFolder) {
        _certFolder = rootFolder;
    }

    @PlaceholderProperty(key = "calendarfolder")
    public void setCalendarFolder(String rootFolder) {
        _calendarFolder = rootFolder;
    }

    public String getStreamObjectCacheSchedule() {
        return _streamObjectCacheSchedule;
    }

    @PlaceholderProperty(key = "streamObjectCacheSchedule")
    public void setStreamObjectCacheSchedule(String streamObjectCacheSchedule) {
        _streamObjectCacheSchedule = streamObjectCacheSchedule;
    }

    @PlaceholderProperty(key = "jsonHashFile")
    public void setJsonHashFile(String file){
        _jsonFileText = file;
    }

    public String getJsonHashFile(){
        return _jsonFileText;
    }

    public String getStreamRequestSchedule() {
        return _streamRequestSchedule;
    }

    @PlaceholderProperty(key = "streamRequestSchedule")
    public void setStreamRequestSchedule(String streamRequestSchedule) {
        _streamRequestSchedule = streamRequestSchedule;
    }

    public String getCertFolder() {
        return _certFolder;
    }

    public String getRootFolder() {
        return _rootFolder;
    }

    public String getCalendarFolder(){
        return _calendarFolder;
    }

    @PlaceholderProperty(key = "mediapath")
    public void setMediaStorePath(String path)
    {
       _mediaStorePath = path;
    }

    public String getMediaStorePath() {
        return _mediaStorePath;
    }

    public File createFile() throws IOException {
        String uid = UUID.randomUUID().toString().replace("-", "");
        File file = generateFileObjFromFileUid(uid);
        if (file != null) {
            createFolders(file.getParentFile());
            file.createNewFile();
        }
        return file;
    }

    public File createFileWithFileId(String fileId) throws IOException {
        File file = generateFileObjFromFileUid(fileId);
        if (file != null) {
            createFolders(file.getParentFile());
            file.createNewFile();
        }
        return file;
    }

    public File getFile(String uid) throws IOException {
        File file = generateFileObjFromFileUid(uid);
        if (file == null || !file.exists())
            throw new FileNotFoundException("entry " + uid + " not found.");
        return file;
    }

    public File getFileNotThrownException(String uid){
        try {
            File file = generateFileObjFromFileUid(uid);
            return file;
        } catch (IOException e) {

        }
        return null;
     }

    public String create() throws IOException {
        String uid = UUID.randomUUID().toString().replace("-", "");
        File file = generateFileObjFromFileUid(uid);
        if (file != null) {
            createFolders(file.getParentFile());
            file.createNewFile();
        }
        return uid;
    }

    private void createFolders(File folders) {
        if (folders != null)
            folders.mkdirs();
    }

    private File generateFileObjFromFileUid(String uid) throws IOException {
        return uid != null ? new File(new File(_rootFolder, generatePath(getHashValue(uid))), uid) : null;
    }

    public String generateFilePath(String uid) {
        return generatePath(getHashValue(uid));
    }

    private String generatePath(String hash) {
        StringBuilder sb = new StringBuilder();
        sb.append(File.separator).append(hash.substring(0, 1));
        for (int i = 1; i < 5; i += 2)
            sb.append(File.separator).append(hash.substring(i, i + 2));

        return sb.toString();
    }

    private String getHashValue(String uid) {
        try {
            return ConversionUtils.createMD5Hash(uid, true);
        } catch (NoSuchAlgorithmException e) {

        }
        return "";
    }

    public List<File> filesToBeRemoved(){
        List<File> files = new ArrayList<File>();
        File file = new File(_rootFolder);
        File listFiles[] = file.listFiles();
        listFiles(files, listFiles);
        List<File> fileToBeRemoved = new ArrayList<File>();
        long currentTime = System.currentTimeMillis();
        for (File currentFile : files){
            long difference = (currentTime - currentFile.lastModified()) / 1000;
            System.out.println("difference: " + difference);
            if (difference > 60 * 60){
                fileToBeRemoved.add(currentFile);
            }
        }
        return fileToBeRemoved;
    }

    private void listFiles(List<File> files, File[] listFiles){

        for (File file : listFiles){
            if (file.isDirectory()){
                listFiles(files, file.listFiles());
            }else{
                System.out.println("adding file: " + file.getName());
                files.add(file);
            }
        }
    }
}


