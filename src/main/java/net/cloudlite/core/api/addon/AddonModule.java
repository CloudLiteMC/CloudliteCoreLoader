package net.cloudlite.core.api.addon;

public abstract class AddonModule {

    /**
     * Gets called when a module is first loaded
     */
    public abstract AddonModule init();

    /**
     * Gets called when a module is first loaded
     */
    public abstract void reload();
}
