
package com.example;

import org.dellroad.stuff.vaadin.SpringContextApplication;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Example of a Spring bean that lives in the per-Applicaton application context.
 */
public class MyApplicationBean extends AbstractApplicationBean {

    protected ConfigurableBean configurableBean;

    public MyApplicationBean() {
        this.log.info(this.getClass().getSimpleName() + " constructor invoked");
        this.log.info(this.getClass().getSimpleName() + " constructor invoking new ConfigurableBean()");
        this.configurableBean = new ConfigurableBean();     // the @Configurable aspect will autowire this configurableBean
    }

    public ConfigurableBean getConfigurableBean() {
        return this.configurableBean;
    }

    @Override
    public String info() {
        return super.info()
          + "\n  configurableBean=" + this.configurableBean;
    }
}

