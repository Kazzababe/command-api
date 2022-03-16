package ravioli.gravioli.command.argument;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class ArgumentSentence extends Argument<String> {
    public ArgumentSentence(@NotNull final String id) {
        super(id);

        this.setCommandSuggestionExecutor(this::getArgumentSuggestions);
    }
    @Override
    public @NotNull String parse(@NotNull final String input) throws IllegalArgumentException {
        return input;
    }

    @Override
    public boolean matches(@NotNull String input) {
        return true;
    }

    private @NotNull List<String> getArgumentSuggestions(@NotNull final CommandSender commandSender,
                                                         @NotNull final String[] args) {
        return Collections.singletonList("<" + this.getId() + ">");
    }
}
