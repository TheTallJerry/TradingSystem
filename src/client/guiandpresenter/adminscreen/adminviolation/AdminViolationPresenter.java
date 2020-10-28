package client.guiandpresenter.adminscreen.adminviolation;

import client.guiandpresenter.SystemPresenter;

public class AdminViolationPresenter extends SystemPresenter {
    /**
     * @return a string displaying the title of a frame
     */
    @Override
    public String getTitle() {
        return "Unfreeze User Violate Rules";
    }

    /**
     * @return a string displaying "Freeze"
     */
    String freezeBtn() {
        return "Freeze";
    }

    /**
     * @return a string displaying "Unfreeze"
     */
    String unfreezeBtn() {
        return "Unfreeze";
    }


    /**
     * @return an arraylist of string containing violation types
     */
    String[] violationType() {
        return new String[]{"Incomplete Trade", "Lend-Borrow Difference", "Weekly Transaction", "Current Frozen User"};
    }

    /**
     * @return a string displaying "Choose threshold to see violation:"
     */
    String chooseThreshold() {
        return "Choose threshold to see violation:";
    }


    /**
     * @param bool the input value that is true iff the freeze is successful, acting as a flag
     * @return a string displaying whether the action freeze is successful or not.
     */
    String frozeResult(boolean bool) {
        if (bool) return "Succeeded!";
        return "<html>Action failed. Make sure you have chose a user and that user is not frozen.<br>" +
                "Check [Current Frozen User] for more information.";
    }

    /**
     * @param bool the input value that is true iff the unfreeze is successful, acting as a flag
     * @return a string displaying whether the action freeze is successful or not.
     */
    String unfreezeResult(boolean bool) {
        if (bool) return "Succeeded!";
        return "<html>Action failed. Make sure you have chose a user and that user is frozen.<br>" +
                "Check [Current Frozen User] for more information.";
    }

    /**
     * Provides information about a violation made by a <code>User</code>.
     *
     * @param name  username of the <code>User</code>.
     * @param limit How much limit the <code>User</code> exceed. If checking frozen user, this limit is 0.
     * @param type  type of violation
     * @return a String contain information about a violation made by a <code>User</code>.
     */
    String violationInfo(String name, int limit, Object type) {
        if (type.toString().equals("Current Frozen User")) return name + " is frozen";
        return "[" + name + "] exceed limit [" + type.toString() + "] by " + limit;
    }

    /**
     * Retrieves the username from the violation information message.
     *
     * @param message the violation information message.
     * @return username in the message.
     */
    String retrieveUsername(String message) {
        if (message.contains("]")) return message.split("]")[0].substring(1);
        return message.split(" ")[0];
    }
}
