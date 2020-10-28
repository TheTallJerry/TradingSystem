package client.guiandpresenter;

/**
 * An abstract class that represents a system presenter
 */
public abstract class SystemPresenter {

    /**
     * Get the message for the situation that the input is invalid.
     *
     * @return a string displaying "Input does not satisfy requirement"
     */
    public String getInvalidInputMsg() {
        return "Input does not satisfy requirement, please re-enter. ";
    }

    /**
     * Get the label of username.
     *
     * @return a string displaying "Username"
     */
    public String getUsernameLblMsg() {
        return "Username: ";
    }

    /**
     * Get the label of password.
     *
     * @return a string displaying "Password"
     */
    public String getPwLblMsg() {
        return "Password: ";
    }

    /**
     * Get the title of this presenter.
     *
     * @return the title of this presenter.
     */
    public abstract String getTitle();

    /**
     * Get the label of confirm.
     *
     * @return a string displaying "Confirm"
     */
    public String getConfirmBtnMsg() {
        return "Confirm";
    }
}
