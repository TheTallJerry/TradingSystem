package core.useritem.reverter;

import core.item.Item;
import core.reverter.ActionReverter;
import core.user.User;

/**
 * Reverts the action of a <code>User</code> adding an <code>Item</code> to <code>wishList</code>.
 */
public class AddToWishListReverter extends ActionReverter {
    private final Item itemToRemove;
    private final User user;

    /**
     * Constructs an AddToWishListReverter with <code>user</code> and <code>itemToRemove</code>
     *
     * @param user         the <code>User</code> who did the action.
     * @param itemToRemove the <code>Item</code> added to <code>user</code>'s <code>wishList</code>.
     */
    public AddToWishListReverter(User user, Item itemToRemove) {
        super(user.getUsername());
        this.itemToRemove = itemToRemove;
        this.user = user;
    }

    /**
     * Undo the action of adding <code>item</code> to <code>user</code>'s <code>wishList</code>. Cannot undo if
     * <code>item</code> is not in <code>user</code>'s <code>wishList</code>.
     *
     * @return whether the undo is successful or not.
     */
    @Override
    public String execute() {
        if (user.getWishList().contains(itemToRemove))
            return "User has already removed this item from their wishlist";

        user.deleteFromWishList(itemToRemove);
        return "revert add to wishlist action successful!";
    }

    /**
     * Gets the type of action it will undo.
     *
     * @return the type of action it will undo.
     */
    @Override
    public String getActionType() {
        return "Add to Wishlist";
    }

    /**
     * Gets a description of action it will undo.
     *
     * @return a description of action it will undo.
     */
    @Override
    public String getActionDescriptionReverted() {
        return this.getAssociatedUsername() + " added " + itemToRemove.getName() + " to wishList.";
    }
}
