
/*
 * Copyright (C) 2014 Archie L. Cobbs. All rights reserved.
 *
 * $Id$
 */

package com.example;

import org.jsimpledb.JObject;
import org.jsimpledb.JTransaction;
import org.jsimpledb.core.Transaction;

// Support superclass for database objects providing a way to publish change events
public abstract class AbstractData implements JObject {

    protected void publishChangeOnCommit(AbstractChangeEvent<?> event) {
        JTransaction.getCurrent().getTransaction().addCallback(new PublishChangeCallback(event));
    }

// PublishChangeCallback - notifies the rest of the application when a data instance has been added/removed/changed

    private static class PublishChangeCallback extends Transaction.CallbackAdapter {

        private final AbstractChangeEvent<?> event;

        PublishChangeCallback(AbstractChangeEvent<?> event) {
            if (event == null)
                throw new IllegalArgumentException("null event");
            this.event = event;
        }

        @Override
        public void afterCommit() {
            ApplicationContextInfo.getInstance().getEventMulticaster().multicastEvent(this.event);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null || obj.getClass() != this.getClass())
                return false;
            final PublishChangeCallback that = (PublishChangeCallback)obj;
            return this.event.equals(that.event);
        }

        @Override
        public int hashCode() {
            return this.event.hashCode();
        }
    }
}

