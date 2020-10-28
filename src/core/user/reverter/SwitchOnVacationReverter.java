package core.user.reverter;

import core.reverter.ActionReverter;
import core.user.User;

/**
 * Reverts the action of switching on/off on-vacation status by a <code>User</code>.
 */
public class SwitchOnVacationReverter extends ActionReverter {
    private final boolean status;
    private final User user;

    /**
     * Construct a SwitchOnVacationReverter with <code>user</code>, <code>status</code>.
     *
     * @param user   the <code>User</code> who did the action.
     * @param status the previous status that <code>user</code> changed.
     */
    public SwitchOnVacationReverter(User user, boolean status) {
        super(user.getUsername());
        this.status = status;
        this.user = user;
    }

    /**
     * Undo the action of switch on/off on-vacation status by <code>user</code>. Cannot undo if <code>user</code> has
     * switch the status again.
     *
     * @return whether the undo is successful or not.
     */
    @Override
    public String execute() {
        if (user.onVacation() == status) {
            return "Action undo failed! The user has turn on-vacation status to " + status + " again. ";
        }
        user.setOnVacation(status);
        return "Action undo succeeded! On-vacation status switch to " + status;
    }

    /**
     * Gets the type of action it will undo.
     *
     * @return the type of action it will undo.
     */
    @Override
    public String getActionType() {
        return "Switch On-Vacation Status";
    }

    /**
     * Gets a description of action it will undo.
     *
     * @return a description of action it will undo.
     */
    @Override
    public String getActionDescriptionReverted() {
        return this.getAssociatedUsername() + " switch on-vacation status to " + !status;
    }
}
