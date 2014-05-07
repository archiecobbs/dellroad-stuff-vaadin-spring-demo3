
/*
 * Copyright (C) 2014 Archie L. Cobbs. All rights reserved.
 *
 * $Id$
 */

package com.example;

import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.NavigableSet;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.dellroad.stuff.vaadin7.FieldBuilder;
import org.dellroad.stuff.vaadin7.ProvidesProperty;
import org.dellroad.stuff.vaadin7.ProvidesPropertySort;
import org.jsimpledb.JTransaction;
import org.jsimpledb.annotation.JField;
import org.jsimpledb.annotation.JSimpleClass;
import org.jsimpledb.annotation.OnChange;
import org.jsimpledb.annotation.OnCreate;
import org.jsimpledb.annotation.OnDelete;
import org.jsimpledb.change.FieldChange;
import org.jsimpledb.core.ObjId;

/*
  Vaadin Notes:
  - The @ProvidesProperty and @ProvidesProperty annotations are used to build a Container
    whose items reflect instances of this class; see PetContainer.
  - The @FieldBuilder annotations is used to build Field objects for editing instances of this class; see PetPanel.
*/

// JSimpleDB database object
@JSimpleClass(storageId = 200)
public abstract class Pet extends AbstractData {

    @FieldBuilder.AbstractField(caption = "Name:", width = "200px")
    @FieldBuilder.AbstractTextField(nullRepresentation = "", nullSettingAllowed = true)
    @JField(storageId = 201)
    @NotNull(message = "Pet must have a name")
    @Pattern(regexp = "[A-Z].*", message = "Pet name must start with a capital letter")
    @ProvidesProperty
    public abstract String getName();
    public abstract void setName(String name);

    @FieldBuilder.AbstractField(caption = "Type:")
    @FieldBuilder.EnumComboBox
    @JField(storageId = 202)
    @NotNull(message = "Pet must have a type")
    @ProvidesProperty
    public abstract PetType getType();
    public abstract void setType(PetType type);

    @FieldBuilder.AbstractField(caption = "Birthday:")
    @FieldBuilder.DateField(resolution = Resolution.DAY)
    @JField(storageId = 203)
    @ProvidesPropertySort
    public abstract Date getBirthday();
    public abstract void setBirthday(Date birthday);

    @FieldBuilder.AbstractField(caption = "Pet is friendly")
    @FieldBuilder.CheckBox
    @JField(storageId = 204)
    public abstract boolean isFriendly();
    public abstract void setFriendly(boolean friendly);

    public static Pet create() {
        return JTransaction.getCurrent().create(Pet.class);
    }

    public static Pet get(ObjId id) {
        return JTransaction.getCurrent().getJObject(id, Pet.class);
    }

    public static NavigableSet<Pet> getAll() {
        return JTransaction.getCurrent().getAll(Pet.class);
    }

// Notify application after committed changes

    @OnChange
    private void onNameChange(FieldChange<Pet> change) {
        this.publishChangeOnCommit(new PetChangeEvent(this, change.getFieldName()));
    }

    @OnCreate
    private void onCreate() {
        this.publishChangeOnCommit(new PetAddEvent(this));
    }

    @OnDelete
    private void onDelete() {
        this.publishChangeOnCommit(new PetDeleteEvent(this));
    }

// Additional Vaadin property configuration

    @ProvidesProperty("objId")
    public Label propertyObjId() {
        return new Label("<code>" + this.getObjId() + "</code>", ContentMode.HTML);
    }

    @ProvidesProperty("birthday")
    private String propertyBirthday() {
        if (this.getBirthday() == null)
            return "Unknown";
        return new SimpleDateFormat("yyyy-MM-dd").format(this.getBirthday());
    }

    @ProvidesProperty("friendly")
    private String propertyFriendly() {
        return this.isFriendly() ? "Yes" : "No";
    }
}

