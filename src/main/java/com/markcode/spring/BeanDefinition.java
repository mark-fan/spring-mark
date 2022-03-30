package com.markcode.spring;

/**
 * @author luofan
 */
public class BeanDefinition {
    private Class aClass;
    private String scope;

    public BeanDefinition() {
    }

    public BeanDefinition(Class aClass, String scope) {
        this.aClass = aClass;
        this.scope = scope;
    }

    public Class getaClass() {
        return aClass;
    }

    public void setaClass(Class aClass) {
        this.aClass = aClass;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
