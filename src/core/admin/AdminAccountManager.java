package core.admin;

import core.account.AccountManager;
import genericdatatype.Pair;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Class that manages <code>Admin</code>'s.
 */
public class AdminAccountManager extends AccountManager<Admin> {
    private List<Pair<Admin, String>> adminCreationRequest;

    /**
     * Construct an <code>AdminInfoManager</code>
     *
     * @param admins               the list of all <code>admin</code>s
     * @param adminCreationRequest adminCreationRequest the request of creating new <code>Admin</code>
     */
    public AdminAccountManager(List<Admin> admins, List<Pair<Admin, String>> adminCreationRequest) {
        super(admins);
        this.adminCreationRequest = adminCreationRequest;
    }

    /**
     * Construct an <code>AdminInfoManager</code>
     *
     * @param admins the list of all <code>admin</code>s
     */
    public AdminAccountManager(List<Admin> admins) {
        super(admins);
    }

    /**
     * Requests to create an <code>Admin</code> with input <code>username</code> and <code>password</code>, and an
     * <code>Email</code> address is also needed for this <code>Admin</code> to receive any notification about the result
     * of this <code>Admin</code> account creation
     *
     * @param username The <code>username</code> that the <code>Admin</code> wants to use
     * @param password The <code>password</code> that the <code>Admin</code> wants to use
     * @param email    The <code>email</code> that the <code>Admin</code> wants to use to receive any notification of
     *                 the result
     * @return a boolean, true if and only if the request is successfully created by <code>admin</code>
     */
    public boolean requestAdminCreation(String username, String password, String email) {
        for (Pair<Admin, String> pair : adminCreationRequest) {
            if (pair.value1.getUsername().equals(username) || pair.value2.equals(email))
                return false;
        }
        adminCreationRequest.add(new Pair<>(new Admin(username, password, false), email));
        return true;
    }

    /**
     * Creates an <code>Admin</code>
     *
     * @param username the username of the admin
     * @param password the password of the admin
     * @return true if successful
     */
    public boolean createAdmin(String username, String password) {
        if (usernameExists(username))
            return false;
        getAccounts().add(new Admin(username, password, false));
        return true;
    }

    /**
     * Create the initial <code>admin</code>
     *
     * @param username the username of the new admin
     * @param password the password of the new admin
     */
    public void createInitialAdmin(String username, String password) {
        Admin admin = new Admin(username, password, true);
        getAccounts().add(admin);
    }

    /**
     * Return a string representation of all the admin creation requests
     *
     * @return a string representation of all the admin creation requests
     */
    public Map<Pair<String, String>, Pair<Admin, String>> getFormattedAdminCreationRequests() {
        Map<Pair<String, String>, Pair<Admin, String>> result = new HashMap<>();
        for (Pair<Admin, String> r : adminCreationRequest) {
            result.put(new Pair<>(r.value1.getUsername(),
                    r.value1.getUsername() + " requests to be an admin"), r);
        }
        return result;
    }

    /**
     * Accept the request of creation of new <code>admin</code>, which means added new <code>admin</code> if and only
     * if this <code>admin</code> does not exists. Then remove this request. Return true if and only if this request
     * is accepted
     *
     * @param currAdminUsername the list of current <code>admin</code>'s <code>username</code>
     * @param request           a pair which value1 is new <code>admin</code>
     * @return true if and only if the request is accepted
     */
    public boolean acceptAdminCreationRequest(String currAdminUsername, Pair<Admin, String> request) {
        if (Objects.requireNonNull(getAccount(currAdminUsername)).notInitialAdmin())
            return false;
        adminCreationRequest.remove(request);
        getAccounts().add(request.value1);
        EmailSender emailSender = new EmailSender(request.value2, MessageFormat.format("Your admin " +
                        "creation request of username {0} and password {1} has been accepted! ",
                request.value1.getUsername(), request.value1.getPassword()));
        emailSender.sendEmail();
        return true;
    }

    /**
     * Deny the request of creation of new <code>admin</code>, which means deny new <code>admin</code> addition
     * if and only if this <code>admin</code> does not exists. Then remove this request. Return true
     * if and only if this request is denied
     *
     * @param currAdminUsername the list of current <code>admin</code>'s <code>username</code>
     * @param request           a pair which value1 is new <code>admin</code>
     * @return true if and only if the request is denied
     */
    public boolean denyAdminCreationRequest(String currAdminUsername, Pair<Admin, String> request) {
        if (Objects.requireNonNull(getAccount(currAdminUsername)).notInitialAdmin())
            return false;

        adminCreationRequest.remove(request);
        EmailSender emailSender = new EmailSender(request.value2, MessageFormat.format("Your admin " +
                        "creation request of username {0} and password {1} has been denied. ",
                request.value1.getUsername(), request.value1.getPassword()));
        emailSender.sendEmail();
        return true;
    }
}
