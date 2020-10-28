package core;

import core.admin.Admin;
import core.admin.AdminAccountManager;
import core.item.Item;
import core.meeting.MeetingManager;
import core.meeting.MeetingThresholdType;
import core.reverter.ActionReverter;
import core.reverter.ReverterManager;
import core.trade.TradeInfoManager;
import core.trade.TradeStatus;
import core.trade.TradeThresholdManager;
import core.trade.TradeThresholdType;
import core.user.User;
import core.user.UserAccountManager;
import core.user.UserStatusManager;
import core.useritem.UserItemManager;
import genericdatatype.Pair;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminFacade {
    private final TradeThresholdManager tradeThresholdManager;
    private final TradeInfoManager tradeInfoManager;
    private final UserStatusManager userStatusManager;
    private final AdminAccountManager adminAccountManager;
    private final UserAccountManager userAccountManager;
    private final UserItemManager userItemManager;
    private final MeetingManager meetingManager;
    private final ReverterManager reverterManager;

    /* data that assists request handling */
    private final Map<Pair<String, String>, Pair<Admin, String>> creationRequestMap;
    private final Map<Pair<String, String>, String> unfreezeRequestMap;
    private final Map<Pair<String, String>, String[]> reportRequestMap;
    private final Map<Pair<String, String>, Pair<String, Item>> itemRequestMap;

    /**
     * Constructor of AdminFacade
     *
     * @param adminCreationRequest        a list of requests to create new admins
     * @param admins                      a list of existing admins
     * @param entityAndReverterCollection an instance of <code>entityAndReverterCollection</code>
     * @param idCounterCollection         an instance of <code>entityAndReverterCollection</code>
     * @param requestCollection           an instance of <code>requestCollection</code>
     * @param thresholdCollection         an instance of <code>thresholdCollection</code>
     */
    public AdminFacade(List<Pair<Admin, String>> adminCreationRequest, List<Admin> admins,
                       EntityAndReverterCollection entityAndReverterCollection,
                       IdCounterCollection idCounterCollection,
                       RequestCollection requestCollection,
                       ThresholdCollection thresholdCollection) {
        /* Initialize managers */
        reverterManager = new ReverterManager(entityAndReverterCollection.actionReverters);
        tradeThresholdManager = new TradeThresholdManager(
                thresholdCollection.maxIncompleteTrade,
                thresholdCollection.maxWeeklyTransaction,
                thresholdCollection.minLendBorrowDifference);
        tradeInfoManager = new TradeInfoManager(entityAndReverterCollection.trades);
        userStatusManager = new UserStatusManager(
                requestCollection.reportRequests,
                requestCollection.unfreezeRequests);
        adminAccountManager = new AdminAccountManager(admins, adminCreationRequest);
        userAccountManager = new UserAccountManager(
                entityAndReverterCollection.users);
        userItemManager = new UserItemManager(requestCollection.itemRequests, idCounterCollection.itemIDCounter);
        meetingManager = new MeetingManager(
                entityAndReverterCollection.meetings,
                thresholdCollection.maxMeetingEdits,
                idCounterCollection.meetingIDCounter,
                thresholdCollection.maxMeetingLateTime);

        /* set up data assisting request handling */
        creationRequestMap = adminAccountManager.getFormattedAdminCreationRequests();
        unfreezeRequestMap = userStatusManager.getFormattedUnfreezeRequests();
        reportRequestMap = userStatusManager.getFormattedReportRequests();
        itemRequestMap = userItemManager.getFormattedItemRequests();
    }

    /**
     * Get the current password of this admin
     *
     * @param currAdminUsername the username for this admin
     * @return the current password of this admin
     */
    public String getPassword(String currAdminUsername) {
        return adminAccountManager.getPassword(adminAccountManager.getAccount(currAdminUsername));
    }

    /**
     * Set the current password of this admin
     *
     * @param currAdminUsername the username for this admin
     * @param password          the password to set
     */
    public void setPassword(String currAdminUsername, String password) {
        ActionReverter a = adminAccountManager.setPassword(adminAccountManager.getAccount(currAdminUsername), password);
        reverterManager.addReverter(a);
    }

    /**
     * get a collection of requests to create accounts
     *
     * @return a collection of requests to create accounts
     */
    public Collection<Pair<String, String>> getCreationRequests() {
        return creationRequestMap.keySet();
    }

    /**
     * get a collection of requests to unfreeze user accounts
     *
     * @return a collection of requests to unfreeze user accounts
     */
    public Collection<Pair<String, String>> getUnfreezeRequests() {
        return unfreezeRequestMap.keySet();
    }

    /**
     * get a collection of requests to report other users
     *
     * @return a collection of requests to report other users
     */
    public Collection<Pair<String, String>> getReportRequests() {
        return reportRequestMap.keySet();
    }

    /**
     * get a collection of requests to add items to the item available list
     *
     * @return a collection of requests to add items to the item available list
     */
    public Collection<Pair<String, String>> getItemRequests() {
        return itemRequestMap.keySet();
    }

    /**
     * Send the <code>message</code> from the Account of the current admin to the Accounts of all users
     *
     * @param currAdminUsername the username fo the current <code>admin</code>.
     * @param message           the <code>message</code> that needs to be sent.
     */
    public void adminToAllUser(String currAdminUsername, String message) {
        adminAccountManager.oneToAllMessage(adminAccountManager.getAccount(currAdminUsername), message);
    }

    /**
     * Searches a <code>User</code> or <code>Admin</code> by using <code>username</code>. If <code>username</code>
     * correspond to a <code>User</code>, include all trading information about this <code>User</code>.
     *
     * @param username possible username of a <code>User</code>.
     * @return Map of short descriptions to long descriptions of the result entries.
     */
    public Map<String, String> searchByUsername(String username) {
        Map<String, String> result = new HashMap<>();
        User user = userAccountManager.getAccount(username);
        if (user != null)
            result.put(username, user.toString());
        Admin admin = adminAccountManager.getAccount(username);
        if (admin != null)
            result.put(username, admin.toString());
        tradeInfoManager.getTrades(username, TradeStatus.NONE).forEach(trade -> {
            result.put("Trade #" + trade.getTradeId(), trade.toString());
            Map<String, String> meetings = new HashMap<>();
            for (int relatedMeeting : trade.getRelatedMeetings()) {
                meetings.put("Meeting #" + relatedMeeting, meetingManager.getMeetingInfo(relatedMeeting));
            }
            result.putAll(meetings);
        });
        return result;
    }

    /**
     * Set the given threshold type to the threshold newLimit
     *
     * @param type     the type indicating which of the two threshold it is, maxMeetingEdits or maxMeetingLateTime
     * @param newLimit the value of the threshold type to be set to
     */
    public void setMeetingThreshold(MeetingThresholdType type, int newLimit) {
        meetingManager.setThreshold(type, newLimit);
    }

    /**
     * @param type the type indicating which of the two threshold it is, maxMeetingEdits or maxMeetingLateTime
     * @return the maximum num of times a user can edit a meeting or the maximum time given to a user to confirm a meeting
     * after the time the meeting should have occurred.
     */
    public int getMeetingThreshold(MeetingThresholdType type) {
        return meetingManager.getThreshold(type);
    }

    /**
     * Sets the trade related threshold of <code>type</code> to <code>value</code>.
     * <p>
     * Precondition: <code>type</code> must be either MAX_INCOMPLETE_TRADES, MAX_WEEKLY_TRANSACTIONS or
     * MIN_LEND_BORROW_DIFF.
     *
     * @param type the type of threshold to be set.
     * @param val  the new value of <code>type</code> to be set.
     */
    public void setTradeThreshold(TradeThresholdType type, int val) {
        tradeThresholdManager.setThreshold(type, val);
    }

    /**
     * Gets the trade related threshold of <code>type</code>.
     * <p>
     * Precondition: <code>type</code> must be either MAX_INCOMPLETE_TRADES, MAX_WEEKLY_TRANSACTIONS or
     * MIN_LEND_BORROW_DIFF.
     *
     * @param type the type of threshold to be shown.
     * @return the value of the threshold <code>type</code>.
     */
    public int getTradeThreshold(TradeThresholdType type) {
        return tradeThresholdManager.getThreshold(type);
    }

    //AdminUserManager and AdminTradeManager

    /**
     * Return a map contains different <code>usernames</code> desired as keys based on input. Values are all the same
     * int to indicating the type of keys
     *
     * @param type a String indicating the desired <code>usernames</code> type
     * @return a map that keys are satisfied <code>users</code>' username, values are int to indicate what type of
     * <code>users</code> have been found
     */
    public Map<String, Integer> getUserViolateLimit(String type) {
        switch (type) {
            case "Incomplete Trade":
                return tradeThresholdManager.getUserOverMaxIncompleteTrade(userAccountManager, tradeInfoManager);
            case "Weekly Transaction":
                return tradeThresholdManager.getUserOverWeeklyTransMaxMap(tradeInfoManager.getTrades());
            case "Lend-Borrow Difference":
                return tradeThresholdManager.getUsersNotUpToMinLentBorrowDifference(userAccountManager);
            default:
                return userStatusManager.getFrozenUsers(userAccountManager);
        }
    }

    /**
     * Freeze the user with the username
     *
     * @param username freeze the user with the username
     * @return true iff this user exists and frozen successfully.
     */
    public boolean freezeUser(String username) {
        return userStatusManager.freeze(userAccountManager.getAccount(username));
    }

    /**
     * Unfreeze the user with the username
     *
     * @param username unfreeze the user with the username
     * @return true iff this user exists and frozen successfully.
     */
    public boolean unfreezeUser(String username) {
        return userStatusManager.unfreeze(userAccountManager.getAccount(username));
    }

    /**
     * @param key               a pair representing the username and password of the potential new admin
     * @param accept            admin's decision to accept the admin creation request or not
     * @param currAdminUsername the username of the <code>admin</code> that accepts or denies the request
     * @return true iff the request is processed successfully
     */
    public boolean processAdminCreationRequest(Pair<String, String> key, boolean accept, String currAdminUsername) {
        boolean successful;
        if (accept)
            successful = adminAccountManager.acceptAdminCreationRequest(currAdminUsername, creationRequestMap.get(key));
        else
            successful = adminAccountManager.denyAdminCreationRequest(currAdminUsername, creationRequestMap.get(key));
        if (successful) {
            creationRequestMap.remove(key);
        }
        return successful;
    }


    /**
     * @param key    a pair representing a request from a user to report another user
     * @param accept admin's decision to accept the report user request or not
     */
    public void processUserReport(Pair<String, String> key, boolean accept) {
        if (accept)
            userStatusManager.acceptUserReport(reportRequestMap.get(key), userAccountManager.getAccount(reportRequestMap.get(key)[0]));
        else
            userStatusManager.denyUserReport(reportRequestMap.get(key));
        reportRequestMap.remove(key);
    }

    /**
     * @param key    a pair representing a user's request to unfreeze themselves
     * @param accept admin's decision to accept the item request or not
     */
    public void processUnfreezeRequest(Pair<String, String> key, boolean accept) {
        if (accept)
            userStatusManager.acceptUserUnfreezeRequest(userAccountManager.getAccount(unfreezeRequestMap.get(key)));
        else userStatusManager.denyUserUnfreezeRequest(unfreezeRequestMap.get(key));
        unfreezeRequestMap.remove(key);
    }

    /**
     * @param key    a pair representing a request from a user to add an item to their item available list
     * @param accept admin's decision to accept the item request or not
     */
    public void processItemRequest(Pair<String, String> key, boolean accept) {
        if (accept) userItemManager.acceptUserItemRequest(itemRequestMap.get(key), userAccountManager);
        else userItemManager.denyUserItemRequest(itemRequestMap.get(key));
        itemRequestMap.remove(key);
    }

    /**
     * undo the action that can be undone by actionReverter r
     *
     * @param r an instance of <code>ActionReverter</code>
     * @return message indicating whether the reversion process is successful or not
     */
    public String undo(ActionReverter r) {
        String s = r.execute();
        reverterManager.removeRevert(r);
        return s;
    }

    /**
     * Gets all the <code>ActionReverter</code>s sorted by username of <code>User</code> who did the action.
     *
     * @return a <code>Map</code> of username as keys and a list of <code>ActionReverter</code> associate with the
     * username as values.
     */
    public Map<String, List<ActionReverter>> getRevertersByUsername() {
        return reverterManager.getRevertersByUsername();
    }

    /**
     * Gets all the <code>ActionReverter</code>s sorted by their type.
     *
     * @return a <code>Map</code> of type as keys and a list of <code>ActionReverter</code> with that type as values.
     */
    public Map<String, List<ActionReverter>> getRevertersByType() {
        return reverterManager.getRevertersByType();
    }

    /**
     * Creates an Admin
     *
     * @param username          the <code>username</code> of the new <code>Admin</code>
     * @param password          the <code>password</code> of the new <code>Admin</code>
     * @param currAdminUsername the <code>username</code> of the current <code>Admin</code>
     * @return true if successful
     */
    public boolean createAdmin(String username, String password, String currAdminUsername) {
        if (adminAccountManager.getAccount(currAdminUsername).notInitialAdmin() ||
                userAccountManager.usernameExists(username))
            return false;
        return adminAccountManager.createAdmin(username, password);
    }
}
