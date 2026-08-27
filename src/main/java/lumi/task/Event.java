package lumi.task;

import java.time.LocalDateTime;

import lumi.datetime.DateTimeParser;

/**
 * Represents a task that occurs between a start and an end date or time.
 */
public class Event extends Task {
    /** The date or time at which the event starts. */
    protected LocalDateTime from;

    /** The date or time at which the event ends. */
    protected LocalDateTime to;

    /**
     * Creates an incomplete event task.
     *
     * @param description description of the event
     * @param from date or time at which the event starts
     * @param to date or time at which the event ends
     */
    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the event's start details for storage and other non-UI uses.
     *
     * @return the date or time at which the event starts
     */
    public LocalDateTime getFrom() {
        return from;
    }

    /**
     * Returns the event's end details for storage and other non-UI uses.
     *
     * @return the date or time at which the event ends
     */
    public LocalDateTime getTo() {
        return to;
    }

    /**
     * Returns the event in the format used when displaying task lists.
     *
     * @return the task type, status, description, start, and end
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + DateTimeParser.format(from)
                + " to: " + DateTimeParser.format(to) + ")";
    }
}
