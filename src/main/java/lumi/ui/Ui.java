package lumi.ui;

import java.util.List;
import java.util.Scanner;

import lumi.task.Task;

/**
 * Handles all console input and output for Lumi.
 * Keeping presentation details here lets the application coordinate behavior
 * without depending directly on {@link System#in} or {@link System#out}.
 */
public class Ui {
    /** Divider shown around each response block. */
    private static final String DIVIDER =
            "____________________________________________________________";

    /** Lumi logo shown when the application starts. */
    private static final String BANNER = " _      _   _ __  __ ___ \n"
            + "| |    | | | |  \\/  |_ _|\n"
            + "| |    | | | | |\\/| || | \n"
            + "| |___ | |_| | |  | || | \n"
            + "|_____| \\___/|_|  |_|___|\n";

    /** Reads commands from standard input. */
    private final Scanner scanner;

    /** Creates a console UI connected to standard input. */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /** Shows Lumi's startup banner and greeting. */
    public void showWelcome() {
        showDivider();
        System.out.print(BANNER);
        System.out.println("Hi there! I'm Lumi, your bright and bubbly chat buddy!");
        System.out.println("I'm popping in to sprinkle a little cheer your way.");
        System.out.println("What can I brighten up for you today?");
    }

    /**
     * Reports whether another command is available.
     *
     * @return true when another input line can be read
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Reads and trims the next user command.
     *
     * @return the next command, without surrounding whitespace
     */
    public String readCommand() {
        return scanner.nextLine().trim();
    }

    /** Shows the divider used around console response blocks. */
    public void showDivider() {
        System.out.println(DIVIDER);
    }

    /** Shows the farewell message. */
    public void showGoodbye() {
        System.out.println(" Bye for now! Keep shining, and I hope to see you again soon!");
    }

    /**
     * Shows all tasks with their one-based list numbers.
     *
     * @param tasks tasks to display
     */
    public void showTaskList(List<Task> tasks) {
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.get(i));
        }
    }

    /** Shows confirmation that a task was marked as complete. */
    public void showMarked(Task task) {
        System.out.println(" Nice! I've marked this task as done:");
        System.out.println("   " + task);
    }

    /** Shows confirmation that a task was marked as incomplete. */
    public void showUnmarked(Task task) {
        System.out.println(" OK, I've marked this task as not done yet:");
        System.out.println("   " + task);
    }

    /** Shows the removed task and updated task count. */
    public void showDeleted(Task task, int taskCount) {
        System.out.println(" Noted. I've removed this task:");
        System.out.println("   " + task);
        showTaskCount(taskCount);
    }

    /** Shows the added task and updated task count. */
    public void showAdded(Task task, int taskCount) {
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + task);
        showTaskCount(taskCount);
    }

    /** Shows an input or command error in Lumi's response format. */
    public void showError(String message) {
        System.out.println(" " + message);
    }

    /** Shows a warning when malformed saved records were skipped. */
    public void showSkippedFileLines(int skippedLineCount) {
        System.out.println("I found " + skippedLineCount
                + " invalid line(s) in the saved task file and skipped them.");
    }

    /** Shows a warning when saved tasks cannot be loaded. */
    public void showLoadingError() {
        System.out.println("I couldn't load saved tasks, so I'm starting with an empty list.");
    }

    /** Shows a warning when changed tasks cannot be saved. */
    public void showSavingError() {
        System.out.println(" Hmm, I couldn't save the latest task changes.");
    }

    /** Shows the task count after an addition or deletion. */
    private void showTaskCount(int taskCount) {
        System.out.println(" Now you have " + taskCount + " tasks in the list.");
    }
}
