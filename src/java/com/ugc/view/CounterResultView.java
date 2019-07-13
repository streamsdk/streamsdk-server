package com.ugc.view;

import org.springframework.web.servlet.view.AbstractView;
import org.json.JSONStringer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;
import java.util.Map;


public class CounterResultView extends AbstractView
{
    protected void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {

        ViewDTO view = (ViewDTO)model.get(ViewConstants.VIEW_DTO);
        JSONStringer jsonObject = new JSONStringer();
        String counter = (String)view.getTransferObject();
        jsonObject.object().key("counter").value(counter);
        jsonObject.endObject();
        String jres = jsonObject.toString();
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(jres.getBytes());
        outputStream.close();

    }
}
