/**
 * Represents the fixed set of commands understood by Lumi.
 */
public enum CommandType {
    TODO("todo", true),
    DEADLINE("deadline", true),
    EVENT("event", true),
    LIST("list", false),
    MARK("mark", true),
    UNMARK("unmark", true),
    DELETE("delete", true),
    BYE("bye", false),
    UNKNOWN(null, false);

    /** The word used to enter this command, or null for an unknown command. */
    private final String keyword;

    /** Whether text is allowed after the command keyword. */
    private final boolean acceptsArgument;

    /**
     * Creates a command type with its user-facing keyword.
     *
     * @param keyword word used to enter the command
     * @param acceptsArgument whether text is allowed after the command keyword
     */
    CommandType(String keyword, boolean acceptsArgument) {
        this.keyword = keyword;
        this.acceptsArgument = acceptsArgument;
    }

    /**
     * Returns the word used to enter this command.
     *
     * @return the command keyword, or null for {@link #UNKNOWN}
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * Determines the command type represented by a complete line of user input.
     *
     * @param input complete user input
     * @return the matching command type, or {@link #UNKNOWN} if none matches
     */
    public static CommandType from(String input) {
        for (CommandType commandType : values()) {
            if (commandType.keyword == null) {
                continue;
            }

            boolean isExactMatch = input.equals(commandType.keyword);
            boolean hasArgument = commandType.acceptsArgument
                    && input.startsWith(commandType.keyword + " ");
            if (isExactMatch || hasArgument) {
                return commandType;
            }
        }
        return UNKNOWN;
    }
}
