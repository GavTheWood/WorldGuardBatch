package de.eldoria.worldguardbatch.commands.subcommands;

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.eldoria.worldguardbatch.RegionLoader;
import de.eldoria.worldguardbatch.commands.IdentificationArg;
import de.eldoria.worldguardbatch.commands.PrimaryArg;
import de.eldoria.worldguardbatch.commands.ScopeArg;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class MembershipManager implements Subcommand {
    private RegionLoader regionLoader;

    /**
     * Creates a new Membership Manager instance.
     * @param regionLoader RegionLoader object.
     */

    public MembershipManager(@Nonnull RegionLoader regionLoader) {
        this.regionLoader = regionLoader;
    }

    @Override
    public boolean directCommand(Player sender, String[] args) {
        if (args.length < 3) return false;

        var primary = PrimaryArg.getPrimary(args[0]);

        var scope = ScopeArg.getScope(args[1]);

        if (scope == ScopeArg.NONE) return false;

        IdentificationArg identificationArg = IdentificationArg.NONE;

        if (args.length > 3) {
            identificationArg = IdentificationArg.getIdentification(args[3]);

            if (identificationArg == IdentificationArg.NONE) {
                //TODO: Invalid identification arg.
                return false;
            }
        }

        if (primary == PrimaryArg.MREM) {
            switch (identificationArg) {
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
            }
        } else if (primary == PrimaryArg.MADD) {
            switch (identificationArg) {
                case NONE:
                    if (scope == ScopeArg.MEMBER || scope == ScopeArg.OWNER) {
                        addPlayer(sender, args, scope);
                    } else {
                        //TODO: Wrong Scope
                        return false;
                    }
                    break;
                case REGEX:
                    if (args.length == 5) {
                        if (scope == ScopeArg.MEMBER || scope == ScopeArg.OWNER) {
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
                        if (scope == ScopeArg.MEMBER || scope == ScopeArg.OWNER) {
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
                        if (scope == ScopeArg.MEMBER || scope == ScopeArg.OWNER) {
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
            }
        }
        return true;
    }


    private boolean removePlayer(Player sender, String[] args, ScopeArg scope) {
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
        }


        removeByScope(scope, regions, playerName);

        return true;
    }

    private boolean removePlayerByName(Player sender, String[] args, ScopeArg scope) {
        var regions = getRegionByCountUp(sender, args);

        if (regions == null) {
            //TODO: No valid number.
            return false;
        }

        removeByScope(scope, regions, args[2]);

        return true;
    }

    private boolean removePlayerByRegex(Player sender, String[] args, ScopeArg scope) {
        var regions = regionLoader.getRegionsWithNameRegex(sender.getWorld(), args[4]);

        removeByScope(scope, regions, args[2]);

        return true;
    }

    private boolean removePlayerByOwner(Player sender, String[] args, ScopeArg scope) {
        var regions = regionLoader.getOwnerRegionsFromPlayerInWorld(sender.getWorld(), args[4]);

        removeByScope(scope, regions, args[2]);

        return true;
    }


    private boolean addPlayer(Player sender, String[] args, ScopeArg scope) {
        var regions = regionLoader.getRegionsInWorld(sender.getWorld());

        addByScope(scope, regions, args[2]);

        return true;
    }

    private boolean addPlayerByName(Player sender, String[] args, ScopeArg scope) {
        var regions = getRegionByCountUp(sender, args);

        if (regions == null) {
            //TODO: No valid number.
            return false;
        }

        addByScope(scope, regions, args[2]);

        return true;
    }

    private boolean addPlayerByRegex(Player sender, String[] args, ScopeArg scope) {
        var regions = regionLoader.getRegionsWithNameRegex(sender.getWorld(), args[4]);

        addByScope(scope, regions, args[2]);

        return true;
    }

    private boolean addPlayerByOwner(Player sender, String[] args, ScopeArg scope) {
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

    private void removeByScope(ScopeArg scope, List<ProtectedRegion> regions, String playerName) {
        var localPlayer = RegionLoader.getLocalPlayerFromName(playerName);

        for (var region : regions) {

            switch (scope) {
                case ALL:
                    removePlayerFromRegion(localPlayer, region);
                    break;
                case OWNER:
                    removeOwnerFromRegion(localPlayer, region);
                    break;
                case MEMBER:
                    removeMemberFromRegion(localPlayer, region);
                    break;
            }

        }
    }

    private void addByScope(ScopeArg scope, List<ProtectedRegion> regions, String playerName) {
        var localPlayer = RegionLoader.getLocalPlayerFromName(playerName);

        for (var region : regions) {
            switch (scope) {
                case OWNER:
                    addOwnerToRegion(localPlayer, region);
                    break;
                case MEMBER:
                    addMemberToRegion(localPlayer, region);
                    break;
            }
        }
    }

    private List<ProtectedRegion> getRegionByCountUp(Player sender, String[] args) {
        var playerName = args[3];

        int min = 0;
        int max = 0;

        try {
            max = Integer.parseInt(args[5]);

        } catch (NumberFormatException e) {
            //TODO: Not a valid number
            return null;
        }
        if (args.length == 7) {
            min = max;
            try {
                max = Integer.parseInt(args[6]);

            } catch (NumberFormatException e) {
                //TODO: not a valid number
                return null;
            }
        }

        return regionLoader.getRegionsWithNameCountUp(sender.getWorld(), playerName, min, max);
    }
}
