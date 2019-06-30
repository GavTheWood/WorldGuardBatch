package de.eldoria.worldguardbatch;

import de.eldoria.worldguardbatch.commands.basecommand.BaseCommand;
import de.eldoria.worldguardbatch.config.ConfigLoader;
import de.eldoria.worldguardbatch.messages.MessageSender;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class WorldGuardBatch extends JavaPlugin {

    private static WorldGuardBatch instance;

    private ConfigLoader config;


    private boolean loaded;

    /**
     * Get the Plugin instance.
     *
     * @return Plugin instance
     */
    public static WorldGuardBatch getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        if (loaded) {
            reload();
        }

        if (!loaded) {
            saveDefaultConfig();
            instance = this;
            config = new ConfigLoader();
            RegionLoader regionLoader = new RegionLoader();
            this.getCommand("wgb").setExecutor(new BaseCommand(regionLoader));
            Bukkit.getLogger().info("World Guard Batch started");
            loaded = true;
        }
    }

    /**
     * Reloads the plugin.
     */
    public void reload() {
        config.reload();
        MessageSender.getInstance().reload();
        Bukkit.getLogger().info("World Guard Batch reloaded");
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("World Guard Batch stopped");
    }

    /**
     * Get the configLoader instance.
     * @return Config loader instance.
     */
    public ConfigLoader getConfigData() {
        return config;
    }
}
