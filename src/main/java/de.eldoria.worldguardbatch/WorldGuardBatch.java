package de.eldoria.worldguardbatch;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import de.eldoria.worldguardbatch.commands.BaseCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class WorldGuardBatch extends JavaPlugin {

    private RegionContainer wg;

    private RegionLoader regionLoader;

    private PluginManager pm;

    @Override
    public void onEnable() {
        pm = Bukkit.getPluginManager();

        wg = WorldGuard.getInstance().getPlatform().getRegionContainer();

        regionLoader = new RegionLoader();


        this.getCommand("wgb").setExecutor(new BaseCommand(regionLoader));
    }

    @Override
    public void onDisable() {


    }


}
