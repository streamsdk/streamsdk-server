package com.ugc.view;

import com.ugc.domain.SystemUser;
import org.json.JSONObject;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class SystemUserAuthView extends AbstractView
{
    protected void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {

        ViewDTO view = (ViewDTO) model.get(ViewConstants.VIEW_DTO);
        SystemUser user = (SystemUser) view.getTransferObject();
        JSONObject jObject = new JSONObject();
        jObject.put("userLoginTime", user.getLoginTime());
        String res = jObject.toString();
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(res.getBytes());
        outputStream.close();

    }
}
