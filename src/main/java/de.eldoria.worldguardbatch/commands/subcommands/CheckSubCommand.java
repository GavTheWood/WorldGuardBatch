package de.eldoria.worldguardbatch.commands.subcommands;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.eldoria.worldguardbatch.RegionLoader;
import de.eldoria.worldguardbatch.commands.CheckArgument;
import de.eldoria.worldguardbatch.commands.PrimaryActionArgument;
import de.eldoria.worldguardbatch.util.IntRange;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

public class CheckSubcommand implements Subcommand {

    private RegionLoader regionLoader;

    /**
     * Creates a new Check Subcommand instance.
     *
     * @param regionLoader
     */
    public CheckSubcommand(RegionLoader regionLoader) {

        this.regionLoader = regionLoader;
    }

    @Override
    public boolean directCommand(Player sender, String[] args) {
        if (args.length < 2) {
            //TODO: Too few arguments.
            return true;
        }

        CheckArgument checkArg = CheckArgument.getCheckScope(args[1]);

        if (checkArg == CheckArgument.NONE) {
            //TODO: No valid check.
            return true;
        }

        List<ProtectedRegion> regions = Collections.emptyList();
        if (checkArg != CheckArgument.ALL && args.length < 3) {
            //TODO: Too few arguments.
            return true;
        }
        switch (checkArg) {

            case ALL:
                regions = regionLoader.getRegionsInWorld(sender.getWorld());
                break;
            case CHILDREN:
                if (args.length == 3) {
                    regions = regionLoader.getAllChildsOfRegionInWorld(sender.getWorld(), args[2]);
                } else {
                    //TODO: wrong arguments.
                }
                break;
            case REGEX:
                if (args.length == 3) {
                    regions = regionLoader.getRegionsWithNameRegex(sender.getWorld(), args[2]);
                } else {
                    //TODO: wrong arguments.
                }
                break;
            case COUNT:
                IntRange range;
                if (args.length == 4) {
                    range = IntRange.parseString(args[3], null);
                } else if (args.length == 5) {
                    range = IntRange.parseString(args[3], args[4]);
                } else {
                    //TODO: Wrong arguments.
                    return true;
                }

                regions = regionLoader.getRegionsWithNameCountUp(sender.getWorld(), args[2], range);
                break;
            case OWNER:
                regions = regionLoader.getOwnerRegionsFromPlayerInWorld(sender.getWorld(), args[2]);
                break;
        }

        sender.sendMessage("Query found " + regions.size() + " regions.");

        if (PrimaryActionArgument.getPrimary(args[0]) == PrimaryActionArgument.LIST) {
            StringJoiner stringJoiner = new StringJoiner("\n");

            regions.forEach(region -> stringJoiner.add(region.getId()));

            sender.sendMessage("Affected Region:\n" + stringJoiner.toString());
        }

        return true;
    }
}
