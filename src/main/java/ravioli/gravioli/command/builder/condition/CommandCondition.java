package ravioli.gravioli.command.builder.condition;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface CommandCondition {
    boolean canUse(@NotNull CommandSender sender, @Nullable String commandString);
}
