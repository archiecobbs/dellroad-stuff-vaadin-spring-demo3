
package com.example;

import com.vaadin.server.Sizeable;
import com.vaadin.server.UserError;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;

@SuppressWarnings("serial")
public class LoginForm extends FormLayout {

    private final TextField usernameField = new TextField("Username:");
    private final PasswordField passwordField = new PasswordField("Password:");

    public LoginForm() {
        this.setWidth(225, Sizeable.Unit.PIXELS);
        this.setHeight(130, Sizeable.Unit.PIXELS);
        this.addComponent(this.usernameField);
        this.addComponent(this.passwordField);
        this.addComponent(new Button("Login", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                // todo
            }
        }));
    }
}

