package com.ugc.view;

import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class FileMetadataView extends AbstractView
{
    protected void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {

        ViewDTO view = (ViewDTO) model.get(ViewConstants.VIEW_DTO);
        Map<String, String> metaDataMap = (Map<String, String>) view.getTransferObject();
        String json = metaDataMap.get("json");
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(json.getBytes());
        outputStream.close();


    }
}
