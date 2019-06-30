package de.eldoria.worldguardbatch.commands.basecommand;

import de.eldoria.worldguardbatch.RegionLoader;
import de.eldoria.worldguardbatch.WorldGuardBatch;
import de.eldoria.worldguardbatch.commands.PrimaryActionArgument;
import de.eldoria.worldguardbatch.messages.MessageSender;
import de.eldoria.worldguardbatch.messages.MessagesLib;
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
    private MessageSender ms;

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
        this.ms = MessageSender.getInstance();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        Player p;
        if (commandSender instanceof Player) {
            p = (Player) commandSender;
        } else {
            commandSender.sendMessage("Only player can user this.");
            return false;
        }

        if (args.length == 0) {
            MessageSender.getInstance().sendNotify(p, "Type '/wgb help' for a list of commands.");
            return true;
        }

        var primaryArg = PrimaryActionArgument.getPrimary(args[0]);

        switch (primaryArg) {
            case NONE:
                ms.sendUnkownCommandError(p);
                break;
            case MADD:
            case MTRANS:
            case MREM:
                membershipManager.directCommand(p, primaryArg, args);
                break;
            case PRIO:
                priorityManager.directCommand(p, primaryArg, args);
                break;
            case PCH:
            case PSET:
            case CREM:
            case PREM:
                parentManager.directCommand(p, primaryArg, args);
                break;
            case FSET:
            case FREM:
                flagManager.directCommand(p, primaryArg, args);
                break;
            case CHECK:
            case LIST:
                checkSubCommand.directCommand(p, primaryArg, args);
                break;
            case HELP:
                executeHelpCommand(p);
                break;
            case RELOAD:
                WorldGuardBatch.getInstance().reload();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + primaryArg);
        }

        return true;
    }

    private void executeHelpCommand(Player p) {
        StringJoiner stringJoiner = new StringJoiner("\n");

        for (PrimaryActionArgument arg : PrimaryActionArgument.values()) {
            MessagesLib.CommandText cmdText = MessagesLib.getCommandText(arg);
            if (cmdText != null) {
                stringJoiner.add(cmdText.getDescription()).add(cmdText.getPattern());
            }
        }

        p.sendMessage(stringJoiner.toString());
    }
}
