package core;

/**
 * Class that collects all the threshold in the system.
 */
public class ThresholdCollection {

    /**
     * <code>User</code> maximum number of incomplete trade limit.
     */
    final int maxIncompleteTrade;

    /**
     * <code>User</code> maximum weekly transaction limit.
     */
    final int maxWeeklyTransaction;

    /**
     * <code>User</code> minimum difference between number of borrow and lend.
     */
    final int minLendBorrowDifference;

    /**
     * <code>User</code> number of edit time on a meeting.
     */
    final int maxMeetingEdits;

    /**
     * <code>User</code> maximum time a meeting does not confirm occurred after arrangement time.
     */
    final int maxMeetingLateTime;


    /**
     * Construct a <code>ThresholdCollection</code> which collects all the threshold in the system.
     *
     * @param maxIncompleteTrade      the maximum number of incomplete trade a user can have
     * @param maxWeeklyTransaction    the maximum number of weekly transaction a user can have
     * @param minLendBorrowDifference a user has to lend at least how many more than they borrow
     * @param maxMeetingEdits         the maximum number of times a user can edit a meeting
     * @param maxMeetingLateTime      <code>User</code> maximum time a meeting does not confirm occurred after arrangement
     *                                time.
     */
    public ThresholdCollection(int maxIncompleteTrade, int maxWeeklyTransaction,
                               int minLendBorrowDifference, int maxMeetingEdits,
                               int maxMeetingLateTime) {
        this.maxIncompleteTrade = maxIncompleteTrade;
        this.maxWeeklyTransaction = maxWeeklyTransaction;
        this.minLendBorrowDifference = minLendBorrowDifference;
        this.maxMeetingEdits = maxMeetingEdits;
        this.maxMeetingLateTime = maxMeetingLateTime;
    }
}
