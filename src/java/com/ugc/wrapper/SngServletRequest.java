package com.ugc.wrapper;

import org.apache.commons.collections.iterators.IteratorEnumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class SngServletRequest extends HttpServletRequestWrapper
{

    public static final String PATH_PARAMETERS = "SngServletRequest.path-parameters";
    public static final String BEST_PATH_MATCH = "SngServletRequest.best-path-match";

    public final String PARAM_SPLITTER = ",";
    public final String ARRAY_SUFFIX = "[]";

    private Map<String, String[]> _parameters = null;

    @SuppressWarnings("unchecked")
    public SngServletRequest(HttpServletRequest httpServletRequest) {
        super(httpServletRequest);
        _parameters = new HashMap<String, String[]>();
        _parameters.putAll(super.getParameterMap());
    }

    @Override
    public String getParameter(String key) {
        String[] values = getParameterValues(key);
        if ((values == null) || (values.length < 1))
            return null;
        return values[0];
    }

    @Override
    public Map getParameterMap() {
        return _parameters;
    }

    @Override
    public Enumeration getParameterNames() {
        return new IteratorEnumeration(_parameters.keySet().iterator());
    }

    @Override
    public String[] getParameterValues(String string) {
        return _parameters.get(string);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setAttribute(String key, Object value) {

        if ((PATH_PARAMETERS.equals(key)) && (value instanceof Map)) {
            addParams((Map<String, String>) value);
        }

        if ((Constants.HTTP_PARAM_SNS_UIDS.equals(key)
             || Constants.SNS_SESSION_TOKENS.equals(key)
             || Constants.SNS_SESSION_UIDS.equals(key)) && value instanceof String[]) {

            addParam(key, (String[]) value);
        }

        if ((Constants.HTTP_PARAM_SNS_UID.equals(key)
             || Constants.HTTP_PARAM_REQUEST_BASE_URL.equals(key)
             || Constants.HTTP_PARAM_AUTH_API_KEY.equals(key)
             || Constants.HTTP_PARAM_AUTO_REGISTRATION_ENABLED.equals(key)
             || "username".equals(key)//TODO delete when fakes not used
             || "password".equals(key)
             || Constants.HTTP_PARAM_SESSION_USER_UID.equals(key)
             || Constants.HTTP_PARAM_SNS_AUTH_STORE_CREDENTIALS.equals(key)
             || Constants.SNS_SESSION_TOKEN.equals(key)
             || Constants.SNS_SESSION_UID.equals(key)) && value instanceof String) {

            addParam(key, (String) value);
        }

        super.setAttribute(key, value);
    }

    private void addParams(Map<String, String> values) {
        for (String mapKey : values.keySet()) {
            addParam(mapKey, values.get(mapKey));
        }
    }

    private void addParam(String key, String[] values) {
        _parameters.put(key, values);
    }

    private void addParam(String key, String value) {
        if (key.endsWith(ARRAY_SUFFIX))
            _parameters.put(key, createArrayParameter(value));
        else
            _parameters.put(key, new String[]{value});
    }

    private String[] createArrayParameter(String value) {
        return value.split(PARAM_SPLITTER);
    }

}
