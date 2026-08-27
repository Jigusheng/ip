package lumi.task;

import java.time.LocalDateTime;

import lumi.datetime.DateTimeParser;

/**
 * Represents a task that must be completed by a particular date or time.
 */
public class Deadline extends Task {
    /** The date or time by which the task must be completed. */
    protected LocalDateTime by;

    /**
     * Creates an incomplete deadline task.
     *
     * @param description description of the task
     * @param by date or time by which the task must be completed
     */
    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns the deadline's due-date details for storage and other non-UI uses.
     *
     * @return the date or time by which the task must be completed
     */
    public LocalDateTime getBy() {
        return by;
    }

    /**
     * Returns the deadline in the format used when displaying task lists.
     *
     * @return the task type, status, description, and deadline
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + DateTimeParser.format(by) + ")";
    }
}
