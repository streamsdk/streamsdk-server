package com.ugc.facade;

import com.ugc.domain.Request;

import java.util.Map;

public interface StreamRequestsFacade
{
    public void countRequest(String appId);

    public void countPushRequest(String appId);

    public Map<String, Request> getRequests();

    public void removeRequest(String appId);
    
}
