package client.guiandpresenter.userscreen.useraccount.useraccountmenu;

import client.guiandpresenter.SystemPresenter;

public class UserAccountMenuPresenter extends SystemPresenter {

    /**
     * @return a string displaying "Item creation request"
     */
    String requestItemCreationBtnMsg() {
        return "Item Creation Request";
    }

    /**
     * @return a string displaying "Modify wishlist"
     */
    String modifyToWishlistBtnMsg() {
        return "Modify Wishlist";
    }

    /**
     * @return a string displaying "Change password"
     */
    String changePwButtonMsg() {
        return "Change Password";
    }

    /**
     * @return a string displaying "Get account info"
     */
    String getAcctInfoBtn() {
        return "Get Account Info";
    }

    /**
     * @return a string displaying "Change on-vacation status"
     */
    String changeOnVacationStatusBtn() {
        return "Change On-Vacation Status";
    }

    /**
     * @return a string displaying "Add/remove users from BlockList"
     */
    String modifyBlocklistBtn() {
        return "Add/Remove Users From Block List";
    }

    /**
     * @return a string displaying "Request account unfreeze"
     */
    String requestUnfreezeBtn() {
        return "Request Account Unfreeze";
    }

    /**
     * @return a string displaying "Set your city"
     */
    String setCityBtn() {
        return "Set Your City";
    }

    /**
     * @return a string displaying "Report users. "
     */
    String reportBtn() {
        return "Report Users";
    }

    @Override
    public String getTitle() {
        return "User Account Menu";
    }
}
