package de.eldoria.worldguardbatch.commands;

public enum ScopeArg {
    /**
     * No value defined. Used for error handling only.
     */
    NONE,
    /**
     * Scope for member and owner.
     */
    ALL,
    /**
     * Scope for only owner.
     */
    OWNER,
    /**
     * Scope for only member.
     */
    MEMBER;


    /**
     * Returns the string as enum.
     *
     * @param s String which should be parsed.
     * @return The enum as string or NONE if no enum could be parsed.
     */
    public static ScopeArg getScope(String s) {
        for (ScopeArg v : ScopeArg.values()) {
            if (v.toString().equalsIgnoreCase(s)) {
                return v;
            }
        }
        return NONE;
    }
}
