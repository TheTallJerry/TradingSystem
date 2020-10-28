package client.controllers;

import client.databundle.DataBundle;
import core.account.LoginManager;
import core.admin.Admin;
import core.user.User;

/**
 * A Controller class responsible for logging in guests/users/admins
 */
public class LoginSystem {
    private final LoginManager<User> userLoginManager;
    private final LoginManager<Admin> adminLoginManager;

    /**
     * Constructs a LoginSystem instance with input essential data of this system
     * If this program has never been run before, admins will be empty
     *
     * @param dataBundle all the essential data of this system
     */
    public LoginSystem(DataBundle dataBundle) {
        userLoginManager = new LoginManager<>(dataBundle.users);
        adminLoginManager = new LoginManager<>(dataBundle.admins);
    }

    /**
     * Return the Type of login (user, admin, failed) according to given username and password
     *
     * @param username the username of this user
     * @param password the password of this user
     * @return the type of user or a failed state
     */
    public LoginType login(String username, String password) {
        if (userLoginManager.verifyAccount(username, password))
            return LoginType.USER;
        else if (adminLoginManager.verifyAccount(username, password))
            return LoginType.ADMIN;
        return LoginType.FAILED;
    }

    /**
     * Gets guest(demo) <code>User</code>'s username.
     *
     * @return username of guest(demo) <code>User</code>.
     */
    public String getGuestUsername() {
        return "GUEST";
    }
}
