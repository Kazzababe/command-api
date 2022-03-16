package ravioli.gravioli.command.argument;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ArgumentEnum<T extends Enum<T>> extends Argument<T> {
    private final Class<T> enumClass;
    private Format format;

    public ArgumentEnum(@NotNull final String id, @NotNull final Class<T> enumClass) {
        super(id);

        this.enumClass = enumClass;
        this.format = Format.UPPER_CASE;

        this.setCommandSuggestionExecutor(this::getArgumentSuggestions);
    }

    public ArgumentEnum<T> format(@NotNull final Format format) {
        this.format = format;

        return this;
    }

    @Override
    public @NotNull T parse(@NotNull final String input) throws IllegalArgumentException {
        return Enum.valueOf(this.enumClass, input.toUpperCase());
    }

    @Override
    public boolean matches(@NotNull final String input) {
        try {
            Enum.valueOf(this.enumClass, input.toUpperCase());

            return true;
        } catch (final IllegalArgumentException e) {
            return false;
        }
    }

    private @NotNull List<String> getArgumentSuggestions(@NotNull final CommandSender commandSender,
                                                        @NotNull final String[] args) {
        final String input = args[args.length - 1];

        return Arrays.stream(this.enumClass.getEnumConstants())
            .map((theEnum) -> this.format.format(theEnum.name()))
            .filter((value) -> value.toLowerCase().contains(input))
            .collect(Collectors.toList());
    }

    public enum Format {
        LOWER_CASE (String::toLowerCase),
        UPPER_CASE (String::toUpperCase),
        CAPITALIZE ((input) -> StringUtils.capitalize(input.toLowerCase()));

        private final Function<String, String> formatter;

        Format(final Function<String, String> formatter) {
            this.formatter = formatter;
        }

        private String format(@NotNull final String input) {
            return this.formatter.apply(input);
        }
    }
}
