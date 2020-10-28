package core.trade;

import core.item.Item;
import core.reverter.ActionReverter;
import core.trade.reverter.DenyTradeReverter;
import core.trade.reverter.RequestTradeReverter;
import genericdatatype.Pair;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that manage actions and information stored in <code>Trade</code>, extends from <code>TradeManager</code>. Note
 * that this class does not mutate any <code>Item</code>.
 */
public class TradeFunctionManager {
    private int newestTradeId;

    /**
     * Construct a UserTradeManager with a list of <code>trades</code>, <code>maxIncompleteTrade</code>,
     * <code>maxWeeklyTransaction</code>, <code>newestTradeId</code>
     *
     * @param newestTradeId the id of the most recent <code>Trade</code> id.
     */
    public TradeFunctionManager(int newestTradeId) {
        this.newestTradeId = newestTradeId;
    }

    /**
     * <code>User</code> with <code>username1</code> initiate a <code>Trade</code> to be accepted by <code>User</code>
     * with <code>username2</code>.
     * <p>
     * Precondition: There exists <code>User</code> with <code>username1</code> and <code>User</code> with
     * <code>username2</code>.
     *
     * @param username1   <code>username</code> of the initiator of the <code>Trade</code>.
     * @param username2   <code>username</code> of the responder of the <code>Trade</code>.
     * @param item1To2    Item lent by username1 to username2.
     * @param item2To1    Item lent by username2 to username1.
     * @param isPermanent Equals true if the <code>Trade</code> initiated is a permanent <code>Trade</code>, false
     *                    if it is a temporary <code>Trade</code>.
     * @param trades      a collection of all <code>Trade</code>s currently stored
     * @return a <code>RequestTradeReverter</code> where the passed in arguments(parameters) include this <code>Trade</code>,
     * the initiator of this <code>Trade</code>, and a collection of all <code>Trade</code>s currently stored
     */
    public ActionReverter requestTrade(String username1, String username2, Item item1To2, Item item2To1,
                                       boolean isPermanent, List<Trade> trades) {
        Trade trade = new Trade(username1, username2, new Item[]{item1To2, item2To1}, isPermanent, ++newestTradeId);
        trades.add(trade);
        return new RequestTradeReverter(trade, username1, trades);
    }

    /* THIS IS NOT AN ACTION DONE BY USER! A trade is automatically cancelled if meeting exceeded maximum edit times.*/

    /**
     * Changes the status of <code>Trade</code> to cancelled.
     *
     * @param trade the <code>Trade</code> that needs to be cancelled.
     */
    public void cancelTrade(Trade trade) {
        if (trade != null) trade.setStatus(TradeStatus.CANCELLED);
    }

    /**
     * Agrees to a <code>Trade</code> request. Before this function returns true, this <code>Trade</code> is not started.
     * Action is successful iff user with username is the responder of this Trade.
     *
     * @param trade    the <code>Trade</code> that user agree to a trade request.
     * @param username the username of the user calling this.
     * @return 0 agree to trade successfully, 1 fail because user is the initiator,
     * 2 fail because trade is <code>NOT_STARTED</code>, other digit means unknown reason.
     */
    public int agreeToTrade(Trade trade, String username) {
        if (trade == null) return -1;
        if (!username.equals(trade.getUsername(1))) return 1;
        if (trade.getStatus() != TradeStatus.NOT_STARTED) return 2;
        trade.setStatus(TradeStatus.ONGOING);
        return 0;
    }

    /**
     * Denys a a <code>Trade</code> request. The status of the trade will be <code>DENIED</code>. Action is successful
     * iff user with username is the responder of this Trade.
     *
     * @param trade    the <code>Trade</code> that user agree to a trade request.
     * @param username the username of the user calling this.
     * @return Pair with value1 null or ActionReverter of this action, value2 as 0 deny trade successfully,
     * 1 fail because user is the initiator, 2 fail because trade is <code>NOT_STARTED</code>,
     * other digit means unknown reason.
     */
    public Pair<ActionReverter, Integer> denyTrade(Trade trade, String username) {
        if (trade == null) return new Pair<>(null, -1);
        if (!username.equals(trade.getUsername(1))) return new Pair<>(null, 1);
        if (trade.getStatus() != TradeStatus.NOT_STARTED) return new Pair<>(null, 2);
        trade.setStatus(TradeStatus.DENIED);
        return new Pair<>(new DenyTradeReverter(trade, username), 0);
    }

    /**
     * Adds the <code>meetingId</code> of a <code>meeting</code> related to given <code>Trade</code>.
     * Return true iff the action succeeded
     *
     * @param trade         the <code>Trade</code> need its meeting added.
     * @param meetingNumber the id of <code>meeting</code>.
     * @return true iff the meeting is added
     */
    public boolean addMeetingRelated(Trade trade, Integer meetingNumber) {
        if (trade != null && canAddMeeting(trade)) {
            trade.addMeetingRelated(meetingNumber);
            return true;
        }
        return false;
    }

    /* Called by addMeetingRelated, check whether or not can add meeting */
    private boolean canAddMeeting(Trade trade) {
        if (trade.getStatus() == TradeStatus.ONGOING) {
            if (trade.getRelatedMeetings().length == 0) {
                return true;
            } else return trade.getRelatedMeetings().length == 1 && !trade.isPermanent();
        }
        return false;
    }

    /**
     * Updates the <code>Trade</code> <code>status</code> as one meeting has completed.
     *
     * @param trade the <code>Trade</code> that has one meeting completed.
     * @return true if this <code>Trade</code> exists and is completed, false if it is still not completed yet.
     */
    public boolean updateTradeOneMeeting(Trade trade) {
        if (trade != null) {
            trade.setCurrentMeetingOccurred();
            if (trade.isPermanent() || trade.getRelatedMeetings().length == 2) {
                trade.setStatus(TradeStatus.COMPLETED);
                trade.setTradeCompletionTime(LocalDateTime.now());
                return true;
            }
        }
        return false;
    }

    /**
     * Checks all trades and change status of trades that has late meeting to "abandoned".
     * A trade is abandoned when the meeting is arranged by the two users but at least one of them does not confirm the
     * meeting has occurred <code>maxMeetingLateTime</code> days after the proposed meeting time.
     *
     * @param meetingIds a list of ids of meetings that are late.
     * @param trades     all <code>trades</code> that will be checked
     * @return list of username of Users involved in abandoned trade.
     */
    public List<String> updateTradesAbandoned(List<Integer> meetingIds, List<Trade> trades) {
        List<String> usernames = new ArrayList<>();
        for (Trade trade : trades) {
            int meetingId = trade.getCurrentMeetingRelated();
            if (meetingId >= 0 && trade.getStatus() == TradeStatus.ONGOING && meetingIds.contains(meetingId)) {
                trade.setStatus(TradeStatus.ABANDONED);
                usernames.add(trade.getUsername(0));
                usernames.add(trade.getUsername(1));
            }
        }
        return usernames;
    }

    /**
     * Return the newest trade id
     *
     * @return the newest trade id
     */
    public int getNewestTradeId() {
        return newestTradeId;
    }
}
