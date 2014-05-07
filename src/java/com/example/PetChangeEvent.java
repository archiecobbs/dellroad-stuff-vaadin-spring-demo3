
/*
 * Copyright (C) 2014 Archie L. Cobbs. All rights reserved.
 *
 * $Id$
 */

package com.example;

public class PetChangeEvent extends AbstractPetChangeEvent {

    private final String field;

    public PetChangeEvent(Pet pet, String field) {
        super(pet, false);
        if (field == null)
            throw new IllegalArgumentException("null field");
        this.field = field;
    }

    public String getField() {
        return this.field;
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj))
            return false;
        final PetChangeEvent that = (PetChangeEvent)obj;
        return this.field.equals(that.field);
    }

    @Override
    public int hashCode() {
        return super.hashCode() ^ this.field.hashCode();
    }
}

