
package com.example;

import java.util.UUID;

import org.dellroad.stuff.java.ErrorAction;
import org.dellroad.stuff.pobj.PersistentObject;
import org.dellroad.stuff.pobj.vaadin.VaadinPersistentObjectListener;
import org.dellroad.stuff.vaadin7.SimpleKeyedContainer;
import org.dellroad.stuff.vaadin7.VaadinConfigurable;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Container where the underlying Java objects are generated from the {@link Data} persistent object.
 *
 * @param <T> the type of the Java objects that back each item in the container
 */
@SuppressWarnings("serial")
@VaadinConfigurable(ifSessionNotLocked = ErrorAction.EXCEPTION)
public abstract class AbstractDataContainer<T extends HasUUID> extends SimpleKeyedContainer<UUID, T> implements Connectable {

    @Autowired
    private PersistentObject<Data> persistentObject;

    private DataListener dataListener;

    /**
     * Constructor.
     *
     * @param type type of the Java objects that back each item in the container
     */
    protected AbstractDataContainer(Class<? super T> type) {
        super(type);
    }

    private void reload(Data data) {
        this.load(this.getContainerObjects(data));
    }

    protected abstract Iterable<? extends T> getContainerObjects(Data data);

// SimpleKeyedContainer

    @Override
    public UUID getKeyFor(T obj) {
        return obj.getUUID();
    }

// Connectable

    @Override
    public void connect() {

        // Start listening for Data changes
        this.dataListener = new DataListener();
        this.dataListener.register();

        // Load container
        this.reload(this.persistentObject.getSharedRoot());
    }

    @Override
    public void disconnect() {

        // Stop listening for Data changes
        if (this.dataListener != null) {
            this.dataListener.unregister();
            this.dataListener = null;
        }
    }

// DataListener

    private class DataListener extends VaadinPersistentObjectListener<Data> {

        DataListener() {
            super(AbstractDataContainer.this.persistentObject);
        }

        @Override
        protected void handlePersistentObjectChange(Data oldRoot, Data newRoot, long version) {
            if (newRoot != null)                // could do more fine-grained checking to determine if a reload is necessary
                AbstractDataContainer.this.reload(newRoot);
        }
    }
}

