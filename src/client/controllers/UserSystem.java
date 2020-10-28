package client.controllers;

import client.databundle.DataBundle;
import core.UserFacade;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * A Controller class responsible for providing functionality to <code>User</code>s.
 */
public class UserSystem {
    private final UserFacade userFacade;
    private final DataBundle dataBundle;
    private final String currUsername;

    /**
     * Constructs UserSystem with <code>dataBundle</code>.
     *
     * @param dataBundle   all the essential data of this system.
     * @param currUsername the username of the current user.
     */
    public UserSystem(DataBundle dataBundle, String currUsername) {
        userFacade = new UserFacade(currUsername, dataBundle.getEntitiesAndReverters(),
                dataBundle.getIdCounters(), dataBundle.getRequests(), dataBundle.getThresholds());
        this.dataBundle = dataBundle;
        this.currUsername = currUsername;
    }

    /**
     * Return a list of string representations of items in this user's wishlist
     *
     * @return a list of string representations of items in this user's wishlist
     */
    public List<String> getUserWishlist() {
        return userFacade.getUserWishlist();
    }

    /**
     * Return a list of string representations of items in this user's itemsAvailable
     *
     * @return a list of string representations of items in this user's itemsAvailable
     */
    public List<String> getUserItemsAvailable() {
        return userFacade.getUserItemsAvailable();
    }

    /* Input must be at least 4 digit length */
    private boolean isNotInputValidString(String input) {
        return input.length() < 4;
    }

    /* Avoids User interact with guest(public User) account */
    private boolean excludeGuest(String username) {
        return !username.equals("GUEST");
    }

    /* Checks if <code>input</code> is a positive integers.*/
    private boolean isNotValidItemID(String input) {
        return !input.matches("^[1-9]\\d*$");
    }

    /* Item name, type, description should contain only letter, number, comma, space or period. */
    private boolean isValidItem(String type, String name, String description) {
        return (name.matches("^[a-zA-Z0-9,. ]*$") && type.matches("^[a-zA-Z0-9,. ]*$") &&
                description.matches("^[a-zA-Z0-9,. ]*$"));
    }

    /* Gets the id of the item in the inventory based on the key*/
    private int getItemIdUsingDescription(String key) {
        return userFacade.getItemDescriptionToIDMap().get(key);
    }

    /* Gets the id of the item in this user's wishlist based on the key*/
    private int getWishlistItemDescriptionToIDMap(String key) {
        return userFacade.getWishlistItemDescriptionToIDMap().get(key);
    }

    /**
     * Gets information of all <code>Trade</code> related to the <code>User</code> currently using this system. Update
     * credit subtraction before retrieving information.
     *
     * @return information of all <code>Trade</code> related to the <code>User</code> get from <code>userFacade</code>.
     */
    public List<String[]> getTradesInfo() {
        userFacade.updateCreditSubtraction();
        return userFacade.getTradesInfo();
    }

    /* DataBundle is stored such that it can be given to the next screen if new system needs to be created. */

    /**
     * Gets <code>dataBundle</code> stored in this <code>UserSystem</code>.
     *
     * @return <code>dataBundle</code>.
     */
    public DataBundle getDataBundle() {
        return dataBundle;
    }

    /**
     * Checks if the current <code>User</code> is guest(demo) account
     *
     * @return true iff the current <code>User</code> username equals guest(demo) account username.
     */
    public boolean isCurrUserGuest() {
        return currUsername.equals("GUEST");
    }

    /**
     * Gets information about the <code>User</code> using the system currently.
     *
     * @return a formatted String contains all information about this User.
     */
    public String[] getUserAccountSpecifics() {
        userFacade.updateCreditSubtraction();
        return userFacade.getUserAccountSpecifics();
    }

    /**
     * Gets current password of the <code>User</code> using the system currently.
     *
     * @return the password of this <code>User</code>.
     */
    public String getCurrPassword() {
        return userFacade.getPassword(currUsername);
    }

    /**
     * Change the password of the <code>User</code> currently using this system to <code>password</code>.
     *
     * @param password new password of this <code>User</code>.
     * @return true iff the change is successful.
     */
    public boolean changeCurrUserPassword(String password) {
        if (isNotInputValidString(password)) return false;
        userFacade.setPassword(password);
        return true;
    }

    /**
     * Adds <code>username</code> to <code>blockList</code> of the <code>User</code> currently using this system.
     *
     * @param username username of a <code>User</code>.
     * @return true iff adding is successful.
     */
    public boolean addToBlockList(String username) {
        if (!isNotInputValidString(username) && excludeGuest(username) && userFacade.usernameExits(username))
            return userFacade.addToBlockList(username);
        return false;
    }

