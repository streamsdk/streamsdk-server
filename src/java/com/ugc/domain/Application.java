package com.ugc.domain;

import java.util.ArrayList;
import java.util.List;

public class Application
{
   private long total = 0L;
   private long storage = 0L;
   private List<Request> requests = new ArrayList<Request>();
   private long totalPush = 0L;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getStorage() {
        return storage;
    }

    public void setStorage(long storage) {
        this.storage = storage;
    }

    public List<Request> getRequests() {
        return requests;
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }

    public void addRequest(Request request){
        requests.add(request);
    }

    public void setPushTotal(long totalPush) {
        this.totalPush = totalPush;
    }

    public long getTotalPush(){
        return totalPush;
    }
}
