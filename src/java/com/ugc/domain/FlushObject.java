package com.ugc.domain;

public class FlushObject
{
    private String json = "";
    private String key = "";
    private String buketName = "";

    public FlushObject(String json, String key, String buketName) {
        this.json = json;
        this.key = key;
        this.buketName = buketName;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getBuketName() {
        return buketName;
    }

    public void setBuketName(String buketName) {
        this.buketName = buketName;
    }
}