    /**
     * Removes <code>username</code> from <code>blockList</code> of the <code>User</code> currently using this system.
     *
     * @param username username of a <code>User</code>.
     * @return true iff removing is successful.
     */
    public boolean removeFromBlockList(String username) {
        if (!isNotInputValidString(username) && excludeGuest(username) && userFacade.usernameExits(username))
            return userFacade.deletedFromBlockList(username);
        return false;
    }

    /**
     * Gets <code>blockList</code> of the <code>User</code> currently using this system.
     *
     * @return <code>blockList</code> of the <code>User</code>.
     */
    public List<String> getBlockList() {
        return userFacade.getBlockList();
    }

    /**
     * Adds <code>Item</code> with <code>itemID</code> to the <code>wishList</code> of the <code>User</code> currently
     * using this system.
     *
     * @param key the description of the said item
     * @return true iff adding successfully.
     */
    public boolean addToUserWishList(String key) {
        return userFacade.addToWishlist(getItemIdUsingDescription(key));
    }

    /**
     * Removes <code>Item</code> with <code>itemID</code> in the <code>wishList</code> of the <code>User</code> currently
     * using this system.
     *
     * @param key the description of the said item
     * @return true iff removing successfully.
     */
    public boolean removeFromUserWishList(String key) {
        return userFacade.deleteFromWishlist(getWishlistItemDescriptionToIDMap(key));
    }

    /**
     * Requests unfreezes on behalf of the <code>User</code> currently using this system.
     *
     * @return true iff the request is successful.
     */
    public boolean requestUnfreeze() {
        return userFacade.requestUnfreeze();
    }

    /**
     * Report <code>User</code> with <code>username</code> on behalf of the <code>User</code> currently using this
     * system, with <code>reason</code>.
     *
     * @param username username of the <code>User</code> being reported
     * @param reason   a string of reasoning of reporting this user.
     * @return true iff the report is successful.
     */
    public boolean reportUser(String username, String reason) {
        if (!isNotInputValidString(username) || !isNotInputValidString(reason))
            if (excludeGuest(username) && userFacade.usernameExits(username))
                return userFacade.reportUser(username, reason);
        return false;
    }

    /**
     * Switch the <code>onVacation</code> status of the <code>User</code> currently using this system.
     *
     * @return true if the switching is successful, false otherwise.
     */
    public boolean switchVacationStatus() {
        return userFacade.switchVacationStatus();
    }

    /**
     * Gets all <code>Item</code>s the <code>User</code> currently using this system can lend. Update credit information
     * before retrieving <code>Item</code>s information.
     *
     * @return all <code>Item</code>s this <code>User</code> can lend get from <code>userFacade</code>.
     */
    public List<String> getInventoryForWishlist() {
        userFacade.updateCreditSubtraction();
        List<String> temp = userFacade.getInventory();
        for (String str : getUserWishlist()) {
            for (String s : userFacade.getInventory())
                if (s.contains(str))
                    temp.remove(s);
        }
        return temp;
    }

    /**
     * Gets information of all items that <code>user</code> can borrow
     *
     * @return a list of String which contains item summary and the corresponding username
     */
    public List<String> getInventoryForTrade() {
        userFacade.updateCreditSubtraction();
        return userFacade.getInventory();
    }

    /**
     * Requests to create and put an <code>Item</code> in <code>ItemAvailable</code> of the <code>User</code> currently
     * using this system, with <code>itemType</code>, <code>itemName</code>, <code>itemDescription</code>.
     * <code>itemType</code>, <code>itemName</code>, <code>itemDescription</code> must match the required format.
     *
     * @param itemType        the type of the <code>Item</code>.
     * @param itemName        the name of the <code>Item</code>.
     * @param itemDescription the description of the <code>Item</code>.
     * @return true if the request is made successfully, false otherwise.
     */
    public boolean createItemAndRequest(String itemType, String itemName, String itemDescription) {
        if (isValidItem(itemType, itemName, itemDescription) && !(itemType.isEmpty() &&
                itemDescription.isEmpty() && itemName.isEmpty())) {
            userFacade.createItemAndRequest(itemType, itemName, itemDescription);
            dataBundle.itemIdCounter = userFacade.getNewestItemId();
            return true;
        }
        return false;
    }

    /**
     * Gets city information of the <code>User</code> currently using this system.
     *
     * @return city information of this <code>User</code>.
     */
    public String getCity() {
        return userFacade.getCity();
    }

