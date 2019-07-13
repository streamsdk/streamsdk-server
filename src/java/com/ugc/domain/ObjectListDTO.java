package com.ugc.domain;

import java.util.Date;

public class ObjectListDTO
{
    String id = "";
    long size = 0;
    Date lastModified;
    long inMillionsSeconds = 0;
    String fileMetadataJson;

    public ObjectListDTO(String id, long size, Date lastModified, long in) {
        this.id = id;
        this.size = size;
        this.lastModified = lastModified;
        this.inMillionsSeconds = in;
    }

    public String getFileMetadataJson() {
        return fileMetadataJson;
    }

    public void setFileMetadataJson(String fileMetadataJson) {
        this.fileMetadataJson = fileMetadataJson;
    }

    public long getTimeInMillions(){
        return inMillionsSeconds;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }


}
