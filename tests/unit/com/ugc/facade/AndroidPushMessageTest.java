package com.ugc.facade;

import com.ugc.domain.AndroidPushMessage;
import junit.framework.Assert;
import org.junit.Test;

import java.util.LinkedList;


public class AndroidPushMessageTest
{

    @Test
    public void testPushConsume(){


        AndroidPushMessageFacadeImp apmf = new AndroidPushMessageFacadeImp();
        apmf.pushMessage("1111", "t1111", "m1");

        LinkedList<AndroidPushMessage> androidPushMessageList = apmf.getMessages("1111", "t1111");
        Assert.assertEquals(1, androidPushMessageList.size());
        apmf.pushMessage("1111", "t1111", "m2");

        long m1Time = androidPushMessageList.get(0).getMessageCreationTime();
        String messageHash = androidPushMessageList.get(0).getMessageHash();

        LinkedList<AndroidPushMessage> androidPushMessageList2 = apmf.getMessages("1111", "t1111");
        Assert.assertEquals(2, androidPushMessageList2.size());

        apmf.pushMessage("1111", "t2222", "m3");
        LinkedList<AndroidPushMessage> androidPushMessageList3 = apmf.getMessages("1111", "t2222");
        Assert.assertEquals("m3", androidPushMessageList3.get(0).getMessage());


        apmf.consumeMessage("1111", "t1111", messageHash, m1Time);

        androidPushMessageList = apmf.getMessages("1111", "t1111");

        Assert.assertEquals(1, androidPushMessageList.size());
        Assert.assertEquals("m1", androidPushMessageList.get(0).getMessage());

        apmf.pushMessage("3333", "t1111", "m1");
        apmf.pushMessage("3333", "t1111", "m2");
        apmf.pushMessage("3333", "t1111", "m3");
        apmf.pushMessage("3333", "t1111", "m3");

        androidPushMessageList = apmf.getMessages("3333", "t1111");
        long time = androidPushMessageList.get(0).getMessageCreationTime();
        String hash = androidPushMessageList.get(0).getMessageHash();
        apmf.consumeMessage("3333", "t1111", hash, time);

        androidPushMessageList = apmf.getMessages("3333", "t1111");
        Assert.assertEquals(3, androidPushMessageList.size());        
        String headOfMessage = androidPushMessageList.get(0).getMessage();
        String m2 = androidPushMessageList.get(1).getMessage();
        String m1 = androidPushMessageList.get(2).getMessage();
        Assert.assertEquals("m3", headOfMessage);
        Assert.assertEquals("m2", m2);
        Assert.assertEquals("m1", m1);

    }

}
