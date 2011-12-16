
package com.example;

import org.dellroad.stuff.vaadin.SpringContextApplication;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Example of a Spring bean that lives in the per-Applicaton application context.
 */
public class MyApplicationBean extends AbstractApplicationBean {

    protected VaadinConfigurableBean vaadinConfigurableBean;

    public MyApplicationBean() {
        this.log.info(this.getClass().getSimpleName() + " constructor invoking new VaadinConfigurableBean()");
        this.vaadinConfigurableBean = new VaadinConfigurableBean();     // bean is @VaadinConfigurable, so the aspect will autowire it
    }

    public VaadinConfigurableBean getVaadinConfigurableBean() {
        return this.vaadinConfigurableBean;
    }

    @Override
    public String info() {
        return super.info()
          + "\n  vaadinConfigurableBean=" + this.vaadinConfigurableBean;
    }
}

