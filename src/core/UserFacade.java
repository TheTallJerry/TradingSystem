package core;

import core.meeting.MeetingManager;
import core.meeting.MeetingThresholdType;
import core.reverter.ActionReverter;
import core.reverter.ReverterManager;
import core.trade.*;
import core.user.UserAccountManager;
import core.user.UserStatusManager;
import core.useritem.UserItemManager;
import genericdatatype.Pair;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * A user facade class that pass all operations a <code>User</code> can do to corresponding manager class.
 */
public class UserFacade {
    private final UserAccountManager userAccountManager;
    private final UserItemManager userItemManager;
    private final UserStatusManager userStatusManager;
    private final TradeFunctionManager tradeFunctionManager;
    private final TradeInfoManager tradeInfoManager;
    private final MeetingManager meetingManager;
    private final TradeThresholdManager tradeThresholdManager;
    private final ReverterManager reverterManager;
    private final String currUsername;

    /**
     * Construct a <code>core.UserFacade</code> with <code>username</code>, <code>allUser</code>,
     * <code>unfreezeRequests</code>,
     * a list of all <code>reportedUsers</code>, a list of all <code>itemRequests</code> send by users,
     * <code>newestItemId</code> for
     *
     * @param currUsername                username of the user currently using.
     * @param entityAndReverterCollection a <code>EntityAndReverterCollection</code> for constructing a
     *                                    <code>ReverterManager</code>
     * @param idCounterCollection         a <code>IdCounterCollection</code> for constructing a
     *                                    <code>UserItemManager</code>
     * @param requestCollection           a <code>RequestCollection</code> for constructing a
     *                                    <code>UserStatusManager</code>
     * @param thresholdCollection         a <code>ThresholdCollection</code> for constructing a
     *                                    <code>TradeThresholdManager</code>
     */
    public UserFacade(String currUsername, EntityAndReverterCollection entityAndReverterCollection,
                      IdCounterCollection idCounterCollection,
                      RequestCollection requestCollection,
                      ThresholdCollection thresholdCollection) {
        this.currUsername = currUsername;
        reverterManager = new ReverterManager(entityAndReverterCollection.actionReverters);
        this.userAccountManager = new UserAccountManager(entityAndReverterCollection.users);
        this.userItemManager = new UserItemManager(requestCollection.itemRequests, idCounterCollection.itemIDCounter);
        this.meetingManager = new MeetingManager(
                entityAndReverterCollection.meetings,
                thresholdCollection.maxMeetingEdits,
                idCounterCollection.meetingIDCounter,
                thresholdCollection.maxMeetingLateTime);
        this.userStatusManager = new UserStatusManager(requestCollection.reportRequests, requestCollection.unfreezeRequests);
        this.tradeFunctionManager = new TradeFunctionManager(idCounterCollection.tradeIDCounter);
        this.tradeThresholdManager = new TradeThresholdManager(
                thresholdCollection.maxIncompleteTrade,
                thresholdCollection.maxWeeklyTransaction,
                thresholdCollection.minLendBorrowDifference);
        this.tradeInfoManager = new TradeInfoManager(entityAndReverterCollection.trades);
    }

    /**
     * Checks if <code>username</code> exits
     *
     * @param username the username to be checked.
     * @return true if a <code>User</code> with username exits.
     */
    public boolean usernameExits(String username) {
        return userAccountManager.usernameExists(username);
    }

    /**
     * Changes <code>user</code> password to the given <code>password</code>.
     *
     * @param password a String of password <code>user</code> want to change to.
     */
    public void setPassword(String password) {
        ActionReverter a = userAccountManager.setPassword(userAccountManager.getAccount(currUsername), password);
        reverterManager.addReverter(a);
    }

    /**
     * Gets <code>user</code>'s password.
     *
     * @param currUsername the current <code>User</code>s username
     * @return a String of password.
     */
    public String getPassword(String currUsername) {
        return userAccountManager.getPassword(userAccountManager.getAccount(currUsername));
    }

    /**
     * Changes <code>user</code>'s city to the given <code>city</code>.
     *
     * @param city a String of city <code>user</code> want to change to.
     */
    public void setCity(String city) {
        reverterManager.addReverter(userAccountManager.setCity(currUsername, city));
    }

