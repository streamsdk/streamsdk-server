package com.ugc.utils;

import com.ugc.domain.Application;
import com.ugc.domain.QueryObject;
import com.ugc.domain.QueryObjectList;
import com.ugc.domain.Request;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.*;


public class JsonUtils
{

    public static String[] convertJsonArrayToString(JSONArray array) throws JSONException {
        int size = array.length();
        String str[] = new String[size];
        for (int i = 0; i < size; i++) {
            String s = array.getString(i);
            str[i] = s;
        }
        return str;
    }

    public static String convertMatchingMapToString(Map<String, JSONArray> matchingMap, String category) throws JSONException {

        JSONObject groupJsonObject = new JSONObject();
        JSONArray jArray = new JSONArray();
        Set<Map.Entry<String, JSONArray>> entries = matchingMap.entrySet();
        for (Map.Entry<String, JSONArray> entry : entries) {
            String id = entry.getKey();
            JSONArray ja = entry.getValue();
            JSONObject newJsonObject = new JSONObject();
            newJsonObject.put("id", id);
            newJsonObject.put("data", ja);
            jArray.put(newJsonObject);
        }

        groupJsonObject.put("group", jArray);
        groupJsonObject.put("category", category);
        String json = groupJsonObject.toString();
        return json;

    }


    public static QueryObjectList convertToQueryObjectList(String json) throws JSONException {
        QueryObjectList qol = new QueryObjectList();
        JSONObject queries = new JSONObject(json);
        JSONArray operators = queries.getJSONArray("operators");
        int size = operators.length();
        for (int i = 0; i < size; i++) {
            JSONObject jObject = operators.getJSONObject(i);
            String key = jObject.getString("key");
            String operator = jObject.getString("operator");
            QueryObject qo;
            if (!operator.equals("contains")) {
                String value = jObject.getString("value");
                qo = new QueryObject(value, key, operator);
            } else {
                JSONArray array = jObject.getJSONArray("value");
                String str[] = JsonUtils.convertJsonArrayToString(array);
                qo = new QueryObject(str, key, operator);
            }
            qol.addQueryObject(key, qo);
        }

        if (qol.size() > 1) {
            String logic = queries.getString("logic");
            qol.setLogic(logic);
        }

        JSONArray limitedKeys = queries.getJSONArray("ids");
        int limitedKeysSize = limitedKeys.length();
        for (int i=0; i < limitedKeysSize; i++){
             String id = limitedKeys.getString(i);
             qol.addLimitedKeys(id);
        }

        return qol;
    }

    private static String dateConvert(long millionSeconds) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy HH:mm");
        long s  = System.currentTimeMillis();
        int sLength = String.valueOf(s).length();
        int mLength = String.valueOf(millionSeconds).length();
        if (sLength > mLength)
            millionSeconds = millionSeconds * 1000;

