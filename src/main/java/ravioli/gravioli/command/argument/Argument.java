package ravioli.gravioli.command.argument;

import ravioli.gravioli.command.builder.executor.CommandSuggestionExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class Argument<T> {
    private final String id;
    private CommandSuggestionExecutor commandSuggestionExecutor;

    public Argument(@NotNull final String id) {
        this.id = id;
    }

    public final Argument<T> setCommandSuggestionExecutor(@NotNull final CommandSuggestionExecutor commandSuggestionExecutor) {
        this.commandSuggestionExecutor = commandSuggestionExecutor;

        return this;
    }

    public final @NotNull String getId() {
        return this.id;
    }

    public final @NotNull List<String> getSuggestions(@NotNull final CommandSender commandSender, @NotNull final String[] args) {
        return Objects.requireNonNullElse(
            this.commandSuggestionExecutor.run(commandSender, args),
            Collections.emptyList()
        );
    }

    public abstract @NotNull T parse(@NotNull final String input) throws IllegalArgumentException;

    public abstract boolean matches(@NotNull final String input);
}
