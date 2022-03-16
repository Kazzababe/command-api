## CommandAPI
A simple way to register commands without having to parse every single argument yourself. Reduces the amount of code you have to write drastically and provides a lot of built in options.

### Registering a command
To register a command, you simple have to create a new class that extends ``com.thepondmc.builder.Command``. Inside that class you can call ``Command#addSubCommand`` to register any amount of sub commands. Here is an example below:

```
public class TestCommand extends Command {  
    public TestCommand() {  
         // The first argument is the base command name and any following arguments are aliases.  
         super("test", "testalias");  
         
         /*  
          * Command#addSubCommand is a simple command that takes in these parameters:
          * 1. The first parameter is a CommandExecutor object with CommandSender and CommandContext parameters
          * 2. All following commands are the arguments the command will use, in this case:
          *    1. The first argument is simply the literal word: "give"
          *    2. The second argument will attempt to match an online player
          *    3. The third and final argument will attempt to match any value from the Material enum 
          *       - The #format call simply handles how the player will see them in the tab completion, in this case 
          *         all suggestions will be lowercase.
          */
          this.addSubCommand(  
                    this::onGive,  
                    ArgumentType.Literal("give"),  
                    ArgumentType.Player("target"),  
                    ArgumentType.Enum("type", Material.class)  
                         .format(ArgumentEnum.Format.LOWER_CASE)  
                )
                .setPermission("test.give") // Any user using this command must have the "test.give" permission  
                .setPermissionFailExecutor(this::onBadPermissions); // This will get called if the permission check fails  
     }  
  
     private void onBadPermissions(@NotNull final CommandSender commandSender, @NotNull final CommandContext context) {  
          commandSender.sendMessage(ChatColor.RED + "You do have the permissions to do that!");  
     }  
     
     private void onGive(@NotNull final CommandSender commandSender, @NotNull final CommandContext context) {  
          final Player player = context.get("target");  
          final Material type = context.get("type");  
          
          player.getInventory().addItem(new ItemStack(type));  
  
          commandSender.sendMessage("You gave " + player.getName() + " one " + type.name() + "!");  
     }  
}
```

There are several built in argument types you can use that are all located in the ``ArgumentType`` class.

### Defining a default executor
Sometimes you'll want to display a message if the command isn't used correctly and to that do that, you can supply a default executor by calling ``setDefaultCommandExecutor`` in your command.

### Registering a command
```
CommandAPI.registerCommand(new TestCommand());
```
