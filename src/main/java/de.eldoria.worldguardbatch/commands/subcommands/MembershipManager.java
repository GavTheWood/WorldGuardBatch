package de.eldoria.worldguardbatch.commands.subcommands;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.eldoria.worldguardbatch.RegionLoader;
import de.eldoria.worldguardbatch.commands.ScopeArg;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class MembershipManager implements Subcommand {
    private RegionLoader regionLoader;

    public MembershipManager(RegionLoader regionLoader) {
        this.regionLoader = regionLoader;
    }

    @Override
    public boolean directCommand(Player sender, String[] args) {
        if (args.length < 3) return false;

        String playerName = args[2];

        var scope = ScopeArg.getScope(args[1]);

        if (scope == ScopeArg.NONE) return false;

        List<ProtectedRegion> regions;

        if (args.length == 3) {
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
        }

    }
}
