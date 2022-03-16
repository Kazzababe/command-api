package ravioli.gravioli.command.argument;

import com.google.common.primitives.Ints;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ArgumentDuration extends Argument<Duration> {
    private final Map<Character, ChronoUnit> TIME_UNIT_MAP = new HashMap<>() {{
        put('y', ChronoUnit.YEARS);
        put('Y', ChronoUnit.YEARS);
        put('M', ChronoUnit.MONTHS);
        put('w', ChronoUnit.WEEKS);
        put('W', ChronoUnit.WEEKS);
        put('d', ChronoUnit.DAYS);
        put('D', ChronoUnit.DAYS);
        put('h', ChronoUnit.HOURS);
        put('H', ChronoUnit.HOURS);
        put('m', ChronoUnit.MINUTES);
        put('s', ChronoUnit.SECONDS);
        put('S', ChronoUnit.SECONDS);
    }};

    public ArgumentDuration(@NotNull final String id) {
        super(id);

        this.setCommandSuggestionExecutor(this::getArgumentSuggestions);
    }

    @Override
    public @NotNull Duration parse(@NotNull final String input) throws IllegalArgumentException {
        return this.privateParse(input).orElseThrow();
    }

    @Override
    public boolean matches(@NotNull final String input) {
        return this.privateParse(input).isPresent();
    }

    private @NotNull List<String> getArgumentSuggestions(@NotNull final CommandSender commandSender,
                                                         @NotNull final String[] args) {
        return Collections.singletonList("<" + this.getId() + ">");
    }

    private @NotNull Optional<Duration> privateParse(@NotNull final String input) {
        final String[] times = input.split("(?<=[a-zA-Z])");
        Duration totalDuration = null;

        for (final String time : times) {
            final char lastChar = time.charAt(time.length() - 1);
            final ChronoUnit unit = TIME_UNIT_MAP.get(lastChar);

            if (unit == null) {
                return Optional.empty();
            }
            final Integer duration = Ints.tryParse(time.substring(0, time.length() - 1));

            if (duration == null) {
                return Optional.empty();
            }
            final Duration trueDuration = unit.getDuration().multipliedBy(duration);

            if (totalDuration == null) {
                totalDuration = trueDuration;
            } else {
                totalDuration = totalDuration.plus(trueDuration);
            }
        }
        return Optional.ofNullable(totalDuration);
    }
}
