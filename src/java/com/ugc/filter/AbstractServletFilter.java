package com.ugc.filter;

import com.ugc.wrapper.Constants;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

public abstract class AbstractServletFilter extends GenericFilterBean
{
    //protected final Log _log = LogFactory.getLog(AbstractServletFilter.class);

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        if (!isHttpRequest(servletRequest, servletResponse))
            throw new ServletException("Unsupported request type");

        servletRequest.setCharacterEncoding("UTF-8");
        servletResponse.setCharacterEncoding("UTF-8");
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        processFilter(httpServletRequest, httpServletResponse, filterChain);
    }

    //-------------------------------------------------------------------

    protected abstract void processFilter(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws IOException, ServletException;

    //-------------------------------------------------------------------

    protected Object getBean(String beanName) {
        return WebApplicationContextUtils.getWebApplicationContext(getServletContext()).getBean(beanName);
    }

    /*protected void logDebug(String message) {
        if (_log.isDebugEnabled())
            _log.debug(message);
    }

    protected void logInfo(String message) {
        if (_log.isInfoEnabled())
            _log.info(message);
    }

    protected void logError(String message) {
        if (_log.isErrorEnabled())
            _log.error(message);
    }

    protected void logError(String message, Exception e) {
        if (_log.isErrorEnabled())
            _log.error(message, e);
    }*/

    protected boolean doesUriPathPatternMatch(HttpServletRequest httpServletRequest, String initParamPatternName) throws IOException, ServletException {
        String initParamPatterns = getFilterConfig().getInitParameter(initParamPatternName);
        if (initParamPatterns != null) {
            String[] patterns = initParamPatterns.split(Constants.HTTP_PARAM_SPLITTER);
            return hasPatterns(patterns, httpServletRequest);
        }

        return false;
    }

    //-------------------------------------------------------------------

    private boolean hasPatterns(String[] patterns, HttpServletRequest httpServletRequest) {
        for (String pattern : patterns)
            if (Pattern.matches(pattern, httpServletRequest.getServletPath() + httpServletRequest.getPathInfo()))
                return true;

        return false;
    }

    private boolean isHttpRequest(ServletRequest servletRequest, ServletResponse servletResponse) {
        return servletRequest instanceof HttpServletRequest && servletResponse instanceof HttpServletResponse;
    }
}
