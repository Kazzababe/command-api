package ravioli.gravioli.command;

import ravioli.gravioli.command.builder.Command;
import ravioli.gravioli.command.builder.CommandWrapper;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public class CommandAPI {
    public static void registerCommand(@NotNull final Command command) {
        Bukkit.getCommandMap().register(command.getRootCommand(), new CommandWrapper(command));
    }

    public static void unregisterCommand(@NotNull final Command command) {
        Bukkit.getCommandMap().getKnownCommands().remove(command.getRootCommand());

        for (final String alias : command.getCommandAliases()) {
            Bukkit.getCommandMap().getKnownCommands().remove(alias);
        }
    }
}
