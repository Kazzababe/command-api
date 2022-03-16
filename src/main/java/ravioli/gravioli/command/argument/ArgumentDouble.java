package ravioli.gravioli.command.argument;

import com.google.common.primitives.Doubles;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class ArgumentDouble extends Argument<Double> {
    private double max = Double.MAX_VALUE;
    private double min = Double.MIN_VALUE;
    private boolean inclusive;

    public ArgumentDouble(@NotNull final String id) {
        super(id);

        this.inclusive = true;

        this.setCommandSuggestionExecutor(this::getArgumentSuggestions);
    }

    public ArgumentDouble min(final double min) {
        this.min = min;

        return this;
    }

    public ArgumentDouble max(final double max) {
        this.max = max;

        return this;
    }

    public ArgumentDouble inclusive(final boolean inclusive) {
        this.inclusive = inclusive;

        return this;
    }

    @Override
    public @NotNull Double parse(@NotNull final String input) throws IllegalArgumentException {
        return Double.parseDouble(input);
    }

    @Override
    public boolean matches(@NotNull String input) {
        final Double value = Doubles.tryParse(input);

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
