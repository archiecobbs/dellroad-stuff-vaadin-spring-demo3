
package com.example;

/**
 * Implemented by GUI classes that connect and disconnect to back-end stuff.
 *
 * Required because in Vaadin there is no equivalent of attach()/detach() for Containers
 * like there is for widgets.
 */
public interface Connectable {

    /**
     * Connect this instance. There will be a Vaadin session associated with the current thread.
     */
    void connect();

    /**
     * Disconnect this instance. There will be a Vaadin session associated with the current thread.
     */
    void disconnect();
}

