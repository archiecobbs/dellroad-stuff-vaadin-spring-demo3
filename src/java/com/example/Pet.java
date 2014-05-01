
/*
 * Copyright (C) 2014 Archie L. Cobbs. All rights reserved.
 *
 * $Id$
 */

package com.example;

import java.util.Date;
import java.util.NavigableSet;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.jsimpledb.JTransaction;
import org.jsimpledb.annotation.JField;
import org.jsimpledb.annotation.JSimpleClass;
import org.jsimpledb.annotation.OnChange;
import org.jsimpledb.annotation.OnCreate;
import org.jsimpledb.annotation.OnDelete;
import org.jsimpledb.change.FieldChange;
import org.jsimpledb.core.ObjId;

// JSimpleDB database object
@JSimpleClass(storageId = 200)
public abstract class Pet extends AbstractData {

    @JField(storageId = 201)
    @NotNull(message = "Pet must have a name")
    @Pattern(regexp = "[A-Z].*", message = "Pet name must start with a capital letter")
    public abstract String getName();
    public abstract void setName(String name);

    @JField(storageId = 202)
    @NotNull(message = "Pet must have a type")
    public abstract PetType getType();
    public abstract void setType(PetType type);

    @JField(storageId = 203)
    public abstract Date getBirthday();
    public abstract void setBirthday(Date birthday);

    @JField(storageId = 204)
    public abstract boolean isFriendly();
    public abstract void setFriendly(boolean friendly);

    public static Pet create() {
        return JTransaction.getCurrent().create(Pet.class);
    }

    public static Pet get(ObjId id) {
        return JTransaction.getCurrent().getJSimpleDB().getJObject(id, Pet.class);
    }

    public static NavigableSet<Pet> getAll() {
        return JTransaction.getCurrent().getAll(Pet.class);
    }

    public void copyFrom(Pet that) {
        this.setName(that.getName());
        this.setType(that.getType());
        this.setBirthday(that.getBirthday());
        this.setFriendly(that.isFriendly());
    }

// Notify application after committed changes

    @OnChange("name")
    private void onNameChange(FieldChange<Pet> change) {
        this.publishChangeOnCommit(new PetChangeEvent(new VPet(this), "name"));
    }

    @OnChange("type")
    private void onTypeChange(FieldChange<Pet> change) {
        this.publishChangeOnCommit(new PetChangeEvent(new VPet(this), "type"));
    }

    @OnChange("birthday")
    private void onBirthdayChange(FieldChange<Pet> change) {
        this.publishChangeOnCommit(new PetChangeEvent(new VPet(this), "birthday"));
    }

    @OnChange("friendly")
    private void onFriendlyChange(FieldChange<Pet> change) {
        this.publishChangeOnCommit(new PetChangeEvent(new VPet(this), "friendly"));
    }

    @OnCreate
    private void onCreate() {
        this.publishChangeOnCommit(new PetAddEvent(new VPet(this)));
    }

    @OnDelete
    private void onDelete() {
        this.publishChangeOnCommit(new PetDeleteEvent(new VPet(this)));
    }
}

