package com.ugc.facade;

import com.ugc.helper.FileCreationHelper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MockFileHelper implements FileCreationHelper
{
    public File getFile(String fileId) throws IOException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public File getFileNotThrownException(String fileId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String create() throws IOException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getImportFolder() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String generateFilePath(String uid) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getRootFolder() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getMediaStorePath() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public File createFile() throws IOException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public File createFileWithFileId(String fileId) throws IOException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getCertFolder() {
        return "./test.txt";  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getCalendarFolder() {
         return "./test.txt";
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
