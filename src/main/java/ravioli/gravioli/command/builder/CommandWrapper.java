package ravioli.gravioli.command.builder;

import ravioli.gravioli.command.builder.executor.CommandExecutor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public final class CommandWrapper extends org.bukkit.command.Command {
    private final Command baseCommand;

    public CommandWrapper(@NotNull final Command baseCommand) {
        super(baseCommand.getRootCommand());

        this.baseCommand = baseCommand;

        this.setAliases(Arrays.asList(baseCommand.getCommandAliases()));
    }

    @Override
    public boolean execute(@NotNull final CommandSender commandSender, @NotNull final String alias,
                           @NotNull final String[] args) {
        final SubCommand subCommand = this.baseCommand.getMatch(commandSender, args);
        final String commandString = StringUtils.join(args, " ");

        if (subCommand != null) {
            if (subCommand.canExecute(commandSender, commandString, true)) {
                subCommand.execute(commandSender, args);
            } else {
                final CommandExecutor permissionFailExecutor = subCommand.getPermissionFailExecutor();

                if (permissionFailExecutor != null) {
                    final CommandContext commandContext = new CommandContext(alias + " " + StringUtils.join(args, " "));

                    permissionFailExecutor.accept(commandSender, commandContext);
                }
            }
            return true;
        }
        final CommandExecutor defaultCommandExecutor = this.baseCommand.getDefaultCommandExecutor();

        if (defaultCommandExecutor != null) {
            final CommandContext commandContext = new CommandContext(alias + " " + StringUtils.join(args, " "));

            defaultCommandExecutor.accept(commandSender, commandContext);
        }
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull final CommandSender sender, @NotNull final String alias,
                                             @NotNull final String[] args) throws IllegalArgumentException {
        return this.baseCommand.getSuggestions(sender, args);
    }
}
