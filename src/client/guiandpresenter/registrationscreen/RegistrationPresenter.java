package client.guiandpresenter.registrationscreen;

import client.guiandpresenter.SystemPresenter;

/**
 * Class that represents a presenter for RegistrationSystem containing string prompts
 */
public class RegistrationPresenter extends SystemPresenter {
    private final String adminChoice = "Admin";

    /**
     * Prompt failure <code>User</code> creation message.
     *
     * @return message denoting that <code>User</code> creation has failed.
     */
    String failurePrompt() {
        return "<html> User creation failed. <br> Please enter another valid username and password. ";
    }

    /**
     * Prompt success <code>User</code> creation message.
     *
     * @return message denoting that <code>User</code> creation has succeed.
     */
    String userCreationSuccessPrompt() {
        return "User account created. ";
    }

    /**
     * Prompt message after request <code>Admin</code> creation.
     *
     * @param username username of the <code>Admin</code> account requested.
     * @param password password of the <code>Admin</code> account requested.
     * @return the message contains information about the request.
     */
    String adminRequestPrompt(String username, String password) {
        return String.format("<html> The request of admin creation with username %s", username) + ", and " +
                String.format("password %s<br>, has been sent to the initial admin. <br>" +
                        "You will receive an email to the account you left ", password) +
                "with further instructions/updates ";
    }

    /**
     * Inform client to enter email if needed.
     *
     * @return message of informing email.
     */
    String getEmailLblMsg() {
        return "Enter your email iff registering as an admin";
    }

    /**
     * Instruction of the registration page.
     *
     * @return the string of instruction.
     */
    String getTitleLblMsg() {
        return "<html> Welcome to the Registration page. Please enter the prompted info. <br> A valid" +
                " username/password has at least 4 characters. ";
    }

    /**
     * Shows Choice of the User.
     *
     * @return a list contains choices.
     */
    String[] getRegChoices() {
        String permUserChoice = "User";
        return new String[]{adminChoice, permUserChoice};
    }

    /**
     * Must select a type string.
     *
     * @return String indicates client to select a type.
     */
    String regChoiceMustBeMadePrompt() {
        return "You must select one of the types of users you want to register as. ";
    }

    /**
     * Get choice for selecting Admin.
     *
     * @return String showing that client choose Admin.
     */
    String getRegAdminChoice() {
        return adminChoice;
    }

    /**
     * Get the title of this presenter.
     *
     * @return the title of this presenter.
     */
    @Override
    public String getTitle() {
        return "Registration Directory Screen";
    }
}
