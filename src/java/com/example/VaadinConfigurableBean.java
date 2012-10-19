
package com.example;

import org.dellroad.stuff.java.ErrorAction;
import org.dellroad.stuff.vaadin.VaadinConfigurable;
import org.dellroad.stuff.vaadin.ContextApplication;

/**
 * Example of a Spring bean that lives in the per-Applicaton application context
 * and is autowired by the @{@link VaadinConfigurable} aspect.
 */
@VaadinConfigurable(ifApplicationNotLocked = ErrorAction.EXCEPTION)
public class VaadinConfigurableBean extends AbstractApplicationBean implements ContextApplication.CloseListener {

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        this.helloWorld.addListener(this);          // DisposableBean doesn't work, so we do this instead
    }

    @Override
    public void applicationClosed(ContextApplication.CloseEvent closeEvent) {
        this.log.info(this.getClass().getSimpleName() + ".applicationClosed() invoked");
    }
}

