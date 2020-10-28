package core.user.reverter;

import core.reverter.ActionReverter;
import core.user.User;

/**
 * Reverts the action of a <code>User</code> delete someone from <code>BlockList</code>.
 */
public class DeleteFromBlockListReverter extends ActionReverter {
    private final String usernameToAdd;
    private final User user;

    /**
     * Constructs a DeleteFromBlockListReverter with <code>user</code> and <code>usernameToAdd</code>.
     *
     * @param user          the <code>User</code> who did the action.
     * @param usernameToAdd the username to add to <code>user</code>'s <code>BlockList</code>.
     */
    public DeleteFromBlockListReverter(User user, String usernameToAdd) {
        super(user.getUsername());
        this.usernameToAdd = usernameToAdd;
        this.user = user;
    }

    /**
     * Undo the action of delete <code>usernameToAdd</code> from <code>user</code>'s <code>BlockList</code>. Cannot
     * undo if <code>usernameToAdd</code> is in <code>user</code>'s <code>BlockList</code>.
     *
     * @return whether the undo is successful or not.
     */
    @Override
    public String execute() {
        if (user.getBlockList().contains(usernameToAdd))
            return "This user had already been added back to the blocklist.";
        user.addToBlockList(usernameToAdd);
        return "Revert deleting from blocklist successful!";
    }

    /**
     * Gets the type of action it will undo.
     *
     * @return the type of action it will undo.
     */
    @Override
    public String getActionType() {
        return "Delete from blocklist";
    }

    /**
     * Gets a description of action it will undo.
     *
     * @return a description of action it will undo.
     */
    @Override
    public String getActionDescriptionReverted() {
        return this.getAssociatedUsername() + " removed " + usernameToAdd + " from BlockList.";
    }
}
