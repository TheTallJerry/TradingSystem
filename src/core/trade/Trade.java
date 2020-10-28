package core.trade;

import core.item.Item;
import genericdatatype.Pair;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * An abstract class that represents a trade between two users
 */
public class Trade implements Serializable {
    private final Pair<String, String> usernames;
    private final Pair<Item, Item> itemsInvolved;
    /* value1 of the pair are an array of meetingId, value2 of the pair are whether or not meeting occurred. */
    private final Pair<Pair<Integer, Integer>, Pair<Boolean, Boolean>> meetingRelated;
    private final boolean isOneWay;
    private final boolean isPermanent;
    private TradeStatus status;
    private LocalDateTime tradeCompletionTime;
    private final int tradeId;

    /**
     * Creates a Trade instance with initiator <code>username1</code>, responder <code>username2</code>,
     * a pair of items involved in the trade <code>itemsInvolved</code>, whether the trade is permanent or not
     * <code>isPermanent</code>, and a unique identifier <code>tradeId</code>. Initially, no meeting is assigned,
     * <code>status</code> of trade is "notStarted". <code>isEvaluate</code> is false
     * <p>
     * Precondition: <code>username1</code> and <code>username2</code> are valid.
     * <code>tradeId</code> is not negative.
     * the second slot of <code>itemsInvolved</code>, which contains item lend by responder, is not null.
     * trade has to be either a permanent trade or temporary trade.
     *
     * @param username1     the username of user that is the initiator of the trade
     * @param username2     the username of user that is the responder of the trade.
     * @param itemsInvolved a pair of Items involved in this trade. Contain one or two items.
     * @param isPermanent   a boolean that is true if the trade is permanent, false if it is temporary.
     * @param tradeId       a unique identifier of trade.
     */
    public Trade(String username1, String username2, Item[] itemsInvolved, boolean isPermanent, int tradeId) {
        this.usernames = new Pair<>(username1, username2);
        this.itemsInvolved = new Pair<>(itemsInvolved[0], itemsInvolved[1]);
        this.isOneWay = itemsInvolved[0] == null;
        this.status = TradeStatus.NOT_STARTED;
        this.isPermanent = isPermanent;
        this.meetingRelated = new Pair<>(new Pair<>(-1, -1), new Pair<>(false, false)); // doesn't contain any meeting yet
        this.tradeId = tradeId;
    }

    /**
     * Gets the <code>tradeId</code> of the trade.
     *
     * @return the <code>tradeId</code> of the trade.
     */
    public int getTradeId() {
        return tradeId;
    }

    /**
     * Gets the username of one of the user involved in the trade depends on the <code>userOrder</code> given.
     * <p>
     * Precondition: <code>userOrder</code> must be 0 or 1.
     *
     * @param userOrder an int a user involved in this trade. 0 refers to the initiator, 1 refers to the responder.
     * @return the username of the User.
     */
    public String getUsername(int userOrder) {
        if (userOrder == 0)
            return usernames.value1;
        else
            return usernames.value2;
    }

    /**
     * Gets <code>itemsInvolved</code> in the trade.
     *
     * @return <code>itemsInvolved</code>, which contains items involved in the trade.
     */
    public Item[] getItemsInvolved() {
        if (isOneWay) return new Item[]{null, itemsInvolved.value2};
        return new Item[]{itemsInvolved.value1, itemsInvolved.value2};
    }

    /**
     * Sets the trade to <code>newStatus</code>.
     * <p>
     * Precondition: newStatus has to be one of NOT_STARTED/DENIED/ONGOING/COMPLETED/CANCELLED/ABANDONED.
     *
     * @param newStatus status of the trade.
     */
    public void setStatus(TradeStatus newStatus) {
        status = newStatus;
    }

    /**
     * Gets the current <code>status</code> of the trade.
     *
     * @return <code>status</code> of the trade.
     */
    public TradeStatus getStatus() {
        return status;
    }

