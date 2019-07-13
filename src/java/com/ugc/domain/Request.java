package com.ugc.domain;

public class Request
{
    private long currentCounts = 0;
    private long currentPushCounts = 0;
    private long date = 0L;


    public void resetRquest(){
        currentCounts = 0;
        currentPushCounts = 0;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getCurrentCounts() {
        return currentCounts;
    }

    public void setCurrentCounts(long currentCounts) {
        this.currentCounts = currentCounts;
    }

    public void increment(){
        currentCounts++;
    }

    public void incrementPush(){
        currentPushCounts++;              
    }

    public void setPush(long push) {
        this.currentPushCounts = push;
    }

    public long getPush(){
        return currentPushCounts;
    }
}
