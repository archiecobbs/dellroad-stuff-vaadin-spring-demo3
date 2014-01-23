
package com.example;

import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

import javax.validation.ConstraintViolation;

import org.dellroad.stuff.java.ErrorAction;
import org.dellroad.stuff.jibx.JiBXUtil;
import org.dellroad.stuff.pobj.PersistentObjectTransactionManager;
import org.dellroad.stuff.pobj.PersistentObjectTransactional;
import org.dellroad.stuff.pobj.PersistentObjectValidationException;
import org.dellroad.stuff.vaadin7.FieldBuilder;
import org.dellroad.stuff.vaadin7.VaadinConfigurable;
import org.dellroad.stuff.validation.SelfValidationException;
import org.dellroad.stuff.validation.ValidationContext;
import org.dellroad.stuff.validation.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("serial")
@VaadinConfigurable(ifSessionNotLocked = ErrorAction.EXCEPTION)
public class PetPanel extends AbstractTablePanel<PetTable> {

    private final Button newButton = new Button("New", new Button.ClickListener() {
        @Override
        public void buttonClick(Button.ClickEvent event) {
            PetPanel.this.table.setValue(PetPanel.this.newButtonClicked());
        }
    });
    private final Button copyButton = new Button("Copy", new Button.ClickListener() {
        @Override
        public void buttonClick(Button.ClickEvent event) {
            PetPanel.this.copyButtonClicked((UUID)PetPanel.this.table.getValue());
        }
    });
    private final Button deleteButton = new Button("Delete", new Button.ClickListener() {
        @Override
        public void buttonClick(Button.ClickEvent event) {
            PetPanel.this.deleteButtonClicked((UUID)PetPanel.this.table.getValue());
        }
    });

    private Component displayComponent;

    public PetPanel() {
        super(new PetTable(), 150);

        // Add button row
        this.addButtonRow(this.newButton, this.copyButton, this.deleteButton);

        // Add display component (placeholder)
        this.replaceDisplayComponent(null);
    }

    @Override
    protected void updateSelection(UUID uuid) {

        // Handle de-selection
        if (uuid == null) {

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

        // Add edit form
        this.replaceDisplayComponent(this.buildEditForm(uuid));
    }

    private void replaceDisplayComponent(Component component) {
        if (component == null)
            component = new Label();
        this.replaceComponent(this.displayComponent, component);
        this.displayComponent = component;
        this.setExpandRatio(this.displayComponent, 1);
    }

    @PersistentObjectTransactional(readOnly = true, shared = true)
    private Component buildEditForm(final UUID uuid) {
        final Data data = PersistentObjectTransactionManager.<Data>getCurrent().getRoot();
        final Pet pet = data.getPetByUUID(uuid);
        if (pet == null)
            return null;
        return new PetEditForm(pet);
    }

// New

    @PersistentObjectTransactional
    private UUID newButtonClicked() {
        final Data data = PersistentObjectTransactionManager.<Data>getCurrent().getRoot();
        final Pet pet = new Pet();
        pet.setName("Newly created Pet");
        pet.setType(PetType.values()[0]);
        pet.setBirthday(new Date());
        pet.setFriendly(true);
        data.getPets().add(pet);
        return pet.getUUID();
    }

// Copy

    @PersistentObjectTransactional
    private void copyButtonClicked(UUID uuid) {
        final Data data = PersistentObjectTransactionManager.<Data>getCurrent().getRoot();
        final Pet orig = data.getPetByUUID(uuid);
        if (orig == null)
            return;
        final Pet copy = new Pet();
        copy.setName(orig.getName() + " (Copy)");
        copy.setType(orig.getType());
        copy.setBirthday(orig.getBirthday());
        copy.setFriendly(orig.isFriendly());
        data.getPets().add(copy);
    }

// Delete

    @PersistentObjectTransactional
    private void deleteButtonClicked(final UUID uuid) {
        final Data data = PersistentObjectTransactionManager.<Data>getCurrent().getRoot();
        for (Iterator<Pet> i = data.getPets().iterator(); i.hasNext(); ) {
            final Pet pet = i.next();
            if (pet.getUUID().equals(uuid)) {
                i.remove();
                Notification.show("Removed pet `" + pet.getName() + "'");
                break;
            }
        }
    }

// Edit

    private class PetEditForm extends FormLayout {

        private final Pet pet;
        private final BeanFieldGroup<Pet> fieldGroup;

        PetEditForm(Pet pet) {
            this.pet = pet;

            // Build fields
            this.fieldGroup = FieldBuilder.buildFieldGroup(this.pet);

            // Layout fields
            this.addComponent(this.fieldGroup.getField("name"));
            this.addComponent(this.fieldGroup.getField("type"));
            this.addComponent(this.fieldGroup.getField("birthday"));
            this.addComponent(this.fieldGroup.getField("friendly"));

            // Populate fields
            this.bindTo(pet, false);

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
                    PetEditForm.this.bindTo(PetEditForm.this.pet, false);
                }
            }));
            this.addComponent(buttonLayout);
        }

        private void bindTo(Pet pet, boolean overwrite) {

            // Change data source, optionally preserving fields' current values
            final HashMap<Field<?>, Object> valueMap = new HashMap<Field<?>, Object>();
            if (overwrite) {
                for (Object propertyId : this.fieldGroup.getBoundPropertyIds()) {
                    final Field<?> field = this.fieldGroup.getField(propertyId);
                    valueMap.put(field, field.getValue());
                }
            }

            // Update data source
            this.fieldGroup.setItemDataSource(pet);

            // Optionally re-apply saved values
            if (overwrite) {
                for (Object propertyId : this.fieldGroup.getBoundPropertyIds()) {
                    final Field<?> field = this.fieldGroup.getField(propertyId);
                    this.setFieldValue(field, valueMap.get(field));
                }
            }
        }

        private void commitChanges() {

            // Apply changes
            String[] error;
            try {
                error = this.applyChanges(this.pet.getUUID());
            } catch (PersistentObjectValidationException e) {
                error = new String[] { "Invalid configuration.", ValidationUtil.describe(e.getViolations()) };
            }

            // Report any error
            if (error != null)
                Notification.show(error[0], error[1], Notification.Type.WARNING_MESSAGE);
        }

        @PersistentObjectTransactional
        private String[] applyChanges(UUID uuid) {

            // Find the pet
            final Data data = PersistentObjectTransactionManager.<Data>getCurrent().getRoot();
            final Pet pet = data.getPetByUUID(uuid);
            if (pet == null)
                return new String[] { "Unable to find pet.", "Pet not found" };

            // Apply field values
            this.bindTo(pet, true);

            // Commit changes
            try {
                this.fieldGroup.commit();
            } catch (FieldGroup.CommitException e) {
                return new String[] { "Invalid configuration.", e.getMessage() };
            }

            // Done
            return null;
        }

        // This method exists solely to bind the generic type parameters
        private <T> void setFieldValue(Field<T> field, Object value) {
            field.setValue(field.getType().cast(value));
        }
    }
}

