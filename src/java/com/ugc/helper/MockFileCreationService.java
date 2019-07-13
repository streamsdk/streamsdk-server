package com.ugc.helper;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;
import java.util.List;

public class MockFileCreationService implements FileCreationHelper
{
    public Map<String, File> _files = new HashMap<String, File>();

    public File getFile(String fileId) throws IOException
    {
        return _files.get(fileId);
    }

    public File getFileNotThrownException(String fileId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String create() throws IOException
    {
       String uid = UUID.randomUUID().toString().replace("-", "");
       _files.put(uid, new File("d:\\temp\\" + uid));
        return uid;
    }

    public String getImportFolder() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String generateFilePath(String uid)
    {
        return "";
    }

    public String getRootFolder()
    {
        return "";
    }

    public String getMediaStorePath() {
        return "";
    }

    public File createFile() throws IOException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public File createFileWithFileId(String fileId) throws IOException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getCertFolder() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getCalendarFolder() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<File> filesToBeRemoved() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getStreamObjectCacheSchedule() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getStreamRequestSchedule() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getExportFolder() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getRedirectUrl() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getJsonHashFile() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


}
