/**
 * Represents a task that occurs between a start and an end date or time.
 */
public class Event extends Task {
    /** The date or time at which the event starts. */
    protected String from;

    /** The date or time at which the event ends. */
    protected String to;

    /**
     * Creates an incomplete event task.
     *
     * @param description description of the event
     * @param from date or time at which the event starts
     * @param to date or time at which the event ends
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the event in the format used when displaying task lists.
     *
     * @return the task type, status, description, start, and end
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
