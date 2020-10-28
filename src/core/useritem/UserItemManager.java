package core.useritem;

import core.item.Item;
import core.reverter.ActionReverter;
import core.user.User;
import core.user.UserAccountManager;
import core.useritem.reverter.AddToWishListReverter;
import core.useritem.reverter.DeleteFromWishListReverter;
import core.useritem.reverter.ItemRequestReverter;
import genericdatatype.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class that manage <code>Item</code>s that are in <code>User</code>s' <code>itemAvailable</code>s and
 * <code>wishList</code>s and related requests (item requests).
 */
public class UserItemManager {
    private final List<Pair<String, Item>> itemRequests;
    private int newestItemId;
    private final Map<String, Integer> itemDescriptionToIDMap, wishlistItemDescriptionToIDMap;

    /**
     * Creates an <code>UserItemManager</code> with <code>itemRequests</code>, all requests from users, and
     * <code>newestItemId</code>, an unique identifier to be assigned to when creating <code>Item</code> object.
     *
     * @param itemRequests a list of <code>pair</code>s, with the <code>value1</code> being the username of the user
     *                     who wants to request the item, and <code>value2</code> being the data of the item to
     *                     be added.
     * @param newestItemId the identifier the the next <code>Item</code> to be created.
     */
    public UserItemManager(List<Pair<String, Item>> itemRequests, int newestItemId) {
        this.itemRequests = itemRequests;
        this.newestItemId = newestItemId;
        itemDescriptionToIDMap = new HashMap<>();
        wishlistItemDescriptionToIDMap = new HashMap<>();
    }

    /**
     * Get a map where the key is a long description of this item used when browsing inventory, and where the
     * value is the id of the said item.
     *
     * @return a map where the key is a long description of this item used when browsing inventory, and where the
     * value is the id of the said item.
     */
    public Map<String, Integer> getItemDescriptionToIDMap() {
        return itemDescriptionToIDMap;
    }

    /**
     * Get a map where the key is a long description of this item used when browsing wishlist, and where the
     * value is the id of the said item.
     *
     * @return a map where the key is a long description of this item used when browsing wishlist, and where the
     * value is the id of the said item.
     */
    public Map<String, Integer> getWishlistItemDescriptionToIDMap() {
        return wishlistItemDescriptionToIDMap;
    }

    /**
     * Returns the <code>username</code> of the account that has the given <code>item</code> in the
     * <code>available items</code>
     * Returns null if there is no such <code>account</code> found in all users
     *
     * @param item The <code>item</code> to be searched
     * @param ua   a UserAccountManager for accessing user information
     * @return The <code>username</code> of the <code>account</code> that contains the <code>item</code>, or null if not found
     */
    public String getUsernameFromItem(Item item, UserAccountManager ua) {
        for (User user : ua.getAccounts((User u) -> true)) {
            if (user.getItemsAvailable().contains(item))
                return user.getUsername();
        }
        return null;
    }

    /**
     * Updates the data of the two user that has completed a transaction of items. This transaction can be either
     * trading or returning Items.
     *
     * @param user1    The initiator User of the Trade
     * @param user2    The responder of the other User of the Trade
     * @param oneToTwo Possible Item that username1 lend to username2
     * @param twoToOne Possible Item that username2 lend to username1
     */
    public void updateUserItems(User user1, User user2, Item oneToTwo, Item twoToOne) {
        if (oneToTwo != null) {
            //update ItemsAvailable after confirm transaction occurred in Trade
            user1.deleteFromItemsAvailable(oneToTwo);
            // update NumLent after confirm transaction occurred in Trade
            user1.addNumLent();
            user2.deleteFromWishList(oneToTwo);
            user2.addNumBorrowed();
        }
        if (twoToOne != null) {
            user2.deleteFromItemsAvailable(twoToOne);
            user2.addNumLent();
            user1.deleteFromWishList(twoToOne);
            user1.addNumBorrowed();
        }
    }

    /**
     * Return the newest item id
     *
     * @return the newest item id
     */
    public int getNewestItemId() {
        return newestItemId;
    }

    /**
     * Gets formatted representation of <code>itemRequests</code>
     *
     * @return A Map from:
     * &emsp;Pairs of (short description, long description) of this requests <br>
     * to:
     * &emsp;Pairs that represent the requests in <code>DataBundle</code> <br>
     */
    public Map<Pair<String, String>, Pair<String, Item>> getFormattedItemRequests() {
        Map<Pair<String, String>, Pair<String, Item>> result = new HashMap<>();
        for (Pair<String, Item> pair : itemRequests) {
            result.put(new Pair<>(pair.value1, pair.value2.toString()), pair);
        }
        return result;
    }

