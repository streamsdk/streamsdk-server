package com.ugc.facade;

import com.ugc.controller.SystemConstants;
import com.ugc.domain.Application;
import com.ugc.domain.Statistics;
import com.ugc.domain.SystemUser;
import com.ugc.domain.UserDTO;
import com.ugc.utils.ConversionUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service(value = "systemUserDomainFacade")
public class SystemUserDomainFacadeImp implements SystemUserDomainFacade
{

    private Map<String, SystemUser> users = new ConcurrentHashMap<String, SystemUser>();
    private StorageDomainFacade storageDomainFacade;
    private StreamRequestFlashToStorage streamRequestFlashToStorage;
    private PushNotificationFacade pushNotificationFacade;

    @Autowired
    public SystemUserDomainFacadeImp(StorageDomainFacade storageDomainFacade, StreamRequestFlashToStorage streamRequestFlashToStorage,
                                     PushNotificationFacade pushNotificationFacade) {
        this.storageDomainFacade = storageDomainFacade;
        this.streamRequestFlashToStorage = streamRequestFlashToStorage;
        this.pushNotificationFacade = pushNotificationFacade;
    }

    public String createApplication(String clientKey) {
        String appId = generateApplicationId();
        //String appId = "3FF513E1FC2888988C20F78A528F0F41";
        String fullKey = clientKey + appId;
        JSONObject application = new JSONObject();

        try {
            Date now = new Date();
            JSONArray array = new JSONArray();
            JSONObject requests = new JSONObject();
            requests.put("requests", 0L);
            requests.put("date", now.getTime());
            array.put(requests);
            application.put("application", array);
            application.put("total", 0L);
            application.put("period", "");
            application.put("storage", 0L);

        } catch (JSONException e) {

        }
        String json = application.toString();
        storageDomainFacade.saveObject(json, SystemConstants.APPLICATION_BUKET, fullKey);
        pushNotificationFacade.createBroadcastChannel(appId);
        return appId;
    }


    public SystemUser createUser(String userName, String password) {

        SystemUser user = new SystemUser();
        String clientKey = generateClientKey();
        String secretKey = generateSecretKey();
        user.setClientKey(clientKey);
        user.setSecretKey(secretKey);
        users.put(clientKey, user);
        //'dummy data' will be the json of the system user metadata, this is used for mobile access
        storageDomainFacade.persistUser(clientKey, "dummy data", secretKey, -1, -1, false, SystemConstants.SYSTEM_USER_BUKET);
        //this system user is used for web access
        createSystemWebUser(userName, password, clientKey, secretKey);
        String appId = createApplication(clientKey);
        user.setCurrentAppId(appId);
        return user;
    }

    private void createSystemWebUser(String userName, String password, String clientKey, String secretKey) {

        JSONObject json = new JSONObject();
        try {
            json.put("clientKey", clientKey);
            json.put("secretKey", secretKey);
            storageDomainFacade.persistUser(userName, json.toString(), password, -1, -1, false, SystemConstants.SYSTEM_WEBUSER_BUKET);

        } catch (JSONException e) {

        }
    }

    private String generateClientKey() {
        String uid = UUID.randomUUID().toString().replace("-", "");
        return getHashValue(uid);
    }


    private String generateSecretKey() {
        String uid = UUID.randomUUID().toString().replace("-", "");
        return getHashValue(uid);
    }

    private String generateApplicationId() {
        String uid = UUID.randomUUID().toString().replace("-", "");
        return getHashValue(uid);
    }


    private String getHashValue(String uid) {

        try {
            return ConversionUtils.createMD5Hash(uid, true);
        } catch (NoSuchAlgorithmException e) {

        }
        return "";

    }

    public boolean isUserExists(String userName) {
        return users.containsKey(userName);
    }

    public boolean authenticate(String clientKey, String secretKey) {
        return storageDomainFacade.authenticateSystemUser(clientKey, secretKey, SystemConstants.SYSTEM_USER_BUKET);
    }

    public UserDTO authenticateFromWeb(String userName, String password) {
        if (storageDomainFacade.authenticateSystemUser(userName, password, SystemConstants.SYSTEM_WEBUSER_BUKET)) {
            return storageDomainFacade.getClientKey(userName);
        }
        return null;
    }

    public Statistics getStatistics(String appId) {

        Date currentDate = streamRequestFlashToStorage.getCurrentDate();
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        String date = "01/" + String.valueOf(month + 1) + "/" + String.valueOf(year) + " (dd/mm/year)";
        Statistics sta = new Statistics();
        sta.setCurrentDate(date);
        try {
            Application app = streamRequestFlashToStorage.getFromStorage(appId, String.valueOf(year) + String.valueOf(month));
            sta.setTotalRequests(app.getTotal());
            sta.setTotalPush(app.getTotalPush());
            sta.setTotalStorage(app.getStorage());
        } catch (JSONException e) {

        }
        return sta;

    }

    public String getStatisticsDate() {
        Date currentDate = streamRequestFlashToStorage.getCurrentDate();
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        String date = "01/" + String.valueOf(month + 1) + "/" + String.valueOf(year) + " (dd/mm/year)";
        return date;
    }

}
