
/*
 * Copyright (C) 2014 Archie L. Cobbs. All rights reserved.
 *
 * $Id$
 */

package com.example;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;

import java.util.Date;
import java.util.HashMap;

import org.dellroad.stuff.java.ErrorAction;
import org.dellroad.stuff.vaadin7.FieldBuilder;
import org.dellroad.stuff.vaadin7.VaadinConfigurable;
import org.dellroad.stuff.vaadin7.VaadinUtil;
import org.dellroad.stuff.validation.ValidationUtil;
import org.jsimpledb.JTransaction;
import org.jsimpledb.ValidationException;
import org.jsimpledb.core.ObjId;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("serial")
@VaadinConfigurable(ifSessionNotLocked = ErrorAction.EXCEPTION)
public class PetPanel extends AbstractTablePanel<PetTable, ObjId> {

    private final Button newButton = new Button("New", new Button.ClickListener() {
        @Override
        public void buttonClick(Button.ClickEvent event) {
            PetPanel.this.newButtonClicked();
        }
    });
    private final Button copyButton = new Button("Copy", new Button.ClickListener() {
        @Override
        public void buttonClick(Button.ClickEvent event) {
            PetPanel.this.copyButtonClicked((ObjId)PetPanel.this.table.getValue());
        }
    });
    private final Button deleteButton = new Button("Delete", new Button.ClickListener() {
        @Override
        public void buttonClick(Button.ClickEvent event) {
            PetPanel.this.deleteButtonClicked((ObjId)PetPanel.this.table.getValue());
        }
    });

    private Component displayComponent;

    public PetPanel() {
        super(new PetTable(), ObjId.class, 150);
        this.log.info("PetPanel() started");

        // Add button row
        this.addButtonRow(this.newButton, this.copyButton, this.deleteButton);

        // Add display component (placeholder)
        this.replaceDisplayComponent(null);
        this.log.info("PetPanel() done");
    }

    @Override
    public void attach() {
        this.log.info("PetPanel attaching");
        try {
            super.attach();
        } catch (Error e) {
            this.log.error("attach failed", e);
            throw e;
        } catch (RuntimeException e) {
            this.log.error("attach failed", e);
            throw e;
        }
        this.log.info("PetPanel attached");
    }

    @Override
    public void detach() {
        this.log.info("PetPanel detaching");
        super.detach();
        this.log.info("PetPanel detached");
    }

    @Override
    protected void updateSelection(ObjId id) {
        this.log.info("PetPanel update selection: " + id);

        // Handle de-selection
        if (id == null) {

            // Update buttons
            this.copyButton.setEnabled(false);
            this.deleteButton.setEnabled(false);

            // Remove edit form
            this.replaceDisplayComponent(null);
            return;
        }

        // Update buttons
        this.copyButton.setEnabled(true);
        this.deleteButton.setEnabled(true);

        // Get VPet from Container
        final VPet vpet = this.table.getContainer().getItem(id).getObject();

        // Add edit form
        this.replaceDisplayComponent(this.buildEditForm(vpet));
    }

    private void replaceDisplayComponent(Component component) {
        if (component == null)
            component = new Label();
        this.replaceComponent(this.displayComponent, component);
        this.displayComponent = component;
        this.setExpandRatio(this.displayComponent, 1);
    }

    private Component buildEditForm(VPet pet) {
        return new PetEditForm(pet);
    }

// New

    @Transactional
    private void newButtonClicked() {
        final Pet pet = Pet.create();
        pet.setName("Newly created Pet");
        pet.setType(PetType.values()[0]);
        pet.setBirthday(new Date());
        pet.setFriendly(true);
        this.log.info("created " + pet.getObjId());
        this.editLater(pet.getObjId());
    }

// Copy

    @Transactional
    private void copyButtonClicked(ObjId id) {
        this.log.info("copy " + id);
        final Pet pet = Pet.get(id);
        if (!pet.exists()) {
            this.log.info("copy pet " + pet + " does not exist");
            return;
        }
        final Pet copy = Pet.create();
        copy.setName(pet.getName() + " (Copy)");
        copy.setType(pet.getType());
        copy.setBirthday(pet.getBirthday());
        copy.setFriendly(pet.isFriendly());
        this.log.info("copied " + id + " to " + copy.getObjId());
        this.editLater(copy.getObjId());
    }

// Delete

