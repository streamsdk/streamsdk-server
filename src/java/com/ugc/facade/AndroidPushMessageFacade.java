package com.ugc.facade;

import com.ugc.domain.AndroidPushMessage;

import java.util.LinkedList;

public interface AndroidPushMessageFacade
{

    public void pushMessage(String clientKey, String token, String message);

    public void consumeMessage(String clientKey, String token, String hash, long messageCreationTime);

    public LinkedList<AndroidPushMessage> getMessages(String clientKey, String token);

}
