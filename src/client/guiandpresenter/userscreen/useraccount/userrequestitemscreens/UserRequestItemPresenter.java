package client.guiandpresenter.userscreen.useraccount.userrequestitemscreens;

import client.guiandpresenter.SystemPresenter;

/**
 * Presenter for the User Request Item menu
 */
class UserRequestItemPresenter extends SystemPresenter {

    @Override
    public String getTitle() {
        return "Request Item Creation";
    }

    /**
     * Return the message for different situation of flag.
     *
     * @param flag A boolean shows current situation of request
     * @return return string of Item request is sent if flag is true, return failure information otherwise
     */
    String getItemRequestedMsg(boolean flag) {
        if (!flag)
            return "<html>" + getInvalidInputMsg() + "<br>Item name, type, description should be none empty and contain" +
                    " only letter, number, comma, space or period.";
        return "Item request sent. ";
    }

    String itemNameLblMsg() {
        return "Item name: ";
    }

    String itemTypeLblMsg() {
        return "Item type: ";
    }

    String itemDescriptionLblMsg() {
        return "Item Description: ";
    }
}
