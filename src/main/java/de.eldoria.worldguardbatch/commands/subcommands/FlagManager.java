package de.eldoria.worldguardbatch.commands.subcommands;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.FlagContext;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.InvalidFlagFormat;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.eldoria.worldguardbatch.RegionLoader;
import de.eldoria.worldguardbatch.commands.PrimaryActionArgument;
import lombok.NonNull;
import org.bukkit.entity.Player;

import java.util.StringJoiner;

import static de.eldoria.worldguardbatch.messages.MessageSender.*;

public class FlagManager implements Subcommand {
    private RegionLoader regionLoader;
    private FlagRegistry flagRegistry;

    /**
     * Creates new Flag Manager instance.
     *
     * @param regionLoader Region Loader instance
     */
    public FlagManager(@NonNull RegionLoader regionLoader) {
        this.regionLoader = regionLoader;
        flagRegistry = WorldGuard.getInstance().getFlagRegistry();
    }

    @Override
    public void directCommand(Player sender, PrimaryActionArgument pArg, String[] args) {
        if (args.length < 4) {
            sendTooFewArgumentError(sender, pArg);
            return;
        }

        var flag = Flags.fuzzyMatchFlag(flagRegistry, args[3]);

        if (flag == null) {
            sendUnkownFlagError(sender);
            return;
        }

        PrimaryActionArgument paa = PrimaryActionArgument.getPrimary(args[0]);

        String regexPattern = args[2];


        var regions = regionLoader.getRegionsWithNameRegex(sender.getWorld(), regexPattern);
        String input = null;
        if (paa == PrimaryActionArgument.FSET) {
            StringJoiner joiner = new StringJoiner(" ");
            for (int i = 4; i < args.length; i++) {
                joiner.add(args[i]);
            }
            input = joiner.toString();
        }

        var actor = BukkitAdapter.adapt(sender);

        var inputValue = input;

        regions.forEach(region -> {
            try {
                setFlag(region, flag, actor, inputValue);
                sendModifiedMessage(sender, region.getId());
            } catch (InvalidFlagFormat e) {
                sendWrongFlagValueError(sender);
            }
        });

        sendTotalModifiedMessage(sender, regions.size());
    }

    private static <V> void setFlag(ProtectedRegion region, Flag<V> flag, Actor sender, String value)
            throws InvalidFlagFormat {
        region.setFlag(flag, flag.parseInput(FlagContext
                .create()
                .setSender(sender)
                .setInput(value)
                .setObject("region", region)
                .build()));
    }

}
