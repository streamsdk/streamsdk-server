package com.ugc.utils;

import javax.servlet.http.HttpServletRequest;

public class HttpServletRequestUtils
{

    private static final String X_FORWARDED_SERVER = "x-forwarded-server";
    private static final String HTTP = "http://";
    private static final String HTTPS = "https://";
    private static final String PROTOCOL_HTTPS = "HTTPS";
    private static final int DEFAULT_PORT = 80;

    private static final String TRANSFER_ENCODING = "Transfer-Encoding";
    private static final String HTTP_CHUNCKED = "chunked";


    public static String getRequestUrl(HttpServletRequest request) throws Exception {
        StringBuffer url = getServerUrl(request);
        url.append(request.getRequestURI());
        return url.toString();
    }

    public static String getBaseUrl(HttpServletRequest request) {
        return getServerUrl(request).append(request.getContextPath()).append(request.getServletPath()).toString();
    }

    public static String getHostUrl(HttpServletRequest request) {
        return getServerUrl(request).toString();
    }

    public static String getStringHeader(HttpServletRequest httpServletRequest, String header) throws HttpHeaderException {
        String value = httpServletRequest.getHeader(header);
        if (value == null)
            throw new HttpHeaderException("Content type header not set");

        return value;
    }

    public static int getIntHeader(HttpServletRequest httpServletRequest, String header) throws HttpHeaderException {
        try {
            String value = httpServletRequest.getHeader(header);
            if (value == null)
                throw new HttpHeaderException(header + " header not set");

            return Integer.valueOf(value);
        } catch (NumberFormatException e) {
            throw new HttpHeaderException(header + " header not set correctly");
        }
    }

    public static boolean isChunkedEncoded(HttpServletRequest httpServletRequest) {
        String transferEncoded = httpServletRequest.getHeader(TRANSFER_ENCODING);
        return transferEncoded != null && transferEncoded.equalsIgnoreCase(HTTP_CHUNCKED);
    }

    //----------------------------------------------------------------

    private static StringBuffer getServerUrl(HttpServletRequest request) {
        String headerForwarderServer = request.getHeader(X_FORWARDED_SERVER);
        String serverName = request.getServerName();
        if (isRequestProxyPassed(headerForwarderServer))
            serverName = headerForwarderServer;
        else if (request.getServerPort() != DEFAULT_PORT)
            serverName = serverName + ":" + request.getServerPort();

        StringBuffer url = new StringBuffer();

        if (request.getProtocol().startsWith(PROTOCOL_HTTPS))
            url.append(HTTPS);
        else
            url.append(HTTP);

        url.append(serverName);
        return url;
    }

    private static boolean isRequestProxyPassed(String headerForwarderServer) {
//        return (serverName.equals(SERVER_NAME_LOCALHOST) || serverName.equals(SERVER_NAME_1270001)) && (headerForwarderServer != null && !"".equals(headerForwarderServer));
        return (headerForwarderServer != null && !headerForwarderServer.isEmpty());
    }

}

