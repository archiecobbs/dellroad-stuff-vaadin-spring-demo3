
package com.example;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Sizeable;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import org.apache.log4j.Logger;

@Theme("mytheme")
@SuppressWarnings("serial")
public class HelloUI extends UI {

    protected transient Logger log = Logger.getLogger(this.getClass());

    private static final float UPPER_BAR_HEIGHT = 42;

    private final VerticalLayout rootLayout = new VerticalLayout();
    private final Label subtitleLabel = new Label();
    private final String rootTitle;

    private Component lowerPanel;

    public HelloUI() {
        this.rootTitle = "Vaadin Layout Bug";
        this.getPage().setTitle(this.rootTitle);
    }

    @Override
    public void init(VaadinRequest request) {
        this.buildRootLayout();
        this.setContent(this.rootLayout);
    }

    @Override
    public void attach() {
        super.attach();
        this.rebuildLowerPanel();
    }

    protected void rebuildLowerPanel() {
        this.updateLowerPanel(null);
    }

    private void updateLowerPanel(Object user) {

        // Remove previous content
        if (this.lowerPanel != null)
            this.rootLayout.removeComponent(this.lowerPanel);

        // If not logged in, present login form, otherwise check user's rights and build lower panel
        Panel panel = new Panel("Login");
        panel.setSizeUndefined();
        panel.addComponent(new LoginForm());
        this.lowerPanel = panel;

        // Update layout
        this.rootLayout.addComponent(this.lowerPanel);
        this.rootLayout.setExpandRatio(this.lowerPanel, 1.0f);
        this.rootLayout.setComponentAlignment(this.lowerPanel, Alignment.MIDDLE_CENTER);
    }

    protected Component getRootLowerPanel() {
        return this.lowerPanel;
    }

    protected void buildRootLayout() {
        this.rootLayout.setSpacing(true);
        this.rootLayout.setSizeFull();
        this.rootLayout.setMargin(new MarginInfo(false, true, true, true));
        this.rootLayout.addComponent(this.buildRootUpperBar());
        this.rootLayout.addComponent(this.buildRootSeparator());
        this.rebuildLowerPanel();
    }

    protected Component buildRootUpperBar() {

        // Logo
        Link logo = new Link(null, new ExternalResource("main"));
        logo.setIcon(new ThemeResource("img/logo.png"));
        HorizontalLayout logoLayout = new HorizontalLayout();
        logoLayout.setWidth(LoginStatus.WIDTH, Sizeable.Unit.PIXELS);
        logoLayout.addComponent(logo);
        logoLayout.setComponentAlignment(logo, Alignment.BOTTOM_LEFT);

        // Title
        Label titleLabel = new Label(this.rootTitle);
        titleLabel.addStyleName("pexp-application-title");
        titleLabel.setSizeUndefined();
        titleLabel.setHeight(18, Sizeable.Unit.PIXELS);
        this.subtitleLabel.setSizeUndefined();
        VerticalLayout titleLayout = new VerticalLayout();
        titleLayout.setSizeUndefined();
        titleLayout.addComponent(titleLabel);
        titleLayout.setComponentAlignment(titleLabel, Alignment.BOTTOM_CENTER);
        titleLayout.addComponent(this.subtitleLabel);
        titleLayout.setComponentAlignment(this.subtitleLabel, Alignment.BOTTOM_CENTER);

        // Login status
        LoginStatus loginStatus = new LoginStatus();

        // Sequence parts
        HorizontalLayout layout = new HorizontalLayout();
        layout.addStyleName("pexp-main-upper-bar");
        layout.setSpacing(true);
        layout.setWidth("100%");
        layout.setHeight(UPPER_BAR_HEIGHT, Sizeable.Unit.PIXELS);
        layout.addComponent(logoLayout);
        layout.setComponentAlignment(logoLayout, Alignment.MIDDLE_LEFT);
        layout.addComponent(titleLayout);
        layout.setExpandRatio(titleLayout, 1.0f);
        layout.setComponentAlignment(titleLayout, Alignment.BOTTOM_CENTER);
        layout.addComponent(loginStatus);
        layout.setComponentAlignment(loginStatus, Alignment.BOTTOM_RIGHT);
        return layout;
    }

    protected Component buildRootSeparator() {
        Panel panel = new Panel();
        panel.addStyleName("pexp-separator");
        panel.setWidth("100%");
        panel.setHeight(3.0f, Sizeable.Unit.PIXELS);
        return panel;
    }
}

