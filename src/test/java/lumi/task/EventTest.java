package lumi.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

/** Tests event rescheduling. */
public class EventTest {
    @Test
    public void reschedule_completedEvent_startMovedAndDurationAndStatusPreserved() {
        Event event = new Event("meeting",
                LocalDateTime.of(2019, 10, 16, 14, 0),
                LocalDateTime.of(2019, 10, 16, 16, 0));
        event.markAsDone();

        event.reschedule(LocalDateTime.of(2019, 10, 21, 15, 0));

        assertEquals(LocalDateTime.of(2019, 10, 21, 15, 0), event.getFrom());
        assertEquals(LocalDateTime.of(2019, 10, 21, 17, 0), event.getTo());
        assertTrue(event.isDone());
    }
}
