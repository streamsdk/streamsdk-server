package com.ugc.facade;

import com.ugc.controller.SystemConstants;
import com.ugc.domain.FlushObject;
import com.ugc.helper.FileCreationHelper;
import com.ugc.utils.FlushObjectRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Service(value = "StreamCacheFlashFacade")
public class StreamCacheFlashFacadeImp implements StreamCacheFlashFacade, Runnable
{

    private StorageDomainFacade storageDomainFacade;
    private StreamObjectCacheFacade streamObjectCache;
    private FlushQueueFacade flushQueueFacade;
    private ExecutorService eService;
    private ScheduledExecutorService scheduler;
    private FileCreationHelper fileCreationHelper;
    private static Log log = LogFactory.getLog(StreamCacheFlashFacadeImp.class);


    @Autowired
    public StreamCacheFlashFacadeImp(StorageDomainFacade storageDomainFacade, StreamObjectCacheFacade streamObjectCache,
                  FileCreationHelper fileCreationHelper, FlushQueueFacade flushQueueFacade) {
        this.storageDomainFacade = storageDomainFacade;
        this.streamObjectCache = streamObjectCache;
        this.flushQueueFacade = flushQueueFacade;
        this.fileCreationHelper = fileCreationHelper;
    }

    @PostConstruct
    public void initIt() throws Exception {
        eService = Executors.newFixedThreadPool(10);
        scheduler =  Executors.newScheduledThreadPool(1);
        String time = fileCreationHelper.getStreamObjectCacheSchedule();
        scheduler.scheduleAtFixedRate(this, 10, Long.parseLong(time), TimeUnit.SECONDS);
    }

    @PreDestroy
    public void cleanUp() throws Exception {
        eService.shutdown();
        scheduler.shutdown();
    }

    private void moveUpdatedObjectsToFlushQueue() {
        for (int i = 0; i < 50000; i++) {
            String key;
            try {
                key = streamObjectCache.getKey(i);
            } catch (Exception e) {
                key = null;
            }
            if (key != null) {
                String categoryJson = streamObjectCache.getUpdatedCategoryObjectJson(key);
                String objectJson = streamObjectCache.getUpdatedObjectJson(key);
                if (categoryJson != null) {
                    FlushObject fo = new FlushObject(categoryJson, key, SystemConstants.CATEGORY_BUUKET);
                    flushQueueFacade.addObjectToFlushQueue(fo);
                    if (log.isInfoEnabled())
                        log.info("Adding updated category object json to flush queue");
          //          streamObjectCache.removeObjectFromCache(key);
                }
                if (objectJson != null) {
                    FlushObject fo = new FlushObject(objectJson, key, SystemConstants.OBJECT_BUKET);
                    flushQueueFacade.addObjectToFlushQueue(fo);
                    if (log.isInfoEnabled())
                        log.info("Adding updated object json to flush queue");
            //        streamObjectCache.removeObjectFromCache(key);
                }
            } else {
                break;
            }
        }
    }

    private void flushObjectsToStorage() {
        List<FlushObject> objects = flushQueueFacade.getObjects();
        for (FlushObject fo : objects) {
            Runnable runnable = new FlushObjectRunner(storageDomainFacade, fo, streamObjectCache);
            eService.submit(runnable);
        }
        flushQueueFacade.removeAll();
    }

    public void run() {

        moveUpdatedObjectsToFlushQueue();
        flushObjectsToStorage();
        removeCachedFiles();
    }

    private void removeCachedFiles(){
        List<File> filesToBeRemoved = fileCreationHelper.filesToBeRemoved();
        for (File file : filesToBeRemoved){
             if (log.isInfoEnabled())
                 log.info("removing file: " + file.getAbsolutePath());
             file.delete();
        }
    }
}
