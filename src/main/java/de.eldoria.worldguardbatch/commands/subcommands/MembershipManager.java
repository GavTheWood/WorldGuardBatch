package de.eldoria.worldguardbatch.commands.subcommands;

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.eldoria.worldguardbatch.RegionLoader;
import de.eldoria.worldguardbatch.commands.RegionIdentificationArgument;
import de.eldoria.worldguardbatch.commands.PrimaryActionArgument;
import de.eldoria.worldguardbatch.commands.MembershipScopeArgument;
import de.eldoria.worldguardbatch.messages.MessageSender;
import de.eldoria.worldguardbatch.util.IntRange;
import lombok.NonNull;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class MembershipManager implements Subcommand {
    private RegionLoader regionLoader;
    private MessageSender ms;

    /**
     * Creates a new Membership Manager instance.
     *
     * @param regionLoader RegionLoader object.
     */
    public MembershipManager(@NonNull RegionLoader regionLoader) {
        this.regionLoader = regionLoader;
        this.ms = MessageSender.getInstance();
    }

    @Override
    public void directCommand(Player sender, PrimaryActionArgument pArg, String[] args) {
        if (args.length < 3) {
            ms.sendTooFewArgumentsError(sender, pArg);
            return;
        }

        var primary = PrimaryActionArgument.getPrimary(args[0]);

        var scope = MembershipScopeArgument.getScope(args[1]);

        if (scope == MembershipScopeArgument.NONE) {
            ms.sendUnkownMembershipScopeError(sender, pArg);
            return;
        }

        RegionIdentificationArgument regionIdentificationArgument = RegionIdentificationArgument.NONE;

        if (args.length > 3) {
            regionIdentificationArgument = RegionIdentificationArgument.getIdentification(args[3]);

            if (regionIdentificationArgument == RegionIdentificationArgument.NONE) {
                ms.sendUnknownRegionQueryError(sender, pArg);
                return;
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
                        ms.sendArgumentMessage(sender, pArg, args, 5);
                        return;
                    }
                    break;
                case COUNT:
                    if (args.length == 6 || args.length == 7) {
                        removePlayerByName(sender, args, scope);
                    } else {
                        ms.sendArgumentMessage(sender, pArg, args, 6, 7);
                        return;
                    }
                    break;
                case OWNER:
                    if (args.length == 5) {
                        removePlayerByOwner(sender, args, scope);
                    } else {
                        ms.sendArgumentMessage(sender, pArg, args, 5);
                        return;
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
                        ms.sendUnkownMembershipScopeError(sender, pArg);
                        return;
                    }
                    break;
                case REGEX:
                    if (args.length == 5) {
                        if (scope == MembershipScopeArgument.MEMBER || scope == MembershipScopeArgument.OWNER) {
                            addPlayerByRegex(sender, args, scope);
                        } else {
                            ms.sendUnkownMembershipScopeError(sender, pArg);
                            return;
                        }
                    } else {
                        ms.sendArgumentMessage(sender, pArg, args, 5);
                        return;
                    }
                    break;
                case COUNT:
                    if (args.length == 6 || args.length == 7) {
                        if (scope == MembershipScopeArgument.MEMBER || scope == MembershipScopeArgument.OWNER) {
                            addPlayerByName(sender, args, scope);
                        } else {
                            ms.sendUnkownMembershipScopeError(sender, pArg);
                            return;
                        }
                    } else {
                        ms.sendArgumentMessage(sender, pArg, args, 6, 7);
                        return;
                    }
                    break;
                case OWNER:
                    if (args.length == 5) {
                        if (scope == MembershipScopeArgument.MEMBER || scope == MembershipScopeArgument.OWNER) {
                            addPlayerByOwner(sender, args, scope);
                        } else {
                            ms.sendUnkownMembershipScopeError(sender, pArg);
                            return;
                        }
                    } else {
                        ms.sendArgumentMessage(sender, pArg, args, 5);
                        return;
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
    }

    private void transferMembership(Player sender, String[] args, MembershipScopeArgument scope) {
        var oldPlayer = RegionLoader.getLocalPlayerFromName(args[2]);
        var newPlayer = RegionLoader.getLocalPlayerFromName(args[3]);

        if (oldPlayer == null || newPlayer == null) {
            ms.sendUnknownPlayerError(sender);
            return;
        }

        if (scope == MembershipScopeArgument.OWNER || scope == MembershipScopeArgument.ALL) {
            var ownerRegions = regionLoader.getOwnerRegionsFromPlayerInWorld(sender, args[2]);

            removeByScope(sender, MembershipScopeArgument.OWNER, ownerRegions, oldPlayer);
            addByScope(sender, MembershipScopeArgument.OWNER, ownerRegions, newPlayer);
        }

        if (scope == MembershipScopeArgument.MEMBER || scope == MembershipScopeArgument.ALL) {
            var memberRegions = regionLoader.getMemberRegionsFromPlayerInWorld(sender, args[2]);

            addByScope(sender, MembershipScopeArgument.MEMBER, memberRegions, newPlayer);
            removeByScope(sender, MembershipScopeArgument.MEMBER, memberRegions, oldPlayer);
        }
    }


    private void removePlayer(Player sender, String[] args, MembershipScopeArgument scope) {
        var playerName = args[2];

        List<ProtectedRegion> regions;

        //noinspection Duplicates
        switch (scope) {
            case ALL:
                regions = regionLoader.getRegionsFromPlayerInWorld(sender, playerName);
                break;
            case OWNER:
                regions = regionLoader.getOwnerRegionsFromPlayerInWorld(sender, playerName);
                break;
            case MEMBER:
                regions = regionLoader.getMemberRegionsFromPlayerInWorld(sender, playerName);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + scope);
        }


        removeByScope(sender, scope, regions, playerName);
    }

    private void removePlayerByName(Player sender, String[] args, MembershipScopeArgument scope) {
        var regions = getRegionByCountUp(sender, args);

        if (regions == null) {
            ms.sendInvalidNumberError(sender);
            return;
        }

        removeByScope(sender, scope, regions, args[2]);

    }

    private void removePlayerByRegex(Player sender, String[] args, MembershipScopeArgument scope) {
        var regions = regionLoader.getRegionsWithNameRegex(sender.getWorld(), args[4]);

        removeByScope(sender, scope, regions, args[2]);
    }

    private void removePlayerByOwner(Player sender, String[] args, MembershipScopeArgument scope) {
        var regions = regionLoader.getOwnerRegionsFromPlayerInWorld(sender, args[4]);

        removeByScope(sender, scope, regions, args[2]);
    }


    private void addPlayer(Player sender, String[] args, MembershipScopeArgument scope) {
        var regions = regionLoader.getRegionsInWorld(sender);

        addByScope(sender, scope, regions, args[2]);
    }

    private void addPlayerByName(Player sender, String[] args, MembershipScopeArgument scope) {
        var regions = getRegionByCountUp(sender, args);

        if (regions == null) {
            ms.sendInvalidNumberError(sender);
            return;
        }

        addByScope(sender, scope, regions, args[2]);
    }

    private void addPlayerByRegex(Player sender, String[] args, MembershipScopeArgument scope) {
        var regions = regionLoader.getRegionsWithNameRegex(sender.getWorld(), args[4]);

        addByScope(sender, scope, regions, args[2]);
    }

    private void addPlayerByOwner(Player sender, String[] args, MembershipScopeArgument scope) {
        var regions = regionLoader.getOwnerRegionsFromPlayerInWorld(sender, args[4]);

        addByScope(sender, scope, regions, args[2]);
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

    private void removeByScope(Player sender, MembershipScopeArgument scope,
                               List<ProtectedRegion> regions, String playerName) {
        var localPlayer = RegionLoader.getLocalPlayerFromName(playerName);

        if (localPlayer == null) {
            ms.sendUnknownPlayerError(sender);
            return;
        }


        removeByScope(sender, scope, regions, localPlayer);

    }

    private void removeByScope(Player p, MembershipScopeArgument scope,
                               List<ProtectedRegion> regions, LocalPlayer player) {

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
            ms.sendModifiedMessage(p, region.getId());
        }
        ms.sendTotalModifiedMessage(p, regions.size());
    }

    private void addByScope(Player sender, MembershipScopeArgument scope,
                            List<ProtectedRegion> regions, String playerName) {
        var localPlayer = RegionLoader.getLocalPlayerFromName(playerName);

        if (localPlayer == null) {
            ms.sendUnknownPlayerError(sender);
            return;
        }

        addByScope(sender, scope, regions, localPlayer);
    }

    private void addByScope(Player p, MembershipScopeArgument scope,
                            List<ProtectedRegion> regions, LocalPlayer player) {

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

            ms.sendModifiedMessage(p, region.getId());
        }
        ms.sendTotalModifiedMessage(p, regions.size());
    }

    private List<ProtectedRegion> getRegionByCountUp(Player sender, String[] args) {
        var name = args[4];

        IntRange range;
        if (args.length == 6) {
            try {
                range = IntRange.parseString(args[5], null);
            } catch (NumberFormatException e) {
                ms.sendInvalidNumberError(sender);
                return Collections.emptyList();
            }
            return regionLoader.getRegionsWithNameCountUp(sender, name, range);
        } else if (args.length == 7) {
            try {
                range = IntRange.parseString(args[5], args[6]);
            } catch (NumberFormatException e) {
                ms.sendInvalidNumberError(sender);
                return Collections.emptyList();
            }

            return regionLoader.getRegionsWithNameCountUp(sender, name, range);
        }
        return Collections.emptyList();
    }
}
