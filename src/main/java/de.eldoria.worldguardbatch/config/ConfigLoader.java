package de.eldoria.worldguardbatch.config;

import de.eldoria.worldguardbatch.WorldGuardBatch;
import lombok.NonNull;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigLoader {

    private FileConfiguration config;

    /**
     * Creates new ConfigLoader object.
     */
    public ConfigLoader() {
        reload();
    }

    /**
     * Reloads the loaded config.
     */
    public void reload() {
        config = WorldGuardBatch.getInstance().getConfig();
    }

    /**
     * Get the error color code.
     *
     * @return String not null
     */
    @NonNull
    public String getErrorColor() {
        return config.getString(ConfigPath.errorColor, "§c");
    }

    /**
     * Get the notify color code.
     *
     * @return String not null
     */
    @NonNull
    public String getNotifyColor() {
        return config.getString(ConfigPath.notifyColor, "§d");
    }
}