    /**
     * Sets city information of the <code>User</code> currently using this system to <code>city</code>.
     *
     * @param city new city to be set as this <code>User</code>'s city.
     * @return true if set successfully. false when city name is invalid.
     */
    public boolean setCity(String city) {
        /* If city is not empty and not a valid city
         * The first character must be upper case digit. The rest can only be letter, digits, space or period(.)
         */
        if (!city.matches("^[A-Z][a-zA-Z .]*$") && !city.equals("")) return false;
        userFacade.setCity(city);
        return true;
    }

    /**
     * Gets the users on-vacation status.
     *
     * @return true if the status is turned on.
     */
    public boolean getOnVacation() {
        return userFacade.getOnVacation();
    }


    /**
     * Gets a list of suggested <code>Item</code> to lend.
     *
     * @param itemIdStr the id of the <code>Item</code>
     * @return a list of suggested <code>Item</code> to lend get from <code>userFacade</code>.
     */
    public List<String> createLendingSuggestion(String itemIdStr) {
        return userFacade.createLendingSuggestion(
                userFacade.getUsernameFromItemID(getItemIdUsingDescription(itemIdStr)));
    }

    /* Return true if the input is not a valid string for location.
     * Location cannot be empty and can only contain letters, digits, comma, figure dash(-) or hash(#).
     */
    private boolean isNotLocation(String input) {
        return !input.matches("^[a-zA-Z0-9, -#]+$");
    }

