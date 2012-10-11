
package com.example;

import com.vaadin.server.SessionDestroyEvent;
import com.vaadin.server.SessionDestroyListener;

import org.dellroad.stuff.vaadin7.VaadinConfigurable;

/**
 * Example of a Spring bean that lives in the per-Applicaton application context
 * and is autowired by the @{@link VaadinConfigurable} aspect.
 */
@VaadinConfigurable
public class VaadinConfigurableBean extends AbstractApplicationBean implements SessionDestroyListener {

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        this.helloWorld.addSessionDestroyListener(this);          // DisposableBean doesn't work, so we do this instead
    }

    @Override
    public void sessionDestroy(SessionDestroyEvent event) {
        this.log.info(this.getClass().getSimpleName() + ".applicationClosed() invoked");
    }
}

