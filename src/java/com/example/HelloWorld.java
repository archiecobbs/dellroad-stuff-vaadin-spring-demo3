
package com.example;

import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;

import org.apache.log4j.Logger;
import org.dellroad.stuff.vaadin.ContextApplication;
import org.dellroad.stuff.vaadin.SpringContextApplication;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ConfigurableWebApplicationContext;

@SuppressWarnings("serial")
public class HelloWorld extends SpringContextApplication implements BeanFactoryAware, InitializingBean, DisposableBean {

    protected transient Logger log = Logger.getLogger(this.getClass());

    @Autowired
    private MyBean myBean;

    @Autowired
    private MyApplicationBean myApplicationBean;

    private VaadinConfigurableBean vaadinConfigurableBean;

    private String someProperty;
    private BeanFactory beanFactory;

    public HelloWorld() {
        this.log.info(this.getClass().getSimpleName() + " constructor invoked");
    }

    @Override
    public void initSpringApplication(ConfigurableWebApplicationContext context) {
        this.log.info("initializing HelloWorld application...");

        // Example of creating a @VaadinConfigurable bean
        this.log.info(this.getClass().getSimpleName() + " invoking new VaadinConfigurableBean()");
        this.vaadinConfigurableBean = new VaadinConfigurableBean();  // bean is @VaadinConfigurable, so the aspect will autowire it

        Window mainWindow = new Window("Vaadin+Spring Demo #3");
        mainWindow.addComponent(new Label(
          "<b>Notes</b>"
          + "<ul>"
          + "<li>The bean <code>myBean</code> bean lives in the Spring application context that is associated with"
          + " the overall servlet context, that is, the Spring application context created by Spring's"
          + " ContextLoaderListener. It will not change until you reload the overall web application.</li>"
          + "<li>The <code>myApplicationBean</code> is a bean in the Spring application context that is automatically"
          + " created along with each new Vaadin application instance (defined by <code>HelloWorld.xml</code>)."
          + " It will not change if you just reload your browser, but if you hit the <b>Close Application</b>"
          + " button below, then it will change.</li>"
          + "<li>Similarly for <code>vaadinConfigurableBean</code>, which is a bean that is"
          + " configured into the Spring application implicitly using AspectJ and the <code>@VaadinConfigurable</code>"
          + " annotation (rather than explictly in <code>HelloWorld.xml</code> as is <code>myApplicationBean</code>)."
          + " However, even though this bean implements <code>DisposableBean</code> and"
          + " <code>ApplicationListener&lt;ApplicationContextEvent&gt;</code>, neither of the corresponding methods"
          + " will be invoked (same as for Spring's <code>@Configurable</code>).</li>"
          + "<li>The <code>self</code> bean is the <code>com.example.HelloWorld</code> Vaadin application instance itself,"
          + " which is configured by and autowired into the <code>HelloWorld.xml</code> application context using"
          + " a <code>factory-method</code> bean definition invoking <code>ContextApplication.get()</code>.</li>"
          + "<li>View the servlet container log file to see the order of bean initialization, etc. Try closing"
          + " and re-opening the browser Window, reloading the WAR, etc. to see bean and application context lifecycles.</li>"
          + "</ul>",
          Label.CONTENT_XHTML));
        mainWindow.addComponent(new Label("----------", Label.CONTENT_PREFORMATTED));
        mainWindow.addComponent(new Label("self: " + this.info(), Label.CONTENT_PREFORMATTED));
        mainWindow.addComponent(new Label("----------", Label.CONTENT_PREFORMATTED));
        mainWindow.addComponent(new Label("myBean: " + this.myBean.info(), Label.CONTENT_PREFORMATTED));
        mainWindow.addComponent(new Label("----------", Label.CONTENT_PREFORMATTED));
        mainWindow.addComponent(new Label("myApplicationBean: " + this.myApplicationBean.info(), Label.CONTENT_PREFORMATTED));
        mainWindow.addComponent(new Label("----------", Label.CONTENT_PREFORMATTED));
        mainWindow.addComponent(new Label("vaadinConfigurableBean: " + this.vaadinConfigurableBean.info(),
          Label.CONTENT_PREFORMATTED));
        mainWindow.addComponent(new Label("----------", Label.CONTENT_PREFORMATTED));
        mainWindow.addComponent(new Button("Close Application", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                HelloWorld.this.log.info("closing HelloWorld application...");
                ContextApplication.get().close();
            }
        }));
        this.setMainWindow(mainWindow);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.log.info(this.getClass().getSimpleName() + ".setBeanFactory() invoked");
        this.beanFactory = beanFactory;
    }

    @Override
    public void afterPropertiesSet() {
        this.log.info(this.getClass().getSimpleName() + ".afterPropertiesSet() invoked");
    }

    @Override
    public void destroy() {
        this.log.info(this.getClass().getSimpleName() + ".destroy() invoked");
    }

    @Override
    public void destroySpringApplication() {
        this.log.info(this.getClass().getSimpleName() + ".destroySpringApplication() invoked");
    }

    public String info() {
        return this.getClass().getSimpleName() + "@" + Integer.toHexString(this.hashCode())
          + "\n  beanFactory=" + (this.beanFactory != null ? this.beanFactory.toString().replaceAll(",", ",\n    ") : null)
          + "\n  vaadinConfigurableBean=" + this.vaadinConfigurableBean
          + "\n  someProperty=" + this.someProperty;
    }

    public void setSomeProperty(String someProperty) {
        this.someProperty = someProperty;
    }
}

