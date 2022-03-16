package ravioli.gravioli.command.argument;

import com.google.common.primitives.Longs;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class ArgumentLong extends Argument<Long> {
    private long max = Long.MAX_VALUE;
    private long min = Long.MIN_VALUE;
    private boolean inclusive;

    public ArgumentLong(@NotNull final String id) {
        super(id);

        this.inclusive = true;

        this.setCommandSuggestionExecutor(this::getArgumentSuggestions);
    }

    public ArgumentLong min(final int min) {
        this.min = min;

        return this;
    }

    public ArgumentLong max(final int max) {
        this.max = max;

        return this;
    }

    public ArgumentLong inclusive(final boolean inclusive) {
        this.inclusive = inclusive;

        return this;
    }

    @Override
    public @NotNull Long parse(@NotNull final String input) throws IllegalArgumentException {
        return Long.parseLong(input);
    }

    @Override
    public boolean matches(@NotNull String input) {
        final Long value = Longs.tryParse(input);

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
