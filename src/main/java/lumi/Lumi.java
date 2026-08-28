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
 * Starts the Lumi chatbot application.
 */
public final class Lumi {
    /** Portable path to the task data file, relative to the project root. */
    private static final Path DATA_FILE = Path.of("data", "lumi.txt");

    /** Prevents creation of this application entry-point class. */
    private Lumi() {
    }

    /**
     * Runs the chatbot and processes user input until the user enters {@code bye}.
     *
     * @param args Command-line arguments; not used by this application.
     */
    public static void main(String[] args) {
        Ui ui = new Ui();
        ui.showWelcome();
        Storage storage = new Storage(DATA_FILE);
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
        ui.showDivider();

        while (ui.hasNextCommand()) {
            String command = ui.readCommand();
            ui.showDivider();
            try {
                CommandType commandType = Parser.parseCommandType(command);
                if (commandType == CommandType.BYE) {
                    ui.showGoodbye();
                    break;
                }

                if (commandType == CommandType.LIST) {
                    ui.showTaskList(tasks);
                } else if (commandType == CommandType.FIND) {
                    String keyword = Parser.parseFindKeyword(command);
                    List<Task> matchingTasks = tasks.stream()
                            .filter(task -> task.hasDescriptionContaining(keyword))
                            .toList();
                    ui.showMatchingTasks(matchingTasks);
                } else if (commandType == CommandType.MARK) {
                    int taskIndex = Parser.parseTaskIndex(command, commandType, tasks.size());
                    tasks.get(taskIndex).markAsDone();
                    ui.showMarked(tasks.get(taskIndex));
                    saveTasks(storage, tasks, ui);
                } else if (commandType == CommandType.UNMARK) {
                    int taskIndex = Parser.parseTaskIndex(command, commandType, tasks.size());
                    tasks.get(taskIndex).markAsNotDone();
                    ui.showUnmarked(tasks.get(taskIndex));
                    saveTasks(storage, tasks, ui);
                } else if (commandType == CommandType.DELETE) {
                    int taskIndex = Parser.parseTaskIndex(command, commandType, tasks.size());
                    Task removedTask = tasks.remove(taskIndex);
                    ui.showDeleted(removedTask, tasks.size());
                    saveTasks(storage, tasks, ui);
                } else if (commandType == CommandType.TODO
                        || commandType == CommandType.DEADLINE
                        || commandType == CommandType.EVENT) {
                    Task newTask = Parser.parseTask(command, commandType);
                    tasks.add(newTask);
                    ui.showAdded(newTask, tasks.size());
                    saveTasks(storage, tasks, ui);
                }
            } catch (LumiException error) {
                ui.showError(error.getMessage());
            } finally {
                ui.showDivider();
            }
        }
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
