package com.ugc.domain;

public class SystemUser
{

    private String userName = "";
    private String password = "";
    private String clientKey = "";
    private String secretKey = "";
    private String email = "";
    long creationTime = 0;
    private String json = "";
    private long loginTime = 0;
    private String appId = "";

    public SystemUser(){}

    public SystemUser(String userName) {
        this.userName = userName;
    }

    public String getClientKey() {
        return clientKey;
    }

    public void setClientKey(String clientKey) {
        this.clientKey = clientKey;
    }

    public String getJson() {
        return json;
    }

    public void setMetadataJson(String json) {
        this.json = json;
    }

    public long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(long loginTime) {
        this.loginTime = loginTime;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public SystemUser(String userName, String password) {

        this.userName = userName;
        this.password = password;
        
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCurrentAppId(String appId) {
        this.appId = appId;
    }

    public String getAppId() {
        return appId;
    }
}
