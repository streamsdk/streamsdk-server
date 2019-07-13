package com.ugc.facade;

import com.ugc.controller.SystemConstants;
import com.ugc.domain.Application;
import com.ugc.domain.ObjectListDTO;
import com.ugc.domain.Request;
import com.ugc.helper.FileCreationHelper;
import com.ugc.utils.JsonUtils;
import com.ugc.utils.StreamUtils;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service(value = "StreamRequestFlashToStorage")
public class StreamRequestFlashToStorageImp implements StreamRequestFlashToStorage, Runnable
{

    private StreamRequestsFacade streamRequestsFacade;
    private StorageDomainFacade storageDomainFacade;
    private ScheduledExecutorService scheduler;
    private FileCreationHelper fch;
    private static Log log = LogFactory.getLog(StreamRequestFlashToStorageImp.class);

    @Autowired
    public StreamRequestFlashToStorageImp(StreamRequestsFacade streamRequestsFacade, StorageDomainFacade storageDomainFacade,  FileCreationHelper fch){
       this.streamRequestsFacade = streamRequestsFacade;
       this.storageDomainFacade = storageDomainFacade;
       this.fch = fch;
    }

    @PostConstruct
    public void initIt() throws Exception {
        scheduler =  Executors.newScheduledThreadPool(1);
        String time = fch.getStreamRequestSchedule();
        scheduler.scheduleAtFixedRate(this, 10, Long.parseLong(time), TimeUnit.SECONDS);
    }

    @PreDestroy
    public void cleanUp() throws Exception {
        scheduler.shutdown();
    }

    public void sendToStorage() {

      try{
        Date currentDate = getCurrentDate();
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        Map<String, Request> requests = streamRequestsFacade.getRequests();
        Set<Map.Entry<String,Request>> entries = requests.entrySet();
        for(Map.Entry<String, Request> entry : entries) {
            String applicationId = entry.getKey();
            Application appData = getFromStorage(applicationId, String.valueOf(year) + String.valueOf(month));
            Request request = entry.getValue();
            request.setDate(System.currentTimeMillis());
            appData.addRequest(request);
            appData.setTotal(appData.getTotal() + entry.getValue().getCurrentCounts());
            appData.setPushTotal(appData.getTotalPush() + entry.getValue().getPush());
            appData.setStorage(getFileStorage(applicationId));
            String jsonDate = JsonUtils.convertApplicationToString(appData);
            if (log.isInfoEnabled())
               log.info("saving " + applicationId + " to storage");
            storageDomainFacade.saveObject(jsonDate, SystemConstants.STATISTICS, applicationId + String.valueOf(year) + String.valueOf(month));
            streamRequestsFacade.removeRequest(applicationId);
        }
      }catch(Exception e){}
    }
    
    private long getFileStorage(String appId){

        List<ObjectListDTO> dtos = storageDomainFacade.getObjectList(SystemConstants.FILE_BUKET, "", appId);
        long total = 0;
        for (ObjectListDTO dto : dtos) {
            total += dto.getSize();
        }
        return total;
    }

   public Date getCurrentDate() {

        File file = new File(fch.getCalendarFolder());
        try {
            FileInputStream in = new FileInputStream(file);
            String dateStr = StreamUtils.readString(in);
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy HH:mm");
            Date date = sdf.parse(dateStr);
            return date;
        } catch (FileNotFoundException e) {

        } catch (ParseException e) {

        }
       return new Date();
    }

    public Application getFromStorage(String appId, String monthAndYear) throws JSONException {

        String finalKey = appId + monthAndYear;
        if (storageDomainFacade.isKeyExists(finalKey, SystemConstants.STATISTICS)){
            String jsonDate = storageDomainFacade.get(finalKey, "", "", SystemConstants.STATISTICS);
            Application requests = JsonUtils.convertAppDataToApplication(jsonDate);
            return requests;
        }else{
            return new Application();
        }

    }


    public void run() {
        sendToStorage();
    }
}
