package com.ugc.utils;

import com.ugc.domain.FlushObject;
import com.ugc.facade.StorageDomainFacade;
import com.ugc.facade.StreamObjectCacheFacade;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.security.NoSuchAlgorithmException;

public class FlushObjectRunner implements Runnable
{

    private StorageDomainFacade storageDomainFacade;
    private FlushObject fo;
    private StreamObjectCacheFacade streamObjectCache;
    private static Log log = LogFactory.getLog(FlushObjectRunner.class);

    public FlushObjectRunner(StorageDomainFacade storageDomainFacade, FlushObject fo, StreamObjectCacheFacade streamObjectCache) {
        this.storageDomainFacade = storageDomainFacade;
        this.fo = fo;
        this.streamObjectCache = streamObjectCache;
    }

    public void run() {
        log.info("sending key: " + fo.getKey() + " to storage");
        storageDomainFacade.saveObject(fo.getJson(), fo.getBuketName(), fo.getKey());
        threadSleep(60);
        String jsonInCache = streamObjectCache.getObject(fo.getKey());
        String hashedJsonInCache = getHashValue(jsonInCache);
        String hashedJsonInStorage = getHashValue(fo.getJson());
        if (hashedJsonInCache.equals(hashedJsonInStorage)){
            streamObjectCache.removeObjectFromCache(fo.getKey());
            log.info("hashed json in cache equals to hashed json in storage so removed it from cache");
        }else{
            log.info("hashed json in cache NOT equals to hashed json in storage so keep it in cache");
        }
   }

   private void threadSleep(long seconds){
       try {
           Thread.sleep(1000 * seconds);
       } catch (InterruptedException e) {

       }
   }

   private String getHashValue(String uid) {
    try {
            return ConversionUtils.createMD5Hash(uid, false);
        } catch (NoSuchAlgorithmException e) {

        }
        return "";
    }
}
