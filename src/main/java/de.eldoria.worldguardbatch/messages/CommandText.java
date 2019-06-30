package de.eldoria.worldguardbatch.messages;

import de.eldoria.worldguardbatch.commands.QueryType;
import lombok.NonNull;

import javax.annotation.Nullable;

public class CommandText {
    private String description;
    private String command;
    private String pattern;
    private boolean queryRequired;
    private QueryType[] queryTypes;

    private final String ARGUMENTS = "§eQueries:";
    private final String REGEX_LOOKUP_SYNTAX = "§7    Regex Lookup: §eregex §c[regex pattern]";
    private final String COUNT_LOOKUP_SYNTAX = "§7    Count Lookup: §ecount §c[count 1] §9<count 2>";
    private final String OWNER_LOOKUP_SYNTAX = "§7    Owner Lookup: §eowner §c[region owner name]";
    private final String WORLD_LOOKUP_SYNTAX = "§7    World Lookup: §eall";
    private final String CHILD_LOOKUP_SYNTAX = "§7    Child Lookup: §echildren §c[regionId]";
    private final String PARENT_LOOKUP_SYNTAX = "§7    Parent Lookup: §eparent §c[regionId]";

    private final String COMMAND = "§7  /wgb §e";
    private final String PATTERN_COLOR = "§9";
    private final String QUERY_REQUIRED = "§c[query]";
    private final String QUERY_OPTIONAL = "§a<query>";

    private final String DESCRIPTION_COLOR = "§6";


    /**
     * Creates new command text object.
     *
     * @param description description of the command
     * @param pattern     pattern of the command
     */
    CommandText(@NonNull String description, @NonNull String command, @NonNull String pattern, boolean queryRequired, QueryType... queryTypes) {
        this.queryRequired = queryRequired;
        this.queryTypes = queryTypes;
        this.description = description;
        this.command = command;
        this.pattern = pattern;
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
        builder.append(COMMAND).append(command).append(" ").append(PATTERN_COLOR).append(pattern).append(" ");
        if (queryRequired) {
            builder.append(QUERY_REQUIRED);
        } else if (queryTypes.length != 0) {
            builder.append(QUERY_OPTIONAL);
        }
        return builder.toString();
    }

    public String getQueries() {
        if (queryTypes.length == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append(ARGUMENTS);

        for (QueryType type : queryTypes) {
            builder.append(System.lineSeparator());

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
            }
            builder.append("§r");
        }
        return builder.toString();
    }
}
