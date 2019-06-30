package de.eldoria.worldguardbatch.messages;

import de.eldoria.worldguardbatch.commands.QueryType;
import lombok.NonNull;

public class CommandText {
    private String description;
    private String command;
    private String pattern;
    private boolean queryRequired;
    private QueryType[] queryTypes;
    private MessageSender messageSender;

    private static final String QUERIES = "§eQueries:";
    private static final String REGEX_LOOKUP_SYNTAX = "§7    Regex Lookup: §eregex §c[regex pattern]";
    private static final String COUNT_LOOKUP_SYNTAX = "§7    Count Lookup: §ecount §c[count 1] §9<count 2>";
    private static final String OWNER_LOOKUP_SYNTAX = "§7    Owner Lookup: §eowner §c[region owner name]";
    private static final String WORLD_LOOKUP_SYNTAX = "§7    World Lookup: §eall";
    private static final String CHILD_LOOKUP_SYNTAX = "§7    Child Lookup: §echildren §c[regionId]";
    private static final String PARENT_LOOKUP_SYNTAX = "§7    Parent Lookup: §eparent §c[regionId]";

    private static final String PATTERN_PREFIX = "§7  /wgb §e";
    private static final String PATTERN_COLOR = "§9";
    private static final String QUERY_REQUIRED = "§c[query]";
    private static final String QUERY_OPTIONAL = "§a<query>";

    private static final String DESCRIPTION_COLOR = "§6";


    /**
     * Creates new command text object.
     *
     * @param description   description of the command
     * @param pattern       pattern of the command
     * @param command       command name
     * @param queryRequired true if a query is required
     * @param queryTypes    all allowed query types
     */
    CommandText(@NonNull String description, @NonNull String command,
                @NonNull String pattern, boolean queryRequired, QueryType... queryTypes) {
        this.queryRequired = queryRequired;
        this.queryTypes = queryTypes;
        this.description = description;
        this.command = command;
        this.pattern = pattern;
        this.messageSender = MessageSender.getInstance();
    }

    /**
     * Get the description of the command.
     *
     * @return Description string
     */
    @NonNull
    public String getDescription() {
        return DESCRIPTION_COLOR + description;
    }

    /**
     * Get the pattern of the command.
     *
     * @return Pattern string
     */
    @NonNull
    public String getPattern() {
        StringBuilder builder = new StringBuilder();
        builder.append(PATTERN_PREFIX).append(command).append(" ").append(PATTERN_COLOR).append(pattern).append(" ");
        if (queryRequired) {
            builder.append(QUERY_REQUIRED);
        } else if (queryTypes.length != 0) {
            builder.append(QUERY_OPTIONAL);
        }
        return builder.toString();
    }

    /**
     * Returns the queries as text block.
     *
     * @return Queries or empty string
     */
    public String getQueries() {
        if (queryTypes.length == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append(QUERIES);

        for (QueryType type : queryTypes) {
            builder.append(messageSender.getNewLine());

            switch (type) {

                case ALL:
                    builder.append(WORLD_LOOKUP_SYNTAX);
                    break;
                case CHILDREN:
                    builder.append(CHILD_LOOKUP_SYNTAX);
                    break;
                case PARENT:
                    builder.append(PARENT_LOOKUP_SYNTAX);
                    break;
                case REGEX:
                    builder.append(REGEX_LOOKUP_SYNTAX);
                    break;
                case COUNT:
                    builder.append(COUNT_LOOKUP_SYNTAX);
                    break;
                case OWNER:
                    builder.append(OWNER_LOOKUP_SYNTAX);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + type);
            }
            builder.append("§r");
        }
        return builder.toString();
    }
}
