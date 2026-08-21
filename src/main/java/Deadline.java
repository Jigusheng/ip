/**
 * Represents a task that must be completed by a particular date or time.
 */
public class Deadline extends Task {
    /** The date or time by which the task must be completed. */
    protected String by;

    /**
     * Creates an incomplete deadline task.
     *
     * @param description description of the task
     * @param by date or time by which the task must be completed
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns the deadline in the format used when displaying task lists.
     *
     * @return the task type, status, description, and deadline
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}
