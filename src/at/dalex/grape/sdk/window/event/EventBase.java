package at.dalex.grape.sdk.window.event;

public abstract class EventBase {

    /**
     * Determines whether or not this event is cancelled,
     * which results in other callbacks responsible
     * for this event not being called.
     *
     * The event can be cancelled using the
     * {@link ViewportInteractionEvent#setCancelled(boolean)} ()} function.
     */
    private boolean isCancelled;

    /**
     * Marks this event as cancelled,
     * which results in other callbacks responsible
     * for this event not being called.
     *
     * @param isCancelled Whether or not this event should be cancelled.
     */
    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    /**
     * Get whether or not this event should be cancelled.
     * @return Cancellation status of this event.
     */
    public boolean isCancelled() {
        return this.isCancelled;
    }
}
