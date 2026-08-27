/**
 * Interprets raw user input as Lumi commands and command arguments.
 * Separating parsing from execution lets future command objects receive
 * already-validated values instead of processing strings themselves.
 */
public final class Parser {
    /** Prevents creation of this stateless utility class. */
    private Parser() {
    }

    /**
     * Identifies the command represented by a complete input line.
     *
     * @param input complete user input
     * @return matching command type
     * @throws LumiException if the input is empty or does not match a command
     */
    public static CommandType parseCommandType(String input) throws LumiException {
        if (input.isEmpty()) {
            throw new LumiException("Hmm, please enter a command.");
        }

        for (CommandType commandType : CommandType.values()) {
            if (commandType.matches(input)) {
                return commandType;
            }
        }
        throw new LumiException("Hmm, I don't recognize that command. "
                + "Try todo, deadline, event, list, mark, unmark, delete, or bye.");
    }

    /**
     * Creates the task represented by an add command.
     * Deadline and event dates are parsed into {@code LocalDateTime} values.
     *
     * @param command complete command entered by the user
     * @param commandType type of task creation command
     * @return a to-do, deadline, or event based on the command type
     * @throws LumiException if required task details are missing or invalid
     */
    public static Task parseTask(String command, CommandType commandType) throws LumiException {
        if (commandType == CommandType.TODO) {
            String description = command.substring(commandType.getKeyword().length()).trim();
            if (description.isEmpty()) {
                throw new LumiException("Hmm, a todo needs a description. "
                        + "Try: todo <description>");
            }
            return new Todo(description);
        }

        if (commandType == CommandType.DEADLINE) {
            String details = command.substring(commandType.getKeyword().length()).trim();
            int byPosition = findSeparator(details, "/by", 0);
            if (byPosition < 0) {
                throw new LumiException("Hmm, a deadline needs a due date. "
                        + "Try: deadline <description> /by <when>");
            }

            String description = details.substring(0, byPosition).trim();
            String by = details.substring(byPosition + "/by".length()).trim();
            if (description.isEmpty()) {
                throw new LumiException("Hmm, a deadline needs a description before /by.");
            }
            if (by.isEmpty()) {
                throw new LumiException("Hmm, the /by value cannot be empty.");
            }
            return new Deadline(description, DateTimeParser.parseUserInput(by));
        }

        if (commandType == CommandType.EVENT) {
            String details = command.substring(commandType.getKeyword().length()).trim();
            int fromPosition = findSeparator(details, "/from", 0);
            int toPosition = fromPosition < 0
                    ? -1
                    : findSeparator(details, "/to", fromPosition + "/from".length());
            if (fromPosition < 0 || toPosition < 0) {
                throw new LumiException("Hmm, an event needs start and end details. "
                        + "Try: event <description> /from <start> /to <end>");
            }

            String description = details.substring(0, fromPosition).trim();
            String from = details.substring(fromPosition + "/from".length(), toPosition).trim();
            String to = details.substring(toPosition + "/to".length()).trim();
            if (description.isEmpty()) {
                throw new LumiException("Hmm, an event needs a description before /from.");
            }
            if (from.isEmpty()) {
                throw new LumiException("Hmm, the /from value cannot be empty.");
            }
            if (to.isEmpty()) {
                throw new LumiException("Hmm, the /to value cannot be empty.");
            }
            return new Event(description, DateTimeParser.parseUserInput(from),
                    DateTimeParser.parseUserInput(to));
        }

        throw new LumiException("Hmm, I don't recognize that task type.");
    }

    /**
     * Converts a task command's user-facing number to a list index.
     *
     * @param command complete command entered by the user
     * @param commandType type of task command, such as mark, unmark, or delete
     * @param taskCount number of tasks currently stored
     * @return the zero-based index of the selected task
     * @throws LumiException if the number is missing, invalid, or out of range
     */
    public static int parseTaskIndex(String command, CommandType commandType, int taskCount)
            throws LumiException {
        String action = commandType.getKeyword();
        String argument = command.substring(action.length()).trim();
        if (argument.isEmpty()) {
            throw new LumiException("Hmm, tell me which task to " + action
                    + ". Try: " + action + " <task number>");
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(argument);
        } catch (NumberFormatException error) {
            throw new LumiException("Hmm, the task number must be a whole number.");
        }

        if (taskCount == 0) {
            throw new LumiException("Hmm, there are no tasks to " + action + " yet.");
        }
        if (taskNumber < 1 || taskNumber > taskCount) {
            throw new LumiException("Hmm, choose a task number from 1 to " + taskCount + ".");
        }
        return taskNumber - 1;
    }

    /**
     * Finds a slash-prefixed separator only when it appears as a complete token.
     *
     * @param text text containing task details
     * @param separator separator to find, such as {@code /by}
     * @param startIndex index from which to begin searching
     * @return the separator index, or -1 if no complete separator token exists
     */
    private static int findSeparator(String text, String separator, int startIndex) {
        int position = text.indexOf(separator, startIndex);
        while (position >= 0) {
            int afterSeparator = position + separator.length();
            boolean hasStartBoundary = position == 0
                    || Character.isWhitespace(text.charAt(position - 1));
            boolean hasEndBoundary = afterSeparator == text.length()
                    || Character.isWhitespace(text.charAt(afterSeparator));
            if (hasStartBoundary && hasEndBoundary) {
                return position;
            }
            position = text.indexOf(separator, position + 1);
        }
        return -1;
    }
}
