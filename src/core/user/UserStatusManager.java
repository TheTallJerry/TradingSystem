package core.user;

import core.reverter.ActionReverter;
import core.user.reverter.SwitchOnVacationReverter;
import genericdatatype.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class that manages the status of users (such as freeze/unfreeze, and various requests)
 */
public class UserStatusManager {
    private final List<String> unfreezeRequests;
    private final List<String[]> reportedUsers;

    /**
     * Construct a <code>AdminUserManager</code>
     *
     * @param unfreezeRequests the list of requests from users to unfreeze themselves
     * @param reportedUsers    requests from a user to report another user
     */
    public UserStatusManager(List<String[]> reportedUsers, List<String> unfreezeRequests) {
        this.reportedUsers = reportedUsers;
        this.unfreezeRequests = unfreezeRequests;
    }

    /**
     * Freezes a <code>User</code>, given <code>username</code>.
     *
     * @param user the <code>User</code> need to be frozen.
     * @return true iff this user exists and froze successfully.
     */
    public boolean freeze(User user) {
        if (user == null || user.isFrozen()) return false;
        user.setFrozen(true);
        return true;
    }

    /**
     * Unfreezes a <code>User</code>, given <code>username</code>.
     *
     * @param user the <code>User</code> need to be unfreeze.
     * @return true iff this user exists and unfroze successfully.
     */
    public boolean unfreeze(User user) {
        if (user == null || !user.isFrozen()) return false;
        user.setFrozen(false);
        return true;
    }

    /**
     * Gets the users on-vacation status.
     *
     * @param user the <code>User</code> wants to see <code>onVacation</code> status.
     * @return true if the status is turned on.
     */
    public boolean onVacation(User user) {
        if (user != null) return user.onVacation();
        return false;
    }

    /**
     * Gets a Map that keys are <code>username</code> of all frozen <code>user</code>, values are all zero to show
     * that this method is finding frozen <code>users</code>
     *
     * @param ua a <code>UserAccountManager</code> to perform actions on users
     * @return a Map where each String is an <code>username</code> of a <code>user</code>, int are all 0 to indicate
     * this Map contains frozen <code>users</code>
     */
    public Map<String, Integer> getFrozenUsers(UserAccountManager ua) {
        Map<String, Integer> frozenUsers = new HashMap<>();
        for (User user : ua.getAccounts(User::isFrozen)) frozenUsers.put(user.getUsername(), 0);
        return frozenUsers;
    }

    /**
     * Freeze the <code>user</code> of given <code>report</code>, remove this <code>request</code> from
     * <code>reportedUsers</code>.
     *
     * @param report the string array representing the report, in the form of
     *               {user who filed the report, user who was reported, reason},
     *               where each user is represented with the (unique) username.
     * @param user   the <code>user</code> been reported
     */
    public void acceptUserReport(String[] report, User user) {
        reportedUsers.removeIf(p -> p[0].equals(report[0]) && p[1].equals(report[1]) && p[2].equals(report[2]));
        if (user != null) user.setFrozen(true);
    }

    /**
     * Do not freeze the <code>user</code> of given <code>report</code>, remove this <code>request</code> from
     * <code>reportedUsers</code>.
     *
     * @param report the string array representing the report, in the form of
     *               {user who filed the report, user who was reported, reason},
     *               where each user is represented with the (unique) username.
     */
    public void denyUserReport(String[] report) {
        reportedUsers.removeIf(p -> p[0].equals(report[0]) && p[1].equals(report[1]) && p[2].equals(report[2]));
    }

    /**
     * Gets a formatted report requests from <code>User</code>s.
     *
     * @return a <code>Map</code> from:
     * &emsp; Pairs of value1 the reporter, value2 a string contains information about the reporter, the
     * <code>User</code> being reported, and the reason. <br>
     * to:
     * &emsp;the actual reportRequest. <br>
     */
    public Map<Pair<String, String>, String[]> getFormattedReportRequests() {
        Map<Pair<String, String>, String[]> result = new HashMap<>();
        for (String[] arr : reportedUsers)
            result.put(new Pair<>(arr[0], arr[0] + " reported " + arr[1] + " because " + arr[2]), arr);
        return result;
    }

    /**
     * Unfreeze the <code>user</code> of given <code>username</code>, remove this <code>user</code> from
     * <code>unfreezeRequests</code>.
     *
     * @param user the <code>user</code> who request unfreeze.
     */
    public void acceptUserUnfreezeRequest(User user) {
        if (user != null) {
            user.setFrozen(false);
            unfreezeRequests.remove(user.getUsername());
        }
    }

    /**
     * Keep the <code>user</code> of given <code>username</code> frozen, remove this <code>user</code> from
     * <code>unfreezeRequests</code>
     *
     * @param username the <code>username</code> of the <code>user</code> who requests unfreeze
     */
    public void denyUserUnfreezeRequest(String username) {
        unfreezeRequests.remove(username);
    }

    /**
     * Gets <code>unfreezeRequests</code> formatted
     *
     * @return a Map from:
     * &emsp;Pairs of (short description, long description) of this requests <br>
     * to:
     * &emsp;the usernames of the User who requested <br>
     */
    public Map<Pair<String, String>, String> getFormattedUnfreezeRequests() {
        Map<Pair<String, String>, String> result = new HashMap<>();
        for (String r : unfreezeRequests) result.put(new Pair<>(r, r + " requests to unfreeze"), r);
        return result;
    }

    /**
     * Turn on vacation status of the user with username if the status is off, otherwise turn it off. No action can
     * be made if the user is not in the system.
     *
     * @param user The username of the user to be set.
     * @return ture if the user on vacation
     */
    public ActionReverter switchVacationStatus(User user) {
        if (!user.onVacation()) user.setOnVacation(true);
        else if (user.onVacation()) user.setOnVacation(false);
        return new SwitchOnVacationReverter(user, !user.onVacation());
    }

    /**
     * Adds unfreeze request to unfreezeRequests list
     *
     * @param user the user who requests to unfreeze his account
     * @return whether the action was done or not.
     */
    public boolean requestUnfreeze(User user) {
        if (user.isFrozen()) {
            unfreezeRequests.add(user.getUsername());
            return true;
        }
        return false;
    }

    /**
     * Report another user for inappropriate actions. Report will only be successful if both user exits in the system
     * and the user is not reporting him/herself.
     *
     * @param reporterUser the <code>user</code> who wants to report the other <code>user</code>.
     * @param reportedUser the <code>user</code> who is reported
     * @param reason       the reason why this <code>user</code> has been reported
     * @return true if the report is successful
     */
    public boolean reportUser(User reporterUser, User reportedUser, String reason) {
        //One user can only report another user with the same reason once.
        String[] check = new String[]{reporterUser.getUsername(), reportedUser.getUsername(), reason};
        if (reportedUsers.contains(check)) return false;
        if (reporterUser != reportedUser) {
            String[] newReport = new String[]{reporterUser.getUsername(), reportedUser.getUsername(), reason};
            reportedUsers.add(newReport);
            return true;
        }
        return false;
    }
}
