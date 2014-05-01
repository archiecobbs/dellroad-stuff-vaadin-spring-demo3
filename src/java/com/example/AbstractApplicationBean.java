
/*
 * Copyright (C) 2014 Archie L. Cobbs. All rights reserved.
 *
 * $Id$
 */

package com.example;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Example of a Spring bean that lives in the per-Applicaton application context.
 */
public abstract class AbstractApplicationBean extends AbstractBean {

    @Autowired
    protected MyBean myBean;

    @Autowired
    protected HelloWorld helloWorld;

    private final Date timestamp = new Date();          // this field shows that (de)serialization works

    @Override
    public String info() {
        return super.info()
          + "\n  myBean=" + this.myBean
          + "\n  helloWorld=" + this.helloWorld
          + "\n  timestamp=" + this.timestamp;
    }
}

