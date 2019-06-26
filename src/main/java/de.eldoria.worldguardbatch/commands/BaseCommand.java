package de.eldoria.worldguardbatch.commands;

import de.eldoria.worldguardbatch.RegionLoader;
import de.eldoria.worldguardbatch.commands.subcommands.FlagManager;
import de.eldoria.worldguardbatch.commands.subcommands.MembershipManager;
import de.eldoria.worldguardbatch.commands.subcommands.ParentManager;
import de.eldoria.worldguardbatch.commands.subcommands.PriorityManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BaseCommand implements CommandExecutor {

    private MembershipManager membershipManager;
    private PriorityManager priorityManager;
    private ParentManager parentManager;
    private FlagManager flagManager;

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
            //TODO help
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
                    parentManager.directCommand(p,args);
                    break;
                case FSET:
                case FREM:
                    flagManager.directCommand(p,args);
                    break;
                case HELP:
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + primaryArg);
            }
        }

        return false;
    }
}
