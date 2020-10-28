package client.guiandpresenter.userscreen.usertrade.usertrademenuscreen;

import client.guiandpresenter.SystemPresenter;

import java.util.List;

/**
 * Presenter for the User Trade Menu.
 */
class UserTradeMenuPresenter extends SystemPresenter {
    /**
     * @param items a list of items
     * @return the three recent trade item of a user
     */
    String getThreeRecentTradeItemMsg(List<String> items) {
        if (items.isEmpty())
            return "You haven't had a trade yet. ";
        StringBuilder sb = new StringBuilder("<html>There are some of your most recent traded items(at most 3):<br>");
        for (String str : items) {
            sb.append("<br>").append(str).append("<br>");
        }
        return sb.toString();
    }

    String cannotRequestTradeMsg() {
        return "<html> You cannot request trade " +
                "because <br> your account's on-vacation status is: on";
    }

    /**
     * @param users a list of users
     * @return the top three trade partner of a user
     */
    String getTopThreeTradePartnerMsg(List<String> users) {
        if (users.isEmpty())
            return "You haven't had a trade yet. ";
        StringBuilder sb = new StringBuilder("<html>There are some of your most recent traded partners(at most 3):");
        for (String str : users) {
            sb.append("<br>[").append(str).append("]<br>");
        }
        return sb.toString();
    }

    /**
     * @param thresholds an arraylist of threshold values
     * @return a string representation of threshold categories and their values
     */
    String getTradeThresholds(int[] thresholds) {
        return "<html> The trade thresholds are: " + "<br>" +
                "Max incomplete trade count: " + thresholds[0] +
                "<br>" + "Min Lend borrow difference: " + thresholds[1] +
                "<br>" + "Max weekly transaction count: " + thresholds[2];
    }

    /**
     * Get the title of this presenter.
     *
     * @return the title of this presenter.
     */
    @Override
    public String getTitle() {
        return "User Trade Menu";
    }

    /**
     * @return string indicating "View your trades" button
     */
    String viewTradeBtn() {
        return "View Your Trades";
    }

    /**
     * @return string indicating "Request a new trade" button
     */
    String requestTradeBtn() {
        return "Request New Trade";
    }

    /**
     * @return string indicating <code>threeRecentTradePartnersBtn</code> button
     */
    String threeRecentTradePartnersBtn() {
        return "<html> Three Most Recent <br> Trading Partners";
    }

    /**
     * @return string indicating <code>threeRecentTradeItemsBtn</code> button
     */
    String threeRecentTradeItemsBtn() {
        return "<html> Three Most Recent <br> Traded Items";
    }

    /**
     * @return string indicating <code>tradeThresholdsBtn</code> button
     */
    String tradeThresholdsBtn() {
        return "<html> View Trade Threshold <br> Values";
    }
}