    /**
     * Get one <code>user</code>'s <code>itemAvailable</code>
     *
     * @param user the target <code>user</code>
     * @return a collection of String description of all <code>items</code> in <code>user</code>'s
     * <code>itemAvailable</code>
     */
    public List<String> getUserItemsAvailable(User user) {
        List<String> temp = new ArrayList<>();
        for (Item item : user.getItemsAvailable()) temp.add(item.toString());
        return temp;
    }

    /**
     * Add the given <code>Item</code> to the given <code>user</code>'s <code>ItemAvailable</code> list, remove
     * this <code>request</code> from <code>itemRequests</code>
     *
     * @param request the request (what is stored in DataBundle)
     * @param ua      a UserAccountManager for accessing user information
     */
    public void acceptUserItemRequest(Pair<String, Item> request, UserAccountManager ua) {
        itemRequests.remove(request);
        ua.getAccount(request.value1).addToItemsAvailable(request.value2);
    }

    /**
     * Keep the given <code>user</code>'s <code>ItemAvailable</code> list no change, remove
     * this <code>request</code> from <code>itemRequests</code>
     *
     * @param request the request (what is stored in DataBundle)
     */
    public void denyUserItemRequest(Pair<String, Item> request) {
        itemRequests.remove(request);
    }

    /**
     * Delete an item to a user's wishlist. An item can be deleted if the user exist in the system and item is in the
     * user's wishlist.
     *
     * @param user This user
     * @param item The item to be deleted
     * @return <code>ActionReverter</code> for undoing this action, null if the action failed.
     */
    public ActionReverter deleteFromWishlist(User user, Item item) {
        if (item != null && user != null && user.getWishList().contains(item)) {
            user.deleteFromWishList(item);
            return new DeleteFromWishListReverter(user, item);
        }
        return null;
    }

    /**
     * Add an item to a user's wishlist. An item can be added if the user exist in the system and item is not in the
     * user's wishlist.
     *
     * @param user This user
     * @param item The item to be added
     * @return <code>ActionReverter</code> for undoing this action, null if the action failed.
     */
    public ActionReverter addToWishlist(User user, Item item) {
        if (item != null && user != null && !user.getWishList().contains(item)) {
            user.addToWishList(item);
            return new AddToWishListReverter(user, item);
        }
        return null;
    }

    /**
     * Requests to add an item to this user's <code>itemAvailable</code>.
     *
     * @param username        the username of the user who requests.
     * @param itemType        the type of the item that is requested.
     * @param itemName        the name of the item that is requested.
     * @param itemDescription the description of the item that is requested.
     * @return <code>ActionReverter</code> for undoing this action, null if the action failed.
     */
    public ActionReverter createItemAndRequest(String username, String itemType, String itemName,
                                               String itemDescription) {
        Item item = new Item(itemType, itemName, itemDescription, ++newestItemId);
        Pair<String, Item> newRequest = new Pair<>(username, item);
        itemRequests.add(newRequest);
        return new ItemRequestReverter(itemRequests, newRequest);
    }

    /*
     * userList is not always the same list. Some user may be filtered so their items are not shown.
     */

    /**
     * Get information of all items in a list of users' items available for lend, in userList.
     *
     * @param users a list of users.
     * @return a list of string representations of items in the inventory
     */
    public List<String> getInventory(List<User> users) {
        List<String> lst = new ArrayList<>();
        for (User user : users) {
            if (getInventoryByUser(user).size() > 0) {
                //lst.add("Username: "+user.getUsername()+", Credit: "+user.getCredit() + "<br>");
                for (Map.Entry<String, List<Item>> entry : getInventoryByUser(user).entrySet()) {
                    for (Item item : entry.getValue()) {
                        String temp = item.toString() + "Username: " + user.getUsername() + ", Credit: " + user.getCredit();
                        //lst.add(item.toString());
                        lst.add(temp);
                        //itemDescriptionToIDMap.put(item.toString(), item.getId());
                        itemDescriptionToIDMap.put(temp, item.getId());
                    }
                }
            }
        }
        return lst;
    }

    /**
     * Called by getInventory to get the inventory of a given user
     *
     * @param user a user that we want to get from
     * @return the inventory of the given user's inventory
     */
    private Map<String, List<Item>> getInventoryByUser(User user) {
        Map<String, List<Item>> temp = new HashMap<>();
        for (Item item : user.getItemsAvailable()) {
            if (!temp.containsKey(user.getUsername())) {
                List<Item> t = new ArrayList<>();
                t.add(item);
                temp.put(user.getUsername(), t);
            } else {
                temp.get(user.getUsername()).add(item);
            }
        }
        return temp;
    }

