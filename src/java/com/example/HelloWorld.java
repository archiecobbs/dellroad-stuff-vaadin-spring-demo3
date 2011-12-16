
package com.example;

import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;

import org.dellroad.stuff.vaadin.ContextApplication;
import org.dellroad.stuff.vaadin.SpringContextApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ConfigurableWebApplicationContext;

@SuppressWarnings("serial")
public class HelloWorld extends SpringContextApplication {

    @Autowired
    private MyBean myBean;

    @Autowired
    private MyApplicationBean myApplicationBean;

    @Override
    public void initSpringApplication(ConfigurableWebApplicationContext context) {
        this.log.info("initializing HelloWorld application...");
        Window mainWindow = new Window("Vaadin+Spring Demo #3");
        mainWindow.addComponent(new Label(
          "Notes: the bean \"myBean\" bean lives in the Spring application context that is associated with"
          + " the overall servlet context, that is, the Spring application context created by Spring's"
          + " ContextLoaderListener. It will not change until you reload the overall web application."
          + " The \"myApplicationBean\" is a bean in the Spring application context that is automatically"
          + " created along with each new Vaadin application instance (defined by \"HelloWorld.xml\")."
          + " It will not change if you just reload your browser, but if hit the \"Close Application\""
          + " URL below, then it will change. Similarly for \"vaadinConfigurableBean\", which is a bean that is"
          + " configured into the Spring application implicitly using AspectJ and the @VaadinConfigurable"
          + " annotation (rather than explictly in \"HelloWorld.xml\" as is \"myApplicationBean\")."));
        mainWindow.addComponent(new Label("----------", Label.CONTENT_PREFORMATTED));
        mainWindow.addComponent(new Label("myBean: " + this.myBean.info(), Label.CONTENT_PREFORMATTED));
        mainWindow.addComponent(new Label("----------", Label.CONTENT_PREFORMATTED));
        mainWindow.addComponent(new Label("myApplicationBean: " + this.myApplicationBean.info(), Label.CONTENT_PREFORMATTED));
        mainWindow.addComponent(new Label("----------", Label.CONTENT_PREFORMATTED));
        mainWindow.addComponent(new Label("vaadinConfigurableBean: " + this.myApplicationBean.getVaadinConfigurableBean().info(),
          Label.CONTENT_PREFORMATTED));
        mainWindow.addComponent(new Label("----------", Label.CONTENT_PREFORMATTED));
        mainWindow.addComponent(new Button("Close Application", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                HelloWorld.this.log.info("closing HelloWorld application...");
                HelloWorld.get().close();
            }
        }));
        this.setMainWindow(mainWindow);
    }

    public static HelloWorld get() {
        return ContextApplication.get(HelloWorld.class);
    }
}

