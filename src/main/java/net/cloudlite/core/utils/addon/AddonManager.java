package net.cloudlite.core.utils.addon;

import net.cloudlite.core.Core;
import net.cloudlite.core.api.addon.Addon;
import net.cloudlite.core.utils.exceptions.AddonAlreadyLoadedException;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.HashMap;

public final class AddonManager {

    private final Core core;
    private final HashMap<String, Addon> addons;

    public AddonManager(@Nonnull final Core core) {
        this.core = core;
        this.addons = new HashMap<>();
    }

    /** Gets the amount of Addon's loaded into the HashMap */
    public final int totalLoadedAddons() {
        return this.addons.size();
    }

    /** Gets a collection of loaded addons */
    public final Collection<Addon> getAddons() {
        return addons.values();
    }

    /** Adds an addon to the HashMap of addons
     * and logs to console */
    public void addAddon(@Nonnull final Addon addon) {
        final String addonNameKey = addon.getAddonName().toUpperCase();
        if (this.addons.get(addonNameKey) != null) throw new AddonAlreadyLoadedException("Addon has already been initilized! (Do 2 of the same addon exist in /addons ?)");

        this.addons.putIfAbsent(addonNameKey, addon);
        this.core.getLog().log("&b " + addon.getAddonName() + "&c v&e" + addon.getVersion() + "&7 Has been initialized!");
    }
}
