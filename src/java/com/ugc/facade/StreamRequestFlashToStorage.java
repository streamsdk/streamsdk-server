package com.ugc.facade;

import com.ugc.domain.Application;
import org.json.JSONException;

import java.util.Date;

public interface StreamRequestFlashToStorage
{
    public void sendToStorage();

    public Date getCurrentDate();

    public Application getFromStorage(String appId, String monthAndYear) throws JSONException;
}
