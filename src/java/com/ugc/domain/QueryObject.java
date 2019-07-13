package com.ugc.domain;

public class QueryObject
{
    private String value = "";
    private String key = "";
    private String operator = "";
    private String valueArray[];

    public QueryObject(String value, String key, String operator) {
        this.value = value;
        this.key = key;
        this.operator = operator;
    }

    public QueryObject(String[] str, String key, String operator) {
        this.valueArray = str;
        this.key = key;
        this.operator = operator;
    }

    public String[] getValueArray(){
        return valueArray;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}
