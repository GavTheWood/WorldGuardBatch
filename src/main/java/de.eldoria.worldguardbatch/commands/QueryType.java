package de.eldoria.worldguardbatch.commands;

public enum QueryType {
    /**
     * Argument for all regions query.
     */
    ALL,

    /**
     * Argument for child regions query.
     */
    CHILDREN,

    /**
     * Argument for parent query
     */
    PARENT,

    /**
     * Argument for regex query.
     */
    REGEX,

    /**
     * Argument for count query.
     */
    COUNT,

    /**
     * Argument for owner query.
     */
    OWNER;

}
