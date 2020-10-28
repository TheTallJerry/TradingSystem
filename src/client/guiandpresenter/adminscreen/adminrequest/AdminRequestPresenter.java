package client.guiandpresenter.adminscreen.adminrequest;

import client.guiandpresenter.SystemPresenter;

/**
 * Presents handling request information.
 */
public class AdminRequestPresenter extends SystemPresenter {
    /**
     * @return a string displaying the title of a frame
     */
    @Override
    public String getTitle() {
        return "Handle User Request";
    }

    /**
     * @return a string displaying "Item Requests:"
     */
    String itemRequestLbl() {
        return "Item Requests: ";
    }

    /**
     * @return a string displaying "Unfreeze Requests:"
     */
    String unfreezeRequestLbl() {
        return "Unfreeze Requests: ";
    }

    /**
     * @return a string displaying "User Reports:"
     */
    String userReportLbl() {
        return "User Reports: ";
    }

    /**
     * @return a string displaying "Admin Creation Requests:"
     */
    String adminCreationLbl() {
        return "Admin Creation Requests: ";
    }

    /**
     * @param success the input value that is true iff the request process is successful, acting as a flag
     * @return a string indicating whether request process is successful
     */
    String requestProcess(boolean success) {
        if (success) return "Request handled successfully.";
        else return "Failed to handle this request.";
    }

    /**
     * @return a string displaying request instructions
     */
    String requestInstruction() {
        return "The following are requests from users, double-click for further information" +
                " and action.";
    }

    /**
     * Gives a detailed information about a request.
     *
     * @param description long description of a request
     * @return detailed information about a request.
     */
    String detailedRequest(String description) {
        return description;
    }

    /**
     * Gives a title for the detailed request frame.
     *
     * @return a title for the detailed request frame.
     */
    String detailedRequestTitle() {
        return "Request Details";
    }

}
