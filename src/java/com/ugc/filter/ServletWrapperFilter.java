package com.ugc.filter;

import com.ugc.wrapper.SngServletRequest;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ServletWrapperFilter extends AbstractServletFilter
{

    protected void processFilter(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                 FilterChain filterChain) throws IOException, ServletException
    {
        SngServletRequest sngServletRequest = new SngServletRequest(httpServletRequest);
        filterChain.doFilter(sngServletRequest, httpServletResponse);
    }

}

