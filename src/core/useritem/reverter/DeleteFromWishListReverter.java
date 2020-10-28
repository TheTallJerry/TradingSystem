package core.useritem.reverter;

import core.item.Item;
import core.reverter.ActionReverter;
import core.user.User;

/**
 * Reverts the action of a <code>User</code> delete an <code>Item</code> from <code>wishList</code>
 */
public class DeleteFromWishListReverter extends ActionReverter {
    private final Item itemToAdd;
    private final User user;

    /**
     * Construct a DeleteFromWishListReverter with <code>user</code> and <code>item</code>.
     *
     * @param user the <code>User</code> who did the action.
     * @param item the <code>Item</code> deleted from <code>user</code>'s <code>wishList</code>.
     */
    public DeleteFromWishListReverter(User user, Item item) {
        super(user.getUsername());
        this.itemToAdd = item;
        this.user = user;
    }

    /**
     * Undo the action of delete <code>item</code> from <code>user</code>'s <code>wishList</code>. Cannot undo if
     * <code>item</code> is in <code>user</code>'s <code>wishList</code>.
     *
     * @return whether the undo is successful or not.
     */
    @Override
    public String execute() {
        if (user.getWishList().contains(itemToAdd))
            return "User has already added this item back to their wishlist";

        user.addToWishList(itemToAdd);
        return "Revert delete from wishlist action successful!";

    }

    /**
     * Gets the type of action it will undo.
     *
     * @return the type of action it will undo.
     */
    @Override
    public String getActionType() {
        return "Delete from Wishlist";
    }

    /**
     * Gets a description of action it will undo.
     *
     * @return a description of action it will undo.
     */
    @Override
    public String getActionDescriptionReverted() {
        return this.getAssociatedUsername() + " deleted " + itemToAdd.getName() + " from wishList.";
    }
}
