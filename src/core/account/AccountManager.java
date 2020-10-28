package core.account;

import core.account.reverter.PrivateMessageReverter;
import core.account.reverter.SetPasswordReverter;
import core.reverter.ActionReverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that manages all kinds of accounts in this system.
 */
public abstract class AccountManager<E extends Account> {
    private final List<E> accounts;

    /**
     * Constructs an <code>AccountManager</code> with a list of <code>accounts</code> and a list of
     * <code>actionReverters</code>.
     *
     * @param accounts a list of <code>Account</code> that contains one type of accounts.
     */
    protected AccountManager(List<E> accounts) {
        this.accounts = accounts;
    }

    /**
     * Gets the <code>Account</code> from all users with the specified <code>username</code>.
     *
     * @param username the <code>username</code> of the <code>account</code> to be searched
     * @return the <code>Account</code> with the specified <code>username</code>, or null if there is no such
     * <code>Account</code> found in <code>accounts</code>.
     */
    public E getAccount(String username) {
        for (E account : accounts) {
            if (account.getUsername().equals(username))
                return account;
        }
        return null;
    }

    /**
     * Get all accounts that satisfy some criterion
     *
     * @param criterion the <code>criterion</code> that will be used to filter <code>users</code>
     * @return a collection of all satisfied <code>users</code>
     */
    public List<E> getAccounts(Criterion<E> criterion) {
        List<E> result = new ArrayList<>();
        for (E account : accounts) {
            if (criterion.accepts(account))
                result.add(account);
        }
        return result;
    }

    /**
     * Gets the <code>password</code> of this <code>Account</code>.
     *
     * @param account the <code>Account</code> that wants to access <code>password</code>.
     * @return the <code>password</code> of this <code>Account</code>.
     */
    public String getPassword(E account) {
        return account.getPassword();
    }

    /**
     * Sets the <code>password</code> of this <code>Account</code>.
     *
     * @param account  the <code>Account</code> that wants to change <code>password</code>.
     * @param password the new <code>password</code> to be set for this <code>Account</code>.
     * @return <code>ActionReverter</code> of the setting password.
     */
    public ActionReverter setPassword(E account, String password) {
        ActionReverter a = new SetPasswordReverter(account, account.getPassword(), password);
        account.setPassword(password);
        return a;
    }

    /**
     * Gets a list of messages that this <code>Account</code> has received.
     *
     * @param account the <code>Account</code> that wants to access all messages received.
     * @return <code>MessageReceived</code>, a list of String messages received of this <code>Account</code>.
     */
    public List<String> getMessageReceived(E account) {
        return account.getMessageReceived();
    }

    /**
     * Gets a list of messages that this <code>Account</code> has sent.
     *
     * @param account the <code>Account</code> that wants to access all messages sent.
     * @return <code>MessageSent</code>, a list of String messages sent by this <code>Account</code>.
     */
    public List<String> getMessageSent(E account) {
        return account.getMessageSent();
    }

    /**
     * Checks if the <code>username</code> already exists in the system.
     *
     * @param username the <code>username</code> to be checked.
     * @return true iff the input <code>username</code> already exists in the system
     */
    public boolean usernameExists(String username) {
        for (E account : accounts) {
            if (account.getUsername().contentEquals(username)) return true;
        }
        return false;
    }

    /* Send the message from sender Account to receiver Account. */
    private void oneToOneMessage(Account sender, Account receiver, String message) {
        String messageFormatted = "To [" + receiver.getUsername() + "]:<br>" + message +
                "<br>Send from [" + sender.getClass().getSimpleName() + " :" + sender.getUsername() + "]<br>";
        sender.addToMessageSent(messageFormatted);
        receiver.addToMessageReceived(messageFormatted);
    }

    /**
     * Sends message to the each <code>Account</code> in <code>accounts</code> on behalf of <code>sender</code>.
     *
     * @param sender the username of the <code>Account</code> who is sending this message.
     * @param msg    the message <code>sender</code> wants to send.
     */
    public void oneToAllMessage(Account sender, String msg) {
        for (E e : accounts)
            if (e != sender)
                oneToOneMessage(sender, e, msg);
    }

    /**
     * A list of accounts managed by this <code>AccountManager</code>.
     *
     * @return <code>accounts</code> stored in this <code>AccountManager</code>.
     */
    protected List<E> getAccounts() {
        return accounts;
    }
}