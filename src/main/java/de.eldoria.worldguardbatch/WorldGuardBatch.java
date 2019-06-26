package de.eldoria.worldguardbatch;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.plugin.java.JavaPlugin;

public class WorldGuardBatch extends JavaPlugin {

    RegionContainer wg;

    RegionLoader regionLoader;

    @Override
    public void onEnable() {
        wg = WorldGuard.getInstance().getPlatform().getRegionContainer();

        regionLoader = new RegionLoader();

    }

    @Override
    public void onDisable() {


    }
}
