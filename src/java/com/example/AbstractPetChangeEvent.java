
/*
 * Copyright (C) 2014 Archie L. Cobbs. All rights reserved.
 *
 * $Id$
 */

package com.example;

public abstract class AbstractPetChangeEvent extends AbstractChangeEvent<Pet> {

    protected AbstractPetChangeEvent(Pet pet, boolean structural) {
        super(pet, structural);
    }
}

