
package com.example;

import java.util.UUID;

/**
 * Implemented by classes sporting a {@link UUID}.
 */
public interface HasUUID {

    /**
     * Get this instance's globally unique identifier.
     */
    UUID getUUID();
}

