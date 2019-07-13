package com.ugc.view;

import com.ugc.domain.UserUser;
import org.springframework.web.servlet.view.AbstractView;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;
import java.util.Map;

public class UserLoginView extends AbstractView
{
    protected void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {

        ViewDTO view = (ViewDTO) model.get(ViewConstants.VIEW_DTO);
        UserUser user = (UserUser) view.getTransferObject();
        JSONObject jObject =  null;
        if (user.getJson()!= null && !user.getJson().equals(""))
        {
            jObject = new JSONObject(user.getJson());
        }else
        {
            String fakeData = "{\"data\":[]}";
            jObject = new JSONObject(fakeData);
        }
        
        jObject.put("userLoginTime", user.getLoginTime());
        jObject.put("userCreationTime", user.getCreationTime());
        String res = jObject.toString();
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(res.getBytes());
        outputStream.close();

    }
}
