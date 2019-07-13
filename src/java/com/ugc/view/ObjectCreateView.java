package com.ugc.view;


import org.json.JSONStringer;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class ObjectCreateView extends AbstractView
{
    @Override
    protected void renderMergedOutputModel(Map map, HttpServletRequest request, HttpServletResponse response) throws Exception {

        ViewDTO view = (ViewDTO)map.get(ViewConstants.VIEW_DTO);
        boolean isCreation = ((Boolean)map.get("isCreation")).booleanValue();
        String objectId = (String) view.getTransferObject();
        JSONStringer jsonObject = new JSONStringer();
        jsonObject.object().key("id").value(objectId);
        if (!isCreation)
            jsonObject.key("modified").value(System.currentTimeMillis());
        else
            jsonObject.key("created").value(System.currentTimeMillis());
        jsonObject.endObject();
        String res = jsonObject.toString();
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(res.getBytes());
        outputStream.close();
   
    }
}
