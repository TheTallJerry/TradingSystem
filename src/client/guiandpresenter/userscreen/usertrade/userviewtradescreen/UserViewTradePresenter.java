package client.guiandpresenter.userscreen.usertrade.userviewtradescreen;

import client.guiandpresenter.SystemPresenter;

public class UserViewTradePresenter extends SystemPresenter {
    @Override
    public String getTitle() {
        return "Trades You Involved In";
    }

    String tradeInstruction() {
        return "<html> <br>Double Click to browse full trade information.<br>Right Click to: [View/Edit Meeting]" +
                "[Accept Trade Request][Deny Trade Request]";
    }

    String tradeNotStarted() {
        return "This trade is not accepted by you or the other user yet, try again later...";
    }

    /**
     * @param result a int indicating different result of the agree to trade action
     * @return a string indicating whether agreeing to trade is successful
     */
    String agreeToTradeStatus(int result) {
        switch (result) {
            case 0:
                return "You have agreed to this trade request. ";
            case 1:
                return "You cannot agree to this trade because you are the initiator.";
            case 2:
                return "You cannot agree to this trade because this trade has started.";
            case 3:
                return "<html>You cannot agree to this trade. Possible reasons are:<br>" +
                        "1. This trade has stared.<br>2. Some items involved has been lend by others.";
            default:
                return "You failed to agree to this trade due to unknown reason. Contact Admin for support.";
        }
    }

    /**
     * @param result a int indicating different result of the decline a trade action
     * @return a string indicating whether declining the trade is successful
     */
    String declineTradeStatus(int result) {
        switch (result) {
            case 0:
                return "You have denied to this trade request. ";
            case 1:
                return "You cannot deny to this trade because you are the initiator.";
            case 2:
                return "You cannot deny to this trade because this trade has started.";
            default:
                return "You failed to deny to this trade due to unknown reason. Contact Admin for support.";
        }
    }

    /**
     * @return a string indicating user haven't made any trade yet.
     */
    String noTrade() {
        return "You haven't made a trade yet. ";
    }

    String[] menuItem() {
        return new String[]{"View/Update Meeting", "Agree Trade Requests", "Deny Trade Requests"};
    }
}
