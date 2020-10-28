package core.user.reverter;

import core.reverter.ActionReverter;
import core.user.User;

/**
 * Reverts the action of setting city information by a <code>User</code>.
 */
public class SetCityReverter extends ActionReverter {
    private final String prevCity, currCity;
    private final User user;

    /**
     * Construct a SetCityReverter with <code>user</code>, <code>prevCity</code> <code>currCity</code>.
     *
     * @param user     the <code>User</code> who did the action.
     * @param prevCity the previous city that <code>user</code> changed.
     * @param currCity the current city that <code>user</code> changed to.
     */
    public SetCityReverter(User user, String prevCity, String currCity) {
        super(user.getUsername());
        this.prevCity = prevCity;
        this.currCity = currCity;
        this.user = user;
    }

    /**
     * Undo the action of setting city by <code>user</code>. Cannot undo if <code>user</code> has reset the city again.
     *
     * @return whether the undo is successful or not.
     */
    @Override
    public String execute() {
        if (currCity.equals(user.getCity())) {
            user.setCity(prevCity);
            return "Action undo succeeded! ";
        } else {
            return "Action undo failed! The user has reset the city again. ";
        }
    }

    /**
     * Gets the type of action it will undo.
     *
     * @return the type of action it will undo.
     */
    @Override
    public String getActionType() {
        return "Set city";
    }

    /**
     * Gets a description of action it will undo.
     *
     * @return a description of action it will undo.
     */
    @Override
    public String getActionDescriptionReverted() {
        String oldCity, newCity;
        if (prevCity.equals("")) oldCity = "nothing";
        else oldCity = prevCity;
        if (currCity.equals("")) newCity = "nothing";
        else newCity = currCity;
        return this.getAssociatedUsername() + " set city from " + oldCity + " to " + newCity;
    }
}
