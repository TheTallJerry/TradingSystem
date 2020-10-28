package client.guiandpresenter.loginscreen;

import client.guiandpresenter.SystemPresenter;

/**
 * Class that represents a presenter for LoginSystem containing string prompts.
 */
public class LoginPresenter extends SystemPresenter {
    /**
     * Get beginning welcoming message.
     *
     * @return the welcoming message.
     */
    String getTitleLblMsg() {
        return "Welcome to the login page. Please enter the prompted info. ";
    }

    /**
     * Message showing that the input username and password does not match any account, and guides to enter again.
     *
     * @return A message showing that the input username and password does not match any account, and guides to enter again
     */
    String loginFailed() {
        return "<html>Username and password do not match any existing user or admin, <br/>please re-enter.</html>";
    }

    /**
     * Message showing the user has logged in successfully.
     *
     * @return A message showing the user has logged in successfully.
     */
    String userLogin() {
        return "User Login successful.";
    }

    /**
     * Prompts message when someone enters system as a guest(demo) <code>User</code>.
     *
     * @return the message indicate entering action.
     */
    String enterAsGuestBtnMsg() {
        return "Enter as Guest";
    }

    /**
     * Message showing the admin has logged in successfully.
     *
     * @return A message showing the admin has logged in successfully.
     */
    String adminLogin() {
        return "Admin Login successful.";
    }

    /**
     * Get the title of this presenter.
     *
     * @return the title of this presenter.
     */
    @Override
    public String getTitle() {
        return "Trade System";
    }
}
