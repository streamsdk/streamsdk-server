package com.ugc.view;

import com.ugc.domain.UserDTO;
import org.json.JSONObject;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class SystemUserLoginView extends AbstractView
{
    protected void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {

        ViewDTO view = (ViewDTO) model.get(ViewConstants.VIEW_DTO);
        UserDTO user = (UserDTO) view.getTransferObject();
        String clientKey = user.getClientKey();
        String secretKey = user.getSecretKey();
        String id = user.getAppId().replace(clientKey, "");
        JSONObject jObject = new JSONObject();
        jObject.put("clientKey", clientKey);
        jObject.put("secretKey", secretKey);
        jObject.put("appId", id);
        request.getSession(true).setAttribute("clientKey", clientKey);
        request.getSession(true).setAttribute("secretKey", secretKey);
        request.getSession(true).setAttribute("appId", id);
        String res = jObject.toString();
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(res.getBytes());
        outputStream.close();

    }
}
