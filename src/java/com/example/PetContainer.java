
package com.example;

import java.util.List;

import javax.validation.constraints.NotNull;

@SuppressWarnings("serial")
public class PetContainer extends AbstractDataContainer<Pet> {

    public PetContainer() {
        super(Pet.class);
    }

    @NotNull
    public List<Pet> getContainerObjects(Data data) {
        return data.getPets();
    }
}

