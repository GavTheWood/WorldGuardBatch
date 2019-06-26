package de.eldoria.worldguardbatch.commands;

public enum RegionIdentificationArgument {
    /**
     * No value defined. Used for error handling only.
     */
    NONE,

    /**
     * Identified by regex detection.
     */
    REGEX,

    /**
     * Identified by count detection.
     */
    COUNT,

    /**
     * Identified by region owner.
     */
    OWNER,

    /**
     * Identified by parent.
     */
    PARENT;

    /**
     * Returns the string as enum.
     *
     * @param s String which should be parsed.
     * @return The enum as string or NONE if no enum could be parsed.
     */
    public static RegionIdentificationArgument getIdentification(String s) {
        for (RegionIdentificationArgument v : RegionIdentificationArgument.values()) {
            if (v.toString().equalsIgnoreCase(s)) {
                return v;
            }
        }
        return NONE;
    }
}
