import java.util.Scanner;

/**
 * Starts the Lumi chatbot application.
 */
public class Lumi {
    /** Maximum number of tasks that Lumi can keep during one run. */
    private static final int MAX_TASKS = 100;

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
        Task[] tasks = new Task[MAX_TASKS];
        int taskCount = 0;

        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();

            System.out.println(divider);
            if (command.equals("bye")) {
                System.out.println(" Bye for now! Keep shining, and I hope to see you again soon!");
                System.out.println(divider);
                break;
            }

            if (command.equals("list")) {
                System.out.println(" Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println(" " + (i + 1) + "." + tasks[i]);
                }
            } else if (command.startsWith("mark ")) {
                int taskNumber = Integer.parseInt(command.substring(5).trim());
                int taskIndex = taskNumber - 1;
                tasks[taskIndex].markAsDone();
                System.out.println(" Nice! I've marked this task as done:");
                System.out.println("   " + tasks[taskIndex]);

            } else if (command.startsWith("unmark ")) {
                int taskNumber = Integer.parseInt(command.substring(7).trim());
                int taskIndex = taskNumber - 1;
                tasks[taskIndex].markAsNotDone();
                System.out.println(" OK, I've marked this task as not done yet:");
                System.out.println("   " + tasks[taskIndex]);
            } else {
                Task newTask = createTask(command);
                tasks[taskCount] = newTask;
                taskCount++;
                System.out.println(" Got it. I've added this task:");
                System.out.println("   " + newTask);
                System.out.println(" Now you have " + taskCount + " tasks in the list.");
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
     */
    private static Task createTask(String command) {
        if (command.startsWith("todo ")) {
            return new Todo(command.substring(5).trim());
        }

        if (command.startsWith("deadline ")) {
            String[] details = command.substring(9).split(" /by ", 2);
            return new Deadline(details[0].trim(), details[1].trim());
        }

        if (command.startsWith("event ")) {
            String[] descriptionAndTimes = command.substring(6).split(" /from ", 2);
            String[] times = descriptionAndTimes[1].split(" /to ", 2);
            return new Event(descriptionAndTimes[0].trim(), times[0].trim(), times[1].trim());
        }

        return new Todo(command.trim());
    }
}
