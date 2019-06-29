package de.eldoria.worldguardbatch.commands.basecommand;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.eldoria.worldguardbatch.RegionLoader;
import de.eldoria.worldguardbatch.commands.PrimaryActionArgument;
import de.eldoria.worldguardbatch.commands.RegionIdentificationArgument;
import de.eldoria.worldguardbatch.messages.MessageSender;
import de.eldoria.worldguardbatch.util.IntRange;
import lombok.NonNull;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

class ParentManager implements Subcommand {
    private RegionLoader regionLoader;
    private MessageSender ms;

    /**
     * Creates new Parent Manager instance.
     *
     * @param regionLoader Region Loader instance
     */
    public ParentManager(@NonNull RegionLoader regionLoader) {
        this.ms = MessageSender.getInstance();
        this.regionLoader = regionLoader;
    }

    @Override
    public void directCommand(Player sender, PrimaryActionArgument pArg, String[] args) {
        if (args.length == 1) {
            ms.sendTooFewArgumentsError(sender, pArg);
            return;
        }

        var primary = PrimaryActionArgument.getPrimary(args[0]);

        RegionIdentificationArgument regionIdentificationArgument;

        if (args.length > 2) {
            regionIdentificationArgument = RegionIdentificationArgument.getIdentification(args[2]);

            if (regionIdentificationArgument == RegionIdentificationArgument.NONE) {
                ms.sendUnknownRegionQueryError(sender, pArg);
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
                    ms.sendArgumentMessage(sender, pArg, args, 3);
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
            ms.sendRegionNotFoundError(sender, args[1]);
            return;
        }

        setParents(sender, regions, parent);
    }

    private void removeParent(Player sender, String[] args, PrimaryActionArgument pArg) {
        if (args.length < 3) {
            ms.sendTooFewArgumentsError(sender, pArg);
            return;
        }
        RegionIdentificationArgument regionIdentificationArgument =
                RegionIdentificationArgument.getIdentification(args[1]);

        List<ProtectedRegion> regions;
        if (regionIdentificationArgument == RegionIdentificationArgument.COUNT) {
            regions = getCountUpRegions(sender, sender.getWorld(), args, 2);
            if (regions.isEmpty()) {
                ms.sendNoRegionsFoundError(sender);
            }
        } else if (regionIdentificationArgument == RegionIdentificationArgument.REGEX) {
            if (args.length == 3) {
                regions = regionLoader.getRegionsWithNameRegex(sender.getWorld(), args[2]);


            } else {
                ms.sendTooManyArgumentsError(sender, pArg);
                return;
            }

            setParents(sender, regions, null);
        }
    }

    private void removeChildren(Player sender, String[] args, PrimaryActionArgument pArg) {
        if (args.length < 2) {
            ms.sendTooFewArgumentsError(sender, pArg);
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
                    ms.sendNoRegionsFoundError(sender);
                    return;
                }

            } else {
                ms.sendUnknownRegionQueryError(sender, pArg);
                return;
            }

            regions.forEach(region -> {
                ProtectedRegion parent = region.getParent();
                if (parent != null && parent.getId().equalsIgnoreCase(args[1])) {
                    try {
                        region.setParent(null);
                        ms.sendModifiedMessage(sender, region.getId());
                    } catch (ProtectedRegion.CircularInheritanceException e) {
                        //This will never happen... EVER!
                    }
                }
            });
            ms.sendTotalModifiedMessage(sender, regions.size());

            return;
        }
        regions = regionLoader.getAllChildsOfRegionInWorld(sender, args[1]);

        setParents(sender, regions, null);
    }

    private void setParent(Player sender, String[] args, PrimaryActionArgument pArg) {
        if (args.length < 2) {
            ms.sendArgumentMessage(sender, pArg, args, 2);
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
                    ms.sendNoRegionsFoundError(sender);
                }
            } else if (regionIdentificationArgument == RegionIdentificationArgument.COUNT) {
                regions = getCountUpRegions(sender, sender.getWorld(), args, 3);
            } else {
                ms.sendUnknownRegionQueryError(sender, pArg);
                return;
            }
        }

        setParents(sender, regions, parent);
    }

    private void setParents(Player p, Collection<ProtectedRegion> regions, ProtectedRegion parent) {
        regions.forEach(region -> {
            try {
                region.setParent(parent);
                ms.sendModifiedMessage(p, region.getId());
            } catch (ProtectedRegion.CircularInheritanceException e) {
                //This will never happen... EVER!
            }
        });
        ms.sendTotalModifiedMessage(p, regions.size());

    }

    private List<ProtectedRegion> getCountUpRegions(Player sender, org.bukkit.World world,
                                                    String[] args, int nameIndex) {
        List<ProtectedRegion> regions = Collections.emptyList();
        IntRange range;
        if (args.length == nameIndex + 2) {
            try {
                range = IntRange.parseString(args[nameIndex + 1], null);
            } catch (NumberFormatException e) {
                ms.sendInvalidNumberError(sender);
                return Collections.emptyList();
            }

            regions = regionLoader
                    .getRegionsWithNameCountUp(sender, args[nameIndex], range);
        } else if (args.length == nameIndex + 3) {
            try {
                range = IntRange.parseString(args[nameIndex + 1], args[nameIndex + 2]);
            } catch (NumberFormatException e) {
                ms.sendInvalidNumberError(sender);
                return Collections.emptyList();
            }

            regions = regionLoader
                    .getRegionsWithNameCountUp(sender, args[nameIndex], range);
        }
        return regions;
    }
}
