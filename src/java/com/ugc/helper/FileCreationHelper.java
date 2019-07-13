package com.ugc.helper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface FileCreationHelper
{

   File getFile(String fileId) throws IOException;

   File getFileNotThrownException(String fileId); 

   String create() throws IOException;

   String getImportFolder();

   String generateFilePath(String uid);

   String getRootFolder();

   String getMediaStorePath();

   File createFile() throws IOException;

   File createFileWithFileId(String fileId) throws IOException; 

   String getCertFolder();

   String getCalendarFolder();

   List<File> filesToBeRemoved();

   String getStreamObjectCacheSchedule();

   String getStreamRequestSchedule();

   String getExportFolder();

   String getRedirectUrl();

   String getJsonHashFile();

}

