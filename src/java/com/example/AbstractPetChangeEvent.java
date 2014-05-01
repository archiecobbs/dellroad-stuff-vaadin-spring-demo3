
/*
 * Copyright (C) 2014 Archie L. Cobbs. All rights reserved.
 *
 * $Id$
 */

package com.example;

public abstract class AbstractPetChangeEvent extends AbstractChangeEvent<VPet> {

    protected AbstractPetChangeEvent(VPet pet, boolean structural) {
        super(pet, structural);
    }
}

