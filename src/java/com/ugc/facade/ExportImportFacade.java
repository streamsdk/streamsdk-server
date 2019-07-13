package com.ugc.facade;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;


public interface ExportImportFacade
{
    public File export(String clientKey, String userName, String buketName) throws IOException;

    public void importData(InputStream in, String clientKey, String buketName) throws IOException;

    public String getExportFilePath(String clientKey, String userName);

}
