package de.eldoria.worldguardbatch.commands;

public enum IdentificationArg {
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
    OWNER;

    /**
     * Returns the string as enum.
     *
     * @param s String which should be parsed.
     * @return The enum as string or NONE if no enum could be parsed.
     */
    public static IdentificationArg getPrimary(String s) {
        for (IdentificationArg v : IdentificationArg.values()) {
            if (v.toString().equalsIgnoreCase(s)) {
                return v;
            }
        }
        return NONE;
    }
}
