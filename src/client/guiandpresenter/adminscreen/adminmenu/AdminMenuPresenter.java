package client.guiandpresenter.adminscreen.adminmenu;

import client.guiandpresenter.SystemPresenter;

/**
 * A presenter class responsible for admin needs.
 */
public class AdminMenuPresenter extends SystemPresenter {
    /**
     * @return a string displaying the title of a frame
     */
    @Override
    public String getTitle() {
        return "Admin Menu";
    }

    /**
     * @return a string displaying "Log Out"
     */
    String textLogOut() {
        return "Log Out";
    }

    /**
     * @return a string displaying "View Requests"
     */
    String textViewRequests() {
        return "View Requests";
    }

    /**
     * @return a string displaying "View and Modify Thresholds"
     */
    String textViewModifyThresholds() {
        return "View and Modify Thresholds";
    }

    /**
     * @return a string displaying "Check Violation"
     */
    String textCheckViolation() {
        return "Check Violation";
    }

    /**
     * @return a string displaying "Create new Admin"
     */
    String textCreateNewAdmin() {
        return "Create New Admin";
    }

    /**
     * @return a string displaying "Search Information"
     */
    String textSearchInfo() {
        return "Search Information";
    }

    /**
     * @return a string displaying "Undo Actions"
     */
    String textUndoAction() {
        return "Undo Actions";
    }

    /**
     * @return a string displaying "Send Announcement"
     */
    String textSendAnnouncement() {
        return "Send Announcement";
    }

    /**
     * @return a string displaying "Change password"
     */
    String textChangePw() {
        return "Change Password";
    }

    /**
     * @return a string asking the user to enter the message to be sent
     */
    String messageEnter() {
        return "Please enter your message: ";
    }

    /**
     * @return a string indicating that announcement has been updated
     */
    String announcementSent() {
        return "Announcement has been updated!";
    }
}
