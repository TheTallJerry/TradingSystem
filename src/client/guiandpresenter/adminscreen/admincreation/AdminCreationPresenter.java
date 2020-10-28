package client.guiandpresenter.adminscreen.admincreation;

import client.guiandpresenter.SystemPresenter;

public class AdminCreationPresenter extends SystemPresenter {
    /**
     * @return a string displaying "Create"
     */
    String textCreate() {
        return "Create";
    }

    /**
     * @param success the input value representing admin creation status, acting as a flag
     * @return a string indicating whether the creation of a new admin account is successful
     */
    String adminCreation(boolean success) {
        if (success) return "New admin created!";
        else return "Admin creation failed! Please make sure the username is" +
                " valid (>= 4 characters) and you are the initial admin.";
    }

    /**
     * @return a string displaying the title of a frame
     */
    @Override
    public String getTitle() {
        return "Create New Admin";
    }
}
