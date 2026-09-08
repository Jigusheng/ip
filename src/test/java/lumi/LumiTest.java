package lumi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Tests Lumi's responses to graphical-interface input. */
public class LumiTest {
    @TempDir
    private Path temporaryDirectory;

    @Test
    public void getResponse_allTaskCommands_commandsExecuteAndPersist() {
        Path dataFile = temporaryDirectory.resolve("data").resolve("lumi.txt");
        Lumi lumi = new Lumi(dataFile);

        assertEquals(" Got it. I've added this task:\n"
                        + "   [T][ ] read book\n"
                        + " Now you have 1 tasks in the list.",
                lumi.getResponse("todo read book"));
        assertEquals(" Got it. I've added this task:\n"
                        + "   [D][ ] return book (by: Oct 15 2019)\n"
                        + " Now you have 2 tasks in the list.",
                lumi.getResponse("deadline return book /by 2019-10-15"));
        assertEquals(" Got it. I've added this task:\n"
                        + "   [E][ ] project meeting (from: Oct 16 2019, 2:00PM"
                        + " to: Oct 16 2019, 4:00PM)\n"
                        + " Now you have 3 tasks in the list.",
                lumi.getResponse("event project meeting /from 2019-10-16 1400"
                        + " /to 2019-10-16 1600"));
        assertEquals(" Nice! I've marked this task as done:\n   [T][X] read book",
                lumi.getResponse("mark 1"));
        assertEquals(" OK, I've marked this task as not done yet:\n   [T][ ] read book",
                lumi.getResponse("unmark 1"));
        assertEquals(" Here are the matching tasks in your list:\n"
                        + " 1.[T][ ] read book\n"
                        + " 2.[D][ ] return book (by: Oct 15 2019)",
                lumi.getResponse("find book"));
        assertEquals(" Noted. I've removed this task:\n"
                        + "   [D][ ] return book (by: Oct 15 2019)\n"
                        + " Now you have 2 tasks in the list.",
                lumi.getResponse("delete 2"));
        assertEquals(" Here are the tasks in your list:\n"
                        + " 1.[T][ ] read book\n"
                        + " 2.[E][ ] project meeting (from: Oct 16 2019, 2:00PM"
                        + " to: Oct 16 2019, 4:00PM)",
                lumi.getResponse("list"));

        Lumi reloadedLumi = new Lumi(dataFile);
        assertEquals(lumi.getResponse("list"), reloadedLumi.getResponse("list"));
    }

    @Test
    public void getResponse_invalidCommand_errorReturnedWithoutChangingTasks() {
        Lumi lumi = new Lumi(temporaryDirectory.resolve("lumi.txt"));

        assertEquals(" Hmm, a todo needs a description. Try: todo <description>",
                lumi.getResponse("todo"));
        assertEquals(" Here are the tasks in your list:", lumi.getResponse("list"));
    }

    @Test
    public void getResponse_snoozeDatedTasks_schedulesAndStatusPersist() {
        Path dataFile = temporaryDirectory.resolve("lumi.txt");
        Lumi lumi = new Lumi(dataFile);
        lumi.getResponse("todo read book");
        lumi.getResponse("deadline submit report /by 2019-10-15");
        lumi.getResponse("event meeting /from 2019-10-16 1400 /to 2019-10-16 1600");
        lumi.getResponse("mark 2");

        assertEquals(" Okay, I've rescheduled this task:\n"
                        + "   [D][X] submit report (by: Oct 20 2019, 6:00PM)",
                lumi.getResponse("snooze 2 /to 2019-10-20 1800"));
        assertEquals(" Okay, I've rescheduled this task:\n"
                        + "   [E][ ] meeting (from: Oct 21 2019, 3:00PM"
                        + " to: Oct 21 2019, 5:00PM)",
                lumi.getResponse("snooze 3 /to 2019-10-21 1500"));
        assertEquals(" Hmm, only deadlines and events can be snoozed.",
                lumi.getResponse("snooze 1 /to 2019-10-22"));

        Lumi reloadedLumi = new Lumi(dataFile);
        assertEquals(" Here are the tasks in your list:\n"
                        + " 1.[T][ ] read book\n"
                        + " 2.[D][X] submit report (by: Oct 20 2019, 6:00PM)\n"
                        + " 3.[E][ ] meeting (from: Oct 21 2019, 3:00PM"
                        + " to: Oct 21 2019, 5:00PM)",
                reloadedLumi.getResponse("list"));
    }

    @Test
    public void getResponse_bye_farewellReturnedAndSessionEnded() {
        Lumi lumi = new Lumi(temporaryDirectory.resolve("lumi.txt"));

        assertTrue(lumi.isRunning());
        assertEquals(" Bye for now! Keep shining, and I hope to see you again soon!",
                lumi.getResponse("bye"));
        assertFalse(lumi.isRunning());
    }

    @Test
    public void constructor_corruptedStoredLine_validTaskLoadedAndWarningAvailable() throws IOException {
        Path dataFile = temporaryDirectory.resolve("lumi.txt");
        Files.writeString(dataFile, "T | 1 | read book\ncorrupted record\n");
        Lumi lumi = new Lumi(dataFile);

        assertEquals("I found 1 invalid line(s) in the saved task file and skipped them.",
                lumi.getStartupMessage());
        assertEquals(" Here are the tasks in your list:\n 1.[T][X] read book",
                lumi.getResponse("list"));
    }
}
