package client.controllers;

import client.databundle.DataBundle;
import core.admin.AdminAccountManager;
import core.user.UserAccountManager;

/**
 * Class that set up the system when the program runs.
 */
public class StartingSystem {
    private final UserAccountManager userAccountManager;
    private final AdminAccountManager adminAccountManager;
    private final DataBundle dataBundle;

    /**
     * Constructs a StartingSystem instance with input essential data <code>dataBundle</code> of this system.
     *
     * @param dataBundle all the essential data of this system.
     */
    public StartingSystem(DataBundle dataBundle) {
        userAccountManager = new UserAccountManager(dataBundle.users);
        adminAccountManager = new AdminAccountManager(dataBundle.admins);
        this.dataBundle = dataBundle;
    }

    /**
     * Initializes the system when no initial <code>Admin</code> or guest(demo) <code>User</code> existed in the system,
     * by creating new  initial <code>Admin</code> or guest(demo) <code>User</code>.
     */
    public void initializeSystem() {
        if (dataBundle.admins.isEmpty()) {
            adminAccountManager.createInitialAdmin("admin", "password");
        }
        if (userAccountManager.guestNotExist()) {
            userAccountManager.createGuest();
        }
    }
}
