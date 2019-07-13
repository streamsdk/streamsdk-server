package com.ugc.domain;

import com.ugc.utils.ConversionUtils;

import java.security.NoSuchAlgorithmException;


public class AndroidPushMessage
{

    long messageCreationTime;
    String message = "";
    String messageHash = "";

    public long getMessageCreationTime() {
        return messageCreationTime;
    }

    public void setMessageCreationTime(long messageCreationTime) {
        this.messageCreationTime = messageCreationTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        this.messageHash = getHashValue(message);
    }

    public void setMessageHash(String hash){
        this.messageHash = hash;
    }

    public String getMessageHash() {
        return messageHash;
    }

    public int hashCode() {
        return messageHash.hashCode() + String.valueOf(messageCreationTime).hashCode();
    }

    public boolean equals(Object obj) {
        AndroidPushMessage apm = (AndroidPushMessage)obj;
        return messageCreationTime == apm.getMessageCreationTime() && messageHash.equals(apm.getMessageHash());
    }

    private String getHashValue(String uid) {
        try {
            return ConversionUtils.createMD5Hash(uid, true);
        } catch (NoSuchAlgorithmException e) {

        }
        return "";
    }
}
