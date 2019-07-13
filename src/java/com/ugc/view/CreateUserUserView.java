package com.ugc.view;

import com.ugc.domain.UserUser;
import org.springframework.web.servlet.view.AbstractView;
import org.json.JSONStringer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;
import java.util.Map;


public class CreateUserUserView extends AbstractView
{
    protected void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {

        ViewDTO view = (ViewDTO) model.get(ViewConstants.VIEW_DTO);
        UserUser user = (UserUser) view.getTransferObject();
        JSONStringer jsonObject = new JSONStringer();
        jsonObject.object().key("creationTime").value(String.valueOf(user.getCreationTime()));
        jsonObject.key("userName").value(user.getUserName());
        jsonObject.endObject();
        String res = jsonObject.toString();
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(res.getBytes());
        outputStream.close();


    }
}