    /**
     * Gets for <code>user</code>'s city
     *
     * @return city of <code>user</code>
     */
    public String getCity() {
        return userAccountManager.getCity(currUsername);
    }

    /**
     * Deletes <code>blockUsername</code> from <code>user</code>'s <code>blockList</code>.
     *
     * @param blockUsername the<code>username</code> of the <code>user</code> that will be blocked
     * @return true if deleted <code>blockUsername</code> successfully.
     */
    public boolean deletedFromBlockList(String blockUsername) {
        ActionReverter a = userAccountManager.deletedFromBlockList(currUsername, blockUsername);
        if (a == null) return false;
        reverterManager.addReverter(a);
        return true;
    }

    /**
     * Adds <code>blockUsername</code> of into current <code>User</code>'s <code>blockList</code>.
     *
     * @param blockUsername the<code>username</code> of the <code>user</code> that will be blocked
     * @return true if added <code>blockUsername</code> successfully.
     */
    public boolean addToBlockList(String blockUsername) {
        ActionReverter a = userAccountManager.addToBlockList(currUsername, blockUsername);
        if (a == null) return false;
        reverterManager.addReverter(a);
        return true;
    }

    /**
     * Gets the <code>blockList</code> of <code>user</code>.
     *
     * @return <code>blockList</code> of <code>user</code>.
     */
    public List<String> getBlockList() {
        return userAccountManager.getBlockList(currUsername);
    }

    public String getUsernameFromItemID(Integer item) {
        return userItemManager.getUsernameFromItem(userItemManager.getItemFromId(
                item, userAccountManager.getFilteredUsers(currUsername)), userAccountManager);
    }

    /**
     * Gets the users on-vacation status.
     *
     * @return true if the status is turned on.
     */
    public boolean getOnVacation() {
        return userStatusManager.onVacation(userAccountManager.getAccount(currUsername));
    }

    /**
     * Gets all details of <code>User</code> account.
     *
     * @return a string representative of the description of <code>User</code> account, or empty list if
     * <code>user</code> does not exist in the system.
     */
    public String[] getUserAccountSpecifics() {
        return userAccountManager.getUserAccountSpecifics(currUsername);
    }

    /**
     * Subtract credit of a list of <code>User</code>s' if they has new <code>Trade</code>s abandoned.
     */
    public void updateCreditSubtraction() {
        userAccountManager.creditSubtraction(tradeFunctionManager.updateTradesAbandoned(
                meetingManager.getLateMeetingIds(), tradeInfoManager.getTrades()));
    }

    /**
     * Send <code>message</code> to <code>User</code> with <code>receiverUsername</code>.
     *
     * @param receiverUsername the username of a <code>User</code> that <code>user</code> send message to.
     * @param message          the String message to be sent.
     * @return true iff the message is sent successfully
     */
    public boolean sendPrivateMessage(String receiverUsername, String message) {
        ActionReverter actionReverter = userAccountManager.oneToOneMessage(userAccountManager.getAccount(currUsername),
                userAccountManager.getAccount(receiverUsername), message);
        if(actionReverter == null)
            return false;
        reverterManager.addReverter(actionReverter);
        return true;
    }

    /**
     * Gets <code>messageReceived</code>, a list of all message received by <code>user</code>.
     *
     * @return <code>messageReceived</code> of <code>user</code>.
     */
    public List<String> getMessageReceived() {
        return userAccountManager.getMessageReceived(userAccountManager.getAccount(currUsername));
    }

    /**
     * Gets <code>messageSent</code>, a list of all message sent by <code>user</code>.
     *
     * @return <code>messageSent</code> of <code>user</code>.
     */
    public List<String> getMessageSent() {
        return userAccountManager.getMessageSent(userAccountManager.getAccount(currUsername));
    }

    /* The following methods are passed to userItemManager */

    /**
     * Get the newest Id for an Item.
     *
     * @return a int of the newest Id of the given Item
     */
    public int getNewestItemId() {
        return userItemManager.getNewestItemId();
    }

    /**
     * Gets information of all items that <code>user</code> can borrow.
     *
     * @return a list of String which contains item summary and the corresponding username.
     */
    public List<String> getInventory() {
        return userItemManager.getInventory(userAccountManager.getFilteredUsers(currUsername));
    }

