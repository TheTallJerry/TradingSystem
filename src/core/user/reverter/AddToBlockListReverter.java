package core.user.reverter;

import core.reverter.ActionReverter;
import core.user.User;

/**
 * Reverts the action of <code>User</code> add another <code>User</code> to <code>BlockList</code>.
 */
public class AddToBlockListReverter extends ActionReverter {
    private final String usernameToDelete;
    private final User user;

    /**
     * Constructs an AddToBlockListReverter with <code>user</code> and <code>usernameToDelete</code>.
     *
     * @param user             the <code>User</code> who did the action.
     * @param usernameToDelete the username to delete from <code>user</code>'s <code>BlockList</code>.
     */
    public AddToBlockListReverter(User user, String usernameToDelete) {
        super(user.getUsername());
        this.usernameToDelete = usernameToDelete;
        this.user = user;
    }

    /**
     * Undo the action of adding <code>usernameToDelete</code> to <code>user</code>'s <code>BlockList</code>. Cannot
     * undo if <code>usernameToDelete</code> is not in <code>user</code>'s <code>BlockList</code>.
     *
     * @return whether the undo is successful or not.
     */
    @Override
    public String execute() {
        if (!user.getBlockList().contains(usernameToDelete))
            return "This user had already been deleted from the blocklist.";
        user.deleteFromBlockList(usernameToDelete);
        return "Revert adding to blocklist successful!";
    }

    /**
     * Gets the type of action it will undo.
     *
     * @return the type of action it will undo.
     */
    @Override
    public String getActionType() {
        return "Add to blocklist";
    }

    /**
     * Gets a description of action it will undo.
     *
     * @return a description of action it will undo.
     */
    @Override
    public String getActionDescriptionReverted() {
        return this.getAssociatedUsername() + " blocked " + usernameToDelete + ".";
    }
}
