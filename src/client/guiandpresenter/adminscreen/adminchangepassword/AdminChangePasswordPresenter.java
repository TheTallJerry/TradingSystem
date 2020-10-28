package client.guiandpresenter.adminscreen.adminchangepassword;

import client.guiandpresenter.SystemPresenter;

public class AdminChangePasswordPresenter extends SystemPresenter {
    /**
     * No title for this screen
     *
     * @return null
     */
    @Override
    public String getTitle() {
        return null;
    }

    /**
     * Displaying whether password has been changed or not
     *
     * @param flag a boolean indicate valid or not given message
     * @return a String message indicate the change of password has succeed
     */
    String getPasswordChangedMsg(boolean flag) {
        if (!flag)
            return getInvalidInputMsg();
        return "Password changed. ";
    }

    /**
     * Displaying the current password
     *
     * @param password the current password
     * @return a message indicating what the current password is
     */
    String currentPasswordLblMsg(String password) {
        return "Current Password: " + password;
    }

    /**
     * * @return displaying the label that "New Password"
     */
    @Override
    public String getPwLblMsg() {
        return "New Password: ";
    }
}
