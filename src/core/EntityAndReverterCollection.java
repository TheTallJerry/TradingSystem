package core;

import core.meeting.Meeting;
import core.reverter.ActionReverter;
import core.trade.Trade;
import core.user.User;

import java.util.List;

/**
 * Class that collect entities and reverters.
 */
public class EntityAndReverterCollection {

    /**
     * a list of <code>Meeting</code>s in the system.
     */
    final List<Meeting> meetings;

    /**
     * a list of <code>Trade</code>s in the system.
     */
    final List<Trade> trades;

    /**
     * a list of <code>User</code>s in the system.
     */
    final List<User> users;

    /**
     * a list of <code>ActionReverter</code>s in the system.
     */
    final List<ActionReverter> actionReverters;

    /**
     * Construct a entities and reverters collection. Contains collections of each entity and collection of
     * <code>ActionReverter</code>.
     *
     * @param meetings        a list of <code>Meeting</code>s in the system.
     * @param trades          a list of <code>Trade</code>s in the system.
     * @param users           a list of <code>User</code>s in the system.
     * @param actionReverters a list of <code>ActionReverter</code>s in the system.
     */
    public EntityAndReverterCollection(List<Meeting> meetings, List<Trade> trades,
                                       List<User> users,
                                       List<ActionReverter> actionReverters) {
        this.meetings = meetings;
        this.trades = trades;
        this.users = users;
        this.actionReverters = actionReverters;
    }
}
