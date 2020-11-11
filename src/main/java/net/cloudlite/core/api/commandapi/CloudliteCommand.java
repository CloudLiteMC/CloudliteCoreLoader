package net.cloudlite.core.api.commandapi;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.cloudlite.core.api.StringUtils.textOfRaw;

public abstract class CloudliteCommand extends BukkitCommand {

    protected CloudliteCommand(@Nonnull final String command,
                            @Nonnull final String desc,
                            @Nonnull final String usage) {
        super(command);
        this.description = desc;
        this.usageMessage = textOfRaw("&c&lIncorrect usage! &e" + usage);
    }

    @Override
    public boolean execute(@Nonnull final CommandSender sender, @Nonnull final String commandLabel, @Nonnull final String[] args) {
        final List<String> newArgs = new ArrayList<>(Arrays.asList(args));
        if (sender instanceof Player) {
            final Player p = ((Player) sender);
            this.onCommand(p, newArgs);
        } else {
            final ConsoleCommandSender c = ((ConsoleCommandSender) sender);
            this.onCommand(c, newArgs);
        }
        return true;
    }

    public abstract void onCommand(@Nonnull final ConsoleCommandSender console, @Nonnull final List<String> args);

    public abstract void onCommand(@Nonnull final Player player, @Nonnull final List<String> args);

    public void sendConsoleNotAllowed(@Nonnull final ConsoleCommandSender sender) {
        sender.sendMessage(textOfRaw("&cConsole is not allowed to execute this command!"));
    }
}
