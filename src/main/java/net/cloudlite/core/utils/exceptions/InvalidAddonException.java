package net.cloudlite.core.utils.exceptions;

import javax.annotation.Nonnull;

public final class InvalidAddonException extends RuntimeException {
    public InvalidAddonException(@Nonnull final String msg) {
        super(msg);
    }
}
