package de.eldoria.worldguardbatch;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import de.eldoria.worldguardbatch.messages.MessagesLib;
import de.eldoria.worldguardbatch.util.IntRange;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class RegionLoader {

    private WorldGuard worldGuard;
    private RegionContainer regionContainer;

    /**
     * Creates a new Region Loader object.
     */
    RegionLoader() {
        worldGuard = WorldGuard.getInstance();
        regionContainer = worldGuard.getPlatform().getRegionContainer();
    }

    /**
     * Finds all regions where a specific Player is member or owner in a world.
     *
     * @param sender     sender of the command.
     * @param playerName Name of the player from whom the regions should found.
     * @return List of regions, where the player is a member or owner.
     */
    public List<ProtectedRegion> getRegionsFromPlayerInWorld(Player sender, String playerName) {

        var regions = getRegionsFromWorld(BukkitAdapter.adapt(sender.getWorld()));

        var p = getLocalPlayerFromName(playerName);

        if (p == null) {
            sender.sendMessage(MessagesLib.ERROR_UNKNOWN_PLAYER);
            return Collections.emptyList();
        }

        return filterRegion(regions.values(), (region -> region.isMember(p)));
    }

    /**
     * Finds all regions where a specific Player is owner in a world.
     *
     * @param sender     sender of command.
     * @param playerName Name of the player from whom the regions should found.
     * @return List of regions, where the player is a member or owner.
     */
    public List<ProtectedRegion> getOwnerRegionsFromPlayerInWorld(Player sender, String playerName) {
        var regions = getRegionsFromWorld(BukkitAdapter.adapt(sender.getWorld()));

        var p = getLocalPlayerFromName(playerName);

        if (p == null) {
            sender.sendMessage(MessagesLib.ERROR_UNKNOWN_PLAYER);
            return Collections.emptyList();
        }

        return filterRegion(regions.values(), region -> region.isOwner(p));
    }

    /**
     * Finds all regions where a specific Player is member or owner in a world.
     *
     * @param sender     sender of the command
     * @param playerName Name of the player from whom the regions should found.
     * @return List of regions, where the player is a member or owner.
     */
    public List<ProtectedRegion> getMemberRegionsFromPlayerInWorld(Player sender, String playerName) {
        var regions = getRegionsFromWorld(BukkitAdapter.adapt(sender.getWorld()));

        var p = getLocalPlayerFromName(playerName);

        if (p == null) {
            sender.sendMessage(MessagesLib.ERROR_UNKNOWN_PLAYER);
            return Collections.emptyList();
        }

        return filterRegion(regions.values(), region -> region.isMemberOnly(p));
    }

    /**
     * Find the regions, where the names match with a regex pattern.
     *
     * @param world World in which the regions should be found
     * @param regex Regex pattern which should match.
     * @return Returns list with regions with matching name pattern.
     */
    public List<ProtectedRegion> getRegionsWithNameRegex(org.bukkit.World world, String regex) {
        var regions = getRegionsFromWorld(BukkitAdapter.adapt(world));

        List<ProtectedRegion> result = new ArrayList<>();

        var pattern = Pattern.compile(regex);

        for (ProtectedRegion region : regions.values()) {
            if (pattern.matcher(region.getId()).matches()) {
                result.add(region);
            }
        }
        return result;
    }

    /**
     * Find the regions, where the names match a count up pattern. The counter is inserted at a '*'
     *
     * @param sender sender of the command.
     * @param name   name of the regions with a start for counter
     * @param range  Range of the numbers
     * @return Returns list of regions with matching name pattern.
     */
    public List<ProtectedRegion> getRegionsWithNameCountUp(Player sender,
                                                           String name, IntRange range) {
        List<ProtectedRegion> result = new ArrayList<>();


        if (range.getMin() < 0 || range.getMax() < 0 || range.getMin() > range.getMax()) {
            return Collections.emptyList();
        }

        var worldContainer = regionContainer.get(BukkitAdapter.adapt(sender.getWorld()));
        if (worldContainer == null) {
            sender.sendMessage(MessagesLib.ERROR_WORLD_NOT_FOUND);
            return Collections.emptyList();
        }

        var regions = worldContainer.getRegions();

        for (int i : range) {
            var num = String.valueOf(i);

            var regName = name.replace("*", num);

            if (regions.containsKey(regName)) {
                result.add(regions.get(regName));
            }
        }

        return result;
    }

    /**
     * Get all regions which are a child of a region.
     *
     * @param sender sender of the command
     * @param name   name of the parent
     * @return list of children of the parent.
     */
    public List<ProtectedRegion> getAllChildsOfRegionInWorld(Player sender, String name) {
        var worldContainer = regionContainer.get(BukkitAdapter.adapt(sender.getWorld()));
        if (worldContainer == null) {
            sender.sendMessage(MessagesLib.ERROR_WORLD_NOT_FOUND);
            return Collections.emptyList();
        }

        var regions = worldContainer.getRegions();

        return filterRegion(regions.values(), region -> region.getId().equalsIgnoreCase(name));
    }

    /**
     * Get all regions in a world.
     *
     * @param sender sender of the command
     * @return List of all regions in the world.
     */
    public List<ProtectedRegion> getRegionsInWorld(Player sender) {
        List<ProtectedRegion> result = new ArrayList<>();

        var worldContainer = regionContainer.get(BukkitAdapter.adapt(sender.getWorld()));

        if (worldContainer == null) {
            sender.sendMessage(MessagesLib.ERROR_WORLD_NOT_FOUND);
            return result;
        }

        var regions = worldContainer.getRegions();

        result.addAll(regions.values());

        return result;
    }

    /**
     * Get one region in a world.
     *
     * @param sender sender of the command
     * @param name   name of the region
     * @return Region or null if the region does not exists
     */
    public ProtectedRegion getRegionInWorld(Player sender, String name) {
        var worldContainer = regionContainer.get(BukkitAdapter.adapt(sender.getWorld()));

        if (worldContainer == null) {
            sender.sendMessage(MessagesLib.ERROR_WORLD_NOT_FOUND);
            return null;
        }

        return worldContainer.getRegion(name);
    }

    private Map<String, ProtectedRegion> getRegionsFromWorld(World world) {
        var worldContainer = regionContainer.get(world);
        if (worldContainer == null) {
            return Collections.emptyMap();

        }

        return worldContainer.getRegions();
    }

    private List<ProtectedRegion> filterRegion(Collection<ProtectedRegion> regions, Predicate<ProtectedRegion> filter) {
        return regions.stream()
                .filter(filter).collect(Collectors.toList());

    }

    /**
     * Get a local player object from a string.
     *
     * @param name name to lookup
     * @return Local Player object. Null if the player never joined the server.
     */
    @Nullable
    public static LocalPlayer getLocalPlayerFromName(String name) {
        if (name == null || name.isEmpty()) {

            return null;
        }

        var player = Bukkit.getPlayer(name);

        if (player == null) {
            var oPlayer = Bukkit.getOfflinePlayers();
            for (OfflinePlayer p : oPlayer) {
                if (p.getName().equalsIgnoreCase(name)) {
                    player = p.getPlayer();
                }
            }
        }

        return WorldGuardPlugin.inst().wrapPlayer(player);
    }

}
