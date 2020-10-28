package core.account;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an account that has username and password.
 */
public abstract class Account implements Serializable {
    private final String username;
    private String password;
    /* Message received by the Account */
    private final List<String> messageReceived;
    /* Message sent by the Account */
    private final List<String> messageSent;

    /**
     * Creates an account with <code>username</code> and <code>password</code>.
     *
     * @param username username of the account.
     * @param password password of the account.
     */
    protected Account(String username, String password) {
        this.username = username;
        this.password = password;
        this.messageReceived = new ArrayList<>();
        this.messageSent = new ArrayList<>();
    }

    /**
     * Gets the <code>username</code> of the account.
     *
     * @return the <code>username</code> of the account.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the <code>password</code> of the account.
     *
     * @return the <code>password</code> of the account.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the <code>password</code> of the account.
     *
     * @param password the new <code>password</code> the account.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Add the given message to <code>messageReceived</code> of the account.
     *
     * @param msg the message that will be added.
     */
    public void addToMessageReceived(String msg) {
        messageReceived.add(msg);
    }

    /**
     * Remove the given message to <code>messageReceived</code> of the account.
     *
     * @param msg the message that will be removed.
     * @return true iff <code>msg</code> is present in the list(therefore removed).
     */
    public boolean deleteFromMessageReceived(String msg) {
        return messageReceived.remove(msg);
    }

    /**
     * Gets the <code>messageReceived</code>, all message received by this account.
     *
     * @return the <code>messageReceived</code> of the account.
     */
    List<String> getMessageReceived() {
        return messageReceived;
    }

    /**
     * Add the given message to <code>messageSent</code> of the account.
     *
     * @param msg the message that will be added.
     */
    public void addToMessageSent(String msg) {
        messageSent.add(msg);
    }

    /**
     * Delete the given message in <code>messageSent</code> of the account.
     *
     * @param msg the message that will be removed.
     * @return true iff <code>msg</code> is present in the list(therefore removed).
     */
    public boolean deleteFromMessageSent(String msg) {
        return messageSent.remove(msg);
    }

    /**
     * Gets the <code>messageSent</code>, all message sent by this account.
     *
     * @return <code>messageSent</code> of the account.
     */
    public List<String> getMessageSent() {
        return messageSent;
    }
}
