package de.eldoria.worldguardbatch.commands.subcommands;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.eldoria.worldguardbatch.RegionLoader;
import de.eldoria.worldguardbatch.commands.PrimaryActionArgument;
import de.eldoria.worldguardbatch.commands.RegionIdentificationArgument;
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
    public boolean directCommand(Player sender, String[] args) {
        if (args.length == 1) return false;

        var primary = PrimaryActionArgument.getPrimary(args[0]);

        RegionIdentificationArgument regionIdentificationArgument = RegionIdentificationArgument.NONE;

        if (args.length > 2) {
            regionIdentificationArgument = RegionIdentificationArgument.getIdentification(args[2]);

            if (regionIdentificationArgument == RegionIdentificationArgument.NONE) {
                //TODO: Invalid identification arg.
                return false;
            }
        }

        switch (primary) {
            case PSET:
                setParent(sender, args);
                break;
            case CREM:
                removeChildren(sender, args);
                break;
            case PREM:
                removeParent(sender, args);
                break;
            case PCH:
                if (args.length == 3) {
                    changeParent(sender, args);
                } else {
                    //TODO: Too many arguments.
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + primary);
        }


        return true;
    }

    private void changeParent(Player sender, String[] args) {
        var regions = regionLoader.getAllChildsOfRegionInWorld(sender.getWorld(), args[1]);

        var parent = regionLoader.getRegionInWorld(sender.getWorld(), args[1]);

        if (parent == null) {
            //TODO: new parent does not exist.
            return;
        }

        setParents(regions, parent);
    }

    private void removeParent(Player sender, String[] args) {
        if (args.length < 3) {
            //TODO: Too few arguments.
            return;
        }
        RegionIdentificationArgument regionIdentificationArgument =
                RegionIdentificationArgument.getIdentification(args[1]);

        List<ProtectedRegion> regions;
        if (regionIdentificationArgument == RegionIdentificationArgument.COUNT) {
            regions = getCountUpRegions(sender.getWorld(), args, 2);
            if (regions.isEmpty()) {
                //TODO: No regions found.
            }
        } else if (regionIdentificationArgument == RegionIdentificationArgument.REGEX) {
            if (args.length == 3) {
                regions = regionLoader.getRegionsWithNameRegex(sender.getWorld(), args[2]);


            } else {
                //TODO: too many arguments
                return;
            }

            setParents(regions, null);
        }
    }

    private void removeChildren(Player sender, String[] args) {
        if (args.length < 2) {
            //TODO: too few arguments.
            return;
        }

        List<ProtectedRegion> regions;

        if (args.length > 2) {
            RegionIdentificationArgument regionIdentificationArgument =
                    RegionIdentificationArgument.getIdentification(args[2]);

            if (regionIdentificationArgument == RegionIdentificationArgument.REGEX) {
                regions = regionLoader.getRegionsWithNameRegex(sender.getWorld(), args[2]);

            } else if (regionIdentificationArgument == RegionIdentificationArgument.COUNT) {
                regions = getCountUpRegions(sender.getWorld(), args, 3);
                if (regions.isEmpty()) {
                    //TODO: No regions found.
                }

            } else {
                //TODO: Invalid Region identification
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
        regions = regionLoader.getAllChildsOfRegionInWorld(sender.getWorld(), args[1]);

        setParents(regions, null);
    }

    private void setParent(Player sender, String[] args) {
        if (args.length < 2) {
            //TODO: Too few arguments.
            return;
        }
        var parent = regionLoader.getRegionInWorld(sender.getWorld(), args[1]);
        List<ProtectedRegion> regions;
        if (args.length == 2) {
            regions = regionLoader.getRegionsInWorld(sender.getWorld());


        } else {
            RegionIdentificationArgument regionIdentificationArgument =
                    RegionIdentificationArgument.getIdentification(args[2]);

            if (regionIdentificationArgument == RegionIdentificationArgument.REGEX) {
                regions = regionLoader.getRegionsWithNameRegex(sender.getWorld(), args[2]);
                if (regions.isEmpty()) {
                    //TODO: No regions found.
                }
            } else if (regionIdentificationArgument == RegionIdentificationArgument.COUNT) {
                regions = getCountUpRegions(sender.getWorld(), args, 3);
            } else {
                //TODO: No Identifier found.
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

    private List<ProtectedRegion> getCountUpRegions(org.bukkit.World world,
                                                    String[] args, int nameIndex) {
        List<ProtectedRegion> regions = Collections.emptyList();
        if (args.length == nameIndex + 2) {
            regions = regionLoader
                    .getRegionsWithNameCountUp(world, args[nameIndex], args[nameIndex + 1], null);
        } else if (args.length == nameIndex + 3) {
            regions = regionLoader
                    .getRegionsWithNameCountUp(world, args[nameIndex], args[nameIndex + 1], args[nameIndex + 2]);
        }
        return regions;
    }
}
