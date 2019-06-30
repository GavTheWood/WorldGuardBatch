package de.eldoria.worldguardbatch.config;

import de.eldoria.worldguardbatch.WorldGuardBatch;
import lombok.NonNull;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigLoader {

    public ConfigLoader() {
        reload();
    }

    /**
     * reloads the loaded config
     */
    public void reload() {
        FileConfiguration config = WorldGuardBatch.getInstance().getConfig();
    }

    /**
     * Get the error color code.
     *
     * @return String not null
     */
    @NonNull
    public String getErrorColor() {
        return WorldGuardBatch.config.getString(ConfigPath.errorColor, "§c");
    }

    /**
     * Get the notify color code.
     *
     * @return String not null
     */
    @NonNull
    public String getNotifyColor() {
        return WorldGuardBatch.config.getString(ConfigPath.notifyColor, "§d");
    }
}
