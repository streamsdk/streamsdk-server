package com.ugc.domain;

public class UserDTO
{
    private String metaDataJson = "";
    private String userName;
    private String loginTime;
    private String creationTime;
    private String clientKey = "";
    private String secretKey = "";
    private String appId = "";

    public String getAppId() {
        return appId;
    }

    public String getClientKey() {
        return clientKey;
    }

    public void setClientKey(String clientKey) {
        this.clientKey = clientKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public String getMetaDataJson() {
        return metaDataJson;
    }

    public void setMetaDataJson(String metaDataJson) {
        this.metaDataJson = metaDataJson;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public void setAppId(String id) {
        this.appId = id;
    }
}
