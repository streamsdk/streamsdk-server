package com.ugc.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class QueryObjectList
{
    private String logic = "";
    private List<QueryObject> queries = new ArrayList<QueryObject>();
    private Set<String> limitedKeys = new HashSet<String>();

    public void addQueryObject(String key, QueryObject qo) {
        queries.add(qo);
    }

    public void addLimitedKeys(String key) {
        limitedKeys.add(key);
    }

    public Set<String> getLimitedKeys() {
        return limitedKeys;
    }

    public List<QueryObject> getQueryObject(String key) {

        List<QueryObject> qos = new ArrayList<QueryObject>();
        for (QueryObject qo : queries) {
            if (qo.getKey().equals(key))
                qos.add(qo);

        }
        return qos;
    }

    public String getLogic() {
        return logic;
    }

    public void setLogic(String logic) {
        this.logic = logic;
    }

    public int limitedKeysSize(){
        return limitedKeys.size();
    }

    public int size() {
        return queries.size();
    }

    public String getGeoKey(){
        for (QueryObject qo : queries){
            if (qo.getOperator().equals("near"))
                return qo.getKey();
        }
        return null;
    }

    public String getGeoValue(){
        for (QueryObject qo : queries){
            if (qo.getOperator().equals("near"))
                return qo.getValue();
        }
        return null;
    }
}
