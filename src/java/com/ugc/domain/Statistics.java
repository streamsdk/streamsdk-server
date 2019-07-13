package com.ugc.domain;


public class Statistics
{
    private String currentDate = "";
    private long totalRequests = 0L;
    private long totalStorage = 0L;
    private long totalPush = 0L;

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentMonth) {
        this.currentDate = currentMonth;
    }

    public long getTotalRequests() {
        return totalRequests;
    }

    public void setTotalRequests(long totalRequests) {
        this.totalRequests = totalRequests;
    }

    public long getTotalStorage() {
        return totalStorage;
    }

    public void setTotalStorage(long totalStorage) {
        this.totalStorage = totalStorage;
    }

    public void setTotalPush(long totalPush) {
        this.totalPush = totalPush;
    }

    public long getTotalPush() {
        return totalPush;
    }
}
