package com.ugc.view;

import com.ugc.domain.SystemUser;
import org.springframework.web.servlet.view.AbstractView;
import org.json.JSONStringer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;
import java.util.Map;

public class CreateSystemUserView extends AbstractView
{
    protected void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {

        ViewDTO view = (ViewDTO) model.get(ViewConstants.VIEW_DTO);
        SystemUser user = (SystemUser) view.getTransferObject();
        JSONStringer jsonObject = new JSONStringer();
        jsonObject.object().key("clientKey").value(user.getClientKey());
        jsonObject.key("secretKey").value(user.getSecretKey());
        jsonObject.key("appId").value(user.getAppId());
        jsonObject.endObject();
        request.getSession(true).setAttribute("clientKey", user.getClientKey());
        request.getSession(true).setAttribute("secretKey", user.getSecretKey());
        request.getSession(true).setAttribute("appId", user.getAppId());
        String res = jsonObject.toString();
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(res.getBytes());
        outputStream.close();


    }
}
