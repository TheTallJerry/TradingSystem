package client.databundle;

import core.EntityAndReverterCollection;
import core.IdCounterCollection;
import core.RequestCollection;
import core.ThresholdCollection;
import core.admin.Admin;
import core.item.Item;
import core.meeting.Meeting;
import core.reverter.ActionReverter;
import core.trade.Trade;
import core.user.User;
import genericdatatype.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that holds all information that needs to be serialized
 */
public class DataBundle implements Serializable {
    /* Using ArrayList here since List does not implement Serializable */
    /**
     * A list containing all <code>Users</code>s in this system
     */
    public final ArrayList<User> users;
    /**
     * A list containing all <code>Admins</code> in this system
     */
    public final ArrayList<Admin> admins;
    /**
     * A list containing all <code>Trade</code>s in this system
     */
    private final ArrayList<Trade> trades;
    /**
     * A list containing all <code>Meeting</code>s in this system
     */
    private final ArrayList<Meeting> meetings;
    /**
     * A list containing all string representatives of unfreezing requests from users in this system
     */
    private final ArrayList<String> unfreezeRequests;
    /**
     * A list of <code>Pair</code>s, with value1 the username of the user who wants to request the <code>Item</code>,
     * and value2 the <code>Item</code> to be added.
     */
    private final ArrayList<Pair<String, Item>> itemRequests;
    /**
     * A list of <code>Pair</code>s, with value1 the username of the <code>Admin</code> to be created, and value2 the password
     * of the <code>Admin</code>.
     */
    public final List<Pair<Admin, String>> adminCreationRequests;
    /**
     * A list of <code>Pair</code>s, with value1 the username of the <code>User</code> reported by others, and value2
     * the username of the <code>User</code> reported value1.
     */
    private final ArrayList<String[]> reportRequests;

    private final ArrayList<ActionReverter> actionReverters;
    /**
     * A number that is larger than all ids <code>Item</code> has in the system. It ensures that the id for each
     * <code>Item</code> is unique.
     */
    public int itemIdCounter;
    /**
     * A number that is larger than all ids <code>Meeting</code>s has in the system. It ensures that the id for each
     * <code>Meeting</code> is unique.
     */
    public int meetingIdCounter;
    /**
     * A number that is larger than all ids <code>Trade</code> has in the system. It ensures that the id for each
     * <code>Trade</code> is unique.
     */
    public int tradeIdCounter;
    /**
     * The minimum difference between number of lending and borrowing of a user.
     */
    public int minLendBorrowDifference;
    /**
     * The maximum number of time a meeting can be edited by a user.
     */
    public int maxMeetingEdits;
    /**
     * The maximum number of incomplete trade a user can have.
     */
    public int maxIncompleteTrade;
    /**
     * The maximum number of trade a user can have in a week.
     */
    public int maxWeeklyTransaction;
    /**
     * The maximum number of day user have to confirm meeting occurred before the meeting is marked as late.
     */
    public int maxMeetingLateTime;

    /**
     * Create a new DataBundle. This is used when the program runs for the first time and data is missing.
     */
    public DataBundle() {
        users = new ArrayList<>();
        admins = new ArrayList<>();
        trades = new ArrayList<>();
        meetings = new ArrayList<>();
        unfreezeRequests = new ArrayList<>();
        itemRequests = new ArrayList<>();
        adminCreationRequests = new ArrayList<>();
        reportRequests = new ArrayList<>();
        actionReverters = new ArrayList<>();
        itemIdCounter = 0;
        meetingIdCounter = 0;
        tradeIdCounter = 0;
        minLendBorrowDifference = 1;
        maxMeetingEdits = 3;
        maxIncompleteTrade = 3;
        maxWeeklyTransaction = 3;
        maxMeetingLateTime = 7;
    }

    /**
     * Get all the reverter from the DataBundle.
     *
     * @return a collection of all the entities and the reverters.
     */
    public EntityAndReverterCollection getEntitiesAndReverters() {
        return new EntityAndReverterCollection(meetings, trades, users, actionReverters);
    }

    /**
     * Get all the ID counters from the DataBundle.
     *
     * @return a collection of all the id counters.
     */
    public IdCounterCollection getIdCounters() {
        return new IdCounterCollection(meetingIdCounter, tradeIdCounter, itemIdCounter);
    }

    /**
     * Get all the requests from the DataBundle.
     *
     * @return a collection of all the requests.
     */
    public RequestCollection getRequests() {
        return new RequestCollection(reportRequests, unfreezeRequests, itemRequests);
    }

    /**
     * Get all the thresholds from the DataBundle
     *
     * @return a collection of all the thresholds.
     */
    public ThresholdCollection getThresholds() {
        return new ThresholdCollection(maxIncompleteTrade, maxWeeklyTransaction, minLendBorrowDifference,
                maxMeetingEdits, maxMeetingLateTime);
    }
}
