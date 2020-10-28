package client.controllers;

import client.databundle.DataBundle;
import core.AdminFacade;
import core.meeting.MeetingThresholdType;
import core.reverter.ActionReverter;
import core.trade.TradeThresholdType;
import genericdatatype.Pair;

import java.util.*;

/**
 * A controller class responsible for administration needs.
 */
public class AdminSystem {

    private final String currAdminUsername;
    private final DataBundle dataBundle;
    private final AdminFacade adminFacade;

    /**
     * Constructs an AdminSystem with <code>dataBundle</code> and <code>currAdminUsername</code>.
     *
     * @param dataBundle        the data bundle that will be used initially.
     * @param currAdminUsername the username of the current admin.
     */
    public AdminSystem(DataBundle dataBundle, String currAdminUsername) {
        this.currAdminUsername = currAdminUsername;
        this.dataBundle = dataBundle;
        adminFacade = new AdminFacade(dataBundle.adminCreationRequests, dataBundle.admins,
                dataBundle.getEntitiesAndReverters(),
                dataBundle.getIdCounters(), dataBundle.getRequests(),
                dataBundle.getThresholds());
    }

    /* Input must be a positive integer. */
    private boolean isNotValidThreshold(String input) {
        return !input.matches("^[1-9]\\d*$");
    }

    /* DataBundle is stored such that it can be given to the next screen if new system needs to be created. */

    /**
     * Gets <code>dataBundle</code> stored in this <code>UserSystem</code>.
     *
     * @return <code>dataBundle</code>.
     */
    public DataBundle getDataBundle() {
        return dataBundle;
    }

    /**
     * Get the users that violate a threshold
     *
     * @param threshold can be "maxIncompleteTrade", "maxWeeklyTransaction", or "minLendBorrowDifference"
     * @return a map containing the usernames as keys and the number they exceeded the limit as values
     */
    public Map<String, Integer> getUserViolateLimit(String threshold) {
        return adminFacade.getUserViolateLimit(threshold);
    }

    /**
     * Freeze the user with the username
     *
     * @param username freeze the user with the username
     * @return true iff this user exists and frozen successfully.
     */
    public boolean freezeUser(String username) {
        return adminFacade.freezeUser(username);
    }

    /**
     * Unfreeze the user with the username
     *
     * @param username unfreeze the user with the username
     * @return true iff this user exists and frozen successfully.
     */
    public boolean unfreezeUser(String username) {
        return adminFacade.unfreezeUser(username);
    }

    /**
     * Return a specific Map
     *
     * @return A Map with:
     * &emsp;-keys being request types<br>
     * &emsp;-values are collections of pairs whose:<br>
     * &emsp;&emsp;-keys are short string descriptions of the requests, used for displaying in a JList<br>
     * &emsp;&emsp;-values are long string descriptions of the requests, used for displaying in JDialog<br>
     */
    public Map<RequestType, Collection<Pair<String, String>>> getRequests() {
        Map<RequestType, Collection<Pair<String, String>>> result = new HashMap<>();
        result.put(RequestType.ADMIN_CREATION, adminFacade.getCreationRequests());
        result.put(RequestType.UNFREEZE, adminFacade.getUnfreezeRequests());
        result.put(RequestType.REPORT, adminFacade.getReportRequests());
        result.put(RequestType.ITEM, adminFacade.getItemRequests());
        return result;
    }

    /* Input must be at least 4 digit length */
    private boolean isNotValidString(String input) {
        return input.length() < 4;
    }

    /**
     * Get the password of this admin
     *
     * @return the current password of this admin
     */
    public String getPassword() {
        return adminFacade.getPassword(currAdminUsername);
    }

    /**
     * Set the password of this admin. Cannot set if <code>password</code> is not a valid string for passwords.
     *
     * @param password the password to set
     * @return true iff set the password successfully.
     */
    public boolean setPassword(String password) {
        if (isNotValidString(password))
            return false;
        adminFacade.setPassword(currAdminUsername, password);
        return true;
    }

