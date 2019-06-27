package de.eldoria.worldguardbatch.commands;

public enum CheckArgument {
    /**
     * Used as undefined.
     */
    NONE,

    /**
     * Argument for all regions check.
     */
    ALL,

    /**
     * Argument for child regions check.
     */
    CHILDS,

    /**
     * Argument for regex check.
     */
    REGEX,

    /**
     * Argument for count check.
     */
    COUNT,

    /**
     * Argument for owner check.
     */
    OWNER;

    /**
     * Returns the string as enum.
     *
     * @param s String which should be parsed.
     * @return The enum as string or NONE if no enum could be parsed.
     */
    public static CheckArgument getCheckScope(String s) {
        for (CheckArgument v : CheckArgument.values()) {
            if (v.toString().equalsIgnoreCase(s)) {
                return v;
            }
        }
        return NONE;
    }

}
