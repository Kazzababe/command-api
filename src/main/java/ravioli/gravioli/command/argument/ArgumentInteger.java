package ravioli.gravioli.command.argument;

import com.google.common.primitives.Ints;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class ArgumentInteger extends Argument<Integer> {
    private int max = Integer.MAX_VALUE;
    private int min = Integer.MIN_VALUE;
    private boolean inclusive;

    public ArgumentInteger(@NotNull final String id) {
        super(id);

        this.inclusive = true;

        this.setCommandSuggestionExecutor(this::getArgumentSuggestions);
    }

    public ArgumentInteger min(final int min) {
        this.min = min;

        return this;
    }

    public ArgumentInteger max(final int max) {
        this.max = max;

        return this;
    }

    public ArgumentInteger inclusive(final boolean inclusive) {
        this.inclusive = inclusive;

        return this;
    }

    @Override
    public @NotNull Integer parse(@NotNull final String input) throws IllegalArgumentException {
        return Integer.parseInt(input);
    }

    @Override
    public boolean matches(@NotNull String input) {
        final Integer value = Ints.tryParse(input);

        if (value == null) {
            return false;
        }
        if (value >= this.min && value <= this.max) {
            return true;
        }
        return false;
    }

    private @NotNull List<String> getArgumentSuggestions(@NotNull final CommandSender commandSender,
                                                         @NotNull final String[] args) {
        return Collections.singletonList("<" + this.getId() + ">");
    }
}
