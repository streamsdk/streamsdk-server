package com.ugc.facade;

import junit.framework.Assert;
import org.json.JSONException;
import org.junit.Test;
import javapns.communication.exceptions.CommunicationException;
import javapns.communication.exceptions.KeystoreException;
import com.ugc.domain.AndroidPushMessage;

import java.util.LinkedList;

public class PushNotificationFacadeImpTest
{

    @Test
    public void testAddNewToken() throws JSONException {

        String clientKey = "testClientKey";
        String token = "token";
        String newToken = "newToken";

        MockStorageDomainFacade msd = new MockStorageDomainFacade();
        AndroidPushMessageFacadeImp android = new AndroidPushMessageFacadeImp();
        PushNotificationFacadeImp pnf = new PushNotificationFacadeImp(msd, new MockFileHelper(), android, new StreamRequestsFacadeImp());

        String expectedResults = "{\"category\":\"pushChannels\",\"group\":[{\"id\":\"token\",\"data\":[{\"value\":[],\"type\":\"array\",\"key\":\"channels\"},{\"value\":\"android\",\"type\":\"string\",\"key\":\"deviceType\"}]}]}";
        String result = pnf.addToken(clientKey, token, "android");
        Assert.assertEquals(expectedResults, result);

        String expectedNewResults = "{\"category\":\"pushChannels\",\"group\":[{\"id\":\"newToken\",\"data\":[{\"value\":[],\"type\":\"array\",\"key\":\"channels\"},{\"value\":\"android\",\"type\":\"string\",\"key\":\"deviceType\"}]}]}";
        String newResult = pnf.addToken(clientKey, newToken, "android");
        Assert.assertEquals(expectedNewResults, newResult);

        String nextResult = pnf.addToken(clientKey, newToken, "android");
        String expectedNextResult = "{\"id\":\"newToken\",\"data\":[{\"value\":[],\"type\":\"array\",\"key\":\"channels\"},{\"value\":\"android\",\"type\":\"string\",\"key\":\"deviceType\"}]}";
        Assert.assertEquals(expectedNextResult, nextResult);

    }


    @Test
    public void testSubChannel() throws JSONException, TokenNotFoundException {

        String clientKey = "testClientKey";
        String token = "token";

        MockStorageDomainFacade msd = new MockStorageDomainFacade();
        AndroidPushMessageFacadeImp android = new AndroidPushMessageFacadeImp();
        PushNotificationFacadeImp pnf = new PushNotificationFacadeImp(msd, new MockFileHelper(), android, new StreamRequestsFacadeImp());

        String expectedResults = "{\"category\":\"pushChannels\",\"group\":[{\"id\":\"token\",\"data\":[{\"value\":[],\"type\":\"array\",\"key\":\"channels\"},{\"value\":\"android\",\"type\":\"string\",\"key\":\"deviceType\"}]}]}";
        String result = pnf.addToken(clientKey, token, "android");
        Assert.assertEquals(expectedResults, result);

        String expectedJson = "{\"category\":\"pushChannels\",\"group\":[{\"id\":\"token\",\"data\":[{\"value\":\"android\",\"type\":\"string\",\"key\":\"deviceType\"},{\"value\":[\"channela\"],\"type\":\"array\",\"key\":\"channels\"}]}]}";
        String newJson = pnf.subscribeChannel(token, clientKey, "channela");
        Assert.assertEquals(expectedJson, newJson);

        String expectedNextNewJson = "{\"category\":\"pushChannels\",\"group\":[{\"id\":\"token\",\"data\":[{\"value\":\"android\",\"type\":\"string\",\"key\":\"deviceType\"},{\"value\":[\"channela\",\"channelb\"],\"type\":\"array\",\"key\":\"channels\"}]}]}";
        String nextNewJson = pnf.subscribeChannel(token, clientKey, "channelb");
        Assert.assertEquals(expectedNextNewJson, nextNewJson);
    }

    @Test
    public void testSendAndroidPush() throws CertificateNotExistException, CommunicationException, JSONException, KeystoreException {

        String clientKey = "testClientKey";
       
        MockStorageDomainFacade msd = new MockStorageDomainFacade();
        AndroidPushMessageFacadeImp android = new AndroidPushMessageFacadeImp();
        PushNotificationFacadeImp pnf = new PushNotificationFacadeImp(msd, new MockFileHelper(), android, new StreamRequestsFacadeImp());
        String tokenJson = "{\"group\":[{\"id\":\"2350df294b424587acc486d6c8b90a53\",\"data\":[{\"value\":\"android\",\"type\":\"string\",\"key\":\"deviceType\"},{\"value\":[\"test1\"],\"type\":\"array\",\"key\":\"channels\"}]},{\"id\":\"cc2ff7445a874420b66113a3549f8f4d\",\"data\":[{\"value\":\"android\",\"type\":\"string\",\"key\":\"deviceType\"},{\"value\":[\"test1\"],\"type\":\"array\",\"key\":\"channels\"}]}]}";
        pnf.sendMessages(tokenJson, "helloworld", "", clientKey, "");
        LinkedList<AndroidPushMessage> pushMessageLinkedList = android.getMessages(clientKey, "2350df294b424587acc486d6c8b90a53");
        LinkedList<AndroidPushMessage> pushMessageLinkedList1 = android.getMessages(clientKey, "cc2ff7445a874420b66113a3549f8f4d");
        String m = pushMessageLinkedList.get(0).getMessage();
        String m1 = pushMessageLinkedList1.get(0).getMessage();
        Assert.assertEquals("helloworld", m);
        Assert.assertEquals("helloworld", m1);


    }

}
