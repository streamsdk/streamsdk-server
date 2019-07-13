package com.ugc.facade;

import com.ugc.controller.SystemConstants;
import com.ugc.domain.UserUser;
import com.ugc.domain.UserDTO;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "UserUserDomainFacade")
public class UserUserDomainFacadeImp implements UserUserDomainFacade
{

    private StorageDomainFacade storageDomainFacade;

    @Autowired
    public UserUserDomainFacadeImp(StorageDomainFacade storageDomainFacade) {
        this.storageDomainFacade = storageDomainFacade;
    }

    public List<UserDTO> getAllUsers(String clientKey){
        return storageDomainFacade.getUserList(SystemConstants.USER_USER_BUKET, clientKey);
    }


    public UserUser createUser(String userName, String json, String password) {
        UserUser user = new UserUser(userName);
        long creationTime = System.currentTimeMillis();
        user.setCreationTime(creationTime);
        user.setPassword(password);
        user.setMetadataJson(json);
        persistUser(userName, json, password, creationTime, -1, false);
        return user;
    }

    public void updateUserLoginTime(UserUser user, String password, String objectKey){
        try {
            long loginTime = user.getLoginTime();
            JSONArray loginArray = null;
            String json = user.getJson();
           /* JSONObject jo = new JSONObject(json);
            JSONArray arrayData = jo.getJSONArray("data");
            int size = arrayData.length();
            for (int i = 0; i < size; i++) {
                JSONObject jObject = arrayData.getJSONObject(i);
                String key = jObject.getString("key");
                if (key.equals("loginHistory")) {
                    JSONArray ja = jObject.getJSONArray("value");
                    loginArray = ja;
                    arrayData.remove(i);
                    break;
                }
            }

            if (loginArray != null) {
                loginArray.put(loginTime);
            } else {
                loginArray = new JSONArray();
                loginArray.put(loginTime);
            }

            JSONObject loginHistory = new JSONObject();
            loginHistory.put("key", "loginHistory");
            loginHistory.put("value", loginArray);
            arrayData.put(loginHistory);

            JSONObject newObject = new JSONObject();
            newObject.put("data", arrayData);

            String newJsonMetadata = newObject.toString();*/
            persistUser(objectKey, json, password, -1, loginTime, true);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public boolean isUserExists(String finalKey) {
        if (keyExists(finalKey))
            return true;
        return false;
    }

    public UserUser findByUserName(String userName) {
        UserUser user = getUserFromS3(userName);
        return user;
    }

    public void updateUser(String key, String json) {
        persistUser(key, json, "", -1, -1, true);
    }

    private UserUser getUserFromS3(String userName) {
        return storageDomainFacade.getUserObject(SystemConstants.USER_USER_BUKET, userName);
    }

    private boolean keyExists(String key) {
        return storageDomainFacade.isKeyExists(key, SystemConstants.USER_USER_BUKET);
    }

    private void persistUser(String key, String json, String password, long creationTime, long loginTime, boolean userExists) {
        storageDomainFacade.persistUser(key, json, password, creationTime, loginTime, userExists, SystemConstants.USER_USER_BUKET);
        //some client or server save the user name as lower case
        storageDomainFacade.persistUser(key.toLowerCase(), json, password, creationTime, loginTime, userExists, SystemConstants.XMPP_USER_BUKET);

   }

    private String addCreationTimeToJson(String json, long creationTime) {
        try {
            JSONObject jObject = new JSONObject(json);
            jObject.put("userCreationTime", creationTime);
            return jObject.toString();

        } catch (JSONException e) {
        }
        return json;
    }

}
