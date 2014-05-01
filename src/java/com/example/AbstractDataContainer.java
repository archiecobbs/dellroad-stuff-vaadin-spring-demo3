
/*
 * Copyright (C) 2014 Archie L. Cobbs. All rights reserved.
 *
 * $Id$
 */

package com.example;

import org.dellroad.stuff.java.ErrorAction;
import org.dellroad.stuff.vaadin7.SimpleItem;
import org.dellroad.stuff.vaadin7.SimpleKeyedContainer;
import org.dellroad.stuff.vaadin7.VaadinApplicationListener;
import org.dellroad.stuff.vaadin7.VaadinConfigurable;
import org.jsimpledb.core.ObjId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.transaction.annotation.Transactional;

/**
 * Container where the underlying Java objects are generated from data layer objects.
 *
 * @param <V> the type of the Java objects that back each item in the container
 * @param <E> the type of {@link org.springframework.context.ApplicationEvent} that should trigger an update or reload
 */
@SuppressWarnings("serial")
@VaadinConfigurable(ifSessionNotLocked = ErrorAction.EXCEPTION)
public abstract class AbstractDataContainer<V extends VObject<? super V>, E extends AbstractChangeEvent<V>>
  extends SimpleKeyedContainer<ObjId, V> {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    @Qualifier("webApplicationMulticaster")                 // get the one from the main context, not the Vaadin context
    private ApplicationEventMulticaster eventMulticaster;

    private DataChangeListener dataChangeListener;

    /**
     * Constructor.
     *
     * @param type type of the Java objects that back each item in the container
     */
    protected AbstractDataContainer(Class<? super V> type) {
        super(type);
    }

    @Transactional(readOnly = true)
    public void reload() {
        this.load(this.getContainerObjects());
    }

    @Override
    public ObjId getKeyFor(V obj) {
        return obj.getObjId();
    }

    protected abstract Iterable<? extends V> getContainerObjects();

// Connectable

    @Override
    public void connect() {
        super.connect();
        this.dataChangeListener = new DataChangeListener();
        this.dataChangeListener.register();
        this.reload();
    }

    @Override
    public void disconnect() {
        if (this.dataChangeListener != null) {
            this.dataChangeListener.unregister();
            this.dataChangeListener = null;
        }
        super.disconnect();
    }

// DataChangeListener

    private class DataChangeListener extends VaadinApplicationListener<E> {

        DataChangeListener() {
            super(AbstractDataContainer.this.eventMulticaster);
            this.setAsynchronous(true);
        }

        @Override
        protected void onApplicationEventInternal(E event) {
            final ObjId id = event.getObject().getObjId();
            final SimpleItem<V> item = (SimpleItem<V>)AbstractDataContainer.this.getItem(id);
            if (item != null && !event.isStructural()) {
                item.getObject().copyFrom(event.getObject());
                item.fireValueChange();
            } else
                AbstractDataContainer.this.reload();
        }
    }
}

