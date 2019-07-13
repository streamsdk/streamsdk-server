package com.ugc.facade;

import com.ugc.controller.SystemConstants;
import junit.framework.Assert;
import org.junit.Test;

public class StreamRequestFlashToStorageImpTest
{
    @Test
    public void testSend() {


        StreamRequestsFacade re = new StreamRequestsFacadeImp();
        re.countRequest("testid");
        re.countRequest("testid");
        re.countRequest("testid");
        re.countRequest("testid");
        re.countRequest("testid1");
        re.countRequest("testid1");
        re.countRequest("testid1");
        re.countRequest("testid1");

        StorageDomainFacade store = new MockStorageDomainFacade();
        MockFileHelper fileHelper = new MockFileHelper();
        StreamRequestFlashToStorageImp sr = new StreamRequestFlashToStorageImp(re, store, fileHelper);
        sr.sendToStorage();
        String json = store.get("testid20124", "", "", SystemConstants.STATISTICS);
        Assert.assertNotNull(json);
        Assert.assertEquals(0, re.getRequests().size());
    }


}
