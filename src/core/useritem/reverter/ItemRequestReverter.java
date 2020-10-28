package core.useritem.reverter;

import core.item.Item;
import core.reverter.ActionReverter;
import genericdatatype.Pair;

import java.util.List;

/**
 * Revert action of requesting an Item.
 */
public class ItemRequestReverter extends ActionReverter {
    private final List<Pair<String, Item>> itemRequests;
    private final Pair<String, Item> request;

    /**
     * This class reverts wishlist addition requests
     *
     * @param itemRequests the itemRequests list, note that this is serialized, so the reverter works
     * @param request      the request to remove for the revert
     */
    public ItemRequestReverter(List<Pair<String, Item>> itemRequests, Pair<String, Item> request) {
        super(request.value1);
        this.itemRequests = itemRequests;
        this.request = request;
    }

    /**
     * Revert the action.
     *
     * @return message resulting from execution
     */
    public String execute() {
        for (Pair<String, Item> request : itemRequests) {
            if (request.equals(this.request)) {
                itemRequests.remove(request);
                return "Revert successful.";
            }
        }
        return "Revert failed, item has already been added to the user. Try reverting the item addition instead of" +
                " request.";
    }

    /**
     * Gets the type of action it will undo.
     *
     * @return the type of action it will undo.
     */
    public String getActionType() {
        return "Item Request";
    }

    /**
     * Gets a description of action it will undo.
     *
     * @return a description of action it will undo.
     */
    @Override
    public String getActionDescriptionReverted() {
        return "Requested item: <br>" + request.value2;
    }
}
