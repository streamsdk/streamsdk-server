package com.ugc.utils;

import java.io.*;
import java.nio.channels.Channel;

public class StreamUtils {
    public static boolean WE_ARE_HARDCODING_MSISDNS = false;

    public static String readString(InputStream is) {
        StringBuffer sb = new StringBuffer();
        try {
            while (is.available() > 0)
                sb.append((char) is.read());
            return sb.toString();
        }
        catch (IOException e) {
            throw new RuntimeException("Cannot read from stream :" + e.getMessage());
        }
    }

    public static String readString(InputStream is, int length) {
        return new String(readByteArray(is, length));
    }

    public static byte[] readByteArray(InputStream is, int length) {
        byte[] bytes = new byte[length];
        try {
            is.read(bytes);
        }
        catch (IOException e) {
            throw new RuntimeException("Cannot read from stream :" + e.getMessage());
        }
        return bytes;
    }

    public static void writeString(OutputStream os, String str) {
        writeBytes(os, str.getBytes());
    }

    public static void writeBytes(OutputStream os, byte[] bytes) {
        try {
            os.write(bytes);
        }
        catch (IOException e) {
            throw new RuntimeException("Cannot write to OutputStream :" + e.getMessage());
        }
    }


    public static void safeFlush(OutputStream outputstream) {
        try {
            outputstream.flush();
        }
        catch (Throwable t) {
        }
    }

    public static void safeClose(InputStream inputStream) {
        try {
            inputStream.close();
        }
        catch (Throwable t) {
        }
    }

    public static void safeClose(OutputStream outputStream) {
        try {
            outputStream.close();
        }
        catch (Throwable t) {
        }
    }


    private static byte[] dumbReadByteArray(InputStream is) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            int readBytes;
            byte buf[] = new byte[1024];
            while ((readBytes = is.read(buf)) != -1) {
                baos.write(buf, 0, readBytes);
            }
            return baos.toByteArray();
        }
        catch (IOException e) {
            throw new RuntimeException("Cannot read from stream :" + e.getMessage());
        }
        finally {
            safeClose(baos);
            safeClose(is);
        }
    }


    private static int safeRead(InputStream is) {
        try {
            return is.read();
        }
        catch (IOException e) {
            return -1;
        }
    }

    public static byte[] readByteArray(InputStream inputStream) {
        return dumbReadByteArray(inputStream);
    }

    public static void writeStream(InputStream from, OutputStream to, int length) {
        try {
            while (length-- > 0) {
                int r = from.read();
                if (r == -1)
                    throw new RuntimeException("length is greater than inputstream length");
                to.write(r);
            }
        }
        catch (IOException ioexp) {
            throw new RuntimeException("Error writing stream " + ioexp);
        }
    }

   public static void writeDoubleStream (InputStream from, OutputStream diskStream, OutputStream networkStream) throws IOException {

       byte buffer[] = new byte[1024 * 10];
       int readBytes;
       while ((readBytes = from.read(buffer)) != -1) {
                diskStream.write(buffer, 0, readBytes);
                networkStream.write(buffer, 0, readBytes);
       }
       safeClose(from);
       safeClose(diskStream);
       safeClose(networkStream);
   }

    public static void writeSingleStream (InputStream from, OutputStream stream) throws IOException {

       byte buffer[] = new byte[1024 * 10];
       int readBytes;
       while ((readBytes = from.read(buffer)) != -1) {
                stream.write(buffer, 0, readBytes);
       }
       safeClose(from);
       safeClose(stream);
   }


    public static void writeStream(InputStream from, OutputStream to) {
        try {
            int r;
            while ((r = from.read()) != -1) {
                to.write(r);
            }
        }
        catch (IOException ioexp) {
            throw new RuntimeException("Error writing stream " + ioexp);
        }
       safeClose(from);
       safeClose(to);
    }

    public static void safeClose(Reader reader) {
        try {
            if (reader != null)
                reader.close();
        } catch (IOException e) {
        }
        reader = null;
    }

    public static void safeClose(Writer writer) {
        try {
            if (writer != null)
                writer.close();
        } catch (IOException e) {
        }
        writer = null;
    }

    public static void safeClose(RandomAccessFile file) {
        try {
            if (file != null)
                file.close();
        } catch (IOException e) {
        }
        file = null;
    }

    public static void safeClose(Channel channel) {
        try {
            if (channel != null)
                channel.close();
        } catch (IOException e) {
        }
        channel = null;
    }

    private static void skipToStartByte(InputStream inputStream, int from) throws IOException {
        inputStream.skip(from);
    }

    private static boolean rangeNotExceeded(int bytesRead, int from, int totalSize) {
        return bytesRead < totalSize - from;
    }

     public static int convertInputStreamToOutputStream(InputStream inputStream, OutputStream outputStream, int totalSize, int from) throws IOException {
        skipToStartByte(inputStream, from);

        // TODO: optimise? channels?
        int value;
        int bytesRead = 0;
        while (rangeNotExceeded(bytesRead, from, totalSize) && (value = inputStream.read()) != -1) {
            outputStream.write(value);
            bytesRead++;
        }
        outputStream.flush();
        return bytesRead;
    }
}
