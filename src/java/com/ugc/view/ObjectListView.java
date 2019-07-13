package com.ugc.view;

import com.ugc.domain.ObjectListDTO;
import com.ugc.controller.SystemConstants;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public class ObjectListView extends AbstractView
{
    protected void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {

        ViewDTO view = (ViewDTO) model.get(ViewConstants.VIEW_DTO);
        JSONStringer jsonObject = new JSONStringer();
        jsonObject.array();
        List<ObjectListDTO> objects = (List<ObjectListDTO>) view.getCollections();
        String prefix = (String) view.getTransferObject();
        for (ObjectListDTO s3Object : objects) {
            String fullKey = s3Object.getId();
            String id = fullKey.replace(prefix, "");
            if (!id.startsWith(SystemConstants.STREAMSDK)) {
                jsonObject.object().key("id").value(id);
                jsonObject.key("size").value(s3Object.getSize());
                jsonObject.key("modified").value(s3Object.getLastModified());
                jsonObject.key("modifiedInMillionsSecs").value(s3Object.getTimeInMillions());
                if (s3Object.getFileMetadataJson() != null && !s3Object.getFileMetadataJson().equals("")) {
                    JSONObject o = new JSONObject(s3Object.getFileMetadataJson());
                    jsonObject.key("metadata").value(o);
                }
                jsonObject.endObject();
            }
        }
        jsonObject.endArray();
        JSONArray array = new JSONArray(jsonObject.toString());
        JSONObject o = new JSONObject();
        o.put("data", array);
        String res = o.toString();
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(res.getBytes());
        outputStream.close();

    }
}
