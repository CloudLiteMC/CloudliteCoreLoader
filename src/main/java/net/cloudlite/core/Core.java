package net.cloudlite.core;

import net.cloudlite.core.api.Logger;
import net.cloudlite.core.api.commandapi.CloudliteCommand;
import net.cloudlite.core.commands.AddonsCommand;
import net.cloudlite.core.utils.addon.AddonLoader;
import net.cloudlite.core.utils.addon.AddonManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;

public final class Core extends JavaPlugin {

    private Core core;
    private final Logger log = new Logger("&b&lCLOUDLITE-LOADER");

    private AddonManager addonManager;
    private AddonLoader addonLoader;

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.core = this;
        this.addonManager = new AddonManager(this.core);
        this.log.log("&aLoading up CloudLite CoreLoader!");
        this.registerCommand("addons", new AddonsCommand(this));

        new BukkitRunnable() {
            @Override
            public void run() {
                addonLoader = new AddonLoader(core);
                log.log("&aA total of &b" + addonManager.totalLoadedAddons() + "&a have been loaded!");
            }
        }.runTaskLater(this, 5);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    /** Gets the logger instance */
    public final Logger getLog() {
        return this.log;
    }

    /**
     * Enables a command from the plugin.yml
     * @param commandName name of the command
     * @param command PrisonCommand command to load
     */
    public void registerCommand(@Nonnull final String commandName, @Nonnull final CloudliteCommand command) {
        try {
            final Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);

            final CommandMap commandMap = ((CommandMap) commandMapField.get(Bukkit.getServer()));
            commandMap.register(commandName, command);
        } catch (@Nonnull final IllegalAccessException | NoSuchFieldException ex) {
            ex.printStackTrace();
        }
    }

    /** gets the AddonManager instance */
    public final AddonManager getAddonManager() {
        return this.addonManager;
    }
}
