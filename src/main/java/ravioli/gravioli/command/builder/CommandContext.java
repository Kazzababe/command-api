package ravioli.gravioli.command.builder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class CommandContext {
    private final String input;
    private final Map<String, Object> argumentList = new HashMap<>();
    private final Map<String, String> rawArgumentList = new HashMap<>();

    public CommandContext(@NotNull final String input) {
        this.input = input;
    }

    public String getInput() {
        return this.input;
    }

    public int getInputCount() {
        return this.argumentList.size();
    }

    public @Nullable<T> T get(@NotNull final String id) {
        return (T) this.argumentList.get(id);
    }

    public @Nullable String getRaw(@NotNull final String id) {
        return this.rawArgumentList.get(id);
    }

    void setArg(@NotNull final String id, @NotNull final Object value, @NotNull final String rawInput) {
        this.argumentList.put(id, value);
        this.rawArgumentList.put(id, rawInput);
    }
}