    /**
     * get a collection of descriptions of all <code>items</code> in the current <code>users</code>
     * <code>itemAvailable</code>
     *
     * @return a list of String that are descriptions of all <code>items</code> in the current <code>users</code>
     * <code>itemAvailable</code>
     */
    public List<String> getUserItemsAvailable() {
        return userItemManager.getUserItemsAvailable(userAccountManager.getAccount(currUsername));
    }

    /**
     * Get a map where the key is a long description of this item used when browsing inventory, and where the
     * value is the id of the said item.
     *
     * @return a map where the key is a long description of this item used when browsing inventory, and where the
     * value is the id of the said item.
     */
    public Map<String, Integer> getItemDescriptionToIDMap() {
        return userItemManager.getItemDescriptionToIDMap();
    }

    /**
     * Get a map where the key is a long description of this item used when browsing inventory, and where the
     * value is the id of the said item.
     *
     * @return a map where the key is a long description of this item used when browsing inventory, and where the
     * value is the id of the said item.
     */
    public Map<String, Integer> getWishlistItemDescriptionToIDMap() {
        return userItemManager.getWishlistItemDescriptionToIDMap();
    }

    /**
     * Requests to add an <code>Item</code> to <code>User</code>'s <code>itemAvailable</code>.
     *
     * @param itemType        the type of the item that is requested.
     * @param itemName        the name of the item that is requested.
     * @param itemDescription the description of the item that is requested.
     */
    public void createItemAndRequest(String itemType, String itemName, String itemDescription) {
        reverterManager.addReverter(userItemManager.createItemAndRequest(
                currUsername, itemType, itemName, itemDescription));
    }

    /**
     * Deletes <code>Item</code> with <code>itemId</code> in <code>user</code>'s wishlist.
     *
     * @param itemId the id of the <code>Item</code>
     * @return true iff the deletion is successful.
     */
    public boolean deleteFromWishlist(int itemId) {
        ActionReverter a = userItemManager.deleteFromWishlist(userAccountManager.getAccount(currUsername),
                userItemManager.getItemFromId(itemId, userAccountManager.getFilteredUsers(currUsername)));
        if (a == null) return false;
        reverterManager.addReverter(a);
        return true;
    }

    /**
     * Adds <code>Item</code> with <code>itemId</code> in <code>user</code>'s wishlist.
     *
     * @param itemId the id of the <code>Item</code>
     * @return true iff the addition is successful
     */
    public boolean addToWishlist(int itemId) {
        ActionReverter a = userItemManager.addToWishlist(userAccountManager.getAccount(currUsername),
                userItemManager.getItemFromId(itemId, userAccountManager.getFilteredUsers(currUsername)));
        if (a == null) return false;
        reverterManager.addReverter(a);
        return true;
    }

    /**
     * Return a list of string representations of items in this user's wishlist
     *
     * @return a list of string representations of items in this user's wishlist
     */
    public List<String> getUserWishlist() {
        return userItemManager.getUserWishlist(userAccountManager.getAccount(currUsername));
    }

    /**
     * Suggests a list of items from <code>User</code> to lend to <code>borrower</code>. Only items that are both in
     * <code>User</code>'s <code>itemAvailable</code> and <code>borrower</code>'s <code>wishList</code> are included.
     *
     * @param borrowerId the id of the borrow transaction
     * @return a list of item that are both in lender's available list and borrower's wish list.
     */
    public List<String> createLendingSuggestion(String borrowerId) {

        return userItemManager.createLendingSuggestion(userAccountManager.getAccount(currUsername),
                userAccountManager.getAccount(borrowerId));
    }

    /**
     * Adds unfreeze request to <code>user</code>'s <code>unfreezeRequests</code> list.
     *
     * @return whether the action was done or not.
     */
    public boolean requestUnfreeze() {
        return userStatusManager.requestUnfreeze(userAccountManager.getAccount(currUsername));
    }

    /**
     * Reports another user for inappropriate actions. Report will only be successful if both user exits in the system
     * and the user is not reporting him/herself.
     *
     * @param reportedUsername the <code>user</code> who is reported
     * @param reason           the reason why this <code>user</code> has been reported
     * @return true if the report is successful
     */
    public boolean reportUser(String reportedUsername, String reason) {
        return userStatusManager.reportUser(userAccountManager.getAccount(currUsername), userAccountManager.getAccount(reportedUsername), reason);
    }

