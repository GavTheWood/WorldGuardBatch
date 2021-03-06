package de.eldoria.worldguardbatch.commands.basecommand;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.eldoria.worldguardbatch.RegionLoader;
import de.eldoria.worldguardbatch.commands.CheckArgument;
import de.eldoria.worldguardbatch.commands.PrimaryActionArgument;
import de.eldoria.worldguardbatch.messages.MessageSender;
import de.eldoria.worldguardbatch.util.IntRange;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

class CheckSubcommand implements Subcommand {

    private RegionLoader regionLoader;
    private MessageSender messageSender;

    /**
     * Creates a new Check Subcommand instance.
     *
     * @param regionLoader region loader instance
     */
    public CheckSubcommand(RegionLoader regionLoader) {
        this.messageSender = MessageSender.getInstance();

        this.regionLoader = regionLoader;
    }

    @Override
    public void directCommand(Player sender, PrimaryActionArgument pArg, String[] args) {

        if (args.length < 2) {
            messageSender.sendTooFewArgumentsError(sender, pArg);
            return;
        }

        CheckArgument checkArg = CheckArgument.getCheckScope(args[1]);

        if (checkArg == CheckArgument.NONE) {
            messageSender.sendUnkownCheckArgumentError(sender, pArg);
            return;
        }

        List<ProtectedRegion> regions = Collections.emptyList();
        if (checkArg != CheckArgument.ALL && args.length < 3) {
            messageSender.sendTooFewArgumentsError(sender, pArg);
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
                    messageSender.sendArgumentMessage(sender, pArg, args, 3);
                }
                break;
            case REGEX:
                if (args.length == 3) {
                    regions = regionLoader.getRegionsWithNameRegex(sender, args[2]);
                } else {
                    messageSender.sendArgumentMessage(sender, pArg, args, 3);
                    return;
                }
                break;
            case COUNT:
                IntRange range;
                if (args.length == 4) {
                    try {
                        range = IntRange.parseString(args[3], null);
                    } catch (NumberFormatException e) {
                        messageSender.sendInvalidNumberError(sender);
                        return;
                    }
                } else if (args.length == 5) {
                    try {
                        range = IntRange.parseString(args[3], args[4]);
                    } catch (NumberFormatException e) {
                        messageSender.sendInvalidNumberError(sender);
                        return;
                    }

                } else {
                    messageSender.sendArgumentMessage(sender, pArg, args, 4,5);
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
            StringJoiner stringJoiner = new StringJoiner(System.lineSeparator());

            regions.forEach(region -> stringJoiner.add(region.getId()));

            sender.sendMessage("Affected Region:" + System.lineSeparator() + stringJoiner.toString());
        }
    }
}