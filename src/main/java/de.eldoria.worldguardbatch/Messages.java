package de.eldoria.worldguardbatch;

import de.eldoria.worldguardbatch.commands.PrimaryActionArgument;
import org.bukkit.event.world.StructureGrowEvent;

public final class Messages {
    private static String ARGUMENTS = "\nQueries:";
    private static String REGEX_LOOKUP_SYNTAX = "\nRegex Lookup: regex [regex pattern]";
    private static String COUNT_LOOKUP_SYNTAX = "\nCount Lookup: count [count 1] <count 2>";
    private static String OWNER_LOOKUP_SYNTAX = "\nOwner Lookup: owner [region owner name]";
    private static String WORLD_LOOKUP_SYNTAX = "\nWorld Lookup: all";
    private static String CHILD_LOOKUP_SYNTAX = "\nChild Lookup: children [regionId]";
    private static String PARENT_LOOKUP_SYNTAX = "\nParent Lookup: parent [regionId]";

    private static CommandText MREM = new CommandText("Remove a member from regions.",
            "/wgb mrem [all/member/owner] [playerToRemove] <query>\n"
                    + ARGUMENTS
                    + REGEX_LOOKUP_SYNTAX
                    + COUNT_LOOKUP_SYNTAX
                    + OWNER_LOOKUP_SYNTAX);

    private static CommandText MADD = new CommandText("Add a player to a region.",
            "/wgb madd [member/owner] [playerToAdd] <query>"
                    + ARGUMENTS
                    + REGEX_LOOKUP_SYNTAX
                    + COUNT_LOOKUP_SYNTAX
                    + OWNER_LOOKUP_SYNTAX);

    private static CommandText MTRANS
            = new CommandText("Transfers the membership of a region from a player to another player.",
            "/wgb mtrans [all/owner/member] [oldPlayer] [newPlayer]");

    private static CommandText PRIO = new CommandText("Changes the priority of a region.",
            "/wgb prio [priority] <query>"
                    + ARGUMENTS
                    + PARENT_LOOKUP_SYNTAX
                    + REGEX_LOOKUP_SYNTAX
                    + COUNT_LOOKUP_SYNTAX);

    private static CommandText PSET = new CommandText("Set or override the parent of a region.",
            "/wgb pset [parent] <query>"
                    + ARGUMENTS
                    + REGEX_LOOKUP_SYNTAX
                    + COUNT_LOOKUP_SYNTAX);

    private static CommandText PCH = new CommandText("Changes the parent of all Children to a new parent.",
            "/wgb pch [oldParent] [newParent]");

    private static CommandText CREM = new CommandText("Removes parent from region depending on parent region.",
            "/wgb crem <query>"
                    + ARGUMENTS
                    + REGEX_LOOKUP_SYNTAX
                    + COUNT_LOOKUP_SYNTAX);

    private static CommandText PREM = new CommandText("Removes the parent from regions.",
            "/wgb prem [query]"
                    + ARGUMENTS
                    + REGEX_LOOKUP_SYNTAX
                    + COUNT_LOOKUP_SYNTAX);

    private static CommandText FSET = new CommandText("Sets a flag.",
            "/wgb fet regex [regex pattern] [flagname] [flagvalue]");

    private static CommandText FREM = new CommandText("Removes a flag.",
            "/wgb frem regex [regex pattern] [flagname]");

    private static CommandText CHECK = new CommandText("Counts the regions affected by query",
            "/wgb check [query]"
                    + ARGUMENTS
                    + WORLD_LOOKUP_SYNTAX
                    + CHILD_LOOKUP_SYNTAX
                    + REGEX_LOOKUP_SYNTAX
                    + COUNT_LOOKUP_SYNTAX
                    + OWNER_LOOKUP_SYNTAX);

    private static CommandText LIST = new CommandText("Lists the regions affected by query",
            "/wgb list [query]"
                    + ARGUMENTS
                    + WORLD_LOOKUP_SYNTAX
                    + CHILD_LOOKUP_SYNTAX
                    + REGEX_LOOKUP_SYNTAX
                    + COUNT_LOOKUP_SYNTAX
                    + OWNER_LOOKUP_SYNTAX);


    public static CommandText getCommandText(PrimaryActionArgument arg) {
        switch (arg) {
            case MREM:
                return MREM;
            case MADD:
                return MADD;
            case PRIO:
                return PRIO;
            case PSET:
                return PSET;
            case CREM:
                return CREM;
            case PREM:
                return PREM;
            case PCH:
                return PCH;
            case MTRANS:
                return MTRANS;
            case FSET:
                return FSET;
            case FREM:
                return FREM;
            case CHECK:
                return CHECK;
            case LIST:
                return LIST;
        }
        return null;
    }


    public static class CommandText {
        private String description;
        private String pattern;

        CommandText(String description, String pattern) {

            this.description = description;
            this.pattern = pattern;
        }

        public String getDescription() {
            return description;
        }

        public String getPattern() {
            return pattern;
        }
    }
}
