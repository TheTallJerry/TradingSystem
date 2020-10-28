package client.guiandpresenter.userscreen.usertrade.userrequesttradescreen;

import client.guiandpresenter.userscreen.InventoryPresenter;

import java.util.List;

/**
 * Presents information related to <code>Item</code>.
 */
class UserRequestTradePresenter extends InventoryPresenter {

    /**
     * @return a String displaying optional lend item label message
     */
    String optionalLendItemLblMsg() {
        return "<html> The item that you would like to lend (optional): ";
    }

    /**
     * @return a String displaying "Item System" which is the title
     */
    @Override
    public String getTitle() {
        return "Item System";
    }

    /**
     * Displaying all the <code>items</code> suggested for the <code>user</code>
     *
     * @param items all <code>items</code> suggested for the <code>user</code>, empty if no suggestion
     * @return a list displaying all the <code>items</code> suggested for the <code>user</code>
     */
    String lendingSuggestion(List<String> items) {
        if (items.isEmpty())
            return "Sorry, none of your item are in their wishlist...";
        StringBuilder sb = new StringBuilder("<html>Their wishlist contains: <br>");
        for (String str : items) sb.append(str).append("<br>");
        return sb.toString();
    }

    /**
     * Return the request trade prompt
     *
     * @return the request trade prompt
     */
    String requestTradePrompt() {
        return "Request Trade Confirmation";
    }

    /**
     * Displaying the <code>user</code>'s most borrowed <code>item</code> type
     *
     * @param items <code>items</code> that user most borrowed, empty if <code>user</code> haven't traded yet
     * @return a String Displaying the <code>user</code>'s most borrowed <code>item</code> type
     */
    String mostBorrowedType(List<String> items) {
        if (items.isEmpty()) return "You are not involved in any trade so we don't know what you will like...Sorry:( ";
        StringBuilder sb = new StringBuilder("<html>We think you might like items of these types: " + items.get(0));
        sb.append("<br>Therefore we recommend you check out the following items: <br>");
        for (int i = 1; i < items.size(); i++) {
            sb.append(items.get(i)).append("<br>");
        }
        return sb.toString();
    }

    /**
     * @return string displaying "Trade type: "
     */
    String tradeTypeMsg() {
        return "Trade type: ";
    }

    /**
     * @param flag a boolean indicating trade request status
     * @return string indicating whether the trade request is successful
     */
    String displayTradeRequestStatus(boolean flag) {
        if (flag)
            return "Trade requested successfully";
        return "Trade request failed. Check your status or input validity. ";
    }

    /**
     * Return the instructions prompt
     *
     * @return the instructions prompt
     */
    String instructionLabel() {
        return "<html> Double click item you want borrow to see trading option. " +
                "<br>Use your wishlist and items your can lend as a reference.";
    }

    /**
     * Return request trade options
     *
     * @return request trade options
     */
    String[] getOptions() {
        return new String[]{"Request trade with this item", "Cancel"};
    }

    /**
     * @return a string displaying "Next"
     */
    String nextBtnMsg() {
        return "Next";
    }

    /**
     * @return an arraylist of string indicating the two possible trade types
     */
    String[] tradeTypes() {
        return new String[]{"Permanent", "Temporary"};
    }

    /**
     * @return string indicating button related to the Ids suggested
     */
    String withIdSuggestionBtn() {
        return "See Item They May Like";
    }

    /**
     * @return string indicating button related the <code>item</code> one most borrowed
     */
    String mostBorrowTypeBtn() {
        return "Items You Might Like";
    }

    /**
     * @return array of 3 string indicating title for each item boxes.
     */
    String[] boxTitle() {
        return new String[]{"Inventory", "Items You Can Lend", "Your WishList"};
    }
}

