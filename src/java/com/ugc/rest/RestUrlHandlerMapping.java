package com.ugc.rest;

import com.ugc.wrapper.SngServletRequest;
import org.springframework.context.ApplicationContextException;
import org.springframework.stereotype.Controller;
import org.springframework.util.PathMatcher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

public class RestUrlHandlerMapping extends SimpleUrlHandlerMapping
{
    private RestUrlPathMatcher _pathMatcher = null;

    public RestUrlHandlerMapping() {
        _pathMatcher = new RestUrlPathMatcher();
        super.setPathMatcher(_pathMatcher);
    }

    @Override
    public void setPathMatcher(PathMatcher pathMatcher) {
        // do not replace parameterized matcher
    }

    public RestUrlPathMatcher getPathMatcher() {
        return _pathMatcher;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Object lookupHandler(String urlPath, HttpServletRequest request) {

        String bestPathMatch = (String) request.getAttribute(SngServletRequest.BEST_PATH_MATCH);
        exposePathWithinMapping(_pathMatcher.extractPathWithinPattern(bestPathMatch, urlPath), request);
        return getHandlerMap().get(bestPathMatch);
    }

    public void initApplicationContext() throws ApplicationContextException {
        super.initApplicationContext();
        detectControllers();
    }

    public void detectControllers() {
        for (String beanName : getApplicationContext().getBeanDefinitionNames()) {
            Class<?> controller = getApplicationContext().getType(beanName);
            if (controller.isAnnotationPresent(Controller.class)) {
                processController(controller, beanName);
            }
        }
    }

    private void processController(Class<?> controller, String beanName) {
        for (Method requestMappingMethod : controller.getDeclaredMethods()) {
            if (requestMappingMethod.isAnnotationPresent(RequestMapping.class)) {
                processMethod(requestMappingMethod, beanName);
            }
        }
    }

    private void processMethod(Method restMethod, String beanName) {
        RequestMapping restMapping = restMethod.getAnnotation(RequestMapping.class);
        registerHandler(restMapping.value(), beanName);
    }
}
