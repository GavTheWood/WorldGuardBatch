package de.eldoria.worldguardbatch.commands.subcommands;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.eldoria.worldguardbatch.Messages;
import de.eldoria.worldguardbatch.RegionLoader;
import de.eldoria.worldguardbatch.commands.PrimaryActionArgument;
import de.eldoria.worldguardbatch.commands.RegionIdentificationArgument;
import de.eldoria.worldguardbatch.util.IntRange;
import lombok.NonNull;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ParentManager implements Subcommand {
    private RegionLoader regionLoader;

    /**
     * Creates new Parent Manager instance.
     *
     * @param regionLoader Region Loader instance
     */
    public ParentManager(@NonNull RegionLoader regionLoader) {

        this.regionLoader = regionLoader;
    }

    @Override
    public void directCommand(Player sender, PrimaryActionArgument pArg, String[] args) {
        if (args.length == 1) {
            sender.sendMessage(Messages.getErrorTooFewArguments(pArg));
            return;
        }

        var primary = PrimaryActionArgument.getPrimary(args[0]);

        RegionIdentificationArgument regionIdentificationArgument;

        if (args.length > 2) {
            regionIdentificationArgument = RegionIdentificationArgument.getIdentification(args[2]);

            if (regionIdentificationArgument == RegionIdentificationArgument.NONE) {
                sender.sendMessage(Messages.getErrorUnknownRegionQuery(pArg));
                return;
            }
        }

        switch (primary) {
            case PSET:
                setParent(sender, args, pArg);
                break;
            case CREM:
                removeChildren(sender, args, pArg);
                break;
            case PREM:
                removeParent(sender, args, pArg);
                break;
            case PCH:
                if (args.length == 3) {
                    changeParent(sender, args);
                } else {
                    Messages.sendArgumentMessage(sender, pArg, args, 3);
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + primary);
        }
    }

    private void changeParent(Player sender, String[] args) {
        var regions = regionLoader.getAllChildsOfRegionInWorld(sender, args[1]);

        var parent = regionLoader.getRegionInWorld(sender, args[1]);

        if (parent == null) {
            sender.sendMessage(Messages.getRegionNotFound(args[1]));
            return;
        }

        setParents(regions, parent);
    }

    private void removeParent(Player sender, String[] args, PrimaryActionArgument pArg) {
        if (args.length < 3) {
            sender.sendMessage(Messages.getErrorTooFewArguments(pArg));
            return;
        }
        RegionIdentificationArgument regionIdentificationArgument =
                RegionIdentificationArgument.getIdentification(args[1]);

        List<ProtectedRegion> regions;
        if (regionIdentificationArgument == RegionIdentificationArgument.COUNT) {
            regions = getCountUpRegions(sender, sender.getWorld(), args, 2);
            if (regions.isEmpty()) {
                sender.sendMessage(Messages.ERROR_NO_REGIONS_FOUND);
            }
        } else if (regionIdentificationArgument == RegionIdentificationArgument.REGEX) {
            if (args.length == 3) {
                regions = regionLoader.getRegionsWithNameRegex(sender.getWorld(), args[2]);


            } else {
                sender.sendMessage(Messages.getErrorTooManyArguments(pArg));
                return;
            }

            setParents(regions, null);
        }
    }

    private void removeChildren(Player sender, String[] args, PrimaryActionArgument pArg) {
        if (args.length < 2) {
            sender.sendMessage(Messages.getErrorTooFewArguments(pArg));
            return;
        }

        List<ProtectedRegion> regions;

        if (args.length > 2) {
            RegionIdentificationArgument regionIdentificationArgument =
                    RegionIdentificationArgument.getIdentification(args[2]);

            if (regionIdentificationArgument == RegionIdentificationArgument.REGEX) {
                regions = regionLoader.getRegionsWithNameRegex(sender.getWorld(), args[2]);

            } else if (regionIdentificationArgument == RegionIdentificationArgument.COUNT) {
                regions = getCountUpRegions(sender, sender.getWorld(), args, 3);
                if (regions.isEmpty()) {
                    sender.sendMessage(Messages.ERROR_NO_REGIONS_FOUND);
                }

            } else {
                sender.sendMessage(Messages.getErrorUnknownRegionQuery(pArg));
                return;
            }

            regions.forEach(region -> {
                ProtectedRegion parent = region.getParent();
                if (parent != null && parent.getId().equalsIgnoreCase(args[1])) {
                    try {
                        region.setParent(null);
                    } catch (ProtectedRegion.CircularInheritanceException e) {
                        //This will never happen... EVER!
                    }
                }
            });
            return;
        }
        regions = regionLoader.getAllChildsOfRegionInWorld(sender, args[1]);

        setParents(regions, null);
    }

    private void setParent(Player sender, String[] args, PrimaryActionArgument pArg) {
        if (args.length < 2) {
            Messages.sendArgumentMessage(sender, pArg, args, 2);
            return;
        }
        var parent = regionLoader.getRegionInWorld(sender, args[1]);
        List<ProtectedRegion> regions;
        if (args.length == 2) {
            regions = regionLoader.getRegionsInWorld(sender);


        } else {
            RegionIdentificationArgument regionIdentificationArgument =
                    RegionIdentificationArgument.getIdentification(args[2]);

            if (regionIdentificationArgument == RegionIdentificationArgument.REGEX) {
                regions = regionLoader.getRegionsWithNameRegex(sender.getWorld(), args[2]);
                if (regions.isEmpty()) {
                    sender.sendMessage(Messages.ERROR_NO_REGIONS_FOUND);
                }
            } else if (regionIdentificationArgument == RegionIdentificationArgument.COUNT) {
                regions = getCountUpRegions(sender, sender.getWorld(), args, 3);
            } else {
                sender.sendMessage(Messages.getErrorUnknownRegionQuery(pArg));
                return;
            }
        }

        setParents(regions, parent);
    }

    private void setParents(Collection<ProtectedRegion> collection, ProtectedRegion parent) {
        collection.forEach(region -> {
            try {
                region.setParent(parent);
            } catch (ProtectedRegion.CircularInheritanceException e) {
                //This will never happen... EVER!
            }
        });
    }

    private List<ProtectedRegion> getCountUpRegions(Player sender, org.bukkit.World world,
                                                    String[] args, int nameIndex) {
        List<ProtectedRegion> regions = Collections.emptyList();
        IntRange range;
        if (args.length == nameIndex + 2) {
            try {
                range = IntRange.parseString(args[nameIndex + 1], null);
            } catch (NumberFormatException e) {
                sender.sendMessage(Messages.ERROR_INVALID_NUMBERS);
                return Collections.emptyList();
            }

            regions = regionLoader
                    .getRegionsWithNameCountUp(sender, args[nameIndex], range);
        } else if (args.length == nameIndex + 3) {
            try {
                range = IntRange.parseString(args[nameIndex + 1], args[nameIndex + 2]);
            } catch (NumberFormatException e) {
                sender.sendMessage(Messages.ERROR_INVALID_NUMBERS);
                return Collections.emptyList();
            }

            regions = regionLoader
                    .getRegionsWithNameCountUp(sender, args[nameIndex], range);
        }
        return regions;
    }
}
