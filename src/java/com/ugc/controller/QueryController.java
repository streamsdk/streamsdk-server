package com.ugc.controller;

import com.ugc.facade.QueryDomainFacade;
import com.ugc.facade.StorageDomainFacade;
import com.ugc.facade.StreamObjectCacheFacade;
import com.ugc.facade.StreamRequestsFacade;
import com.ugc.utils.JsonUtils;
import com.ugc.view.ViewConstants;
import com.ugc.view.ViewDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class QueryController
{

    private StorageDomainFacade storageDomainFacade;
    private StreamObjectCacheFacade streamObjectCache;
    private QueryDomainFacade queryDomainFacade;
    private StreamRequestsFacade streamRequestsFacade;
    private static Log log = LogFactory.getLog(QueryController.class);

    @Autowired
    public QueryController(StorageDomainFacade storageDomainFacade,
                           QueryDomainFacade queryDomainFacade, StreamObjectCacheFacade streamObjectCache, StreamRequestsFacade streamRequestsFacade) {
        this.storageDomainFacade = storageDomainFacade;
        this.streamObjectCache = streamObjectCache;
        this.queryDomainFacade = queryDomainFacade;
        this.streamRequestsFacade = streamRequestsFacade;
    }

    @RequestMapping(value = "/query/(*:clientKey)/(*:userName)", method = RequestMethod.GET)
    public String getObjects(@RequestParam("userName")String userName,
                             @RequestParam("clientKey")String clientKey,
                             @RequestParam("query")String query,
                             @RequestParam("category")String categoryName,
                             @RequestParam("all")String isAll,
                             ModelMap map) {

        streamRequestsFacade.countRequest(clientKey);
        String finalKey = clientKey + userName + categoryName;
        String json = streamObjectCache.getObject(finalKey);
        if (json == null) {
            if (!storageDomainFacade.isKeyExists(finalKey, SystemConstants.CATEGORY_BUUKET)) {
                ViewDTO view = new ViewDTO();
                view.setTransferObject(categoryName + " does not exist, please create it first");
                map.addAttribute(view);
                return ViewConstants.DEFAULT;
            }
            json = getFromStorage(clientKey, userName, categoryName, SystemConstants.CATEGORY_BUUKET);
        }

        Map<String, JSONArray> results = null;
        String js = "";
        try {
            if (isAll != null && isAll.equals("false")){
                results = queryDomainFacade.findMatchingObjects(json, query);
                JSONObject jo = new JSONObject(json);
                try {
                    String category = jo.getString("category");
                } catch (JSONException e) {
                    //this can happen sometimes
                    jo.put("category", categoryName);
                    String newJson = jo.toString();
                    streamObjectCache.updateCategoryCache(finalKey, newJson);
                }
                js = JsonUtils.convertMatchingMapToString(results, categoryName);
                if (log.isInfoEnabled()) {
                    log.info("receive query " + query + " for app id " + clientKey);
                    log.info("query results: " + js + " for app id " + clientKey);
                }
            }else{
                js = json;
            }

        } catch (JSONException e) {
            ViewDTO view = new ViewDTO();
            view.setTransferObject("data corrupted");
            map.addAttribute(view);
            return ViewConstants.DEFAULT;


        } catch (NumberFormatException e) {
            ViewDTO view = new ViewDTO();
            view.setTransferObject("the compared field is not a valid interger/long");
            map.addAttribute(view);
            return ViewConstants.DEFAULT;
        }

        if (!js.equals("")) {
            ViewDTO view = new ViewDTO();
            view.setTransferObject(js);
            map.addAttribute(view);
            return ViewConstants.OBJECT_JSON_VIEW;
        } else {
            ViewDTO view = new ViewDTO();
            view.setTransferObject("unexpected error occured");
            map.addAttribute(view);
            return ViewConstants.DEFAULT;
        }

    }

    public String getFromStorage(String clientKey, String userName, String category, String buketName) {
        return storageDomainFacade.retriveAsCategory(clientKey, userName, category, buketName);
    }


}
