package com.ugc.view;

import com.ugc.domain.AndroidPushMessage;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;
import java.util.LinkedList;
import java.util.Map;

public class AndroidPushMessagesView extends AbstractView
{
    protected void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {

         ViewDTO view = (ViewDTO) model.get(ViewConstants.VIEW_DTO);
         LinkedList<AndroidPushMessage> messages = (LinkedList<AndroidPushMessage>)view.getCollections();
         JSONObject ms = new JSONObject();
         JSONArray m = new JSONArray();

        if (messages != null) {
            for (AndroidPushMessage message : messages) {
                JSONObject one = new JSONObject();
                one.put("message", message.getMessage());
                one.put("hash", message.getMessageHash());
                one.put("time", message.getMessageCreationTime());
                m.put(one);
            }
        }

         ms.put("messages", m);

         String json = ms.toString();
         ServletOutputStream outputStream = response.getOutputStream();
         outputStream.write(json.getBytes());
         outputStream.close();


    }
}
