package core;

import core.item.Item;
import genericdatatype.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that collects different requests.
 */
public class RequestCollection {

    /**
     * Requests from a user to report another user.
     */
    final List<String[]> reportRequests;

    /**
     * The list of requests from users to unfreeze themselves.
     */
    final List<String> unfreezeRequests;

    /**
     * Requests from a user to add an item to their item available list.
     */
    final ArrayList<Pair<String, Item>> itemRequests;

    /**
     * Construct a <code>RequestCollection</code> that contains collections of different requests.
     *
     * @param reportRequests   requests from a user to report another user.
     * @param unfreezeRequests the list of requests from users to unfreeze themselves.
     * @param itemRequests     requests from a user to add an item to their item available list.
     */
    public RequestCollection(List<String[]> reportRequests,
                             List<String> unfreezeRequests,
                             ArrayList<Pair<String, Item>> itemRequests) {
        this.reportRequests = reportRequests;
        this.unfreezeRequests = unfreezeRequests;
        this.itemRequests = itemRequests;
    }

}