        Date resultdate = new Date(millionSeconds);
        String str = sdf.format(resultdate);
        return str;
    }

    public static String updateOrAddObject(String oldJson, String newJson) throws JSONException {

        Map<String, Long> modified = new HashMap<String, Long>();
        Map<String, JSONArray> oldKeys = convertJsonToMap(oldJson, modified);
        Map<String, JSONArray> newKeys = convertJsonToMap(newJson, modified);


        Map<String, JSONArray> keyToBeAdded = new HashMap<String, JSONArray>();
        Map<String, JSONArray> keyToBeUpdated = new HashMap<String, JSONArray>();

        Set<Map.Entry<String, JSONArray>> entries = newKeys.entrySet();
        for (Map.Entry<String, JSONArray> entry : entries) {
            String key = entry.getKey();
            if (oldKeys.containsKey(key))
                keyToBeUpdated.put(key, entry.getValue());
            else
                keyToBeAdded.put(key, entry.getValue());
        }

        JSONObject jo = new JSONObject(oldJson);
        JSONArray data = jo.getJSONArray("group");
        Set<Map.Entry<String, JSONArray>> set = keyToBeAdded.entrySet();
        for (Map.Entry<String, JSONArray> stringJSONArrayEntry : set) {
            JSONObject jobject = new JSONObject();
            jobject.put("id", stringJSONArrayEntry.getKey());
            jobject.put("data", stringJSONArrayEntry.getValue());
            Long m = modified.get(stringJSONArrayEntry.getKey());
            if (m != null){
                jobject.put("modifiedinmillionseconds", m);
                jobject.put("modified", dateConvert(m));
            }
            data.put(jobject);
        }


        int size = data.length();
        for (int i = 0; i < size; i++) {
            JSONObject jobject = data.getJSONObject(i);
            String id = jobject.getString("id");
            if (keyToBeUpdated.containsKey(id)) {

                Map<String, JSONObject> keyValue = convertDataToJsonMap(jobject);
                Map<String, JSONObject> updatedKeyValue = convertArrayToJsonMap(keyToBeUpdated.get(id));
                Set<Map.Entry<String, JSONObject>> set1 = updatedKeyValue.entrySet();
                for (Map.Entry<String, JSONObject> entry : set1) {
                    String key = entry.getKey();
                    keyValue.put(key, entry.getValue());
                }
                Set<Map.Entry<String, JSONObject>> set2 = keyValue.entrySet();
                JSONArray newJsonArray = new JSONArray();
                for (Map.Entry<String, JSONObject> entry : set2) {
                    newJsonArray.put(entry.getValue());
                }

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", id);
                jsonObject.put("data", newJsonArray);
                Long m = modified.get(id);
                if (m != null){
                    jsonObject.put("modifiedinmillionseconds", m);
                    jsonObject.put("modified", dateConvert(m));
                }
                data.put(i, jsonObject);
            }
        }

        JSONObject jonewjson = new JSONObject(newJson);
        String category = jonewjson.getString("category");
        JSONObject newJsonObject = new JSONObject();
        newJsonObject.put("group", data);
        newJsonObject.put("category", category);
        return newJsonObject.toString();

    }

    public static String deleteStreamObjectKey(String key, String oldJson) throws JSONException {

        JSONObject dataOldJsonArray = new JSONObject(oldJson);
        JSONArray oldJsonArray = dataOldJsonArray.getJSONArray("data");
        Map<String, JSONObject> oldJsonObjects = convertArrayToJsonMap(oldJsonArray);
        oldJsonObjects.remove(key);
        JSONArray newJsonArray = new JSONArray();
        Set<Map.Entry<String, JSONObject>> newEntries = oldJsonObjects.entrySet();
        for (Map.Entry<String, JSONObject> newEntry : newEntries) {
            newJsonArray.put(newEntry.getValue());
        }
        JSONObject newObject = new JSONObject();
        newObject.put("data", newJsonArray);
        String json = newObject.toString();
        return json;

    }

    public static String updateStreamObject(String updatedJson, String oldJson) throws JSONException {

        JSONObject dataUpdatedJsonArray  = new JSONObject(updatedJson);
        JSONObject dataOldJsonArray  = new JSONObject(oldJson);
        JSONArray updatedJsonArray = dataUpdatedJsonArray.getJSONArray("data");
        JSONArray oldJsonArray = dataOldJsonArray.getJSONArray("data");
        Map<String, JSONObject> updatedObjects = convertArrayToJsonMap(updatedJsonArray);
        Map<String, JSONObject> oldJsonObjects = convertArrayToJsonMap(oldJsonArray);
        JSONArray newJsonArray = new JSONArray();
        Set<Map.Entry<String, JSONObject>> updatedEntries = updatedObjects.entrySet();
        for (Map.Entry<String, JSONObject> updatedEntry : updatedEntries){
            String key = updatedEntry.getKey();
            JSONObject object = updatedEntry.getValue();
            oldJsonObjects.put(key, object);
        }
        Set<Map.Entry<String, JSONObject>> newEntries = oldJsonObjects.entrySet();
        for (Map.Entry<String, JSONObject> newEntry : newEntries) {
            newJsonArray.put(newEntry.getValue());
        }
        JSONObject newObject = new JSONObject();
        newObject.put("data", newJsonArray);
        String json = newObject.toString();
        return json;

    }

   
    private static Map<String, JSONObject> convertDataToJsonMap(JSONObject jobject) throws JSONException {
        Map<String, JSONObject> keyValue = new HashMap<String, JSONObject>();
        JSONArray ja = jobject.getJSONArray("data");
        int l = ja.length();
        for (int x = 0; x < l; x++) {
            JSONObject jObject = ja.getJSONObject(x);
            String key = jObject.getString("key");
            keyValue.put(key, jObject);
        }
        return keyValue;
    }

    private static Map<String, JSONObject> convertArrayToJsonMap(JSONArray ja) throws JSONException {
        Map<String, JSONObject> keyValue = new HashMap<String, JSONObject>();
        int l = ja.length();
        for (int x = 0; x < l; x++) {
            JSONObject jObject = ja.getJSONObject(x);
            String key = jObject.getString("key");
            keyValue.put(key, jObject);
        }
        return keyValue;

    }

    public static String getjson(String json, String objectId) throws JSONException {

        JSONObject jo = new JSONObject(json);
        JSONArray data = jo.getJSONArray("group");
        int size = data.length();
        for (int i = 0; i < size; i++) {
            JSONObject jobject = data.getJSONObject(i);
            String id = jobject.getString("id");
            if (id.equals(objectId))
                return jobject.toString();
        }
        return null;

    }

    public static boolean isGroupTagExist(String json) {

        try {
            JSONObject jo = new JSONObject(json);
            JSONArray data = jo.getJSONArray("group");
            return true;
        } catch (JSONException e) {

        }
        return false;
    }

    private static Map<String, JSONArray> convertJsonToMap(String json, Map<String, Long> modified) throws JSONException {
        Map<String, JSONArray> jsonMap = new HashMap<String, JSONArray>();
        JSONObject jo = new JSONObject(json);
        JSONArray data = jo.getJSONArray("group");
        int size = data.length();
        for (int i = 0; i < size; i++) {
            JSONObject jobject = data.getJSONObject(i);
            JSONArray ja = jobject.getJSONArray("data");
            String id = jobject.getString("id");
            try {
                Long m = jobject.getLong("modified");
                modified.put(id, m);
            } catch (Throwable t) {
            }
            jsonMap.put(id, ja);
        }
        return jsonMap;
    }

    public static String deleteObjects(String oldJson, String jsonIdList) throws JSONException {
        Set<String> ids = convertJsonListToSet(jsonIdList);
        JSONObject jo = new JSONObject(oldJson);
        JSONArray data = jo.getJSONArray("group");
        int size = data.length();
        int index = 0;
        while (index < size) {
            JSONObject jobject = data.getJSONObject(index);
            String id = jobject.getString("id");
            if (ids.contains(id)) {
                data.remove(index);
                if (index != size - 1) {
                    size--;
                    continue;
                }
                if (index == size - 1) {
                    break;
                }
            }
            index++;
        }

        JSONObject newJsonObject = new JSONObject();
        newJsonObject.put("group", data);
        return newJsonObject.toString();
    }

    public static String deleteObjectsFromIdSet(String oldJson, Set<String> ids) throws JSONException {


        JSONObject jo = new JSONObject(oldJson);
        JSONArray data = jo.getJSONArray("group");
        int size = data.length();
        int index = 0;
        while (index < size) {
            JSONObject jobject = data.getJSONObject(index);
            String id = jobject.getString("id");
            if (ids.contains(id)) {
                data.remove(index);
                if (index != size - 1) {
                    size--;
                    continue;
                }
                if (index == size - 1) {
                    break;
                }
            }
            index++;
        }

        JSONObject newJsonObject = new JSONObject();
        newJsonObject.put("group", data);
        return newJsonObject.toString();

    }

    private static Set<String> convertJsonListToSet(String jsonIdList) throws JSONException {
        Set<String> ids = new HashSet<String>();
        JSONObject jobject = new JSONObject(jsonIdList);
        JSONArray data = jobject.getJSONArray("ids");
        int size = data.length();
        for (int i = 0; i < size; i++) {
            String id = data.getString(i);
            ids.add(id);
        }
        return ids;
    }

    public static String deleteField(String jsonString, String id, String fieldName) throws JSONException {

        JSONObject jo = new JSONObject(jsonString);
        JSONArray data = jo.getJSONArray("group");
        int size = data.length();
        boolean found = false;
        for (int i = 0; i < size; i++) {
            JSONObject jobject = data.getJSONObject(i);
            String jId = jobject.getString("id");
            if (jId.equals(id)) {
                JSONArray ja = jobject.getJSONArray("data");
                int length = ja.length();
                for (int j = 0; j < length; j++) {
                    JSONObject jObject = ja.getJSONObject(j);
                    String key = jObject.getString("key");
                    if (key.equals(fieldName)) {
                        ja.remove(j);
                        found = true;
                    }
                    if (found) {
                        break;
                    }
                }
                if (found) {
                    jobject.put("data", ja);
                    data.remove(i);
                    data.put(jobject);
                    jo.remove("group");
                    jo.put("group", data);
                    break;
                }
            }
        }
        String newJson = jo.toString();
        return newJson;
    }

    public static Application convertAppDataToApplication(String jsonDate) throws JSONException {
        JSONObject application = new JSONObject(jsonDate);
        long requestTotal = application.getLong("total");
        long totalPush = application.getLong("totalPush");
        long stotage = application.getLong("storage");
        JSONArray array = application.getJSONArray("application");
        List<Request> requests = new ArrayList<Request>();
        for (int i=0; i <array.length(); i++){
            JSONObject j = (JSONObject) array.get(i);
            long r = j.getLong("requests");
            long push = j.getLong("push");
            long time = j.getLong("date");
            Request request = new Request();
            request.setCurrentCounts(r);
            request.setDate(time);
            request.setPush(push);
            requests.add(request);
        }
        Application app = new Application();
        app.setRequests(requests);
        app.setTotal(requestTotal);
        app.setPushTotal(totalPush);
        app.setStorage(stotage);
        return app;
    }

    public static String convertApplicationToString(Application appData) throws JSONException {
        List<Request> requests = appData.getRequests();
        JSONArray array = new JSONArray();
        for (Request request : requests) {
             JSONObject r = new JSONObject();
             r.put("requests", request.getCurrentCounts());
             r.put("push", request.getPush());
             r.put("date", request.getDate());
             array.put(r);
        }
        JSONObject application = new JSONObject();
        application.put("application", array);
        application.put("total", appData.getTotal());
        application.put("totalPush", appData.getTotalPush());
        application.put("storage", appData.getStorage());
        String json = application.toString();
        return json;
    }
}
