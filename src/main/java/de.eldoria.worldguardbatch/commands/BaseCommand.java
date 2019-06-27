package de.eldoria.worldguardbatch.commands;

import de.eldoria.worldguardbatch.Messages;
import de.eldoria.worldguardbatch.RegionLoader;
import de.eldoria.worldguardbatch.commands.subcommands.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.StringJoiner;

public class BaseCommand implements CommandExecutor {

    private MembershipManager membershipManager;
    private PriorityManager priorityManager;
    private ParentManager parentManager;
    private FlagManager flagManager;
    private CheckSubcommand checkSubCommand;

    /**
     * Creates a new Base Command Object.
     *
     * @param regionLoader RegionLoader Object
     */
    public BaseCommand(RegionLoader regionLoader) {
        this.membershipManager = new MembershipManager(regionLoader);
        this.priorityManager = new PriorityManager(regionLoader);
        this.parentManager = new ParentManager(regionLoader);
        this.flagManager = new FlagManager(regionLoader);
        this.checkSubCommand = new CheckSubcommand(regionLoader);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        Player p;
        if (commandSender instanceof Player) {
            p = (Player) commandSender;
        } else {
            return false;
        }

        if (args.length == 0) {
            //TODO: Info command
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("help")) {
                executeHelpCommand(p);
                return true;
            }
        }
        if (args.length > 1) {
            var primaryArg = PrimaryActionArgument.getPrimary(args[0]);

            switch (primaryArg) {
                case NONE:
                    //TODO: No valid argument found.
                    break;
                case MADD:
                case MTRANS:
                case MREM:
                    membershipManager.directCommand(p, args);
                    break;
                case PRIO:
                    priorityManager.directCommand(p, args);
                    break;
                case PCH:
                case PSET:
                case CREM:
                case PREM:
                    parentManager.directCommand(p, args);
                    break;
                case FSET:
                case FREM:
                    flagManager.directCommand(p, args);
                    break;
                case CHECK:
                case LIST:
                    checkSubCommand.directCommand(p, args);
                    break;
                case HELP:
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + primaryArg);
            }
        }

        return true;
    }

    private void executeHelpCommand(Player p) {
        StringJoiner stringJoiner = new StringJoiner("\n");
        
        for (PrimaryActionArgument arg : PrimaryActionArgument.values()) {
            Messages.CommandText cmdText = Messages.getCommandText(arg);
            if (cmdText != null) {
                stringJoiner.add(cmdText.getDescription()).add(cmdText.getPattern());
            }
        }

        p.sendMessage(stringJoiner.toString());
    }
}
