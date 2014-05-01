
/*
 * Copyright (C) 2014 Archie L. Cobbs. All rights reserved.
 *
 * $Id$
 */

package com.example;

import com.vaadin.data.Property;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

import org.apache.log4j.Logger;

/**
 * Support superclass for panels showing {@link Table}s backed by containters.
 *
 * @param <T> table type
 * @param <I> container item ID type
 */
@SuppressWarnings("serial")
public abstract class AbstractTablePanel<T extends Table, I> extends VerticalLayout {

    protected Logger log = Logger.getLogger(this.getClass());
    protected final T table;
    protected final Class<I> itemIdType;

    protected AbstractTablePanel(T table, Class<I> itemIdType) {
        this(table, itemIdType, 250);
    }

    protected AbstractTablePanel(T table, Class<I> itemIdType, int heightInPixels) {

        // Sanity check
        if (table == null)
            throw new IllegalArgumentException("null table");
        if (itemIdType == null)
            throw new IllegalArgumentException("null itemIdType");
        this.itemIdType = itemIdType;

        // Initialize layout
        this.setMargin(true);
        this.setSpacing(true);
        this.setHeight("100%");

        // Add table
        this.table = table;
        this.table.setWidth("100%");
        this.table.setHeight(heightInPixels, Sizeable.Unit.PIXELS);
        this.addComponent(this.table);

        // Listen for row selection changes
        this.table.setSelectable(true);
        this.table.setImmediate(true);
        this.table.setNullSelectionAllowed(false);
        this.table.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                AbstractTablePanel.this.updateSelection(AbstractTablePanel.this.itemIdType.cast(event.getProperty().getValue()));
            }
        });
    }

    @Override
    public void attach() {
        super.attach();
        this.updateSelection(this.itemIdType.cast(this.table.getValue()));
    }

    protected void addButtonRow(Button... buttons) {
        final HorizontalLayout buttonRow = new HorizontalLayout();
        buttonRow.setSpacing(true);
        buttonRow.setWidth("100%");
        for (Button button : buttons)
            buttonRow.addComponent(button);
        final Label spacer = new Label();
        buttonRow.addComponent(spacer);
        buttonRow.setExpandRatio(spacer, 1);
        this.addComponent(buttonRow);
        this.setComponentAlignment(buttonRow, Alignment.TOP_LEFT);
    }

    protected abstract void updateSelection(I item);
}

