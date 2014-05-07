
/*
 * Copyright (C) 2014 Archie L. Cobbs. All rights reserved.
 *
 * $Id$
 */

package com.example;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;

@SuppressWarnings("serial")
public class PetContainer extends AbstractDataContainer<Pet, PetChangeEvent> {

    public PetContainer() {
        super(Pet.class);
    }

    @Override
    protected Iterable<Pet> getContainerObjects() {
        return Iterables.transform(Pet.getAll(), new Function<Pet, Pet>() {
            @Override
            public Pet apply(Pet pet) {
                return (Pet)pet.copyOut();
            }
        });
    }
}

