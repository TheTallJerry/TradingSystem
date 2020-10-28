package core.user;

import core.account.Account;
import core.item.Item;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a registered user using this program.
 */
public class User extends Account implements Serializable {
    private final List<Item> wishList;
    private final List<Item> itemsAvailable; // items available for lend
    private final List<String> blockList;
    private String city;
    private int numLent;
    private int numBorrowed;
    private boolean isFrozen;
    private boolean onVacation;
    private int credit;

    /**
     * Creates an user with given <code>username</code> and <code>password</code>, all lists are default empty,
     * <code>isFrozen</code> is false, <code>city</code> is empty, <code>credit</code> is 60,
     * <code>completedTradeWithoutEvaluation</code> = 0.
     *
     * @param username the username for this user.
     * @param password the password for this user.
     */
    public User(String username, String password) {
        super(username, password);
        isFrozen = false;
        onVacation = false;
        wishList = new ArrayList<>();
        itemsAvailable = new ArrayList<>();
        blockList = new ArrayList<>();
        city = "";
        credit = 60;
    }

    /**
     * Adds an <code>item</code> to the user's <code>ItemsAvailable</code>.
     * <p>
     * Precondition: The item is not this user's item.
     *
     * @param item the item to be added.
     */
    public void addToItemsAvailable(Item item) {
        this.itemsAvailable.add(item);
    }

    /**
     * Removes an <code>item</code> from the user's <code>ItemsAvailable</code>.
     *
     * @param item the item to be removed.
     */
    public void deleteFromItemsAvailable(Item item) {
        this.itemsAvailable.remove(item);
    }

    /**
     * Gets the <code>ItemsAvailable</code> list of the user, which is a list of items the user can lend.
     *
     * @return the user's <code>ItemsAvailable</code>.
     */
    public List<Item> getItemsAvailable() {
        return itemsAvailable;
    }

    /**
     * Adds <code>item</code> to the user's <code>wishList</code>.
     * <p>
     * Precondition: The item is not this user's <code>wishList</code>.
     *
     * @param item the item to be added.
     */
    public void addToWishList(Item item) {
        this.wishList.add(item);
    }

    /**
     * Removes item from the user's <code>wishList</code>.
     *
     * @param item the item to be removed.
     */
    public void deleteFromWishList(Item item) {
        this.wishList.remove(item);
    }

    /**
     * Gets for the <code>wishList</code> of the user.
     *
     * @return the user's <code>wishList</code>.
     */
    public List<Item> getWishList() {
        return wishList;
    }

    /**
     * Adds other user's <code>username</code> to the user's <code>blockList</code>.
     * <p>
     * Precondition: The <code>username</code> is not this user's username.
     *
     * @param username the username of the other user that this user want to block.
     */
    public void addToBlockList(String username) {
        this.blockList.add(username);
    }

    /**
     * Removes the <code>username</code> of a user from current user's <code>blockList</code>.
     *
     * @param username the username to be removed from the <code>blockList</code>.
     */
    public void deleteFromBlockList(String username) {
        this.blockList.remove(username);
    }

    /**
     * Gets a list of usernames of users that are blocked by this user.
     *
     * @return <code>blockList</code>, which is the list of usernames of users that are blocked by this user.
     */
    public List<String> getBlockList() {
        return this.blockList;
    }

    /**
     * Sets the user's <code>city</code>.
     *
     * @param city the new city to be updated.
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Gets the <code>city</code> where user lives in.
     *
     * @return a string which represents the user's city.
     */
    public String getCity() {
        return this.city;
    }

    /**
     * Sets the <code>isFrozen</code> status of the user.
     *
     * @param frozen the new isFrozen status to be updated.
     */
    public void setFrozen(boolean frozen) {
        isFrozen = frozen;
    }

    /**
     * Gets the <code>isFrozen</code> status of the user.
     *
     * @return true iff the user is frozen.
     */
    public boolean isFrozen() {
        return isFrozen;
    }

    /**
     * Sets the <code>onVacation</code> status of the user.
     *
     * @param onVacation the new onVacation status to be updated.
     */
    public void setOnVacation(boolean onVacation) {
        this.onVacation = onVacation;
    }

