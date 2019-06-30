package de.eldoria.worldguardbatch.messages;

import de.eldoria.worldguardbatch.commands.QueryType;
import lombok.NonNull;

public class CommandText {
    private String description;
    private String command;
    private String pattern;
    private boolean queryRequired;
    private QueryType[] queryTypes;

    private final String arguments = "§eQueries:";
    private final String regexLookupSyntax = "§7    Regex Lookup: §eregex §c[regex pattern]";
    private final String countLookupSyntax = "§7    Count Lookup: §ecount §c[count 1] §9<count 2>";
    private final String ownerLookupSyntax = "§7    Owner Lookup: §eowner §c[region owner name]";
    private final String worldLookupSyntax = "§7    World Lookup: §eall";
    private final String childLookupSyntax = "§7    Child Lookup: §echildren §c[regionId]";
    private final String parentLookupSyntax = "§7    Parent Lookup: §eparent §c[regionId]";

    private final String patternPrefix = "§7  /wgb §e";
    private final String patternColor = "§9";
    private final String queryRequiredText = "§c[query]";
    private final String queryOptionalText = "§a<query>";

    private final String descriptionColor = "§6";


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
    }

    /**
     * Get the description of the command.
     *
     * @return Description string
     */
    @NonNull
    public String getDescription() {
        return descriptionColor + description;
    }

    /**
     * Get the pattern of the command.
     *
     * @return Pattern string
     */
    @NonNull
    public String getPattern() {
        StringBuilder builder = new StringBuilder();
        builder.append(patternPrefix).append(command).append(" ").append(patternColor).append(pattern).append(" ");
        if (queryRequired) {
            builder.append(queryRequiredText);
        } else if (queryTypes.length != 0) {
            builder.append(queryOptionalText);
        }
        return builder.toString();
    }

    /**
     * Returns the queries as text block.
     * @return Queries or empty string
     */
    public String getQueries() {
        if (queryTypes.length == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append(arguments);

        for (QueryType type : queryTypes) {
            builder.append(System.lineSeparator());

            switch (type) {

                case ALL:
                    builder.append(worldLookupSyntax);
                    break;
                case CHILDREN:
                    builder.append(childLookupSyntax);
                    break;
                case PARENT:
                    builder.append(parentLookupSyntax);
                    break;
                case REGEX:
                    builder.append(regexLookupSyntax);
                    break;
                case COUNT:
                    builder.append(countLookupSyntax);
                    break;
                case OWNER:
                    builder.append(ownerLookupSyntax);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + type);
            }
            builder.append("§r");
        }
        return builder.toString();
    }
}
