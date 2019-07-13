package com.ugc.facade;

import org.json.JSONException;
import javapns.communication.exceptions.CommunicationException;
import javapns.communication.exceptions.KeystoreException;

public interface PushNotificationFacade
{

    public String addToken(String clientKey, String token, String deviceType) throws JSONException;

    public String subscribeChannel(String token, String clientKey, String channel) throws JSONException, TokenNotFoundException;

    public String unsubscribeChannel(String token, String clientKey, String channel) throws JSONException;

    public String saveChannel(String clientKey, String channel) throws JSONException;

    public String getsubscribedChannels(String clientKey, String token) throws JSONException;

    public String getAllChannels(String clientKey);

    public String getTokensForChannel(String clientKey, String channel) throws JSONException;

    void sendMessages(String tokenJson, String message, String password, String clientKey, String soundFile) throws JSONException, CertificateNotExistException, CommunicationException, KeystoreException;

    void sendMessage(String token, String clientKey, String message, String certPassword, String soundFile) throws CommunicationException, KeystoreException, CertificateNotExistException;

    void sendAndroidMessage(String token, String clientKey, String message);

    void createBroadcastChannel(String clientKey);
}
