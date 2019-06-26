package de.eldoria.worldguardbatch.commands.subcommands;

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.eldoria.worldguardbatch.RegionLoader;
import de.eldoria.worldguardbatch.commands.RegionIdentificationArgument;
import de.eldoria.worldguardbatch.commands.PrimaryActionArgument;
import de.eldoria.worldguardbatch.commands.MembershipScopeArgument;
import lombok.NonNull;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MembershipManager implements Subcommand {
    private RegionLoader regionLoader;

    /**
     * Creates a new Membership Manager instance.
     *
     * @param regionLoader RegionLoader object.
     */

    public MembershipManager(@NonNull RegionLoader regionLoader) {
        this.regionLoader = regionLoader;
    }

    @Override
    public boolean directCommand(Player sender, String[] args) {
        if (args.length < 3) return false;

        var primary = PrimaryActionArgument.getPrimary(args[0]);

        var scope = MembershipScopeArgument.getScope(args[1]);

        if (scope == MembershipScopeArgument.NONE) return false;

        RegionIdentificationArgument regionIdentificationArgument = RegionIdentificationArgument.NONE;

        if (args.length > 3) {
            regionIdentificationArgument = RegionIdentificationArgument.getIdentification(args[3]);

            if (regionIdentificationArgument == RegionIdentificationArgument.NONE) {
                //TODO: Invalid identification arg.
                return false;
            }
        }

        if (primary == PrimaryActionArgument.MREM) {
            switch (regionIdentificationArgument) {
                case NONE:
                    removePlayer(sender, args, scope);
                    break;
                case REGEX:
                    if (args.length == 5) {
                        removePlayerByRegex(sender, args, scope);
                    } else {
                        //TODO: Wrong arguments
                        return false;
                    }
                    break;
                case COUNT:
                    if (args.length == 6 || args.length == 7) {
                        removePlayerByName(sender, args, scope);
                    } else {
                        //TODO: Wrong arguments
                        return false;
                    }
                    break;
                case OWNER:
                    if (args.length == 5) {
                        removePlayerByOwner(sender, args, scope);
                    } else {
                        //TODO: Wrong arguments
                        return false;
                    }
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + regionIdentificationArgument);
            }
        } else if (primary == PrimaryActionArgument.MADD) {
            switch (regionIdentificationArgument) {
                case NONE:
                    if (scope == MembershipScopeArgument.MEMBER || scope == MembershipScopeArgument.OWNER) {
                        addPlayer(sender, args, scope);
                    } else {
                        //TODO: Wrong Scope
                        return false;
                    }
                    break;
                case REGEX:
                    if (args.length == 5) {
                        if (scope == MembershipScopeArgument.MEMBER || scope == MembershipScopeArgument.OWNER) {
                            addPlayerByRegex(sender, args, scope);
                        } else {
                            //TODO: Wrong Scope
                            return false;
                        }
                    } else {
                        //TODO: Wrong arguments
                        return false;
                    }
                    break;
                case COUNT:
                    if (args.length == 6 || args.length == 7) {
                        if (scope == MembershipScopeArgument.MEMBER || scope == MembershipScopeArgument.OWNER) {
                            addPlayerByName(sender, args, scope);
                        } else {
                            //TODO: Wrong Scope
                            return false;
                        }
                    } else {
                        //TODO: Wrong arguments
                        return false;
                    }
                    break;
                case OWNER:
                    if (args.length == 5) {
                        if (scope == MembershipScopeArgument.MEMBER || scope == MembershipScopeArgument.OWNER) {
                            addPlayerByOwner(sender, args, scope);
                        } else {
                            //TODO: Wrong Scope
                            return false;
                        }
                    } else {
                        //TODO: Wrong arguments
                        return false;
                    }
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + regionIdentificationArgument);
            }
        } else if (primary == PrimaryActionArgument.MTRANS) {
            if (args.length == 4) {
                transferMembership(sender, args, scope);
            }
        }
        return true;
    }

    private void transferMembership(Player sender, String[] args, MembershipScopeArgument scope) {
        var oldPlayer = RegionLoader.getLocalPlayerFromName(args[2]);
        var newPlayer = RegionLoader.getLocalPlayerFromName(args[3]);

        if (scope == MembershipScopeArgument.OWNER || scope == MembershipScopeArgument.ALL) {
            var ownerRegions = regionLoader.getOwnerRegionsFromPlayerInWorld(sender.getWorld(), args[2]);

            removeByScope(MembershipScopeArgument.OWNER, ownerRegions, oldPlayer);
            addByScope(MembershipScopeArgument.OWNER, ownerRegions, newPlayer);
        }

        if (scope == MembershipScopeArgument.MEMBER || scope == MembershipScopeArgument.ALL) {
            var memberRegions = regionLoader.getMemberRegionsFromPlayerInWorld(sender.getWorld(), args[2]);

            addByScope(MembershipScopeArgument.MEMBER, memberRegions, newPlayer);
            removeByScope(MembershipScopeArgument.MEMBER, memberRegions, oldPlayer);
        }
    }


    private boolean removePlayer(Player sender, String[] args, MembershipScopeArgument scope) {
        var playerName = args[2];

        List<ProtectedRegion> regions = new ArrayList<>();

        //noinspection Duplicates
        switch (scope) {
            case ALL:
                regions = regionLoader.getRegionsFromPlayerInWorld(sender.getWorld(), playerName);
                break;
            case OWNER:
                regions = regionLoader.getOwnerRegionsFromPlayerInWorld(sender.getWorld(), playerName);
                break;
            case MEMBER:
                regions = regionLoader.getMemberRegionsFromPlayerInWorld(sender.getWorld(), playerName);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + scope);
        }


        removeByScope(scope, regions, playerName);

        return true;
    }

    private boolean removePlayerByName(Player sender, String[] args, MembershipScopeArgument scope) {
        var regions = getRegionByCountUp(sender, args);

        if (regions == null) {
            //TODO: No valid number.
            return false;
        }

        removeByScope(scope, regions, args[2]);

        return true;
    }

    private boolean removePlayerByRegex(Player sender, String[] args, MembershipScopeArgument scope) {
        var regions = regionLoader.getRegionsWithNameRegex(sender.getWorld(), args[4]);

        removeByScope(scope, regions, args[2]);

        return true;
    }

    private boolean removePlayerByOwner(Player sender, String[] args, MembershipScopeArgument scope) {
        var regions = regionLoader.getOwnerRegionsFromPlayerInWorld(sender.getWorld(), args[4]);

        removeByScope(scope, regions, args[2]);

        return true;
    }


    private boolean addPlayer(Player sender, String[] args, MembershipScopeArgument scope) {
        var regions = regionLoader.getRegionsInWorld(sender.getWorld());

        addByScope(scope, regions, args[2]);

        return true;
    }

    private boolean addPlayerByName(Player sender, String[] args, MembershipScopeArgument scope) {
        var regions = getRegionByCountUp(sender, args);

        if (regions == null) {
            //TODO: No valid number.
            return false;
        }

        addByScope(scope, regions, args[2]);

        return true;
    }

    private boolean addPlayerByRegex(Player sender, String[] args, MembershipScopeArgument scope) {
        var regions = regionLoader.getRegionsWithNameRegex(sender.getWorld(), args[4]);

        addByScope(scope, regions, args[2]);

        return true;
    }

    private boolean addPlayerByOwner(Player sender, String[] args, MembershipScopeArgument scope) {
        var regions = regionLoader.getOwnerRegionsFromPlayerInWorld(sender.getWorld(), args[4]);

        addByScope(scope, regions, args[2]);

        return true;
    }

    private void removePlayerFromRegion(LocalPlayer player, ProtectedRegion region) {
        removeMemberFromRegion(player, region);
        removeOwnerFromRegion(player, region);
    }

    private void removeOwnerFromRegion(LocalPlayer player, ProtectedRegion region) {
        if (region.isOwner(player)) {
            var owners = region.getOwners();
            owners.removePlayer(player);
            region.setOwners(owners);
        }
    }

    private void removeMemberFromRegion(LocalPlayer player, ProtectedRegion region) {
        if (region.isMemberOnly(player)) {
            var members = region.getMembers();
            members.removePlayer(player);
            region.setMembers(members);
        }
    }

    private void addOwnerToRegion(LocalPlayer player, ProtectedRegion region) {
        var owners = region.getOwners();
        owners.addPlayer(player);
        region.setOwners(owners);
    }

    private void addMemberToRegion(LocalPlayer player, ProtectedRegion region) {
        var members = region.getMembers();
        members.addPlayer(player);
        region.setMembers(members);
    }

    private void removeByScope(MembershipScopeArgument scope, List<ProtectedRegion> regions, String playerName) {
        var localPlayer = RegionLoader.getLocalPlayerFromName(playerName);

        removeByScope(scope, regions, localPlayer);

    }

    private void removeByScope(MembershipScopeArgument scope, List<ProtectedRegion> regions, LocalPlayer player) {

        for (var region : regions) {

            switch (scope) {
                case ALL:
                    removePlayerFromRegion(player, region);
                    break;
                case OWNER:
                    removeOwnerFromRegion(player, region);
                    break;
                case MEMBER:
                    removeMemberFromRegion(player, region);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + scope);
            }

        }
    }

    private void addByScope(MembershipScopeArgument scope, List<ProtectedRegion> regions, String playerName) {
        var localPlayer = RegionLoader.getLocalPlayerFromName(playerName);

        addByScope(scope, regions, localPlayer);
    }

    private void addByScope(MembershipScopeArgument scope, List<ProtectedRegion> regions, LocalPlayer player) {

        for (var region : regions) {
            switch (scope) {
                case OWNER:
                    addOwnerToRegion(player, region);
                    break;
                case MEMBER:
                    addMemberToRegion(player, region);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + scope);
            }
        }
    }

    private List<ProtectedRegion> getRegionByCountUp(Player sender, String[] args) {
        var name = args[4];

        if (args.length == 6) {
            return regionLoader.getRegionsWithNameCountUp(sender.getWorld(), name, args[5], null);
        } else if (args.length == 7) {
            return regionLoader.getRegionsWithNameCountUp(sender.getWorld(), name, args[5], args[6]);
        }
        return Collections.emptyList();
    }
}
