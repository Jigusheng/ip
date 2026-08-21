import java.util.ArrayList;
import java.util.Scanner;

/**
 * Starts the Lumi chatbot application.
 */
public class Lumi {
    /**
     * Runs the chatbot and processes user input until the user enters {@code bye}.
     *
     * @param args command-line arguments; not used by this application
     */
    public static void main(String[] args) {
        String divider = "____________________________________________________________";
        String banner = " _      _   _ __  __ ___ \n"
                + "| |    | | | |  \\/  |_ _|\n"
                + "| |    | | | | |\\/| || | \n"
                + "| |___ | |_| | |  | || | \n"
                + "|_____| \\___/|_|  |_|___|\n";

        System.out.println(divider);
        System.out.print(banner);
        System.out.println("Hi there! I'm Lumi, your bright and bubbly chat buddy!");
        System.out.println("I'm popping in to sprinkle a little cheer your way.");
        System.out.println("What can I brighten up for you today?");
        System.out.println(divider);

        Scanner scanner = new Scanner(System.in);
        ArrayList<Task> tasks = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String command = scanner.nextLine().trim();

            System.out.println(divider);
            if (command.equals("bye")) {
                System.out.println(" Bye for now! Keep shining, and I hope to see you again soon!");
                System.out.println(divider);
                break;
            }

            try {
                if (command.equals("list")) {
                    System.out.println(" Here are the tasks in your list:");
                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.println(" " + (i + 1) + "." + tasks.get(i));
                    }
                } else if (isCommand(command, "mark")) {
                    int taskIndex = parseTaskIndex(command, "mark", tasks.size());
                    tasks.get(taskIndex).markAsDone();
                    System.out.println(" Nice! I've marked this task as done:");
                    System.out.println("   " + tasks.get(taskIndex));
                } else if (isCommand(command, "unmark")) {
                    int taskIndex = parseTaskIndex(command, "unmark", tasks.size());
                    tasks.get(taskIndex).markAsNotDone();
                    System.out.println(" OK, I've marked this task as not done yet:");
                    System.out.println("   " + tasks.get(taskIndex));
                } else if (isCommand(command, "delete")) {
                    int taskIndex = parseTaskIndex(command, "delete", tasks.size());
                    Task removedTask = tasks.remove(taskIndex);
                    System.out.println(" Noted. I've removed this task:");
                    System.out.println("   " + removedTask);
                    System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
                } else if (isCommand(command, "todo")
                        || isCommand(command, "deadline")
                        || isCommand(command, "event")) {
                    Task newTask = createTask(command);
                    tasks.add(newTask);
                    System.out.println(" Got it. I've added this task:");
                    System.out.println("   " + newTask);
                    System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
                } else if (command.isEmpty()) {
                    throw new LumiException("Hmm, please enter a command.");
                } else {
                    throw new LumiException("Hmm, I don't recognize that command. "
                            + "Try todo, deadline, event, list, mark, unmark, delete, or bye.");
                }
            } catch (LumiException error) {
                System.out.println(" " + error.getMessage());
            }
            System.out.println(divider);
        }
    }

    /**
     * Creates the task represented by an add command.
     * Date and time details are deliberately retained as plain text.
     *
     * @param command command entered by the user
     * @return a to-do, deadline, or event based on the command prefix
     * @throws LumiException if required task details are missing
     */
    private static Task createTask(String command) throws LumiException {
        if (isCommand(command, "todo")) {
            String description = command.substring("todo".length()).trim();
            if (description.isEmpty()) {
                throw new LumiException("Hmm, a todo needs a description. "
                        + "Try: todo <description>");
            }
            return new Todo(description);
        }

        if (isCommand(command, "deadline")) {
            String details = command.substring("deadline".length()).trim();
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
            return new Deadline(description, by);
        }

        if (isCommand(command, "event")) {
            String details = command.substring("event".length()).trim();
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
            return new Event(description, from, to);
        }

        throw new LumiException("Hmm, I don't recognize that task type.");
    }

    /**
     * Converts a task command's user-facing number to a list index.
     *
     * @param command complete command entered by the user
     * @param action command word, such as {@code mark}, {@code unmark}, or {@code delete}
     * @param taskCount number of tasks currently stored
     * @return the zero-based index of the selected task
     * @throws LumiException if the number is missing, invalid, or out of range
     */
    private static int parseTaskIndex(String command, String action, int taskCount)
            throws LumiException {
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
     * Checks whether the input is a command word with an optional argument.
     *
     * @param input complete user input
     * @param commandWord command word to match
     * @return true if the input starts with the complete command word
     */
    private static boolean isCommand(String input, String commandWord) {
        return input.equals(commandWord) || input.startsWith(commandWord + " ");
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
