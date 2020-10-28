package client.guiandpresenter.adminscreen.adminsearch;

import client.guiandpresenter.SystemPresenter;

/**
 * Presents Searching User information.
 */
public class AdminSearchPresenters extends SystemPresenter {
    /**
     * @return a string displaying "Search Users Info"
     */
    @Override
    public String getTitle() {
        return "Search Users Info";
    }

    /**
     * @return a string displaying "Enter here"
     */
    String enterDirection() {
        return "Enter User Username:";
    }

    /**
     * @return a string displaying "Search"
     */
    String textSearchInfo() {
        return "Search";
    }
}
