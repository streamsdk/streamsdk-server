package com.ugc.facade;

import com.ugc.domain.Request;
import org.springframework.stereotype.Service;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service(value = "StreamRequestsFacade")
public class StreamRequestsFacadeImp implements StreamRequestsFacade
{
    Map<String, Request> requests = new ConcurrentHashMap<String, Request>();
    private static Log log = LogFactory.getLog(StreamRequestsFacadeImp.class);

    public void countRequest(String appId) {
         if (log.isDebugEnabled())
             log.debug("add request id: " + appId);
         if (requests.containsKey(appId))
              requests.get(appId).increment();
          else{
              requests.put(appId, new Request());
          }
    }

    public void countPushRequest(String appId) {
         if (log.isDebugEnabled())
             log.debug("add push request id: " + appId);
         if (requests.containsKey(appId))
              requests.get(appId).incrementPush();
         else{
              requests.put(appId, new Request());
         }
    }


    public Map<String, Request> getRequests() {
        return requests;
    }

    public void removeRequest(String appId) {
        requests.remove(appId);
    }
}
