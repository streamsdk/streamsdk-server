package com.ugc.view;

import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class ObjectJsonView extends AbstractView
{
    @Override
    protected void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {

        ViewDTO view = (ViewDTO)model.get(ViewConstants.VIEW_DTO);
        String json = (String) view.getTransferObject();
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(json.getBytes("UTF-8"));
        outputStream.close();

    }
}