    /**
     * Turns on vacation status of the user with username if the status is off, otherwise turn it off. No action can
     * be made if the user is not in the system.
     *
     * @return true if the user on vacation.
     */
    public boolean switchVacationStatus() {
        reverterManager.addReverter(userStatusManager.switchVacationStatus(userAccountManager.getAccount(currUsername)));
        return userAccountManager.getAccount(currUsername).onVacation();
    }

    /* The following methods are passed to userTradeManager */

    /**
     * Return the newest trade id
     *
     * @return the newest trade id
     */
    public int getNewestTradeId() {
        return tradeFunctionManager.getNewestTradeId();
    }

    /**
     * Gets three recent trade item information of <code>User</code>.
     *
     * @return a list of information about at most 3 recent traded <code>Item</code>s in Trade. An empty list if no
     * trade happened.
     */
    public List<String> getThreeRecentTradeItem() {
        return tradeInfoManager.getThreeRecentTradeItem(currUsername);
    }

    /**
     * Gets information about 3 most frequent trader partner of <code>User</code>.
     *
     * @return a list of information about at most 3 most frequent trader partner with given User. An empty list if no
     * trade happened.
     */
    public List<String> getTopThreeTradePartner() {
        return tradeInfoManager.getTopThreeTradePartner(currUsername);
    }

    /**
     * Agree to a trade request. Before this function returns true, this trade is not started.
     *
     * @param tradeId the id of the Trade that user agree to a trade request
     * @return 0 agree to trade successfully, 1 fail because user is the initiator,
     * 2 fail because trade is <code>NOT_STARTED</code>, 3 fail because items has been traded,
     * other digit means unknown reason.
     */
    public int agreeToTrade(int tradeId) {
        int userOrder = tradeInfoManager.findUserOrder(tradeId, currUsername);
        /* item is not null, not in available of one of the user */
        if ((tradeInfoManager.getItemsInvolved(tradeId)[userOrder] != null &&
                userItemManager.itemCanNotLend(userAccountManager.getAccount(currUsername),
                        tradeInfoManager.getItemsInvolved(tradeId)[userOrder].getId())) ||
                (tradeInfoManager.getItemsInvolved(tradeId)[Math.abs(userOrder - 1)] != null &&
                        userItemManager.itemCanNotLend(userAccountManager.getAccount(
                                tradeInfoManager.getUsername(tradeId, Math.abs(userOrder - 1))),
                                tradeInfoManager.getItemsInvolved(tradeId)[Math.abs(userOrder - 1)].getId()))) {
            return 3;
        }
        int result = tradeFunctionManager.agreeToTrade(tradeInfoManager.getTrade(tradeId), currUsername);
        if (result == 0) userItemManager.updateUserItems(userAccountManager.getAccount(
                tradeInfoManager.getUsername(tradeId, 0)), userAccountManager.getAccount(currUsername),
                tradeInfoManager.getItemsInvolved(tradeId)[0], tradeInfoManager.getItemsInvolved(tradeId)[1]);
        return result;
    }

    /**
     * Denies a trade request. If the action is successful, add <code>ActionReverter</code> to
     * <code>reverterManager</code>.
     *
     * @param tradeId the id of the Trade that user agree to a trade request
     * @return 0 deny trade successfully, 1 fail because user is the initiator,
     * 2 fail because trade is <code>NOT_STARTED</code>, other digit means unknown reason.
     */
    public int denyTrade(int tradeId) {
        Pair<ActionReverter, Integer> pair = tradeFunctionManager.denyTrade(tradeInfoManager.getTrade(tradeId), currUsername);
        if (pair.value2 == 0) reverterManager.addReverter(pair.value1);
        return pair.value2;
    }

    /**
     * Suggest a list of items user might like depends on the user's most frequent borrow types.
     *
     * @return a list, with the first thing a string of the user's most frequent borrow types, the rest of thing the
     * items the user may like as Strings.
     */
    public List<String> createBorrowingSuggestion() {
        return userItemManager.createBorrowingSuggestion(userAccountManager.getAccount(currUsername),
                userAccountManager.getFilteredUsers(currUsername),
                tradeInfoManager.getTopThreeTradeItemType(currUsername));
    }

