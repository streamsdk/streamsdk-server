package com.ugc.view;

import com.ugc.domain.UserDTO;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.text.SimpleDateFormat;

public class UserListView extends AbstractView
{
    protected void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {

        ViewDTO view = (ViewDTO) model.get(ViewConstants.VIEW_DTO);
        List<UserDTO> users = (List<UserDTO>) view.getCollections();
        JSONObject jo = new JSONObject();
        JSONArray ja = new JSONArray();
        for (UserDTO user : users) {
            JSONObject u = new JSONObject();
            u.put("id", user.getUserName());
            long signUpTime = Long.parseLong(user.getCreationTime());

            u.put("signuptime", dateConvert(signUpTime));
            u.put("logintime", user.getLoginTime() == null ? "0" : dateConvert(Long.parseLong(user.getLoginTime())));

            u.put("signuptimemillionsecs", signUpTime);
            u.put("logintimemillionsecs", user.getLoginTime() == null ? 0 : user.getLoginTime());

            JSONObject metaData = null;
            if (user.getMetaDataJson() != null && !user.getMetaDataJson().equals(""))
            {
               try{
                metaData = new JSONObject(user.getMetaDataJson());
               }catch(Throwable t){
                   String fakeData = "{\"data\":[]}";
                   metaData = new JSONObject(fakeData);
               }
            }else{
                String fakeData = "{\"data\":[]}";
                metaData = new JSONObject(fakeData);
            } 
            u.put("metadata", metaData);
            ja.put(u);
        }
        jo.put("users", ja);

        String output = jo.toString();
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(output.getBytes());
        outputStream.close();


    }

    private String dateConvert(long millionSeconds) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM ,yyyy HH:mm");
        Date resultdate = new Date(millionSeconds);
        String str = sdf.format(resultdate);
        return str;
    }
}
