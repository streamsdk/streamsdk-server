package com.ugc.facade;

import com.ugc.helper.FileCreationHelper;
import com.ugc.utils.ImageHash;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.FileInputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service(value = "ImageHashSearchFacade")
public class ImageHashSearchFacadeImp implements ImageHashSearchFacade
{

    private ExecutorService eService;
    private PushNotificationFacade pushNotificationFacade;
    private FileCreationHelper fileCreationHelper;
    private String jsonHash;
    public static final String STREAM_TOKEN = "reserved.streamsdktoken";
     private static Log log = LogFactory.getLog(ImageHashSearchFacadeImp.class);

    @Autowired
    public ImageHashSearchFacadeImp(PushNotificationFacade pushNotificationFacade, FileCreationHelper fileCreationHelper){
        this.pushNotificationFacade = pushNotificationFacade;
        this.fileCreationHelper = fileCreationHelper;
    }

    @PostConstruct
    public void initIt() throws Exception {
       eService = Executors.newFixedThreadPool(3);
       jsonHash = FileUtils.readFileToString(new File(fileCreationHelper.getJsonHashFile()));

    }

    @PreDestroy
    public void cleanUp() throws Exception {
       eService.shutdown();
    }

    public void searchImageAndSendPush(String json, final String clientKey, final File file) {

        String token = null;
        try {
            JSONObject metaData = new JSONObject(json);
            JSONArray ja = metaData.getJSONArray("data");
            int l = ja.length();
            for (int x = 0; x < l; x++) {
               JSONObject jObject = ja.getJSONObject(x);
               String key = jObject.getString("key");
               if (key.equals(STREAM_TOKEN))
                   token = jObject.getString("value");
            }

        } catch (JSONException e) {
            log.error(e.getMessage());
        }

        if (token == null)
            return;

        final String tempToken = token;
        eService.submit(new Runnable()
        {

            public void run() {

                try {
                    JSONObject hashObject = new JSONObject(jsonHash);
                    JSONArray ja = hashObject.getJSONArray("data");
                    ImageHash hash = new ImageHash();
                    String ih = hash.getHash(new FileInputStream(file));
                    int size = ja.length();
                    for (int i = 0; i < size; i++) {
                        JSONObject jo = ja.getJSONObject(i);
                        String h = jo.getString("hash");
                        String url = jo.getString("url");
                        int d = hash.distance(ih, h);
                        if (d < 10){
                           pushNotificationFacade.sendAndroidMessage(tempToken, clientKey, url);
                           break;
                        }
                    }

                } catch (Exception e) {
                    log.error(e.getMessage());
                }
             }
          }
        );
     }

    public boolean isTestMode() {
        return false;
    }
}
