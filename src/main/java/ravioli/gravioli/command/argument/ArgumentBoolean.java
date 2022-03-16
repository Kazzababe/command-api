package ravioli.gravioli.command.argument;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ArgumentBoolean extends Argument<Boolean> {
    public ArgumentBoolean(@NotNull final String id) {
        super(id);

        this.setCommandSuggestionExecutor(this::getArgumentSuggestions);
    }

    @Override
    public @NotNull Boolean parse(@NotNull final String input) throws IllegalArgumentException {
        return Boolean.parseBoolean(input);
    }

    @Override
    public boolean matches(@NotNull final String input) {
        return input.equalsIgnoreCase("true") || input.equalsIgnoreCase("false");
    }

    private @NotNull List<String> getArgumentSuggestions(@NotNull final CommandSender commandSender,
                                                         @NotNull final String[] args) {
        final String input = args[args.length - 1];

        return Stream.of("true", "false")
            .filter((value) -> value.contains(input))
            .collect(Collectors.toList());
    }
}
