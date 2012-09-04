
package com.example;

import com.vaadin.terminal.WrappedRequest;
import com.vaadin.terminal.gwt.client.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Root;
import com.vaadin.ui.VerticalLayout;

import org.apache.log4j.Logger;
import org.dellroad.stuff.vaadin.VaadinConfigurable;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ConfigurableWebApplicationContext;

// In Vaadin 7, we used @VaadinConfigurable to have this class autowired

@SuppressWarnings("serial")
@VaadinConfigurable
public class HelloRoot extends Root {

    protected transient Logger log = Logger.getLogger(this.getClass());

    @Autowired
    private HelloWorld helloWorld;

    private VaadinConfigurableBean vaadinConfigurableBean;

    public HelloRoot() {
        super("Vaadin+Spring Demo #3");
        this.log.info(this.getClass().getSimpleName() + " constructor invoked");
    }

    @Override
    public void init(WrappedRequest wrappedRequest) {
        this.log.info("initializing HelloRoot root...");

        // Example of creating a @VaadinConfigurable bean
        this.log.info(this.getClass().getSimpleName() + " invoking new VaadinConfigurableBean()");
        this.vaadinConfigurableBean = new VaadinConfigurableBean();  // bean is @VaadinConfigurable, so the aspect will autowire it

        // Build layout
        VerticalLayout layout = new VerticalLayout();
        layout.addComponent(new Label(
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
          + "<li>The <code>helloWorld</code> bean is the <code>com.example.HelloWorld</code> Vaadin application instance itself,"
          + " which is configured by and autowired into the <code>HelloWorld.xml</code> application context using"
          + " a <code>factory-method</code> bean definition invoking <code>ContextApplication.get()</code>.</li>"
          + "<li>The <code>helloRoot</code> bean is the <code>com.example.HelloRoot</code> Vaadin Root instance,"
          + " also configured and autowired into the <code>HelloWorld.xml</code> application via <code>@VaadinConfigurable</code>."
          + " Note how reloading the browser refreshes this bean, but not <code>helloWorld</code>.</li>"
          + "<li>View the servlet container log file to see the order of bean initialization, etc. Try closing"
          + " and re-opening the browser Window, reloading the WAR, etc. to see bean and application context lifecycles.</li>"
          + "</ul>",
          ContentMode.XHTML));
        layout.addComponent(new Label("----------", ContentMode.PREFORMATTED));
        layout.addComponent(new Label("helloWorld: " + this.helloWorld.info(), ContentMode.PREFORMATTED));
        layout.addComponent(new Label("----------", ContentMode.PREFORMATTED));
        layout.addComponent(new Label("helloRoot: " + this.info(), ContentMode.PREFORMATTED));
        layout.addComponent(new Label("----------", ContentMode.PREFORMATTED));
        layout.addComponent(new Label("myBean: " + this.helloWorld.getMyBean().info(), ContentMode.PREFORMATTED));
        layout.addComponent(new Label("----------", ContentMode.PREFORMATTED));
        layout.addComponent(new Label("myApplicationBean: " + this.helloWorld.getMyApplicationBean().info(),
          ContentMode.PREFORMATTED));
        layout.addComponent(new Label("----------", ContentMode.PREFORMATTED));
        layout.addComponent(new Label("vaadinConfigurableBean: " + this.vaadinConfigurableBean.info(),
          ContentMode.PREFORMATTED));
        layout.addComponent(new Label("----------", ContentMode.PREFORMATTED));
        layout.addComponent(new Button("Close Application", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                HelloRoot.this.log.info("closing HelloWorld application...");
                HelloRoot.this.helloWorld.close();
            }
        }));
        this.setContent(layout);
    }

    public String info() {
        return this.getClass().getSimpleName() + "@" + Integer.toHexString(this.hashCode())
          + "\n  vaadinConfigurableBean=" + this.vaadinConfigurableBean;
    }
}

