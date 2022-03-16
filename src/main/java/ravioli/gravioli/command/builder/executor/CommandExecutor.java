package ravioli.gravioli.command.builder.executor;

import ravioli.gravioli.command.builder.CommandContext;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface CommandExecutor {
    void accept(@NotNull final CommandSender commandSender, @NotNull final CommandContext commandContext);
}
