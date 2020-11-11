package net.cloudlite.core.utils.exceptions;

import javax.annotation.Nonnull;

public final class AddonAlreadyLoadedException extends RuntimeException {
    public AddonAlreadyLoadedException(@Nonnull final String msg) {
        super(msg);
    }
}