    /**
     * Gets the <code>onVacation</code> status of the user.
     *
     * @return true iff the user is on vacation.
     */
    public boolean onVacation() {
        return onVacation;
    }

    /**
     * Gets the number of items the user has lent.
     *
     * @return <code>numLent</code>, which is the number of items the user has lent.
     */
    public int getNumLent() {
        return numLent;
    }

    /**
     * Gets the number of items the user has borrowed.
     *
     * @return <code>numBorrowed</code>, which is the number of items the user has borrowed.
     */
    public int getNumBorrowed() {
        return numBorrowed;
    }

    /**
     * Gets user's current <code>credit</code>.
     *
     * @return <code>credit</code>, which is the credit of the current user.
     */
    public int getCredit() {
        return credit;
    }

    /**
     * Adds 1 to the number of items lent <code>numLent</code>.
     */
    public void addNumLent() {
        numLent += 1;
    }

    /**
     * Adds 1 to the number of items borrowed <code>numBorrowed</code>.
     */
    public void addNumBorrowed() {
        numBorrowed += 1;
    }

    /**
     * Adds 2 points to <code>credit</code>.
     */
    public void creditAddition() {
        credit += 1;
    }

    /**
     * Subtract 5 points from <code>credit</code>.
     */
    public void creditSubtraction() {
        credit -= 5;
    }


    /**
     * Gives String representation of each information of the user;
     *
     * @return information about the user as a string, including <code>username</code>, <code>itemsAvailable</code>,
     * <code>wishList</code>, <code>blockList</code>, <code>city</code> the user lives in, number of items
     * borrowed and lent.
     */
    public String[] getInfoList() {
        String[] infoList = new String[4];
        infoList[0] = giveBasicInfo();
        if (itemsAvailable.isEmpty()) infoList[1] = "You have no item that you can lend. Request some items!";
        else {
            StringBuilder itemAvailableStr = new StringBuilder("<html>");
            itemsAvailable.forEach(item -> itemAvailableStr.append(item.toString()).append("<br>"));
            infoList[1] = itemAvailableStr.toString();
        }
        if (wishList.isEmpty()) infoList[2] = "You have no item in your wishlist.";
        else {
            StringBuilder wishListStr = new StringBuilder("<html>");
            wishList.forEach(item -> wishListStr.append(item.toString()).append("<br>"));
            infoList[2] = wishListStr.toString();
        }
        if (blockList.isEmpty()) infoList[3] = "You didn't block any user.";
        else {
            StringBuilder blockListStr = new StringBuilder("<html>");
            blockList.forEach(username -> blockListStr.append("[").append(username).append("]<br>"));
            infoList[3] = blockListStr.toString();
        }
        return infoList;
    }

    /* Basic Info that does not involve lists */
    private String giveBasicInfo() {
        StringBuilder str = new StringBuilder("<html>Username: " + getUsername() + "<br>Password: " + getPassword());
        str.append("<br>City: ");
        if (city.equals("")) str.append("[None]");
        else str.append(city);
        str.append("<br>Current credit: ").append(credit).append("<br>On-vacation Status: ");
        if (onVacation) str.append("[ON]");
        else str.append("[OFF]");
        if (isFrozen) str.append("<br>Account [FROZEN]");
        else str.append("<br>Account [Active]");
        str.append("<br>Lent ").append(numLent).append(" Item(s)");
        str.append("<br>Borrow ").append(numBorrowed).append(" Item(s)");
        return str.toString();
    }

    /**
     * A <code>toString</code> method, given the desired information in String
     *
     * @return a String that contains all the information required
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder(giveBasicInfo());
        String[] listInfo = getInfoList();
        s.append(listInfo[0]);
        String[] listTitle = new String[]{"<br>Item To Lend: <br>", "<br> Wishlist: <br>", "<br> Block List: <br>"};
        for (int i = 1; i < 4; i++) {
            s.append(listTitle[i - 1]);
            if (listInfo[i].contains("<html>")) s.append(listInfo[i].split("<html>")[1]);
            else s.append("[NONE]");
        }
        return s.toString();
    }
}

