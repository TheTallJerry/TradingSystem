package core.reverter;

import java.io.Serializable;

/**
 * Reverts actions that can reasonably be undone. Since ActionReverter has to be serializable, it should
 * only reference entities which are also serializable, but it cannot reference Managers.
 * This means, upon creation, the ActionReverter should have all the data required to undo.
 */
public abstract class ActionReverter implements Serializable {
    private final String username;

    /**
     * Construct an ActionReverter.
     *
     * @param username the <code>username</code> of a <code>User</code>.
     */
    protected ActionReverter(String username) {
        this.username = username;
    }

    /**
     * Revert the action.
     *
     * @return message resulting from execution.
     */
    public abstract String execute();

    /**
     * Gets <code>username</code> that is associated to what is undone
     *
     * @return the username stored.
     */
    public String getAssociatedUsername() {
        return username;
    }

    /**
     * Gets the type of action this ActionReverter reverts.
     *
     * @return a phrase describing this ActionReverter's type. An example would be: "Item Addition Request", ...
     */
    public abstract String getActionType();

    /**
     * Gets a description of the action this this ActionReverter reverts.
     *
     * @return a phrase describing the action this ActionReverter ; some examples include: "Revert Item Addition
     * request from User", ...
     */
    public abstract String getActionDescriptionReverted();
}
