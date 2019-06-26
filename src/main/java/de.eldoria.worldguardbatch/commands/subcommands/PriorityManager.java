package de.eldoria.worldguardbatch.commands.subcommands;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.eldoria.worldguardbatch.RegionLoader;
import de.eldoria.worldguardbatch.commands.RegionIdentificationArgument;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class PriorityManager implements Subcommand {
    private RegionLoader regionLoader;

    /**
     * Creates a ne Priority Manager instance.
     * @param regionLoader
     */
    public PriorityManager(RegionLoader regionLoader) {
        this.regionLoader = regionLoader;
    }

    @Override
    public boolean directCommand(Player sender, String[] args) {
        if (args.length < 2) {
            return false;
        }

        RegionIdentificationArgument regionIdentificationArgument = RegionIdentificationArgument.NONE;

        if (args.length > 2) {
            regionIdentificationArgument = RegionIdentificationArgument.getIdentification(args[3]);

            if (regionIdentificationArgument == RegionIdentificationArgument.NONE || regionIdentificationArgument == RegionIdentificationArgument.OWNER) {
                //TODO: Invalid identification arg.
                return false;
            }
        }

        int prio;

        try {
            prio = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            //TODO: Not a number.
            return true;
        }

        if (prio < 0 || prio > 10) {
            //TODO: Prio must be between 0 and 10.
            return true;
        }

        switch (regionIdentificationArgument) {
            case NONE:
                changePriority(sender, prio);
                break;
            case REGEX:
                if (args.length == 4) {
                    changePriorityByRegex(sender, args, prio);
                } else {
                    //TODO: Wrong arguments.
                    return true;
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
        }
        return true;
    }

    private void changePriorityByParent(Player sender, String[] args, int prio) {
        var regions = regionLoader.getAllChildsOfRegionInWorld(sender.getWorld(), args[3]);

        regions.forEach(region -> region.setPriority(prio));
    }

    private void changePriorityByCount(Player sender, String[] args, int prio) {
        List<ProtectedRegion> regions = Collections.emptyList();
        if (args.length == 5) {
            regions = regionLoader.getRegionsWithNameCountUp(sender.getWorld(), args[3], args[4], null);
        } else if (args.length == 6) {
            regions = regionLoader.getRegionsWithNameCountUp(sender.getWorld(), args[3], args[4], args[5]);
        }

        regions.forEach(region -> region.setPriority(prio));
    }

    private void changePriority(Player sender, int prio) {
        var regions = regionLoader.getRegionsInWorld(sender.getWorld());

        regions.forEach(region -> region.setPriority(prio));
    }

    private void changePriorityByRegex(Player sender, String[] args, int prio) {
        var regions = regionLoader.getRegionsWithNameRegex(sender.getWorld(), args[3]);

        regions.forEach(region -> region.setPriority(prio));
    }
}