    /**
     * Get a list of this user's items in wishlist
     *
     * @param user this user
     * @return a list of this user's items in wishlist
     */
    public List<String> getUserWishlist(User user) {
        List<String> temp = new ArrayList<>();
        for (Item item : user.getWishList()) {
            temp.add(item.toString());
            wishlistItemDescriptionToIDMap.put(item.toString(), item.getId());
        }
        return temp;
    }

    /**
     * Helper function called by ItemWithBorrowTypeSuggestion to get all the available item.
     *
     * @param users a user that we want to get from
     * @return a list of all available items in the given list of users' items available list
     */
    private List<Item> getInventoryHelper(List<User> users) {
        List<Item> temp = new ArrayList<>();
        for (User user : users) {
            temp.addAll(user.getItemsAvailable());
        }
        return temp;
    }

    /**
     * Suggest a list of items from <code>lender</code> to lend to <code>borrower</code>. Only items that are both in
     * lender's <code>itemAvailable</code> and borrower's <code>wishList</code> are included.
     *
     * @param lender   the lender that needs suggestion about items to lend to a borrower.
     * @param borrower the borrower that the lender is interested in lending to.
     * @return a list of item that are both in lender's available list and borrower's wish list.
     */
    public List<String> createLendingSuggestion(User lender, User borrower) {
        List<String> lst = new ArrayList<>();
        if (lender != null && borrower != null) {
            for (Item item1 : lender.getItemsAvailable()) {
                for (Item item2 : borrower.getWishList()) {
                    if (item1.getName().equals(item2.getName())) lst.add(item1.toString());
                }
            }
        }
        return lst;
    }

    /**
     * Suggest a list of items user might like depends on the user's most frequent borrow types.
     *
     * @param user      the user looking for items to trade.
     * @param users     a list of users the user can trade with
     * @param itemTypes a collection of String representation of <code>itemTypes</code>
     * @return a list, with the first thing a string of the user's most frequent borrow types, the rest of thing the
     * items the user may like as Strings.
     */
    public List<String> createBorrowingSuggestion(User user, List<User> users, List<String> itemTypes) {
        List<String> lst = new ArrayList<>();
        if (user != null && !itemTypes.isEmpty()) {
            StringBuilder allType = new StringBuilder();
            itemTypes.forEach(type -> allType.append(type).append(","));
            lst.add(allType.toString());
            for (Item item : getInventoryHelper(users))
                if (itemTypes.contains(item.getType())) lst.add(item.toString());
            return lst;
        }
        return lst;
    }

    /**
     * Get <code>Item</code> correspond to <code>itemId</code> in <code>itemsAvailable</code> of any <code>User</code>
     * in <code>users</code>.
     *
     * @param itemId the id of the <code>Item</code>.
     * @param users  the list of <code>User</code>s that may has this <code>Item</code>.
     * @return the <code>Item</code> object if such <code>Item</code> exist, null otherwise.
     */
    public Item getItemFromId(int itemId, List<User> users) {
        for (Item item : getInventoryHelper(users)) if (item.getId() == itemId) return item;
        return null;
    }

    /**
     * Get <code>Item</code> correspond to <code>itemId</code> in a <code>User</code>'s <code>itemsAvailable</code>
     *
     * @param itemId the id of the <code>Item</code>.
     * @param user   the user that may has this <code>Item</code>.
     * @return the <code>Item</code> object if such <code>Item</code> exist, null otherwise.
     */
    public Item getItemFromId(int itemId, User user) {
        for (Item item : user.getItemsAvailable()) if (item.getId() == itemId) return item;
        return null;
    }

    /**
     * Check if the <code>Item</code> with <code>itemId</code> can be lent by <code>user</code>. Item can be lent if it
     * is in this <code>user</code>'s <code>itemsAvailable</code>.
     *
     * @param itemId the id of the <code>Item</code>.
     * @param user   the user who wants to lend the <code>Item</code> correspond to <code>itemId</code>.
     * @return true if item in users' itemsAvailable, false otherwise.
     */
    public boolean itemCanNotLend(User user, int itemId) {
        return getItemFromId(itemId, user) == null;
    }

    /**
     * Check if the <code>Item</code> with <code>itemId</code> can be borrowed. Item can be borrowed if it
     * is <code>itemsAvailable</code> of and <code>User</code> in <code>users</code>.
     *
     * @param users  the list of users this user can borrow from.
     * @param itemId the id of this item.
     * @return true if item in users' itemsAvailable, false otherwise.
     */
    public boolean itemCanBorrow(List<User> users, int itemId) {
        return getItemFromId(itemId, users) != null;
    }
}
