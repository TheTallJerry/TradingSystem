package core.admin;

import core.account.Account;

import java.io.Serializable;

/**
 * Represents an administrator in this system.
 */
public class Admin extends Account implements Serializable {
    private final boolean isInitialAdmin;

    /**
     * Creates an administrator with given <code>username</code>, <code>password</code> and <code>isInitialAdmin</code>
     * status.
     *
     * @param username       the username of the administrator.
     * @param password       the password of the administrator.
     * @param isInitialAdmin whether the admin is the initial administrator (the first administrator in this program).
     */
    public Admin(String username, String password, boolean isInitialAdmin) {
        super(username, password);
        this.isInitialAdmin = isInitialAdmin;
    }

    /**
     * Gets the admin's <code>isInitialAdmin</code> status, which is whether the admin is the first admin in the system.
     *
     * @return <code>isInitialAdmin</code>.
     */
    public boolean notInitialAdmin() {
        return !isInitialAdmin;
    }

    /**
     * Gives a string representation of the admin.
     *
     * @return a string contains <code>username</code> of the admin.
     */
    public String toString() {
        return "Username: " + getUsername();
    }
}