    /**
     * Gets all <code>meetingId</code> of meetings related to this trade.
     *
     * @return an array of <code>meetingId</code> related to this trade. If no meeting is created, return empty array.
     */
    public int[] getRelatedMeetings() {
        if (meetingRelated.value1.value1 < 0) return new int[0];
        if (meetingRelated.value1.value2 < 0) return new int[]{meetingRelated.value1.value1};
        return new int[]{meetingRelated.value1.value1, meetingRelated.value1.value2};
    }

    /**
     * Gets the <code>meetingId</code> of the currently ongoing meeting about this trade.
     *
     * @return <code>meetingId</code> of the currently ongoing meeting about this trade. If no meeting is ongoing,
     * return -1.
     */
    public int getCurrentMeetingRelated() {
        if (meetingRelated.value1.value2 >= 0 && !meetingRelated.value2.value2) return meetingRelated.value1.value2;
        if (meetingRelated.value1.value1 >= 0 && !meetingRelated.value2.value1) return meetingRelated.value1.value1;
        return -1;
    }

    /**
     * Adds the <code>meetingId</code> of a meeting related to this trade.
     *
     * @param meetingId the meetingId of a meeting related to this trade.
     */
    public void addMeetingRelated(Integer meetingId) {
        if (meetingRelated.value1.value1 >= 0) meetingRelated.value1.value2 = meetingId;
        else meetingRelated.value1.value1 = meetingId;
    }

    /**
     * Indicates that the current on going Meeting related to this trade has occurred.
     */
    public void setCurrentMeetingOccurred() {
        if (meetingRelated.value2.value1) meetingRelated.value2.value2 = true;
        else meetingRelated.value2.value1 = true;
    }

    /**
     * Gets the <code>tradeCompletionTime</code>, which is the time that all the transactions have been confirmed by
     * both users.
     *
     * @return the date and time when the trade is completed. All the transactions involved in the trade have been
     * confirmed by both users by this date.
     */
    public LocalDateTime getTradeCompletionTime() {
        return this.tradeCompletionTime;
    }

    /**
     * Sets the <code>tradeCompletionTime</code> Time, which is the time that all the transactions have been confirmed
     * by both users.
     *
     * @param tradeCompletionTime the date and time when the trade is completed. All the transactions involved in the
     *                            trade have been confirmed by both users by this date.
     */
    public void setTradeCompletionTime(LocalDateTime tradeCompletionTime) {
        this.tradeCompletionTime = tradeCompletionTime;
    }

    /**
     * Gets whether the trade is a permanent trade or not.
     *
     * @return true if the trade is a permanent trade, false if it is a temporary trade.
     */
    public boolean isPermanent() {
        return isPermanent;
    }

    /**
     * Gives one simplified description of the trade.
     *
     * @return information about the trade as array of string, including usernames of user involved and tradeId.
     */
    public String simplifiedInfo() {
        return "Trade ID: [" + tradeId + "]     Username1(Initiator): [" + usernames.value1 +
                "]     Username2(Responder): [" + usernames.value2 + "] " + status;
    }

    /* Gives a formatted string contains all information about items in the trade. */
    private String itemInfo(Item item) {
        if (item == null) return "None";
        else return "<br>&ensp;Name: " + item.getName() + "<br>&ensp;Type: " + item.getType() +
                "<br>&ensp;ID: " + item.getId();
    }

    /**
     * Gives a formatted string contains all information about trade.
     *
     * @return information about the trade as array of string, including usernames of user involved, all items involved, trade
     * type, meetingId of meeting related to this trade.
     */
    public String toString() {
        StringBuilder s = new StringBuilder("<html>Username1(Initiator): " + usernames.value1 +
                "<br>Username2(Responder): " + usernames.value2 + "<br>Item From[" + usernames.value1 + "]: ");
        s.append(itemInfo(itemsInvolved.value1)).append("<br>Item From[").append(usernames.value2).append("]: ");
        s.append(itemInfo(itemsInvolved.value2)).append("<br>Trade Status: ").append(status).append("<br>Trade Type: ");
        if (isOneWay)
            s.append("[One Way]");
        else
            s.append("[Two Way]");
        if (isPermanent)
            s.append("[Permanent]</html>");
        else
            s.append("[Temporary]</html>");
        return s.toString();
    }
}
