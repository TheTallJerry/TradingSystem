package core.account;

import java.util.List;

/**
 * Class that logs <code>Account</code>s in.
 */
public class LoginManager<E extends Account> {
    private final List<E> accounts;

    /**
     * Creates a <code>LoginManager</code> with a list of <code>Account</code> <code>accounts</code>.
     *
     * @param accounts A list of one type of <code>Account</code>s using this system
     */
    public LoginManager(List<E> accounts) {
        this.accounts = accounts;
    }

    /**
     * Checks if the given <code>username</code> and <code>password</code> fits any existing <code>Account</code> in the
     * system.
     *
     * @param username the <code>username</code> of a possible <code>Account</code>.
     * @param password the <code>password</code> of a possible <code>Account</code>.
     * @return true iff the input <code>username</code> and <code>password</code> fits any <code>Account</code>
     * in <code>accounts</code>.
     */
    public boolean verifyAccount(String username, String password) {
        for (Account a : accounts) {
            if (a.getUsername().equals(username) && a.getPassword().equals(password)) return true;
        }
        return false;
    }
}
