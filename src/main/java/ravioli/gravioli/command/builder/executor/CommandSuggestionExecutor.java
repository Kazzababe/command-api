package ravioli.gravioli.command.builder.executor;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@FunctionalInterface
public interface CommandSuggestionExecutor {
    static @NotNull List<String> Player(@NotNull final CommandSender sender, @NotNull String[] args) {
        final String input = args[args.length - 1].toLowerCase();

        return Bukkit.getOnlinePlayers()
            .stream()
            .map(Player::getName)
            .filter((name) -> name.toLowerCase().startsWith(input))
            .collect(Collectors.toList());
    }

    static @NotNull <T extends Enum<T>> List<String> Enum(@NotNull final CommandSender sender,
                                                          @NotNull final String[] args,
                                                          @NotNull Class<T> enumClass) {
        final String input = args[args.length - 1].toLowerCase();

        return Arrays.stream(enumClass.getEnumConstants())
            .map(Enum::name)
            .filter((name) -> name.toLowerCase().startsWith(input))
            .collect(Collectors.toList());
    }

    static @NotNull List<String> StaticGroup(@NotNull final CommandSender sender, @NotNull final String[] args,
                                             @NotNull final String... words) {
        final String input = args[args.length - 1].toLowerCase();

        return Arrays.stream(words)
            .filter((word) -> word.toLowerCase().startsWith(input))
            .collect(Collectors.toList());
    }

    @NotNull List<String> run(@NotNull CommandSender commandSender, @NotNull String[] args);
}
