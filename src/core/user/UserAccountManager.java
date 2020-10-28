package core.user;

import core.account.Account;
import core.account.AccountManager;
import core.account.reverter.PrivateMessageReverter;
import core.reverter.ActionReverter;
import core.user.reverter.AddToBlockListReverter;
import core.user.reverter.DeleteFromBlockListReverter;
import core.user.reverter.SetCityReverter;
import genericdatatype.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that manages <code>User</code>'s.
 */
public class UserAccountManager extends AccountManager<User> {
    private final String GUEST_USERNAME = "GUEST";

    /**
     * Creates a <code>UserInfoManager</code> with list of <code>users</code>.
     *
     * @param users A list of all <code>User</code>s in this system.
     */
    public UserAccountManager(List<User> users) {
        super(users);
    }

    /**
     * Creates a <code>User</code> with input <code>username</code> and <code>password</code>.
     *
     * @param username The <code>username</code> that the <code>User</code> wants to use.
     * @param password The <code>password</code> that the <code>User</code> wants to use.
     */
    public void createUser(String username, String password) {
        getAccounts().add(new User(username, password));
    }

    /* Called by initializeSystem to create guest.*/

    /**
     * Creates and adds a guest(demo) <code>User</code> to <code>accounts</code>.
     */
    public void createGuest() {
        getAccounts().add(new User(GUEST_USERNAME, "GUEST"));
    }

    /**
     * Checks whether or not a guest(demo) <code>User</code> exists in <code>accounts</code>.
     *
     * @return true if no guest exists, false if guest exists.
     */
    public boolean guestNotExist() {
        return getAccount(GUEST_USERNAME) == null;
    }

    /**
     * Gets the <code>city</code> that the current <code>User</code> is in.
     *
     * @param username the username of the <code>User</code> to be checked.
     * @return the <code>city</code> that the <code>User</code> lives in.
     */
    public String getCity(String username) {
        User user = getAccount(username);
        if (user != null) return user.getCity();
        return "";
    }

    /**
     * Changes <code>User</code>'s <code>city</code> information. An empty list is set if user want to delete his/her
     * city information.
     *
     * @param username the username of the <code>User</code> to be checked.
     * @param city     the new <code>city</code> information of this <code>User</code>.
     * @return <code>ActionReverter</code> for reverting this action.
     */
    public ActionReverter setCity(String username, String city) {
        User user = getAccount(username);
        if (user == null) return null;
        ActionReverter a = new SetCityReverter(user, user.getCity(), city);
        user.setCity(city);
        return a;
    }

    /**
     * Deletes the <code>username</code> of another user from current <code>User</code>'s <code>blockList</code>.
     *
     * @param username      the username of the <code>User</code> who wants to edit his <code>blockList</code>.
     * @param blockUsername the <code>username</code> of another user to be deleted from current <code>User</code>'s
     *                      <code>blockList</code>.
     * @return <code>ActionReverter</code> for reverting this action.
     */
    public ActionReverter deletedFromBlockList(String username, String blockUsername) {
        User user = getAccount(username);
        if (user != null && user.getBlockList().contains(blockUsername)) {
            user.deleteFromBlockList(blockUsername);
            return new DeleteFromBlockListReverter(user, blockUsername);
        }
        return null;
    }

    /**
     * Adds the <code>username</code> of another user into current <code>User</code>'s <code>blockList</code>.
     *
     * @param username      the username of <code>User</code> who wants to edit his <code>blockList</code>.
     * @param blockUsername the <code>username</code> of another user to be added into current <code>User</code>'s
     *                      <code>blockList</code>.
     * @return <code>ActionReverter</code> for reverting this action.
     */
    public ActionReverter addToBlockList(String username, String blockUsername) {
        User user = getAccount(username);
        if (user != null && !user.getBlockList().contains(blockUsername)) {
            user.addToBlockList(blockUsername);
            return new AddToBlockListReverter(user, blockUsername);
        }
        return null;
    }

    /**
     * Gets the <code>username</code>s of all users in the <code>blockList</code> of <code>User</code>.
     *
     * @param username the username of the <code>User</code> whose <code>blockList</code> to be returned.
     * @return a list of String, each represents a <code>username</code> of a user blocked by <code>user</code>.
     */
    public List<String> getBlockList(String username) {
        User user = getAccount(username);
        if (user != null) return user.getBlockList();
        return new ArrayList<>();
    }

    /**
     * Gets all details of the input <code>user</code>.
     *
     * @param username the <code>User</code> to be searched.
     * @return a string representative of the description of this <code>User</code> account, or empty list if
     * the <code>user</code> does not exist in the system.
     */
    public String[] getUserAccountSpecifics(String username) {
        User user = getAccount(username);
        if (user != null) return user.getInfoList();
        return new String[]{"User Doesn't Exist", "User Doesn't Exist", "User Doesn't Exist", "User Doesn't Exist"};
    }

    /**
     * Finds all <code>User</code> that <code>user</code> can trade with. That is, <code>User</code>s who are in the same
     * <code>city</code> with <code>user</code>, not in <code>user</code>'s <code>blockList</code>, not blocked
     * <code>user</code>, not <code>onVacation</code>, not Guest, etc.
     *
     * @param username the username of the <code>User</code> that want to find <code>User</code>s.
     * @return a list of <code>User</code>s satisfied all the conditions mentioned above.
     */
    public List<User> getFilteredUsers(String username) {
        User user = getAccount(username);
        if (user == null || user.onVacation() || user.isFrozen()) return new ArrayList<>();
        return getAccounts((otherUser) -> !(otherUser == user || otherUser.getUsername().equals("GUEST") ||
                user.getBlockList().contains(otherUser.getUsername()) ||
                otherUser.getBlockList().contains(user.getUsername()) ||
                otherUser.onVacation() || (!otherUser.getCity().equals(user.getCity()) && !user.getCity().isEmpty())
        ));
    }

    /**
     * Add points to given user's credit.
     *
     * @param username the username of the user whose credit is going to go up.
     */
    public void creditAddition(String username) {
        User user = getAccount(username);
        if (user != null) user.creditAddition();
    }

    /**
     * Subtract points from given user's credit.
     *
     * @param usernames a list of username of <code>User</code>s whose credits are going to go down.
     */
    public void creditSubtraction(List<String> usernames) {
        for (String username : usernames) {
            User user = getAccount(username);
            if (user != null) user.creditSubtraction();
        }
    }

    /**
     * Send the <code>message</code> from <code>sender</code> Account to <code>receiver</code> Account.
     * <code>senderType</code> will be shown in the message. If <code>sender</code> is blocked by the
     * <code>receiver</code>, <code>sender</code> will still see the message in <code>messageSent</code> but
     * <code>receiver</code> won't see it in <code>messageReceived</code>.
     *
     * @param sender   the <code>Account</code> that sends the <code>message</code>.
     * @param receiver the <code>Account</code> that receives the <code>message</code>.
     * @param message  the <code>message</code> that needs to be sent.
     * @return <code>ActionReverter</code> for reverting this action.
     */
    public ActionReverter oneToOneMessage(Account sender, Account receiver, String message) {
        if(sender == null || receiver == null)
            return null;
        String messageFormatted = "To [User " + receiver.getUsername() + "]:<br>" + message +
                "<br>Send from [User " + sender.getUsername() + "]<br>";
        sender.addToMessageSent(messageFormatted);
        if (!getAccount(receiver.getUsername()).getBlockList().contains(sender.getUsername())) {
            receiver.addToMessageReceived(messageFormatted);
        }
        return new PrivateMessageReverter(sender, receiver, messageFormatted);
    }
}
