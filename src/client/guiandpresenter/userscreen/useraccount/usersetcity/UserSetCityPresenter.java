package client.guiandpresenter.userscreen.useraccount.usersetcity;

import client.guiandpresenter.SystemPresenter;

public class UserSetCityPresenter extends SystemPresenter {
    @Override
    public String getTitle() {
        return "Set City";
    }

    /**
     * @return a string indicating the "Change City" button
     */
    String setNewCityButton() {
        return "Change City";
    }

    /**
     * @return a string indicating the "Delete City" button
     */
    String deleteCityButton() {
        return "Delete City";
    }

    /**
     * @param city the city of the current user.
     * @return a string displaying a user's current city
     */
    String getCityMsg(String city) {
        if (city.equals("")) {
            return "Your have not set a city yet.";
        } else {
            return "Your city is: " + city;
        }
    }

    /**
     * @param success true if the action succeed, false if the action failed.
     * @return a string indicating <code>User</code> has set city successfully.
     */
    String updateCityAction(boolean success) {
        if (success) return "Update city successfully";
        else return "<html>Invalid City: The first character must be upper case digit. " +
                "<br>The rest can only be letter, digits, space or period(.)";
    }

    /**
     * @return a string indicating <code>User</code> has set city successfully.
     */
    String WrongEnter() {
        return "Please do not enter empty string.";
    }
}
