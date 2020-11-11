package net.cloudlite.core.commands;

import net.cloudlite.core.Core;
import net.cloudlite.core.api.addon.Addon;
import net.cloudlite.core.api.commandapi.CloudliteCommand;
import net.cloudlite.core.utils.holders.AddonsInventoryHolder;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static net.cloudlite.core.api.StringUtils.textOfRaw;

public final class AddonsCommand extends CloudliteCommand {

    private final Core core;

    public AddonsCommand(@Nonnull final Core core) {
        super("addons",
                "Command for managing addons",
                "/addons");
        this.core = core;
        final ArrayList<String> aliases = new ArrayList<>();
        aliases.add("addon");
        this.setAliases(aliases);
    }

    @Override
    public void onCommand(@Nonnull ConsoleCommandSender console, @Nonnull List<String> args) {
        if (args.size() == 0) {
            this.sendConsoleNotAllowed(console);
        }
    }

    @Override
    public void onCommand(@Nonnull Player player, @Nonnull List<String> args) {
        if (args.size() == 0) {

        }
    }

    /** Gets the addons invetory */
    private Inventory getAddonsInventory() {
        final Collection<Addon> addons = this.core.getAddonManager().getAddons();
        final Inventory inventory = Bukkit.createInventory(new AddonsInventoryHolder(), 54, textOfRaw("&b&lADDONS"));
        addons.forEach(addon -> {
            final ItemStack stack = new ItemStack(addon.getMaterial(), 1);
            final ItemMeta meta = stack.getItemMeta();

            meta.setDisplayName(textOfRaw("&e" + addon.getAddonName() + " &cv&b" + addon.getVersion()));
            final List<String> lore = new ArrayList<>();
            lore.add(textOfRaw("&f"));
            if (addon.isEnabled()) {
                lore.add(textOfRaw("&7&lAddon Status: &a&lENABLED"));
            } else lore.add(textOfRaw("&7&lAddon Status: &c&lDISABLED"));
            meta.setLore(lore);

            stack.setItemMeta(meta);
            inventory.addItem(stack);
        });
        return inventory;
    }
}
