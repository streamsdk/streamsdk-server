package com.ugc.facade;

import org.json.JSONException;
import org.json.JSONArray;

import java.util.Map;

public interface QueryDomainFacade
{

    public Map<String,JSONArray> findMatchingObjects(String json, String query) throws JSONException;


}