    /**
     * Get a specific map.
     *
     * @return A Map with:
     * &emsp;-keys being usernames<br>
     * &emsp;-values are maps whose:<br>
     * &emsp;&emsp;-keys are a short string descriptions of the reverter, used for displaying in JList<br>
     * &emsp;&emsp;-values are list of pairs whose: <br>
     * &emsp;&emsp;&emsp;-value1 is a long string descriptions of the reverter, used for displaying in JDialog<br>
     * &emsp;&emsp;&emsp;-value2 is an Object that must be passed as an argument to <code>AdminSystem.undo</code><br>
     */
    public Map<String, Map<String, List<Pair<String, Object>>>> getFormattedRevertersByUsername() {
        Map<String, Map<String, List<Pair<String, Object>>>> result = new HashMap<>();
        for (Map.Entry<String, List<ActionReverter>> e : adminFacade.getRevertersByUsername().entrySet()) {
            result.put(e.getKey(), new HashMap<>());
            e.getValue().forEach((r) -> {
                if (!result.get(e.getKey()).containsKey(r.getActionType()))
                    result.get(e.getKey()).put(r.getActionType(), new ArrayList<>());
                result.get(e.getKey()).get(r.getActionType()).add(new Pair<>(r.getActionDescriptionReverted(), r));
            });
        }
        return result;
    }

    /**
     * Get a reverter by the username.
     *
     * @return A Map similar to that of <code>AdminSystem.getFormattedRevertersByUsername</code>
     */
    public Map<String, Map<String, List<Pair<String, Object>>>> getFormattedRevertersByType() {
        Map<String, Map<String, List<Pair<String, Object>>>> result = new HashMap<>();
        for (Map.Entry<String, List<ActionReverter>> e : adminFacade.getRevertersByType().entrySet()) {
            result.put(e.getKey(), new HashMap<>());
            e.getValue().forEach((r) -> {
                if (!result.get(e.getKey()).containsKey(r.getAssociatedUsername()))
                    result.get(e.getKey()).put(r.getAssociatedUsername(), new ArrayList<>());
                result.get(e.getKey()).get(r.getAssociatedUsername()).add(new Pair<>(r.getActionDescriptionReverted(), r));
            });
        }
        return result;
    }

    /**
     * Undo the action related to <code>reverter</code>.
     *
     * @param reverter <code>ActionReverter</code> used to undo an action.
     * @return String message indicating whether the undo is successful or not.
     */
    public String undo(Object reverter) {
        return adminFacade.undo((ActionReverter) reverter);
    }

    /**
     * Searches a <code>User</code> or <code>Admin</code> by using <code>username</code>. If <code>username</code>
     * correspond to a <code>User</code>, include all trading information about this <code>User</code>.
     *
     * @param username possible username of a <code>User</code>.
     * @return Map of short descriptions to long descriptions of the result entries. Short descriptions are displayed
     * in a JList and long descriptions are displayed in a JDialog.
     */
    public Map<String, String> searchByUsername(String username) {
        return adminFacade.searchByUsername(username);
    }

    /**
     * Accepts or denies a request with <code>type</code>, <code>request</code>,and <code>accepted</code>.
     *
     * @param type     request type.
     * @param request  the actual request going to be accepted or denied.
     * @param accepted true if accept the request, false if deny the request.
     * @return true if the request is handled successfully.
     */
    public boolean handleRequest(RequestType type, Pair<String, String> request, boolean accepted) {
        switch (type) {
            case ITEM:
                adminFacade.processItemRequest(request, accepted);
                return true;
            case REPORT:
                adminFacade.processUserReport(request, accepted);
                return true;
            case UNFREEZE:
                adminFacade.processUnfreezeRequest(request, accepted);
                return true;
            default:
                return adminFacade.processAdminCreationRequest(request, accepted, currAdminUsername);
        }
    }

    /**
     * Creates a new <code>Admin</code> with <code>username</code> and <code>password</code>.
     *
     * @param username possible username of the new <code>Admin</code>.
     * @param password possible password of the new <code>Admin</code>.
     * @return true iff the creation is successful.
     */
    public boolean createAdmin(String username, String password) {
        return adminFacade.createAdmin(username, password, currAdminUsername);
    }

    /**
     * Sends announcement <code>message</code> to all <code>User</code>s in the system.
     *
     * @param message the message to be sent.
     */
    public void setAnnouncement(String message) {
        adminFacade.adminToAllUser(currAdminUsername, message);
    }

    /**
     * Sets <code>User</code> minimum difference between number of borrow and lend.
     *
     * @param newDifference the new threshold to be set.
     * @return true iff set successfully.
     */
    public boolean setBorrowLendDifference(String newDifference) {
        if (isNotValidThreshold(newDifference)) return false;
        dataBundle.minLendBorrowDifference = Integer.parseInt(newDifference);
        adminFacade.setTradeThreshold(TradeThresholdType.MIN_LEND_BORROW_DIFF,
                Integer.parseInt(newDifference));
        return true;
    }

