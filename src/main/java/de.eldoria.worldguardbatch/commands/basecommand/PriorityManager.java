package de.eldoria.worldguardbatch.commands.basecommand;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.eldoria.worldguardbatch.RegionLoader;
import de.eldoria.worldguardbatch.commands.PrimaryActionArgument;
import de.eldoria.worldguardbatch.commands.RegionIdentificationArgument;
import de.eldoria.worldguardbatch.messages.MessageSender;
import de.eldoria.worldguardbatch.util.IntRange;
import lombok.NonNull;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

class PriorityManager implements Subcommand {
    private RegionLoader regionLoader;
    private MessageSender messageSender;

    /**
     * Creates a ne Priority Manager instance.
     *
     * @param regionLoader Region Loader instance
     */
    public PriorityManager(@NonNull RegionLoader regionLoader) {
        this.messageSender = MessageSender.getInstance();
        this.regionLoader = regionLoader;
    }

    @Override
    public void directCommand(Player sender, PrimaryActionArgument pArg, String[] args) {
        if (args.length < 2) {
            messageSender.sendTooFewArgumentsError(sender, pArg);
            return;
        }

        RegionIdentificationArgument regionIdentificationArgument = RegionIdentificationArgument.NONE;

        if (args.length > 2) {
            regionIdentificationArgument = RegionIdentificationArgument.getIdentification(args[3]);

            if (regionIdentificationArgument == RegionIdentificationArgument.NONE
                    || regionIdentificationArgument == RegionIdentificationArgument.OWNER) {
                messageSender.sendUnknownRegionQueryError(sender, pArg);
                return;
            }
        }

        int prio;

        try {
            prio = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            messageSender.sendInvalidNumberError(sender);
            return;
        }

        switch (regionIdentificationArgument) {
            case NONE:
                changePriority(sender, prio);
                break;
            case REGEX:
                if (args.length == 4) {
                    changePriorityByRegex(sender, args, prio);
                } else {
                    messageSender.sendArgumentMessage(sender, pArg, args, 4);
                    return;
                }
                break;
            case COUNT:
                if (args.length == 5 || args.length == 6) {
                    changePriorityByCount(sender, args, prio);
                }
                break;
            case PARENT:
                if (args.length == 4) {
                    changePriorityByParent(sender, args, prio);
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + regionIdentificationArgument);
        }
    }

    private void changePriorityByParent(Player sender, String[] args, int prio) {
        var regions = regionLoader.getAllChildsOfRegionInWorld(sender, args[3]);

        regions.forEach(region -> {
            region.setPriority(prio);
            messageSender.sendModifiedMessage(sender, region.getId());
        });
        messageSender.sendTotalModifiedMessage(sender, regions.size());

    }

    private void changePriorityByCount(Player sender, String[] args, int prio) {
        List<ProtectedRegion> regions = Collections.emptyList();
        IntRange range;
        if (args.length == 5) {
            try {
                range = IntRange.parseString(args[4], null);
            } catch (NumberFormatException e) {
                messageSender.sendInvalidNumberError(sender);
                return;
            }

            regions = regionLoader.getRegionsWithNameCountUp(sender, args[3], range);
        } else if (args.length == 6) {
            try {
                range = IntRange.parseString(args[4], args[5]);
            } catch (NumberFormatException e) {
                messageSender.sendInvalidNumberError(sender);
                return;
            }

            regions = regionLoader.getRegionsWithNameCountUp(sender, args[3], range);
        }

        regions.forEach(region -> {
            region.setPriority(prio);
            messageSender.sendModifiedMessage(sender, region.getId());
        });
        messageSender.sendTotalModifiedMessage(sender, regions.size());

    }

    private void changePriority(Player sender, int prio) {
        var regions = regionLoader.getRegionsInWorld(sender);

        regions.forEach(region -> {
            region.setPriority(prio);
            messageSender.sendModifiedMessage(sender, region.getId());
        });
        messageSender.sendTotalModifiedMessage(sender, regions.size());

    }

    private void changePriorityByRegex(Player sender, String[] args, int prio) {
        var regions = regionLoader.getRegionsWithNameRegex(sender, args[3]);

        regions.forEach(region -> {
            region.setPriority(prio);
            messageSender.sendModifiedMessage(sender, region.getId());
        });
        messageSender.sendTotalModifiedMessage(sender, regions.size());

    }
}
