
/*
 * Copyright (C) 2014 Archie L. Cobbs. All rights reserved.
 *
 * $Id$
 */

package com.example;

import org.springframework.context.ApplicationEvent;

// Superclass for change events sent from the data layer to the rest of the application
public abstract class AbstractChangeEvent<T> extends ApplicationEvent {

    private final boolean structural;

    protected AbstractChangeEvent(T obj, boolean structural) {
        super(obj);
        this.structural = structural;
    }

    /**
     * Get the instance that changed.
     */
    @SuppressWarnings("unchecked")
    public T getObject() {
        return (T)this.getSource();
    }

    public boolean isStructural() {
        return this.structural;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null || obj.getClass() != this.getClass())
            return false;
        final AbstractChangeEvent<?> that = (AbstractChangeEvent<?>)obj;
        return this.getObject().equals(that.getObject());
    }

    @Override
    public int hashCode() {
        return this.getObject().hashCode();
    }
}

