package com.ugc.facade;

import java.io.File;


public interface ImageHashSearchFacade
{
    public void searchImageAndSendPush(String json, String clientKey, File file);

    public boolean isTestMode();
}
