package de.eldoria.worldguardbatch.commands;

import de.eldoria.worldguardbatch.RegionLoader;
import de.eldoria.worldguardbatch.commands.subcommands.MembershipManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BaseCommand implements CommandExecutor {

    private RegionLoader regionLoader;
    private MembershipManager membershipManager;

    public BaseCommand(RegionLoader regionLoader) {
        this.regionLoader = regionLoader;
        membershipManager = new MembershipManager(regionLoader);
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
            var primaryArg = PrimaryArg.getPrimary(args[0]);

            switch (primaryArg) {
                case NONE:
                    //TODO: No valid argument found.
                    break;
                case MREM:
                    membershipManager.directCommand(p, args);
                    break;
                case MADD:
                    break;
                case PRIO:
                    break;
                case PSET:
                    break;
                case CREM:
                    break;
                case PREM:
                    break;
                case MTRANS:
                    break;
                case HELP:
                    break;
            }
        }

        return false;
    }
}