    /**
     * Sets <code>User</code> maximum weekly transaction limit.
     *
     * @param newLimit the new threshold to be set.
     * @return true iff set successfully.
     */
    public boolean setWeeklyTransactionLimit(String newLimit) {
        if (isNotValidThreshold(newLimit)) return false;
        dataBundle.maxWeeklyTransaction = Integer.parseInt(newLimit);
        adminFacade.setTradeThreshold(TradeThresholdType.MAX_WEEKLY_TRANSACTIONS,
                Integer.parseInt(newLimit));
        return true;
    }

    /**
     * Sets <code>User</code> maximum number of incomplete trade limit.
     *
     * @param newLimit the new threshold to be set.
     * @return true iff set successfully.
     */
    public boolean setIncompleteTradeLimit(String newLimit) {
        if (isNotValidThreshold(newLimit)) return false;
        dataBundle.maxIncompleteTrade = Integer.parseInt(newLimit);
        adminFacade.setTradeThreshold(TradeThresholdType.MAX_INCOMPLETE_TRADES,
                Integer.parseInt(newLimit));
        return true;
    }

    /**
     * Sets <code>User</code> number of edit time on a meeting.
     *
     * @param newLimit the new threshold to be set.
     * @return true iff set successfully.
     */
    public boolean setMeetingEditLimit(String newLimit) {
        if (isNotValidThreshold(newLimit)) return false;
        dataBundle.maxMeetingEdits = Integer.parseInt(newLimit);
        adminFacade.setMeetingThreshold(MeetingThresholdType.MAX_EDITS, Integer.parseInt(newLimit));
        return true;
    }

    /**
     * Sets <code>User</code> maximum time a meeting does not confirm occurred after arrangement time.
     *
     * @param newLimit the new threshold to be set.
     * @return true iff set successfully.
     */
    public boolean setMeetingLateLimit(String newLimit) {
        if (isNotValidThreshold(newLimit)) return false;
        dataBundle.maxMeetingLateTime = Integer.parseInt(newLimit);
        adminFacade.setMeetingThreshold(MeetingThresholdType.MAX_LATE_TIMES, Integer.parseInt(newLimit));
        return true;
    }

    /**
     * Gets <code>User</code> maximum weekly transaction limit.
     *
     * @return <code>MAX_WEEKLY_TRANSACTION</code> stored in <code>UserTradeThresholdManager</code> as String.
     */
    public String getWeeklyTransactionLimit() {
        return String.valueOf(adminFacade.getTradeThreshold(
                TradeThresholdType.MAX_WEEKLY_TRANSACTIONS));
    }

    /**
     * Gets <code>User</code> minimum difference between number of borrow and lend.
     *
     * @return <code>MIN_LEND_BORROW_DIFF</code> stored in <code>UserTradeThresholdManager</code> as String.
     */
    public String getBorrowLendDifference() {
        return String.valueOf(adminFacade.getTradeThreshold(
                TradeThresholdType.MIN_LEND_BORROW_DIFF));
    }

    /**
     * Gets <code>User</code> maximum number of incomplete trade limit.
     *
     * @return <code>MAX_INCOMPLETE_TRADES</code> stored in <code>UserTradeThresholdManager</code> as String.
     */
    public String getIncompleteTrade() {
        return String.valueOf(adminFacade.getTradeThreshold(
                TradeThresholdType.MAX_INCOMPLETE_TRADES));
    }

    /**
     * Gets <code>User</code> number of edit time on a meeting.
     *
     * @return <code>MAX_LATE_TIMES</code> stored in <code>MeetingManager</code> as String.
     */
    public String getMeetingEditLimit() {
        return String.valueOf(adminFacade.getMeetingThreshold(MeetingThresholdType.MAX_EDITS));
    }

    /**
     * Gets <code>User</code> maximum time a meeting does not confirm occurred after arrangement time.
     *
     * @return <code>MAX_LATE_TIMES</code> stored in <code>MeetingManager</code> as String.
     */
    public String getMeetingLateLimit() {
        return String.valueOf(adminFacade.getMeetingThreshold(MeetingThresholdType.MAX_LATE_TIMES));
    }
}
