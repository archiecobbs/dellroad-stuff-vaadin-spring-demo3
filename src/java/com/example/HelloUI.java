
package com.example;

import com.vaadin.annotations.Push;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import org.apache.log4j.Logger;
import org.dellroad.stuff.vaadin7.VaadinConfigurable;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ConfigurableWebApplicationContext;

@Push(transport = Transport.STREAMING)
@SuppressWarnings("serial")
@VaadinConfigurable
public class HelloUI extends UI {

    protected transient Logger log = Logger.getLogger(this.getClass());

    @Autowired
    private HelloWorld helloWorld;

    private VaadinConfigurableBean vaadinConfigurableBean;

    public HelloUI() {
        this.log.info(this.getClass().getSimpleName() + " constructor invoked");
        this.getPage().setTitle("Vaadin+Spring Demo #3");
    }

    @Override
    public void init(VaadinRequest vaadinRequest) {
        this.log.info("initializing HelloUI...");

        // Example of creating a @VaadinConfigurable bean
        this.log.info(this.getClass().getSimpleName() + " invoking new VaadinConfigurableBean()");
        this.vaadinConfigurableBean = new VaadinConfigurableBean();  // bean is @VaadinConfigurable, so the aspect will autowire it

        // Build layout
        final VerticalLayout layout = new VerticalLayout();
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
          + " will be invoked (same as for Spring's <code>@Configurable</code>); you may see a resulting warning in the logs.</li>"
          + "<li>The <code>helloWorld</code> bean is the <code>com.example.HelloWorld</code> is a Vaadin application singleton"
          + " that is configured and autowired by the <code>HelloWorld.xml</code> context normally like any other Spring bean.</li>"
          + "<li>The <code>helloUI</code> bean is the <code>com.example.HelloUI</code> Vaadin UI instance,"
          + " also configured and autowired into the <code>HelloWorld.xml</code> application via <code>@VaadinConfigurable</code>."
          + " Note how reloading the browser refreshes this bean, but not the <code>helloWorld</code> bean.</li>"
          + "<li>View the servlet container log file to see the order of bean initialization, etc. Try closing"
          + " and re-opening the browser Window, reloading the WAR, etc. to see bean and application context lifecycles.</li>"
          + "</ul>",
          ContentMode.HTML));
        layout.addComponent(new Label("----------", ContentMode.PREFORMATTED));
        layout.addComponent(new Label("helloWorld: " + this.helloWorld.info(), ContentMode.PREFORMATTED));
        layout.addComponent(new Label("----------", ContentMode.PREFORMATTED));
        layout.addComponent(new Label("helloUI: " + this.info(), ContentMode.PREFORMATTED));
        layout.addComponent(new Label("----------", ContentMode.PREFORMATTED));
        layout.addComponent(new Label("myBean: " + this.helloWorld.getMyBean().info(), ContentMode.PREFORMATTED));
        layout.addComponent(new Label("----------", ContentMode.PREFORMATTED));
        layout.addComponent(new Label("myApplicationBean: " + this.helloWorld.getMyApplicationBean().info(),
          ContentMode.PREFORMATTED));
        layout.addComponent(new Label("----------", ContentMode.PREFORMATTED));
        layout.addComponent(new Label("vaadinConfigurableBean: " + this.vaadinConfigurableBean.info(),
          ContentMode.PREFORMATTED));
        layout.addComponent(new Label("----------", ContentMode.PREFORMATTED));
        layout.addComponent(new Link("Restart Application", new ExternalResource("?restartApplication")));
        layout.addComponent(new Link("Close Application", new ExternalResource("?closeApplication")));
        layout.addComponent(new Button("Show Persistent Object Demo", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                layout.removeAllComponents();
                layout.addComponent(new PetPanel());
            }
        }));
        this.setContent(layout);
    }

    public String info() {
        return this.getClass().getSimpleName() + "@" + Integer.toHexString(this.hashCode())
          + "\n  vaadinConfigurableBean=" + this.vaadinConfigurableBean;
    }
}

