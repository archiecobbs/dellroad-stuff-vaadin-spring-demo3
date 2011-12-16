
package com.example;

import org.dellroad.stuff.vaadin.VaadinConfigurable;

/**
 * Example of a Spring bean that lives in the per-Applicaton application context
 * and is autowired by the @{@link VaadinConfigurable} aspect.
 */
@VaadinConfigurable
public class VaadinConfigurableBean extends AbstractApplicationBean {
}

