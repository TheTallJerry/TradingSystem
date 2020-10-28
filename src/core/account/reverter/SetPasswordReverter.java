package core.account.reverter;

import core.account.Account;
import core.reverter.ActionReverter;

/**
 * A reverter responsible for reverting set password by an <code>Account</code>.
 */
public class SetPasswordReverter extends ActionReverter {
    private final Account user;
    private final String oldPassword, newPassword;

    /**
     * Construct a Action with a list of <code>meetings</code> and <code>reportedUsers</code>
     *
     * @param user        the user who changed password
     * @param oldPassword the old password changed
     * @param newPassword the new password changed to.
     */
    public SetPasswordReverter(Account user, String oldPassword, String newPassword) {
        super(user.getUsername());
        this.user = user;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    /**
     * Reverts the action of an <code>Account</code> sending message to another <code>Account</code>. Action undo will
     * only be successful if the string message sent is in the sender's list of <code>massageSent</code>.
     *
     * @return a string message that will be sent to the sender's list of <code>massageSent</code>
     */
    @Override
    public String execute() {
        if (!user.getPassword().equals(newPassword))
            return "Action undo failed! User Changed Password Again.";
        user.setPassword(oldPassword);
        return "Password has been set back to " + oldPassword + " from " + newPassword;
    }

    /**
     * Gets the type of action it will undo.
     *
     * @return a Sting description of action it will undo.
     */
    @Override
    public String getActionType() {
        return "Set Password";
    }

    /**
     * Gets a description of action it will undo.
     *
     * @return a Sting description of action it will undo.
     */
    @Override
    public String getActionDescriptionReverted() {
        return "Password set to " + oldPassword + " from " + newPassword;
    }

}
