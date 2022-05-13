package ravioli.gravioli.command.builder;

import org.jetbrains.annotations.Nullable;
import ravioli.gravioli.command.argument.Argument;
import ravioli.gravioli.command.argument.ArgumentSentence;
import ravioli.gravioli.command.builder.executor.CommandExecutor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Command {
    private final Plugin plugin;
    private final String rootCommand;
    private final String[] commandAliases;
    private final List<SubCommand> subCommands = new ArrayList<>();

    private CommandExecutor defaultCommandExecutor;

    public Command(@NotNull final Plugin plugin, @NotNull final String rootCommand, @NotNull final String... commandAliases) {
        this.plugin = plugin;
        this.rootCommand = rootCommand;
        this.commandAliases = commandAliases;
    }

    protected  @NotNull SubCommand addSubCommand(@NotNull final CommandExecutor commandExecutor, @NotNull final Argument<?>... arguments) {
        for (int i = 0; i < arguments.length - 1; i++) {
            final Argument<?> argument = arguments[i];

            if (argument instanceof ArgumentSentence) {
                throw new IllegalStateException("Can only use ArgumentSentence as the last parameter of a subcommand!");
            }
        }
        final SubCommand subCommand = new SubCommand(this, commandExecutor, arguments);

        this.subCommands.add(subCommand);

        return subCommand;
    }

    protected void setDefaultCommandExecutor(final CommandExecutor commandExecutor) {
        this.defaultCommandExecutor = commandExecutor;
    }

    protected @NotNull CommandExecutor createDefaultCommandExecutor() {
        return (commandSender, commandContext) -> {
            final List<Component> lines = new ArrayList<>();

            lines.add(MiniMessage.miniMessage().deserialize("<green> --- Available Commands ---"));

            this.subCommands.forEach((subCommand) -> {
                if (subCommand.canExecute(commandSender, commandContext.getInput(), false)) {
                    lines.add(subCommand.toComponent());
                }
            });

            for (final Component line : lines) {
                commandSender.sendMessage(line);
            }
        };
    }

    public final @NotNull String getRootCommand() {
        return this.rootCommand;
    }

    public final @NotNull String[] getCommandAliases() {
        return this.commandAliases;
    }

    public final @NotNull CommandExecutor getDefaultCommandExecutor() {
        return this.defaultCommandExecutor;
    }

    public final @Nullable SubCommand getMatch(@NotNull final CommandSender commandSender, @NotNull final String[] args) {
        for (final SubCommand subCommand : this.subCommands) {
            if (subCommand.matches(args)) {
                return subCommand;
            }
        }
        return null;
    }

    public final @NotNull List<String> getSuggestions(@NotNull final CommandSender commandSender, @NotNull final String[] args) {
        final List<String> suggestions = new ArrayList<>();
        final String commandString = StringUtils.join(args, " ");

        for (final SubCommand subCommand : this.subCommands) {
            if (!subCommand.currentlyMatches(args)) {
                continue;
            }
            if (!subCommand.canExecute(commandSender, commandString, false)) {
                continue;
            }
            suggestions.addAll(subCommand.getSuggestions(commandSender, args));
        }
        return suggestions;
    }

    protected final @NotNull Plugin getPlugin() {
        return this.plugin;
    }
}
