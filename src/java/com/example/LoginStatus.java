
package com.example;

import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.themes.BaseTheme;

@SuppressWarnings("serial")
public class LoginStatus extends HorizontalLayout {

    public static final float WIDTH = 240;
    public static final float BOTTOM_LAYOUT_HEIGHT = 25;

    public LoginStatus() {
        this.setSpacing(true);
        this.setSizeUndefined();
        this.setWidth(WIDTH, Sizeable.Unit.PIXELS);
        this.setHeight("100%");
        this.setMargin(false);
    }

    @Override
    public void attach() {
        super.attach();
        this.updateLoginStatus(null, false);
    }

    private void updateLoginStatus(Object user, boolean pending) {
        this.removeAllComponents();
        Label label = new Label("Not logged in.");
        label.setSizeUndefined();
        this.addComponent(label);
        this.setComponentAlignment(label, Alignment.MIDDLE_RIGHT);
    }
}

