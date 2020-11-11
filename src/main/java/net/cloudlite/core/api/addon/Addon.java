package net.cloudlite.core.api.addon;

import org.bukkit.Material;

import javax.annotation.Nonnull;

public abstract class Addon {

    private boolean enabled;
    private String addonName;
    private String version;
    private Material material;

    public abstract void onEnable();

    public abstract void onDisable();

    public abstract void onReload();

    public final boolean isEnabled() {
        return enabled;
    }

    public String getAddonName() {
        return this.addonName;
    }

    public String getVersion() {
        return this.version;
    }

    public final Material getMaterial() {
        return this.material;
    }

    public void setAddonName(@Nonnull final String addonName) {
        this.addonName = addonName;
    }

    public void setVersion(@Nonnull final String version) {
        this.version = version;
    }

    public void setMaterial(@Nonnull final Material mat) {
        this.material = mat;
    }

    public void setEnabled(final boolean value) {
        this.enabled = value;
    }
}
