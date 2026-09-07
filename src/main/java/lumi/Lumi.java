package lumi;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import lumi.command.CommandType;
import lumi.exception.LumiException;
import lumi.parser.Parser;
import lumi.storage.Storage;
import lumi.task.Task;
import lumi.ui.Ui;

/**
 * Runs the Lumi chatbot and generates replies for its graphical interface.
 */
public final class Lumi {
    /** Portable path to the task data file, relative to the project root. */
    private static final Path DATA_FILE = Path.of("data", "lumi.txt");

    /** Creates a Lumi chatbot instance for use by the graphical interface. */
    public Lumi() {
    }

    /**
     * Generates a response for the user's chat message.
     *
     * @param input User's chat message.
     * @return Lumi's response.
     */
    public String getResponse(String input) {
        return "Lumi heard: " + input;
    }

    /**
     * Runs the chatbot and processes user input until the user enters {@code bye}.
     *
     * @param args Command-line arguments; not used by this application.
     */
    public static void main(String[] args) {
        Ui ui = new Ui();
        Storage storage = new Storage(DATA_FILE);
        ui.showWelcome();
        ArrayList<Task> tasks = loadTasks(storage, ui);
        ui.showDivider();
        runCommandLoop(ui, storage, tasks);
    }

    /**
     * Loads saved tasks and reports recoverable storage problems.
     *
     * @param storage Storage from which tasks are loaded.
     * @param ui Console UI used to show loading warnings.
     * @return Mutable list containing every successfully loaded task.
     */
    private static ArrayList<Task> loadTasks(Storage storage, Ui ui) {
        ArrayList<Task> tasks = new ArrayList<>();
        try {
            Storage.LoadResult loadResult = storage.load();
            tasks.addAll(loadResult.tasks());
            if (loadResult.skippedLineCount() > 0) {
                ui.showSkippedFileLines(loadResult.skippedLineCount());
            }
        } catch (IOException error) {
            ui.showLoadingError();
        }
        return tasks;
    }

    /**
     * Reads and executes console commands until input ends or Lumi receives {@code bye}.
     *
     * @param ui Console UI used for input and output.
     * @param storage Storage updated after task changes.
     * @param tasks Current mutable task list.
     */
    private static void runCommandLoop(Ui ui, Storage storage, List<Task> tasks) {
        while (ui.hasNextCommand()) {
            String command = ui.readCommand();
            ui.showDivider();
            try {
                if (!executeCommand(command, ui, storage, tasks)) {
                    return;
                }
            } catch (LumiException error) {
                ui.showError(error.getMessage());
            } finally {
                ui.showDivider();
            }
        }
    }

    /**
     * Executes one parsed command.
     *
     * @param command Complete user command.
     * @param ui Console UI used to display the result.
     * @param storage Storage updated after task changes.
     * @param tasks Current mutable task list.
     * @return False when the command ends the application; true otherwise.
     * @throws LumiException If the command or its arguments are invalid.
     */
    private static boolean executeCommand(String command, Ui ui, Storage storage,
            List<Task> tasks) throws LumiException {
        CommandType commandType = Parser.parseCommandType(command);
        switch (commandType) {
            case BYE:
                ui.showGoodbye();
                return false;
            case LIST:
                ui.showTaskList(tasks);
                break;
            case FIND:
                showMatchingTasks(command, tasks, ui);
                break;
            case MARK:
            case UNMARK:
                updateTaskStatus(command, commandType, tasks, storage, ui);
                break;
            case DELETE:
                deleteTask(command, commandType, tasks, storage, ui);
                break;
            case TODO:
            case DEADLINE:
            case EVENT:
                addTask(command, commandType, tasks, storage, ui);
                break;
            default:
                throw new LumiException("Hmm, I don't recognize that command.");
        }
        return true;
    }

    /** Finds and displays tasks whose descriptions contain the requested keyword. */
    private static void showMatchingTasks(String command, List<Task> tasks, Ui ui)
            throws LumiException {
        String keyword = Parser.parseFindKeyword(command);
        List<Task> matchingTasks = tasks.stream()
                .filter(task -> task.hasDescriptionContaining(keyword))
                .toList();
        ui.showMatchingTasks(matchingTasks);
    }

    /** Marks or unmarks the task selected by a status command. */
    private static void updateTaskStatus(String command, CommandType commandType,
            List<Task> tasks, Storage storage, Ui ui) throws LumiException {
        int taskIndex = Parser.parseTaskIndex(command, commandType, tasks.size());
        Task task = tasks.get(taskIndex);
        switch (commandType) {
            case MARK:
                task.markAsDone();
                ui.showMarked(task);
                break;
            case UNMARK:
                task.markAsNotDone();
                ui.showUnmarked(task);
                break;
            default:
                throw new IllegalArgumentException("Expected a mark or unmark command");
        }
        saveTasks(storage, tasks, ui);
    }

    /** Removes and displays the task selected by a delete command. */
    private static void deleteTask(String command, CommandType commandType,
            List<Task> tasks, Storage storage, Ui ui) throws LumiException {
        int taskIndex = Parser.parseTaskIndex(command, commandType, tasks.size());
        Task removedTask = tasks.remove(taskIndex);
        ui.showDeleted(removedTask, tasks.size());
        saveTasks(storage, tasks, ui);
    }

    /** Creates, stores, and displays the task described by an add command. */
    private static void addTask(String command, CommandType commandType,
            List<Task> tasks, Storage storage, Ui ui) throws LumiException {
        Task newTask = Parser.parseTask(command, commandType);
        tasks.add(newTask);
        ui.showAdded(newTask, tasks.size());
        saveTasks(storage, tasks, ui);
    }

    /**
     * Saves a changed task list while allowing the chatbot to continue if the
     * file system is temporarily unavailable.
     *
     * @param storage Task storage to update.
     * @param tasks Current task list.
     * @param ui Console UI used to report saving errors.
     */
    private static void saveTasks(Storage storage, List<Task> tasks, Ui ui) {
        try {
            storage.save(tasks);
        } catch (IOException error) {
            ui.showSavingError();
        }
    }

}
