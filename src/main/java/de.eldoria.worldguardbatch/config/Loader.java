package de.eldoria.worldguardbatch.config;

import de.eldoria.worldguardbatch.WorldGuardBatch;

public class Loader {
    public static String getErrorColor() {
        return WorldGuardBatch.config.getString(ConfigPath.errorColor);
    }

    public static String getNotifyColor() {
        return WorldGuardBatch.config.getString(ConfigPath.notifyColor);
    }
}
