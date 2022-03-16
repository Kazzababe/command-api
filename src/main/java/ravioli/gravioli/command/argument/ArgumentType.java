package ravioli.gravioli.command.argument;

import org.jetbrains.annotations.NotNull;

public class ArgumentType {
    public static ArgumentString String(@NotNull final String id) {
        return new ArgumentString(id);
    }

    public static ArgumentGroup Group(@NotNull final String id, @NotNull final String... words) {
        return new ArgumentGroup(id, words);
    }

    public static ArgumentInteger Integer(@NotNull final String id) {
        return new ArgumentInteger(id);
    }

    public static <T extends Enum<T>> ArgumentEnum<T> Enum(@NotNull final String id, @NotNull final Class<T> enumClass) {
        return new ArgumentEnum<>(id, enumClass);
    }

    public static ArgumentPlayer Player(@NotNull final String id) {
        return new ArgumentPlayer(id);
    }

    public static ArgumentLiteral Literal(@NotNull final String id) {
        return new ArgumentLiteral(id);
    }

    public static ArgumentLong Long(@NotNull final String id) {
        return new ArgumentLong(id);
    }

    public static ArgumentBoolean Boolean(@NotNull final String id) {
        return new ArgumentBoolean(id);
    }

    public static ArgumentDouble Double(@NotNull final String id) {
        return new ArgumentDouble(id);
    }

    public static ArgumentSentence Sentence(@NotNull final String id) {
        return new ArgumentSentence(id);
    }

    public static ArgumentDuration Duration(@NotNull final String id) {
        return new ArgumentDuration(id);
    }
}
