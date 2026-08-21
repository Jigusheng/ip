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
                tasks[taskCount] = new Task(command);
                taskCount++;
                System.out.println(" added: " + command);
            }
            System.out.println(divider);
        }
    }
}