    /* If itemId1To2 is -1, meaning that username1 do not lend anything to username2, result in getItemFromId returning
     * a null value.
     */

    /**
     * Requests a trade on behalf of this User. Request will only be successful if the <code>Item</code> this
     * <code>User</code> wants to borrow exists and can be borrow by him. If this <code>User</code> want to lend
     * <code>Item</code> to the responder of this trade, that <code>Item</code> must be in his ItemAvailable.
     *
     * @param itemId1To2  An Item that User with username1 want to lend to User with username username2.
     * @param itemId2To1  An Item that username1 want username2 to borrow.
     * @param isPermanent Equals true if the trade initiated is a permanent trade, false if it is a temporary trade.
     * @return true if the trade is requested successfully.
     */
    public boolean requestTrade(int itemId1To2, int itemId2To1, boolean isPermanent) {
        if (userItemManager.itemCanNotLend(userAccountManager.getAccount(currUsername), itemId1To2) && itemId1To2 != -1) {
            return false;
        }
        if (userItemManager.itemCanBorrow(userAccountManager.getFilteredUsers(currUsername), itemId2To1)) {
            ActionReverter a = tradeFunctionManager.requestTrade(
                    userAccountManager.getAccount(currUsername).getUsername(),
                    userItemManager.getUsernameFromItem(
                            userItemManager.getItemFromId(itemId2To1, userAccountManager.getFilteredUsers(currUsername)),
                            userAccountManager),
                    userItemManager.getItemFromId(itemId1To2, userAccountManager.getAccount(currUsername)),
                    userItemManager.getItemFromId(itemId2To1,
                            userAccountManager.getAccount(getUsernameFromItemID(itemId2To1))),
                    isPermanent, tradeInfoManager.getTrades());
            if (a == null) return false;
            reverterManager.addReverter(a);
            return true;
        }
        return false;
    }

    /**
     * Find all information of Trade of User that match tradeStatus and meetingIDs.
     *
     * @return a List of String which contain the information of Trades.
     */
    public List<String[]> getTradesInfo() {
        return tradeInfoManager.getTradesInfo(currUsername);
    }

    /**
     * Gets the <code>meetingId</code> of the currently ongoing meeting about this trade.
     *
     * @param tradeId the id of Trade.
     * @return null if Trade not exists, or return <code>meetingId</code> of the currently ongoing meeting about
     * this trade. If no meeting is ongoing, return -1.
     */
    public int getCurrentMeetingRelated(int tradeId) {
        return tradeInfoManager.getCurrentMeetingRelated(tradeId);
    }

    /**
     * Gets a threshold related to Trade. It can be <code>maxIncompleteTrade</code>, <code>maxWeeklyTransactions</code>,
     * or <code>minLendBorrowDifference</code>.
     *
     * @return the corresponding threshold being asked for.
     */
    public int[] getTradeThresholds() {
        return new int[]{
                tradeThresholdManager.getThreshold(TradeThresholdType.MAX_INCOMPLETE_TRADES),
                tradeThresholdManager.getThreshold(TradeThresholdType.MIN_LEND_BORROW_DIFF),
                tradeThresholdManager.getThreshold(TradeThresholdType.MAX_WEEKLY_TRANSACTIONS)};
    }

    /**
     * Return whether the <code>Trade</code> is on the <code>NOT_STARTED</code> status.
     *
     * @param tradeId tradeId of the <code>Trade</code>
     * @return true iff the trade is <code>NOT_STARTED</code>.
     */
    public boolean tradeNotStarted(int tradeId) {
        return tradeInfoManager.tradeNotStarted(tradeId);
    }

    /* The following methods are passed to userMeetingManager */

    /**
     * Return the newest meeting id
     *
     * @return the newest meeting id
     */
    public int getNewestMeetingId() {
        return meetingManager.getNewestMeetingId();
    }

    /**
     * Find the information of Trade of given meeting ID
     *
     * @param meetingId the id of meeting
     * @return String of information of meeting, otherwise, empty string
     */
    public String getMeetingInfo(int meetingId) {
        return meetingManager.getMeetingInfo(meetingId);
    }

