package com.ugc.utils;

import org.junit.Test;
import org.junit.Assert;

public class StringUtilsTest
{

    @Test
    public void testGetTokenFromIndex() {
        String uri = "/this/how/to/get/it";
        Assert.assertEquals("it", StringUtils.getTokenFromIndex(uri, "/", 4));
    }

    @Test
    public void testGetTotalToken() {
        String uri = "/this/how/to/get/it";
        Assert.assertEquals(5, StringUtils.countTotalTokens(uri, "/"));
    }

    @Test
    public void testReplace() {
        String str = "rt\\wq\\op\\mkz\\dk";
        Assert.assertEquals("rt/wq/op/mkz/dk", StringUtils.hackedUrl(str));
    }

    @Test
    public void testGetIndexOf() {

        String addressInfo = "receiver city fulladdress";
        String str = StringUtils.getIndexOf(addressInfo, " ", 0);
        Assert.assertEquals("receiver", str);
    }
    

    @Test
    public void testParseToken(){

        String token = "Device Token=<e8b0f899 159f5e26 9f540282 b7099040 d49ec492 d0e66ed5 7dac56bb b41a42bf>";

        String tokenWihoutSpace = StringUtils.parseToken(token);
        String expected = "e8b0f899159f5e269f540282b7099040d49ec492d0e66ed57dac56bbb41a42bf";
        Assert.assertEquals(expected, tokenWihoutSpace);
      
    }
}
