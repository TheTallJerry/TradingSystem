package client.controllers;

import client.databundle.DataBundle;
import core.admin.AdminAccountManager;
import core.user.UserAccountManager;

/**
 * A Controller class responsible for registering new users and requesting the creation of non-initial admins,
 * after the first time the program runs.
 */
public class RegistrationSystem {
    private final UserAccountManager userAccountManager;
    private final AdminAccountManager adminAccountManager;

    /**
     * Constructs a RegistrationSystem instance with input essential data <code>dataBundle</code> of this system.
     *
     * @param dataBundle all the essential data of this system.
     */
    public RegistrationSystem(DataBundle dataBundle) {
        userAccountManager = new UserAccountManager(dataBundle.users);
        adminAccountManager = new AdminAccountManager(dataBundle.admins, dataBundle.adminCreationRequests);
    }

    /**
     * Check whether the input string has a valid length.
     *
     * @param input a string input
     * @return true if the input is equal to or more than 4 digit length, false otherwise
     */
    private boolean isNotInputValidString(String input) {
        return input.length() < 4;
    }

    /**
     * Check whether the input email is valid.
     *
     * @param email a string input
     * @return true if the input is in correct email format, false otherwise
     */
    private boolean isValidEmail(String email) {
        return email.matches("^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
    }

    /**
     * Creates a new <code>User</code> with <code>username</code> and <code>password</code>. <code>User</code> can be
     * created if no existed <code>User</code> or <code>Admin</code> has the same <code>username</code>.
     *
     * @param username username wanted for <code>User</code>.
     * @param password password wanted for <code>User</code>.
     * @return whether or not the creation is successful.
     */
    public boolean createUser(String username, String password) {
        if ((userAccountManager.usernameExists(username) || adminAccountManager.usernameExists(username))
                || isNotInputValidString(password) || isNotInputValidString(username))
            return false;
        userAccountManager.createUser(username, password);
        return true;
    }

    /**
     * Requests to have a new <code>Admin</code> account with <code>username</code> and <code>password</code>.
     * <code>Admin</code> can be created if no existed <code>User</code> or <code>Admin</code> has the same
     * <code>username</code>, and the initial admin accept the request. If the request is accepted/denied, an email
     * will be sent to <code>userEmail</code>.
     *
     * @param username  username wanted by the client.
     * @param password  password wanted by the client.
     * @param userEmail email used by the client.
     * @return whether or not the creation is successful.
     */
    public boolean requestAdmin(String username, String password, String userEmail) {
        if (userAccountManager.usernameExists(username) || adminAccountManager.usernameExists(username)
                || isNotInputValidString(username) || isNotInputValidString(password) || !isValidEmail(userEmail))
            return false;
        return adminAccountManager.requestAdminCreation(username, password, userEmail);
    }
}
