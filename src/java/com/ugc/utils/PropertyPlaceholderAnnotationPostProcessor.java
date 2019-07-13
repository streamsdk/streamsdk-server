package com.ugc.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

public class PropertyPlaceholderAnnotationPostProcessor implements BeanFactoryPostProcessor, BeanFactoryAware, BeanNameAware, PriorityOrdered
{
    private BeanFactory beanFactory;
    private String beanName;

    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        for (String beanName1 : beanFactory.getBeanDefinitionNames())
            // Check that we're not parsing our own bean definition,
            // to avoid failing on unresolvable placeholders in properties file locations.
            if (!(beanName1.equals(this.beanName) && beanFactory.equals(this.beanFactory)) && !beanFactory.getBeanDefinition(beanName1).isAbstract())
                injectPropertiesFromAnnotations(beanName1, beanFactory.getBeanDefinition(beanName1));
    }

    private void injectPropertiesFromAnnotations(String beanName1, BeanDefinition bd) {
        try {
            for (Method method : Class.forName(bd.getBeanClassName()).getMethods()) {
                String propertyName = StringUtils.uncapitalize(method.getName().replaceFirst("set", ""));
                PlaceholderProperty annotation = method.getAnnotation(PlaceholderProperty.class);
                if (annotation != null)
                    bd.getPropertyValues().addPropertyValue(propertyName, "${" + annotation.key() + "}");
            }
        } catch (Exception ex) {
            throw new BeanDefinitionStoreException(bd.getResourceDescription(), beanName1, ex.getMessage());
        }
    }

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    public void setBeanName(String name) {
        beanName = name;
    }

    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}

