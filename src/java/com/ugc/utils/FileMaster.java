package com.ugc.utils;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.Channels;
import java.nio.ByteBuffer;


public class FileMaster
{
    File _file;
    private static final String MODE_READ_WRITE = "rw";
    private int _currentChannelPosition = 0;
    private FileChannel _channel = null;

    public FileMaster(File file)
    {
        _file = file;
    }

    public int writeToFileSystem(InputStream in)
    {

        int totalWritten = 0;
        try {
             openFileChannel();
             ReadableByteChannel inChannel = Channels.newChannel(in);
             for (ByteBuffer buffer = ByteBuffer.allocate(1024 * 10); inChannel.read(buffer) != -1; buffer.clear())
             {
                buffer.flip();
                while (buffer.hasRemaining()){
                    totalWritten += _channel.write(buffer);
                }
             }
           } catch (Throwable exception)
           {
                System.out.println("Exception caught while writing to spool " + exception);
                exception.printStackTrace();
           }finally {
            safeClose(in);
            safeClose(_channel);
           }
        return totalWritten;
    }

    private void openFileChannel() throws IOException
    {
        RandomAccessFile file = new RandomAccessFile(_file, MODE_READ_WRITE);
        _channel = file.getChannel();
        _channel.position(_currentChannelPosition);
    }

    private boolean safeClose(Closeable closeable) {
        try {
            if (closeable != null)
                closeable.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public void setCurrentChannelPosition(int i)
    {
        _currentChannelPosition = i;
    }
}

