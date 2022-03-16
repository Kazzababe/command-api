package ravioli.gravioli.command.argument;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class ArgumentPlayer extends Argument<Player> {
    public ArgumentPlayer(@NotNull final String id) {
        super(id);

        this.setCommandSuggestionExecutor(this::getArgumentSuggestions);
    }

    @Override
    public @NotNull Player parse(@NotNull final String input) throws IllegalArgumentException {
        final Player player = Bukkit.getPlayer(input);

        if (player == null) {
            throw new IllegalArgumentException("No player found.");
        }
        return player;
    }

    @Override
    public boolean matches(@NotNull final String input) {
        return Bukkit.getPlayer(input) != null;
    }

    private @NotNull List<String> getArgumentSuggestions(@NotNull final CommandSender commandSender,
                                                         @NotNull final String[] args) {
        final String input = args[args.length - 1];

        return Bukkit.getOnlinePlayers()
            .stream()
            .map(Player::getName)
            .filter((name) -> name.toLowerCase().contains(input))
            .collect(Collectors.toList());
    }
}