    /**
     * Creates a new the meeting with location, time, and userOrder. Increase TimesEdited of the user who creates the
     * meeting. Add the meeting to <code>meetingRelated</code> of the <code>Trade</code> with <code>tradeId</code> if
     * the meeting is created successfully.
     *
     * @param tradeId  the id of Trade related to this meeting.
     * @param location the location of the meeting.
     * @param time     the time of the meeting.
     * @return true if the meeting is created and added successfully
     */
    public boolean createMeeting(int tradeId, String location, LocalDateTime time) {
        int meetingId = tradeInfoManager.getCurrentMeetingRelated(tradeId);
        /* check if the last meeting is done. */
        if (meetingId < 0 || meetingManager.meetingOccurred(meetingId)) {
            return tradeFunctionManager.addMeetingRelated(tradeInfoManager.getTrade(tradeId),
                    meetingManager.createMeeting(location, time, tradeInfoManager.findUserOrder(tradeId, currUsername)));
        }
        return false;
    }

    /**
     * Confirm time and place of this meeting, given userOrder. Confirmation of the meeting can be made only when the
     * user has not confirmed and the other one has.
     *
     * @param tradeId the id of Trade related to this meeting.
     * @return true if the confirmation is made, false otherwise.
     */
    public boolean confirmMeetingArrangement(int tradeId) {
        ActionReverter a = meetingManager.confirmMeetingArrangement(tradeInfoManager.getCurrentMeetingRelated(tradeId),
                tradeInfoManager.findUserOrder(tradeId, currUsername), currUsername);
        if (a == null) return false;
        reverterManager.addReverter(a);
        return true;
    }

    /**
     * Confirm that the meeting has taken place offline. This confirmation will be successful only when the time and
     * place of the meeting is both confirmed and the user has not confirm meeting occurred before.
     *
     * @param tradeId the id of Trade related to this meeting
     * @return true iff confirmation is successful.
     */
    public boolean confirmMeetingOccurred(int tradeId) {
        //A meeting's occurrence can only be confirmed if the trade is ongoing
        if (tradeInfoManager.getTrade(tradeId).getStatus() != TradeStatus.ONGOING)
            return false;
        int meetingId = tradeInfoManager.getCurrentMeetingRelated(tradeId);
        ActionReverter a = meetingManager.confirmMeetingOccurred(meetingId, tradeInfoManager.findUserOrder(
                tradeId, currUsername), currUsername);
        if (a == null) return false;
        reverterManager.addReverter(a);
        if (meetingManager.meetingOccurred(meetingId) &&
                tradeFunctionManager.updateTradeOneMeeting(tradeInfoManager.getTrade(tradeId)))
            userAccountManager.creditAddition(currUsername);
        return true;
    }

    /**
     * Edits the time and location of the current meeting. Edit is successful only if the user haven't exceed edit limit.
     *
     * @param tradeId     the id of Trade related to this meeting
     * @param meetingTime the meeting date and time
     * @param location    the meeting location
     * @return true if edited meeting successfully. false if the user is exceeding his edit limit.
     */
    public boolean editMeeting(int tradeId, LocalDateTime meetingTime, String location) {
        int meetingId = tradeInfoManager.getCurrentMeetingRelated(tradeId);
        int userOrder = tradeInfoManager.findUserOrder(tradeId, currUsername);
        if (meetingManager.canEditMeeting(meetingId, userOrder)) {
            ActionReverter a = meetingManager.editMeeting(meetingId, meetingTime, location, userOrder, currUsername);
            if (a == null) {
                tradeFunctionManager.cancelTrade(tradeInfoManager.getTrade(tradeId));
                String msg = "Your trade" + tradeId + "is cancelled due to exceeding edit times.";
                /* Function like a system message */
                userAccountManager.oneToOneMessage(userAccountManager.getAccount(currUsername),
                        userAccountManager.getAccount(currUsername), msg);
                return false;
            }
            reverterManager.addReverter(a);
            return true;
        }
        return false;
    }

    /**
     * Gets <code>maxMeetingLateTime</code> and <code>maxMeetingEdits</code>.
     *
     * @return <code>maxMeetingLateTime</code>, <code>maxMeetingEdits</code>.
     */
    public int[] getMeetingThresholds() {
        return new int[]{meetingManager.getThreshold(MeetingThresholdType.MAX_EDITS),
                meetingManager.getThreshold(MeetingThresholdType.MAX_LATE_TIMES)};
    }
}
