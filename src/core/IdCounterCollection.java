package core;

/**
 * Class that collects id counters for entities.
 */
public class IdCounterCollection {
    /**
     * A number that is larger than all ids <code>Meeting</code>s has in the system. It ensures that the id for each
     * <code>Meeting</code> is unique.
     */
    final int meetingIDCounter;
    /**
     * A number that is larger than all ids <code>Trade</code> has in the system. It ensures that the id for each
     * <code>Trade</code> is unique.
     */
    final int tradeIDCounter;
    /**
     * A number that is larger than all ids <code>Item</code> has in the system. It ensures that the id for each
     * <code>Item</code> is unique.
     */
    final int itemIDCounter;

    /**
     * Constructs an <code>IdCounterCollection</code> that collects id counters for entities.
     *
     * @param meetingIDCounter A number that is larger than all ids <code>Meeting</code>s has in the system.
     * @param tradeIDCounter   A number that is larger than all ids <code>Trade</code> has in the system.
     * @param itemIDCounter    A number that is larger than all ids <code>Item</code> has in the system.
     */
    public IdCounterCollection(int meetingIDCounter, int tradeIDCounter,
                               int itemIDCounter) {
        this.meetingIDCounter = meetingIDCounter;
        this.tradeIDCounter = tradeIDCounter;
        this.itemIDCounter = itemIDCounter;
    }
}
