
/*
 * Copyright (C) 2014 Archie L. Cobbs. All rights reserved.
 *
 * $Id$
 */

package com.example;

public class PetAddEvent extends AbstractPetChangeEvent {

    public PetAddEvent(VPet pet) {
        super(pet, true);
    }
}

