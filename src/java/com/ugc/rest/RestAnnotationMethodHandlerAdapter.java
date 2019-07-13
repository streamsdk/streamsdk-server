package com.ugc.rest;

import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;

public class RestAnnotationMethodHandlerAdapter extends AnnotationMethodHandlerAdapter
{
    public RestAnnotationMethodHandlerAdapter() {
        setPathMatcher(new RestUrlPathMatcher());
    }
}