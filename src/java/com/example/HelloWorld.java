
/*
 * Copyright (C) 2014 Archie L. Cobbs. All rights reserved.
 *
 * $Id$
 */

package com.example;

import org.apache.log4j.Logger;
import org.dellroad.stuff.vaadin7.VaadinApplication;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

// Note how in Vaadin 7 this class contains no GUI-related code, only "application" related code.
// Also, use of an application-wide singleton like this is optional in Vaadin 7.

@SuppressWarnings("serial")
public class HelloWorld extends VaadinApplication implements BeanFactoryAware, InitializingBean, DisposableBean {

    protected transient Logger log = Logger.getLogger(this.getClass());

    @Autowired
    private MyBean myBean;

    @Autowired
    private MyApplicationBean myApplicationBean;

    private String someProperty;
    private BeanFactory beanFactory;

    public HelloWorld() {
        this.log.info(this.getClass().getSimpleName() + " constructor invoked");
    }

    public MyBean getMyBean() {
        return this.myBean;
    }

    public MyApplicationBean getMyApplicationBean() {
        return this.myApplicationBean;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.log.info(this.getClass().getSimpleName() + ".setBeanFactory() invoked");
        this.beanFactory = beanFactory;
    }

    @Override
    public void afterPropertiesSet() {
        this.log.info(this.getClass().getSimpleName() + ".afterPropertiesSet() invoked");
    }

    @Override
    public void destroy() {
        this.log.info(this.getClass().getSimpleName() + ".destroy() invoked");
    }

    public String info() {
        return this.getClass().getSimpleName() + "@" + Integer.toHexString(this.hashCode())
          + "\n  beanFactory=" + (this.beanFactory != null ? this.beanFactory.toString().replaceAll(",", ",\n    ") : null)
          + "\n  someProperty=" + this.someProperty;
    }

    public void setSomeProperty(String someProperty) {
        this.someProperty = someProperty;
    }
}

