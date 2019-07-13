package com.ugc.view;

import org.springframework.web.servlet.view.AbstractView;
import org.json.JSONStringer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;
import java.util.Map;


public class DefaultView extends AbstractView
{
    protected void renderMergedOutputModel(Map map, HttpServletRequest req,
                                           HttpServletResponse res) throws Exception {

        ViewDTO view = (ViewDTO) map.get(ViewConstants.VIEW_DTO);
        String response = (String) view.getTransferObject();
        JSONStringer jsonObject = new JSONStringer();
        if (!response.equals("OK"))
            jsonObject.object().key("message").value(response);
        else
            jsonObject.object().key("succeed").value(response);
        jsonObject.endObject();
        String jres = jsonObject.toString();
        ServletOutputStream outputStream = res.getOutputStream();
        outputStream.write(jres.getBytes());
        outputStream.close();

    }
}

