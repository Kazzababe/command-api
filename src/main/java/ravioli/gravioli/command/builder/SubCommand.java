package ravioli.gravioli.command.builder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import ravioli.gravioli.command.argument.Argument;
import ravioli.gravioli.command.argument.ArgumentLiteral;
import ravioli.gravioli.command.argument.ArgumentSentence;
import ravioli.gravioli.command.builder.condition.CommandCondition;
import ravioli.gravioli.command.builder.executor.CommandExecutor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SubCommand {
    private final Command rootCommand;
    private final CommandExecutor commandExecutor;
    private final Argument<?>[] arguments;
    private final List<CommandCondition> commandConditions = new ArrayList<>();

    private String permissionNode;
    private CommandExecutor permissionFailExecutor;
    private boolean async;

    public SubCommand(@NotNull final Command rootCommand, @NotNull final CommandExecutor commandExecutor,
                      @NotNull final Argument<?>... arguments) {
        this.rootCommand = rootCommand;
        this.commandExecutor = commandExecutor;
        this.arguments = arguments;
    }

    public final @NotNull SubCommand setPermission(@Nullable final String permissionNode) {
        this.permissionNode = permissionNode;

        return this;
    }

    public final @NotNull SubCommand setAsync(final boolean async) {
        this.async = async;

        return this;
    }

    public final @NotNull SubCommand setPermissionFailExecutor(@NotNull final CommandExecutor permissionFailExecutor) {
        this.permissionFailExecutor = permissionFailExecutor;

        return this;
    }

    public final @NotNull SubCommand addCondition(@NotNull final CommandCondition commandCondition) {
        this.commandConditions.add(commandCondition);

        return this;
    }

    final boolean canExecute(@NotNull final CommandSender commandSender, @Nullable final String commandString,
                             final boolean duringExecution) {
        if (this.permissionNode != null) {
            if (!commandSender.hasPermission(this.permissionNode)) {
                return false;
            }
        }
        return this.commandConditions.stream()
            .allMatch((condition) -> condition.canUse(commandSender, commandString, duringExecution));
    }

    final @Nullable String getPermissionNode() {
        return this.permissionNode;
    }

    final @Nullable CommandExecutor getPermissionFailExecutor() {
        return this.permissionFailExecutor;
    }

    final @NotNull Component toComponent() {
        final StringBuilder text = new StringBuilder("<gray>/")
            .append(this.rootCommand.getRootCommand());

        for (final Argument<?> argument : this.arguments) {
            if (argument instanceof ArgumentLiteral) {
                text.append(" ")
                    .append(argument.getId());
            } else {
                text.append(" ")
                    .append("<")
                    .append(argument.getId())
                    .append(">");
            }
        }
        return MiniMessage.miniMessage().deserialize(text.toString());
    }

    final boolean currentlyMatches(@NotNull final String[] args) {
        if (args.length > this.arguments.length) {
            if (this.arguments.length > 0) {
                final Argument<?> lastArgument = this.arguments[this.arguments.length - 1];

                return lastArgument instanceof ArgumentSentence;
            }
            return false;
        }
        for (int i = 0; i < args.length - 1; i++) {
            final Argument<?> argument = this.arguments[i];
            final String arg = args[i];

            if (!argument.matches(arg)) {
                return false;
            }
        }
        return true;
    }

    final boolean matches(@NotNull final String[] args) {
        if (this.arguments.length != args.length) {
            if (this.arguments.length > 0) {
                final Argument<?> lastArgument = this.arguments[this.arguments.length - 1];

                if (lastArgument instanceof ArgumentSentence) {
                    return args.length >= this.arguments.length;
                }
            }
            return false;
        }
        for (int i = 0; i < this.arguments.length; i++) {
            final Argument<?> argument = this.arguments[i];
            final String arg = args[i];

            if (!argument.matches(arg)) {
                return false;
            }
        }
        return true;
    }

    final @NotNull List<String> getSuggestions(@NotNull final CommandSender commandSender, @NotNull final String[] args) {
        if (args.length > this.arguments.length) {
            final Argument<?> lastArgument = this.arguments[this.arguments.length - 1];

            if (!(lastArgument instanceof ArgumentSentence)) {
                return Collections.emptyList();
            } else {
                return List.of("<" + lastArgument.getId() + ">");
            }
        }
        return this.arguments[args.length - 1].getSuggestions(commandSender, args);
    }

    final void execute(@NotNull final CommandSender commandSender, @NotNull final String[] args) {
        final String input = StringUtils.join(args, " ");
        final CommandContext commandContext = new CommandContext(this.rootCommand.getRootCommand() + " " + input);

        for (int i = 0; i < this.arguments.length; i++) {
            final Argument<?> argument = this.arguments[i];
            final Object value = argument instanceof ArgumentSentence ?
                StringUtils.join(Arrays.copyOfRange(args, i, args.length), " ") :
                argument.parse(args[i]);

            commandContext.setArg(argument.getId(), value, args[i]);
        }
        if (this.async) {
            Bukkit.getScheduler().runTaskAsynchronously(
                this.rootCommand.getPlugin(),
                () -> this.commandExecutor.accept(commandSender, commandContext)
            );
        } else {
            this.commandExecutor.accept(commandSender, commandContext);
        }
    }

    @Override
    public final String toString() {
        final JsonObject json = new JsonObject();
        final JsonArray jsonArray = new JsonArray();

        for (final Argument<?> argument : this.arguments) {
            jsonArray.add(argument.getId());
        }
        json.addProperty("root_command", this.rootCommand.getRootCommand());
        json.add("arguments", jsonArray);

        return json.toString();
    }
}
