package com.ugc.utils;

import com.ugc.facade.StorageDomainFacade;

public class FlushCacheUtils
{
    public static void flush(StorageDomainFacade sdf, String buketName, String key, String json){
          sdf.saveObject(json, buketName, key);
    }


}
