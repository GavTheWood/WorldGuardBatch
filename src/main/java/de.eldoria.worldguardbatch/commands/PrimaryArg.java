package de.eldoria.worldguardbatch.commands;

public enum PrimaryArg {
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
     * Transfer membership arg.
     */
    MTRANS,

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
    public static PrimaryArg getPrimary(String s) {
        for (PrimaryArg v : PrimaryArg.values()) {
            if (v.toString().equalsIgnoreCase(s)) {
                return v;
            }
        }
        return NONE;
    }
}

