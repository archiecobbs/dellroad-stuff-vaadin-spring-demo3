
package com.example;

import org.springframework.beans.factory.annotation.Configurable;

/**
 * Example of a Spring bean that lives in the per-Applicaton application context
 * and is autowired by the @{@link VaadinConfigurable} aspect.
 */
@Configurable
public class ConfigurableBean extends AbstractApplicationBean {

    public ConfigurableBean() {
        this.log.info(this.getClass().getSimpleName() + " constructor invoked");
    }
}

