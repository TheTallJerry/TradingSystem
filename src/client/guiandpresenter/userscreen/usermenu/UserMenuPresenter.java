package client.guiandpresenter.userscreen.usermenu;

import client.guiandpresenter.SystemPresenter;

/**
 * Class that represents a presenter for UserSystem containing string prompts
 */
public class UserMenuPresenter extends SystemPresenter {
    /**
     * Get the title of this presenter.
     *
     * @return the title of this presenter.
     */
    @Override
    public String getTitle() {
        return "User Menu";
    }

    /**
     * An array of buttons in a screen.
     *
     * @return an array contains "Trade", "Account", "Message".
     */
    String[] buttons() {
        return new String[]{"Trade", "Account", "Message", "Log out"};
    }
}
