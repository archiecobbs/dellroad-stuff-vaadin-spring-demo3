
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
        mainWindow.addComponent(new Label("myBean: " + this.myBean.info(), Label.CONTENT_PREFORMATTED));
        mainWindow.addComponent(new Label("----------", Label.CONTENT_PREFORMATTED));
        mainWindow.addComponent(new Label("myApplicationBean: " + this.myApplicationBean.info(), Label.CONTENT_PREFORMATTED));
        mainWindow.addComponent(new Label("----------", Label.CONTENT_PREFORMATTED));
        mainWindow.addComponent(new Label("configurableBean: " + this.myApplicationBean.getConfigurableBean().info(),
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