    /**
     * Creates meeting and add to meeting related of the <code>Trade</code> with <code>tradeId</code>, with
     * <code>location</code> and <code>timeStr</code>. <code>location</code> and <code>timeStr</code> must
     * match the required format.
     *
     * @param tradeId  id of the <code>Trade</code> related to the <code>Meeting</code>.
     * @param location location of the <code>Meeting</code>
     * @param timeStr  String formatted as time of the <code>Meeting</code>.
     * @return 1 if the meeting is successfully created. 2 if the creation failed because of invalid location.
     * 3 if the creation failed because of time incorrectness, 4 if create meeting not successful for other
     * reasons
     */
    public int createMeeting(int tradeId, String location, String timeStr) {
        try {
            if (isNotLocation(location)) {
                return 2; /* fail because of invalid location string */
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime time = LocalDateTime.parse(timeStr, formatter);
            if (!time.isAfter(LocalDateTime.now())) {
                System.out.println(time);
                return 3; /* fail because time not in future */
            }
            if (userFacade.createMeeting(tradeId, location, time)) {
                dataBundle.meetingIdCounter = userFacade.getNewestMeetingId();
                return 1; /* Successful */
            }
            return 4; /* fail because of other reason */
        } catch (DateTimeParseException e) {
            System.out.println(timeStr);
            return 3; /* fail because of invalid time string */
        }
    }

    /**
     * Edits the current <code>Meeting</code> related to <code>Trade</code> with <code>tradeId</code> with
     * <code>location</code> and <code>timeStr</code>. <code>location</code> and <code>timeStr</code> must
     * match the required format.
     *
     * @param tradeId  id of the <code>Trade</code> related to the <code>Meeting</code>.
     * @param location location of the <code>Meeting</code>
     * @param timeStr  String formatted as time of the <code>Meeting</code>.
     * @return if edit meeting successful, 2 if place is not formatted correctly, 3 if datetime is not formatted
     * * correctly, 4 if edit meeting not successful for other reasons
     */
    public int editMeeting(int tradeId, String location, String timeStr) {
        try {
            if (isNotLocation(location))
                return 2;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime meetingTime = LocalDateTime.parse(timeStr, formatter);
            if (userFacade.editMeeting(tradeId, meetingTime, location)) return 1;
            return 4;
        } catch (DateTimeParseException e) {
            return 3;
        }
    }

    /**
     * Confirms the arrangement of current <code>Meeting</code> related to <code>Trade</code> with <code>tradeId</code>.
     *
     * @param tradeId id of the <code>Trade</code> related to the <code>Meeting</code>.
     * @return true if confirm arrangement of <code>Meeting</code> successfully, false otherwise.
     */
    public boolean confirmMeetingArrangement(int tradeId) {
        return userFacade.confirmMeetingArrangement(tradeId);
    }

    /**
     * Confirms the current <code>Meeting</code> related to <code>Trade</code> with <code>tradeId</code> occurred.
     *
     * @param tradeId id of the <code>Trade</code> related to the <code>Meeting</code>.
     * @return true if confirm <code>Meeting</code> occurred successfully, false otherwise.
     */
    public boolean confirmMeetingOccurred(int tradeId) {
        return userFacade.confirmMeetingOccurred(tradeId);
    }

    /**
     * Gets current <code>Meeting</code> information of the <code>Trade</code> with <code>tradeIdStr</code>.
     *
     * @param tradeIdStr the string of tradeId
     * @return information about the <code>Meeting</code> related to this <code>Trade</code>.
     */
    public String getMeetingInfo(String tradeIdStr) {
        return userFacade.getMeetingInfo(userFacade.getCurrentMeetingRelated(Integer.parseInt(tradeIdStr)));
    }

    /**
     * Gets all meeting thresholds in the system.
     *
     * @return an array of all meeting thresholds get from <code>userFacade</code>.
     */
    public int[] getMeetingThreshold() {
        return userFacade.getMeetingThresholds();
    }

    /**
     * Requests a trade on behalf of the <code>User</code> currently using this system.
     *
     * @param item1To2Str    item from initiator to responder. If this trade is a one-way trade, it is an empty String.
     * @param item2To1Str    item from responder to initiator.
     * @param isPermanentStr "Permanent" if this trade is permanent trade, "Temporary" if this trade is temporary.
     * @return true iff the trade is created successfully.
     */
    public boolean requestTrade(String item2To1Str, String item1To2Str, String isPermanentStr) {
        if ((isNotValidItemID(item1To2Str) && !item1To2Str.equals(""))) return false;
        int item1To2;
        if (!item1To2Str.equals("")) item1To2 = Integer.parseInt(item1To2Str);
        else item1To2 = -1;
        if (userFacade.requestTrade(item1To2, getItemIdUsingDescription(item2To1Str), isPermanentStr.equals("Permanent"))) {
            dataBundle.tradeIdCounter = userFacade.getNewestTradeId();
            return true;
        }
        return false;
    }

    /**
     * Gets all trade thresholds in the system.
     *
     * @return an array of all trade thresholds get from <code>userFacade</code>.
     */
    public int[] getTradeThresholds() {
        return userFacade.getTradeThresholds();
    }

    /**
     * Gets three most recent trade of the <code>User</code> currently using this system.
     *
     * @return three most recent trade get from <code>userFacade</code>.
     */
    public List<String> getThreeRecentTradeItem() {
        return userFacade.getThreeRecentTradeItem();
    }

    /**
     * Gets top three trade partner of the <code>User</code> currently using this system.
     *
     * @return top three trade partner get from <code>userFacade</code>.
     */
    public List<String> getTopThreeTradePartner() {
        return userFacade.getTopThreeTradePartner();
    }

    /**
     * Accepts the <code>Trade</code> with tradeId on behalf of the <code>User</code> currently using this system.
     *
     * @param tradeId id of a <code>Trade</code> <code>User</code> wants to accept.
     * @return 0 agree to trade successfully, 1 fail because user is the initiator,
     * 2 fail because trade is <code>NOT_STARTED</code>, other digit means unknown reason.
     */
    public int agreeToTrade(int tradeId) {
        return userFacade.agreeToTrade(tradeId);
    }

    /**
     * Denies the <code>Trade</code> with tradeId on behalf of the <code>User</code> currently using this system.
     *
     * @param tradeId id of a <code>Trade</code> <code>User</code> wants to deny.
     * @return true if deny the <code>Trade</code> successfully, false otherwise.
     */
    public int denyTrade(int tradeId) {
        return userFacade.denyTrade(tradeId);
    }

    /**
     * Return whether the <code>Trade</code> is on the <code>NOT_STARTED</code> status.
     *
     * @param tradeId tradeId of the <code>Trade</code>
     * @return true iff the trade is <code>NOT_STARTED</code>.
     */
    public boolean tradeNotStarted(int tradeId) {
        return userFacade.tradeNotStarted(tradeId);
    }

    /**
     * Gets the most frequent borrow type of the <code>User</code> currently using this system.
     *
     * @return the most frequent borrow type get from <code>userFacade</code>.
     */
    public List<String> createBorrowingSuggestion() {
        return userFacade.createBorrowingSuggestion();
    }

    /**
     * Sends private <code>massage</code> on behalf of the <code>User</code> currently using this system, to the <code>User</code>
     * with <code>receiverUsername</code>.
     *
     * @param receiverUsername the username of the <code>User</code> receiving this message.
     * @param message          the message to be sent.
     * @return true iff the message is sent successfully
     */
    public boolean sendPrivateMessage(String receiverUsername, String message) {
        return userFacade.sendPrivateMessage(receiverUsername, message);
    }

    /**
     * Gets all message received by the <code>User</code> currently using this system.
     *
     * @return all message received get from <code>userFacade</code>.
     */
    public List<String> getMessageReceived() {
        return userFacade.getMessageReceived();
    }

    /**
     * Gets all message sent by the <code>User</code> currently using this system.
     *
     * @return all message sent get from <code>userFacade</code>.
     */
    public List<String> getMessageSent() {
        return userFacade.getMessageSent();
    }
}
