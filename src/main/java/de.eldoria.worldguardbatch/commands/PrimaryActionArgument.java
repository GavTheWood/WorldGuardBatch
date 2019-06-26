package de.eldoria.worldguardbatch.commands;

public enum PrimaryActionArgument {
    /**
     * No value defined. Used for error handling only.
     */
    NONE,
    /**
     * Remove membership arg.
     */
    MREM,

    /**
     * Add membership arg.
     */
    MADD,

    /**
     * Change priority arg.
     */
    PRIO,

    /**
     * Set parent arg.
     */
    PSET,

    /**
     * Remove children arg.
     */
    CREM,

    /**
     * Remove parent arg.
     */
    PREM,

    /**
     * Change parent arg.
     */
    PCH,

    /**
     * Transfer membership arg.
     */
    MTRANS,

    /**
     * Set flag arg.
     */
    FSET,

    /**
     * Remove flag arg.
     */
    FREM,

    /**
     * Call help command.
     */
    HELP;

    /**
     * Returns the string as enum.
     *
     * @param s String which should be parsed.
     * @return The enum as string or NONE if no enum could be parsed.
     */
    public static PrimaryActionArgument getPrimary(String s) {
        for (PrimaryActionArgument v : PrimaryActionArgument.values()) {
            if (v.toString().equalsIgnoreCase(s)) {
                return v;
            }
        }
        return NONE;
    }
}