    @Transactional
    private void deleteButtonClicked(ObjId id) {
        this.log.info("delete " + id);
        final Pet pet = Pet.get(id);
        if (!pet.exists()) {
            this.log.info("delete pet " + pet + " does not exist");
            return;
        }
        Notification.show("Removed pet `" + pet.getName() + "'");
        pet.delete();
        this.log.info("deleted " + id);
        this.editLater(null);
    }

// Edit

    private void editLater(final ObjId id) {
        VaadinUtil.invokeLater(VaadinUtil.getCurrentSession(), new Runnable() {
            @Override
            public void run() {
                PetPanel.this.table.setValue(id);
            }
        });
    }

    private class PetEditForm extends FormLayout {

        private final VPet vpet;
        private final BeanFieldGroup<VPet> fieldGroup;

        PetEditForm(VPet vpet) {
            this.vpet = vpet;
            this.fieldGroup = FieldBuilder.buildFieldGroup(this.vpet);

            // Layout fields
            this.addComponent(this.fieldGroup.getField("name"));
            this.addComponent(this.fieldGroup.getField("type"));
            this.addComponent(this.fieldGroup.getField("birthday"));
            this.addComponent(this.fieldGroup.getField("friendly"));

            // Populate fields
            this.rebind(false);

            // Add buttons
            final HorizontalLayout buttonLayout = new HorizontalLayout();
            buttonLayout.setSpacing(true);
            buttonLayout.addComponent(new Button("Save", new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    PetEditForm.this.commitChanges();
                }
            }));
            buttonLayout.addComponent(new Button("Cancel", new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    PetEditForm.this.rebind(false);
                }
            }));
            this.addComponent(buttonLayout);
        }

        private void rebind(boolean overwrite) {

            // Change data source, optionally preserving fields' current values
            final HashMap<Field<?>, Object> valueMap = new HashMap<Field<?>, Object>();
            if (overwrite) {
                for (Object propertyId : this.fieldGroup.getBoundPropertyIds()) {
                    final Field<?> field = this.fieldGroup.getField(propertyId);
                    valueMap.put(field, field.getValue());
                }
            }

            // Update data source
            this.fieldGroup.setItemDataSource(this.vpet);

            // Optionally re-apply saved values
            if (overwrite) {
                for (Object propertyId : this.fieldGroup.getBoundPropertyIds()) {
                    final Field<?> field = this.fieldGroup.getField(propertyId);
                    this.setFieldValue(field, valueMap.get(field));
                }
            }
        }

        // This method exists solely to bind the generic type parameters
        private <T> void setFieldValue(Field<T> field, Object value) {
            field.setValue(field.getType().cast(value));
        }

        @Transactional
        private void commitChanges() {

            // Apply field values
            PetPanel.this.log.info("applying changes to pet " + this.vpet.getObjId());
            this.rebind(true);
            try {
                this.fieldGroup.commit();
            } catch (FieldGroup.CommitException e) {
                Notification.show("Invalid configuration.", e.getMessage(), Notification.Type.WARNING_MESSAGE);
                return;
            }

            // Find pet in database
            final Pet pet = this.vpet.getPet();
            if (pet == null) {
                PetPanel.this.log.info("pet " + this.vpet.getObjId() + " was deleted before we could apply changes");
                Notification.show("Pet has been deleted!", Notification.Type.WARNING_MESSAGE);
                return;
            }

            // Update pet
            pet.copyFrom(this.vpet);

            // Validate database
            try {
                JTransaction.getCurrent().validate();
            } catch (ValidationException e) {
                Notification.show("Invalid configuration.",
                  ValidationUtil.describe(e.getViolations()), Notification.Type.WARNING_MESSAGE);
                JTransaction.getCurrent().getTransaction().setRollbackOnly();
            }
        }
    }
}

