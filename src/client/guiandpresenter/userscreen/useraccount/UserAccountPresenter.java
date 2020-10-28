package client.guiandpresenter.userscreen.useraccount;

import client.guiandpresenter.userscreen.InventoryPresenter;

import java.util.List;

/**
 * Presents most information related to screens that related to account.
 */
class UserAccountPresenter extends InventoryPresenter {
    /**
     * @param val the input value representing the status of on-vacation status, acting as a flag.
     * @return a string indicating whether the account status is on vacation or not.
     */
    String getOnVacationStatusMsg(boolean val) {
        if (val) {
            return "Your account's on-vacation status is now: On";
        } else {
            return "Your account's on-vacation status is now: Off";
        }
    }

    /**
     * @return a string talling this user to select actions.
     */
    String wishlistPrompt() {
        return "Please select below the action you'd like to proceed with";
    }

    /**
     * @param success true if the action is successful, false otherwise.
     * @return a string indicating whether the item is added to wishlist successfully or not.
     */
    String addToWishlistStatus(boolean success) {
        if (success) return "Item added to wishlist";
        return "Action failed. ";
    }

    /**
     * @return a string providing instruction for the user.
     */
    String modifyWishlistInstruction() {
        return "<html> <br>On the left is the inventory. On the right is your wishlist. " +
                "<br>Select the item you want to process and double click to select an option. </html>";
    }

    /**
     * @return an array of strings that each string is an option for user.
     */
    String[] addOptions() {
        return new String[]{"Add", "Cancel"};
    }

    /**
     * @return an array of strings that each string is an option for user.
     */
    String[] removeOptions() {
        return new String[]{"Remove", "Cancel"};
    }

    /**
     * @param success true if the action is successful, false otherwise.
     * @return a string indicating whether the item is removed from wishlist successfully or not.
     */
    String removeFromWishlistStatus(boolean success) {
        if (success) return "Item removed from wishlist";
        return "Action failed. ";
    }

    /**
     * @param flag true if the action is successful, false otherwise.
     * @return a string indicating whether the username had been added to the <code>BlockList</code> of this user.
     */
    String displayAddToBlocklistStatus(boolean flag) {
        if (flag)
            return "User added to block list. ";
        return "Action failed. Please enter a valid username not in block list ";
    }

    /**
     * @param flag true if the action is successful, false otherwise.
     * @return a string indicating whether the user has been removed from this user's <code>BlockList</code>.
     */
    String displayRemoveFromBlocklistStatus(boolean flag) {
        if (flag)
            return "User removed from block list";
        return "Action failed. Please enter a valid username in block list ";
    }

    /**
     * @param username a list of username in this user's <code>BlockList</code>.
     * @return a string displaying this user's <code>BlockList</code>.
     */
    String displayBlockList(List<String> username) {
        if (username.isEmpty()) return "Your current block list  is empty.";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<html>Users in your current block list :<br>");
        username.forEach(name -> stringBuilder.append("[").append(name).append("] "));
        return stringBuilder.toString();
    }

    /**
     * @return a string indicating the button used for adding to <code>blockList</code>
     */
    String addBlockListBtn() {
        return "Add to BlockList";
    }

    /**
     * @return a string indicating the button used for removing from <code>blockList</code>
     */
    String removeBlockListBtn() {
        return "Remove from BlockList";
    }

    /**
     * @param flag the input value that is true iff the password has been changed, acting as a flag
     * @return a string indicating if the password has been changed
     */
    String getPasswordChangedMsg(boolean flag) {
        if (!flag)
            return getInvalidInputMsg();
        return "Password changed. ";
    }

    /**
     * @param password a string of the current password
     * @return a string displaying the current password with description
     */
    String currentPasswordLblMsg(String password) {
        return "Current Password: " + password;
    }

    /**
     * @return a string indicating label asking for the <code>username</code> <code>user</code> wants to block.
     */
    String reportNameLbl() {
        return "Username of the user you want to report: ";
    }

    /**
     * @return a string indicating label asking for reason why <code>user</code> wants to block.
     */
    String reportReasonLbl() {
        return "Why you want to report this user: ";
    }

    /**
     * @param flag true if the action is successful, false otherwise.
     * @return a string indicating whether report user is successful.
     */
    String reportUserStatus(boolean flag) {
        if (flag) return "User successfully reported";
        return "Failed to report. Please re-enter a valid username and a valid reason. ";
    }

    /**
     * @param flag true if the action is successful, false otherwise.
     * @return a string indicate if the unfreeze request is sent.
     */
    String displayUnfreezeRequestStatus(boolean flag) {
        if (flag) return "Request has been sent to admins";
        return "<html> Couldn't send request. <br> This means that your account is not frozen";
    }

    /**
     * Gets the title of User Account screen.
     */
    @Override
    public String getTitle() {
        return "User Account";
    }
}
