
/*
 * Copyright (C) 2014 Archie L. Cobbs. All rights reserved.
 *
 * $Id$
 */

package com.example;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;

@SuppressWarnings("serial")
public class PetContainer extends AbstractDataContainer<VPet, PetChangeEvent> {

    public PetContainer() {
        super(VPet.class);
    }

    @Override
    protected Iterable<VPet> getContainerObjects() {
        return Iterables.transform(Pet.getAll(), new Function<Pet, VPet>() {
            @Override
            public VPet apply(Pet pet) {
                return new VPet(pet);
            }
        });
    }
}

