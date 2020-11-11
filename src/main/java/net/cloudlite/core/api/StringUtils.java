package net.cloudlite.core.api;

import org.bukkit.ChatColor;

import javax.annotation.Nonnull;

public final class StringUtils {

    public static String textOfRaw(@Nonnull final String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
