
/*
 * Copyright (C) 2014 Archie L. Cobbs. All rights reserved.
 *
 * $Id$
 */

package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.event.ApplicationEventMulticaster;

public final class ApplicationContextInfo implements ApplicationContextAware {

    private static final ApplicationContextInfo INSTANCE = new ApplicationContextInfo();

    private ApplicationContext context;

    @Autowired
    @Qualifier("webApplicationMulticaster")
    private ApplicationEventMulticaster eventMulticaster;

    private ApplicationContextInfo() {
    }

    public static ApplicationContextInfo getInstance() {
        return ApplicationContextInfo.INSTANCE;
    }

    @Override
    public void setApplicationContext(ApplicationContext context) {
        this.context = context;
    }

    public ApplicationContext getApplicationContext() {
        if (this.context == null)
            throw new IllegalStateException("no context configured");
        return this.context;
    }

    public ApplicationEventMulticaster getEventMulticaster() {
        if (this.eventMulticaster == null)
            throw new IllegalStateException("no eventMulticaster configured");
        return this.eventMulticaster;
    }
}

