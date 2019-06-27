package de.eldoria.worldguardbatch;

import de.eldoria.worldguardbatch.commands.PrimaryActionArgument;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;

public final class Messages {
    /**
     * number is negative or not a number.
     */
    public static final String ERROR_INVALID_NUMBERS = "Invalid Numbers. Numbers must be positive. "
            + "Min must be smaller than max";

    /**
     * World could not be found.
     */
    public static final String ERROR_WORLD_NOT_FOUND = "Couldn't find the world. Please Report this Error.";

    /**
     * Player is not known. Never joined the server
     */
    public static final String ERROR_UNKNOWN_PLAYER = "This player was never on this server.";
    /**
     * unknown flag type.
     */
    public static final String ERROR_UNKNOWN_FLAG = "Unknown Flag. Please check available world guard flags.";

    /**
     * Wrong value for flag.
     */
    public static final String ERROR_WRONG_FLAG_VALUE = "This value is not allowed for this flag. Please check value.";

    /**
     * No region found in query.
     */
    public static final String ERROR_NO_REGIONS_FOUND = "No regions found under this query.";


    private static final String ARGUMENTS = "\nQueries:";
    private static final String REGEX_LOOKUP_SYNTAX = "\nRegex Lookup: regex [regex pattern]";
    private static final String COUNT_LOOKUP_SYNTAX = "\nCount Lookup: count [count 1] <count 2>";
    private static final String OWNER_LOOKUP_SYNTAX = "\nOwner Lookup: owner [region owner name]";
    private static final String WORLD_LOOKUP_SYNTAX = "\nWorld Lookup: all";
    private static final String CHILD_LOOKUP_SYNTAX = "\nChild Lookup: children [regionId]";
    private static final String PARENT_LOOKUP_SYNTAX = "\nParent Lookup: parent [regionId]";

    private static final String ERROR_TOO_FEW_ARGUMENTS = "Too few arguments. Please check pattern:\n";
    private static final String ERROR_TOO_MANY_ARGUMENTS = "Too many arguments. Please check command pattern:\n";
    private static final String ERROR_UNKNOWN_COMMAND = "Unknown Command. Please use /wgb help for a list of commands.";
    private static final String ERROR_UNKNOWN_REGION_QUERY = "The query does not exists or is not"
            + " allowed for this command.\n"
            + "Take a look at the allowed queries:\n";
    private static final String ERROR_UNKNOWN_MEMBERSHIP_SCOPE = "This membership scope does not exists"
            + " or is not allowed in this command. Take a look at the command\n";
    private static final String ERROR_UNKNOWN_CHECK_ARGUMENT = "Check argument is invalid.\n"
            + "Take a look at the command:\n";

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


    private static CommandText getCommandTextSave(PrimaryActionArgument arg) {
        var com = getCommandText(arg);
        return com == null ? new CommandText("Command not defined.Please report this error.",
                "Command not defined.Please report this error.") : com;
    }


    /**
     * Get the error for the command.
     *
     * @param regionId id of the region
     * @return Error a string
     */
    public static String getRegionNotFound(String regionId) {
        return "Region '" + regionId + "' not found.";
    }

    /**
     * Get the error for the command.
     *
     * @param primaryActionArgument command identifier
     * @return Error a string
     */
    public static String getErrorTooFewArguments(PrimaryActionArgument primaryActionArgument) {
        return ERROR_TOO_FEW_ARGUMENTS + getCommandTextSave(primaryActionArgument).getPattern();
    }

    /**
     * Get the error for the command.
     *
     * @param primaryActionArgument command identifier
     * @return Error a string
     */
    public static String getErrorTooManyArguments(PrimaryActionArgument primaryActionArgument) {
        return ERROR_TOO_MANY_ARGUMENTS + getCommandTextSave(primaryActionArgument).getPattern();
    }

    /**
     * Get the error for the command.
     *
     * @return Error a string
     */
    public static String getErrorUnknownCommand() {
        return ERROR_UNKNOWN_COMMAND;
    }

    /**
     * Get the error for the command.
     *
     * @param primaryActionArgument command identifier
     * @return Error a string
     */
    public static String getErrorUnknownRegionQuery(PrimaryActionArgument primaryActionArgument) {
        return ERROR_UNKNOWN_REGION_QUERY + getCommandTextSave(primaryActionArgument).getPattern();
    }

    /**
     * Get the error for the command.
     *
     * @param primaryActionArgument command identifier
     * @return Error a string
     */
    public static String getErrorUnknownMembershipScope(PrimaryActionArgument primaryActionArgument) {
        return ERROR_UNKNOWN_MEMBERSHIP_SCOPE + getCommandTextSave(primaryActionArgument).getPattern();
    }

    /**
     * Get the error for the command.
     *
     * @param primaryActionArgument command identifier
     * @return Error a string
     */
    public static String getErrorUnknownCheckArgument(PrimaryActionArgument primaryActionArgument) {
        return ERROR_UNKNOWN_CHECK_ARGUMENT + getCommandTextSave(primaryActionArgument).getPattern();
    }

    /**
     * Get the command text object for the action.
     *
     * @param arg action of the command
     * @return Command text object. Can be null of command is none
     */
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
            default:
        }
        return null;
    }


    /**
     * Sends a too few or too many arg message.
     *
     * @param p              player which should receive the message
     * @param actionArgument action argument of command
     * @param args           args array
     * @param requiredArgs   required args.
     */
    public static void sendArgumentMessage(Player p, PrimaryActionArgument actionArgument,
                                           String[] args, int requiredArgs) {
        sendArgumentMessage(p, actionArgument, args, requiredArgs, requiredArgs);
    }

    /**
     * Sends a too few or too many arg message.
     *
     * @param p              player which should receive the message
     * @param actionArgument action argument of command
     * @param args           args array
     * @param min            min required argument count
     * @param max            max required argument count
     */
    public static void sendArgumentMessage(Player p, PrimaryActionArgument actionArgument,
                                           String[] args, int min, int max) {
        if (args.length < min) {
            p.sendMessage(getErrorTooFewArguments(actionArgument));
        } else if (args.length > max) {
            p.sendMessage(getErrorTooManyArguments(actionArgument));
        }

    }


    public static class CommandText {
        private String description;
        private String pattern;

        /**
         * Creates new command text object.
         *
         * @param description description of the command
         * @param pattern     pattenr of the command
         */
        CommandText(String description, String pattern) {

            this.description = description;
            this.pattern = pattern;
        }

        /**
         * Get the description of the command.
         *
         * @return Description string
         */
        @Nullable
        public String getDescription() {
            return description;
        }

        /**
         * Get the pattern of the command.
         *
         * @return Pattern string
         */
        @Nullable
        public String getPattern() {
            return pattern;
        }
    }
}
