
package com.example;

public enum PetType {
    DOG,
    CAT,
    FISH,
    PARAKEET;

    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}

