package ravioli.gravioli.command.argument;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ArgumentGroup extends Argument<String> {
    private final String[] words;
    private boolean caseInsensitive;

    public ArgumentGroup(@NotNull final String id, @NotNull final String... words) {
        super(id);

        this.words = words;
        this.caseInsensitive = true;

        this.setCommandSuggestionExecutor(this::getArgumentSuggestions);
    }

    public ArgumentGroup caseSensitive(final boolean caseSensitive) {
        this.caseInsensitive = caseSensitive;

        return this;
    }

    @Override
    public @NotNull String parse(@NotNull final String input) throws IllegalArgumentException {
        return input;
    }

    @Override
    public boolean matches(@NotNull final String input) {
        for (final String word : this.words) {
            if (this.caseInsensitive) {
                if (input.equalsIgnoreCase(word)) {
                    return true;
                }
            } else if (input.equals(word)) {
                return true;
            }
        }
        return false;
    }

    private @NotNull List<String> getArgumentSuggestions(@NotNull final CommandSender commandSender,
                                                        @NotNull final String[] args) {
        final String input = args[args.length - 1];

        return Stream.of(this.words)
            .filter((word) -> word.toLowerCase().contains(input))
            .collect(Collectors.toList());
    }
}
