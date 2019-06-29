package de.eldoria.worldguardbatch.config;

import de.eldoria.worldguardbatch.WorldGuardBatch;
import lombok.NonNull;

public class Loader {
    /**
     * Get the error color code.
     * @return String not null
     */
    @NonNull
    public static String getErrorColor() {
        return WorldGuardBatch.config.getString(ConfigPath.errorColor, "§c");
    }

    /**
     * Get the notify color code.
     * @return String not null
     */
    @NonNull
    public static String getNotifyColor() {
        return WorldGuardBatch.config.getString(ConfigPath.notifyColor, "§d");
    }
}
