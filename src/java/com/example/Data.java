
package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.validation.constraints.NotNull;

public class Data {

    private List<Pet> pets = new ArrayList<Pet>();

    @NotNull
    public List<Pet> getPets() {
        return this.pets;
    }
    public void setPets(List<Pet> pets) {
        this.pets = pets;
    }

    public Pet getPetByUUID(UUID uuid) {
        for (Pet pet : this.pets) {
            if (pet.getUUID().equals(uuid))
                return pet;
        }
        return null;
    }
}

