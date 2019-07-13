package com.ugc.utils;

import junit.framework.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class StreamUtilsTest
{
    @Test
    public void testDoubleWrite() throws IOException {

        byte bytes[] = "test double write stream working".getBytes();
        ByteArrayOutputStream b1 = new ByteArrayOutputStream();
        ByteArrayOutputStream b2 = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);

        StreamUtils.writeDoubleStream(in, b1, b2);

        String bs1 = new String(b1.toByteArray());
        String bs2 = new String(b2.toByteArray());

        Assert.assertEquals("test double write stream working", bs1);
        Assert.assertEquals("test double write stream working", bs2);

    }

}
