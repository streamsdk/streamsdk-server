package com.ugc.filter;

import com.ugc.wrapper.SngServletRequest;
import com.ugc.wrapper.Constants;
import com.ugc.rest.RestUrlHandlerMapping;
import com.ugc.utils.HttpServletRequestUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class RestUrlFilter extends AbstractServletFilter
{
    private final String ERROR_RESOURCE_NOT_FOUND = "Resource not found";

    @SuppressWarnings("unchecked")
    protected void processFilter(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws IOException, ServletException {

        RestUrlHandlerMapping handlerMapping = (RestUrlHandlerMapping) getBean("restUrlHandlerMapping");
        Map<String, String> bestParameters = null;
        String urlPath = httpServletRequest.getPathInfo();
        String bestPathMatch = null;

        for (Object key : handlerMapping.getHandlerMap().keySet()) {
            String registeredPath = (String) key;
            Map<String, String> parameters = handlerMapping.getPathMatcher().namedParameters(registeredPath, urlPath);

            if ((parameters != null) && (bestPathMatch == null || bestPathMatch.length() <= registeredPath.length())) {
                bestPathMatch = registeredPath;
                bestParameters = parameters;
            }
        }

        if (bestPathMatch == null) {
            httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND, ERROR_RESOURCE_NOT_FOUND);
        } else {
            httpServletRequest.setAttribute(SngServletRequest.BEST_PATH_MATCH, bestPathMatch);
            httpServletRequest.setAttribute(SngServletRequest.PATH_PARAMETERS, bestParameters);
            httpServletRequest.setAttribute(Constants.HTTP_PARAM_REQUEST_BASE_URL, HttpServletRequestUtils.getBaseUrl(httpServletRequest));
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }

    }

}