package core.reverter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages all the <code>ActionReverter</code>s that can be used by <code>Admin</code>s.
 */
public class ReverterManager {
    /**
     * A list of <code>ActionReverters</code>. <code>ActionReverters</code>  are used to undo <code>User</code> actions.
     */
    private final List<ActionReverter> actionReverters;

    /**
     * Construct an AdminReverterManager with <code>actionReverters</code>.
     *
     * @param actionReverters A list of <code>ActionReverters</code> that are used to undo <code>User</code> actions.
     */
    public ReverterManager(List<ActionReverter> actionReverters) {
        this.actionReverters = actionReverters;
    }

    /**
     * Gets all the <code>ActionReverter</code>s sorted by username of <code>User</code> who did the action.
     *
     * @return a <code>Map</code> of username as keys and a list of <code>ActionReverter</code> associate with the
     * username as values.
     */
    public Map<String, List<ActionReverter>> getRevertersByUsername() {
        Map<String, List<ActionReverter>> usernameReverterMap = new HashMap<>();
        for (ActionReverter a : actionReverters) {
            String username = a.getAssociatedUsername();
            if (!usernameReverterMap.containsKey(username)) {
                usernameReverterMap.put(username, new ArrayList<>());
            }
            usernameReverterMap.get(username).add(a);
        }
        return usernameReverterMap;
    }

    /**
     * Gets all the <code>ActionReverter</code>s sorted by their type.
     *
     * @return a <code>Map</code> of type as keys and a list of <code>ActionReverter</code> with that type as values.
     */
    public Map<String, List<ActionReverter>> getRevertersByType() {
        Map<String, List<ActionReverter>> usernameReverterMap = new HashMap<>();
        for (ActionReverter a : actionReverters) {
            String type = a.getActionType();
            if (!usernameReverterMap.containsKey(type)) {
                usernameReverterMap.put(type, new ArrayList<>());
            }
            usernameReverterMap.get(type).add(a);
        }
        return usernameReverterMap;
    }

    /**
     * Add the given <code>reverter</code> to the collection of all <code>reverters</code>,
     * named <code>actionReverters</code>
     *
     * @param reverter the <code>reverter</code> that will be added to <code>actionReverters</code>
     */
    public void addReverter(ActionReverter reverter) {
        actionReverters.add(reverter);
    }

    /**
     * Remover the given <code>reverter</code> from the collection of all <code>reverters</code>,
     * named <code>actionReverters</code>
     *
     * @param reverter the <code>reverter</code> that will be removed from <code>actionReverters</code>
     */
    public void removeRevert(ActionReverter reverter) {
        actionReverters.remove(reverter);
    }
}
