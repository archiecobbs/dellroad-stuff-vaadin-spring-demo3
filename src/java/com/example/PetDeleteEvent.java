
/*
 * Copyright (C) 2014 Archie L. Cobbs. All rights reserved.
 *
 * $Id$
 */

package com.example;

public class PetDeleteEvent extends AbstractPetChangeEvent {

    public PetDeleteEvent(VPet pet) {
        super(pet, true);
    }
}

