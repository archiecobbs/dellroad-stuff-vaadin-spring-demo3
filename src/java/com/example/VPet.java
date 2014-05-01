
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

import org.dellroad.stuff.vaadin7.FieldBuilder;
import org.dellroad.stuff.vaadin7.ProvidesProperty;
import org.dellroad.stuff.vaadin7.ProvidesPropertySort;
import org.jsimpledb.core.ObjId;

/*

  Notes:

  - The @ProvidesProperty and @ProvidesProperty annotations are used to build a Container
    whose items reflect instances of this class; see PetContainer.

  - The @FieldBuilder annotations is used to build Field objects for editing instances of this class; see PetPanel.

*/

// Vaadin GUI Pet object
public class VPet extends Pet implements VObject<Pet> {

    private final ObjId id;

    private String name;
    private PetType type;
    private Date birthday;
    private boolean friendly;

    public VPet(Pet pet) {
        this.id = pet.getObjId();
        this.copyFrom(pet);
    }

    // Must be running within a transaction
    public Pet getPet() {
        return Pet.get(this.id);
    }

    @Override
    public ObjId getObjId() {
        return this.id;
    }

    @FieldBuilder.AbstractField(caption = "Name:", width = "200px")
    @FieldBuilder.AbstractTextField(nullRepresentation = "", nullSettingAllowed = true)
    @Override
    @ProvidesProperty
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @FieldBuilder.AbstractField(caption = "Type:")
    @FieldBuilder.EnumComboBox
    @Override
    @ProvidesProperty
    public PetType getType() {
        return this.type;
    }

    @Override
    public void setType(PetType type) {
        this.type = type;
    }

    @FieldBuilder.AbstractField(caption = "Birthday:")
    @FieldBuilder.DateField(resolution = Resolution.DAY)
    @Override
    @ProvidesPropertySort
    public Date getBirthday() {
        return this.birthday;
    }

    @Override
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @FieldBuilder.AbstractField(caption = "Pet is friendly")
    @FieldBuilder.CheckBox
    @Override
    public boolean isFriendly() {
        return this.friendly;
    }

    @Override
    public void setFriendly(boolean friendly) {
        this.friendly = friendly;
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

