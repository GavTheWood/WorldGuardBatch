package de.eldoria.worldguardbatch.commands.subcommands;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.eldoria.worldguardbatch.Messages;
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
     * @param regionLoader region loader instance
     */
    public CheckSubcommand(RegionLoader regionLoader) {

        this.regionLoader = regionLoader;
    }

    @Override
    public void directCommand(Player sender, PrimaryActionArgument pArg, String[] args) {

        if (args.length < 2) {
            sender.sendMessage(Messages.getErrorTooFewArguments(pArg));
            return;
        }

        CheckArgument checkArg = CheckArgument.getCheckScope(args[1]);

        if (checkArg == CheckArgument.NONE) {
            sender.sendMessage(Messages.getErrorUnknownCheckArgument(pArg));
            return;
        }

        List<ProtectedRegion> regions = Collections.emptyList();
        if (checkArg != CheckArgument.ALL && args.length < 3) {
            sender.sendMessage(Messages.getErrorTooFewArguments(pArg));
            return;
        }
        switch (checkArg) {

            case ALL:
                regions = regionLoader.getRegionsInWorld(sender);
                break;
            case CHILDREN:
                if (args.length == 3) {
                    regions = regionLoader.getAllChildsOfRegionInWorld(sender, args[2]);
                } else {
                    Messages.sendArgumentMessage(sender, pArg, args, 3);
                }
                break;
            case REGEX:
                if (args.length == 3) {
                    regions = regionLoader.getRegionsWithNameRegex(sender.getWorld(), args[2]);
                } else {
                    Messages.sendArgumentMessage(sender, pArg, args, 3);
                    return;
                }
                break;
            case COUNT:
                IntRange range;
                if (args.length == 4) {
                    try {
                        range = IntRange.parseString(args[3], null);
                    } catch (NumberFormatException e) {
                        sender.sendMessage(Messages.ERROR_INVALID_NUMBERS);
                        return;
                    }
                } else if (args.length == 5) {
                    try {
                        range = IntRange.parseString(args[3], args[4]);
                    } catch (NumberFormatException e) {
                        sender.sendMessage(Messages.ERROR_INVALID_NUMBERS);
                        return;
                    }

                } else {
                    if (args.length < 4) {
                        sender.sendMessage(Messages.getErrorTooFewArguments(pArg));
                    }
                    if (args.length > 5) {
                        sender.sendMessage(Messages.getErrorTooManyArguments(pArg));
                    }

                    return;
                }

                regions = regionLoader.getRegionsWithNameCountUp(sender, args[2], range);
                break;
            case OWNER:
                regions = regionLoader.getOwnerRegionsFromPlayerInWorld(sender, args[2]);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + checkArg);
        }

        sender.sendMessage("Query found " + regions.size() + " regions.");

        if (PrimaryActionArgument.getPrimary(args[0]) == PrimaryActionArgument.LIST) {
            StringJoiner stringJoiner = new StringJoiner("\n");

            regions.forEach(region -> stringJoiner.add(region.getId()));

            sender.sendMessage("Affected Region:\n" + stringJoiner.toString());
        }

        return;
    }
}
