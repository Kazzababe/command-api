package ravioli.gravioli.command.argument;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class ArgumentLiteral extends Argument<String> {
    public ArgumentLiteral(@NotNull final String id) {
        super(id);

        this.setCommandSuggestionExecutor(this::getArgumentSuggestions);
    }

    @Override
    public @NotNull String parse(@NotNull final String input) throws IllegalArgumentException {
        return input;
    }

    @Override
    public boolean matches(@NotNull final String input) {
        return this.getId().equalsIgnoreCase(input);
    }

    private @NotNull List<String> getArgumentSuggestions(@NotNull final CommandSender commandSender,
                                                         @NotNull final String[] args) {
        final String input = args[args.length - 1];

        if (this.getId().toLowerCase().contains(input)) {
            return Collections.singletonList(this.getId());
        }
        return Collections.emptyList();
    }
}
