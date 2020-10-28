package core.trade;

import core.item.Item;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Class that manages <code>Trade</code>s.
 */
public class TradeInfoManager {
    private final List<Trade> trades;

    /**
     * Construct an instance of <code>TradeManager</code>.
     *
     * @param trades a list containing all the <code>Trade</code>s in the system
     */
    public TradeInfoManager(List<Trade> trades) {
        this.trades = trades;
    }

    /**
     * Finds the <code>Trade</code> related to the given tradeId.
     *
     * @param tradeId id of this <code>Trade</code>.
     * @return the <code>Trade</code> or null if <code>Trade</code> not exists.
     */
    public Trade getTrade(int tradeId) {
        for (Trade trade : trades) if (trade.getTradeId() == tradeId) return trade;
        return null;
    }

    /**
     * Gets <code>Item</code>s involved in the <code>Trade</code> with <code>tradeId</code>.
     *
     * @param tradeId id of a <code>Trade</code>.
     * @return array of <code>Item</code>s involved in the <code>Trade</code>, or empty array if doesn't exist
     * a <code>Trade</code> with <code>tradeId</code>.
     */
    public Item[] getItemsInvolved(int tradeId) {
        Trade trade = getTrade(tradeId);
        if (trade != null) return trade.getItemsInvolved();
        return new Item[]{};
    }

    /**
     * Return whether the <code>Trade</code> is on the <code>NOT_STARTED</code> status.
     *
     * @param tradeId tradeId of the <code>Trade</code>
     * @return true iff the trade is <code>NOT_STARTED</code>.
     */
    public boolean tradeNotStarted(int tradeId) {
        Trade trade = getTrade(tradeId);
        if (trade != null) return trade.getStatus().equals(TradeStatus.NOT_STARTED);
        return true;
    }

    /*Called by getThreeRecentTradeItem and getTopThreeTradePartner. Get best three qualified object from given map.*/
    private <T, S> List<T> extractTWithBestThreeS(Map<T, S> map, Comparator<S> comparator) {
        List<Map.Entry<T, S>> list = new ArrayList<>(map.entrySet());
        Comparator<Map.Entry<T, S>> entryComp =
                (Map.Entry<T, S> a, Map.Entry<T, S> b) -> comparator.compare(a.getValue(), b.getValue());
        list.sort(entryComp);
        List<T> returnList = new ArrayList<>();
        list.forEach((e) -> returnList.add(e.getKey()));
        if (returnList.size() > 3)
            return returnList.subList(0, 3);
        return returnList;
    }

    /**
     * Gets three recent <code>trade</code> <code>item</code> information of a <code>User</code>.
     *
     * @param username the username of this <code>User</code>.
     * @return a list of information about at most 3 recent traded <code>Item</code>s in Trade.
     * an empty list if no trade happened.
     */
    public List<String> getThreeRecentTradeItem(String username) {
        HashMap<Item, LocalDateTime> itemDate = new HashMap<>();
        for (Trade trade : getTrades(username, TradeStatus.COMPLETED)) {
            for (Item item : trade.getItemsInvolved())
                if (item != null) itemDate.put(item, trade.getTradeCompletionTime());
        }
        List<String> itemInfoList = new ArrayList<>();
        for (Item item : extractTWithBestThreeS(itemDate, (a, b) -> -a.compareTo(b))) {
            itemInfoList.add(item.toString());
        }
        return itemInfoList;
    }

    /**
     * Gets information about 3 most frequent trader partner of a <code>User</code>.
     *
     * @param username the username of this <code>User</code>.
     * @return a list of information about at most 3 most frequent trader partner with given User.
     * an empty list if no trade happened.
     */
    public List<String> getTopThreeTradePartner(String username) {
        HashMap<String, Integer> tradeUserMap = new HashMap<>();
        for (Trade trade : getTrades(username, TradeStatus.COMPLETED)) {
            String tradePartnerUsername = trade.getUsername(Math.abs(findUserOrder(trade.getTradeId(), username) - 1));
            tradeUserMap.put(tradePartnerUsername,
                    !tradeUserMap.containsKey(tradePartnerUsername) ? 1 : tradeUserMap.get(tradePartnerUsername) + 1);
        }
        return extractTWithBestThreeS(tradeUserMap, (a, b) -> -Integer.compare(a, b));
    }

    /**
     * Gets types of at most 3 most frequent traded item type a <code>User</code>.
     *
     * @param username the username of this <code>User</code>.
     * @return a list of information about at most 3 most frequent trader partner with given User.
     * an empty list if no trade happened.
     */
    public List<String> getTopThreeTradeItemType(String username) {
        HashMap<String, Integer> tradeItemTypeMap = new HashMap<>();
        for (Trade trade : getTrades(username, TradeStatus.NONE)) {
            if (trade.getItemsInvolved()[0] != null) {
                tradeItemTypeMap.put(trade.getItemsInvolved()[0].getType(),
                        !tradeItemTypeMap.containsKey(trade.getItemsInvolved()[0].getType()) ?
                                1 : tradeItemTypeMap.get(trade.getItemsInvolved()[0].getType()) + 1);
            }
            tradeItemTypeMap.put(trade.getItemsInvolved()[1].getType(),
                    !tradeItemTypeMap.containsKey(trade.getItemsInvolved()[1].getType()) ?
                            1 : tradeItemTypeMap.get(trade.getItemsInvolved()[1].getType()) + 1);
        }
        return extractTWithBestThreeS(tradeItemTypeMap, (a, b) -> -Integer.compare(a, b));
    }

    /**
     * Finds the username of a <code>User</code> that is the initiator of the <code>Trade</code> with <code>tradeId</code>.
     *
     * @param tradeId   id of the <code>Trade</code>.
     * @param userOrder an int representing the user that is performing this action. 0 is the initiator of the trade,
     *                  1 is the responder of the trade.
     * @return the username of the initiator of the <code>Trade</code>.
     */
    public String getUsername(int tradeId, int userOrder) {
        return getTrade(tradeId).getUsername(userOrder);
    }

    /**
     * Finds the <code>Trade</code> if it exits and check if the given <code>username</code> is initiator or responder
     *
     * @param tradeId  the id of the <code>Trade</code>.
     * @param username the name of the <code>User</code>.
     * @return a int of -1, 0, 1. -1 stand for no <code>Trade</code>, 0 refers to the initiator,
     * 1 refers to the responder.
     */
    public int findUserOrder(int tradeId, String username) {
        Trade trade = getTrade(tradeId);
        if (trade == null) return -1;
        if (username.equals(trade.getUsername(0))) return 0;
        else if (username.equals(trade.getUsername(1))) return 1;
        return -1;
    }

    /**
     * Gets all <code>Trade</code> of this <code>username</code> in given <code>Trade</code> status.
     *
     * @param username    the name of the <code>User</code>.
     * @param tradeStatus the status of <code>Trade</code>.
     * @return a List of<code>Trade</code> which satisfies the require <code>Trade</code> status of
     * the <code>User</code>.
     */
    public List<Trade> getTrades(String username, TradeStatus tradeStatus) {
        ArrayList<Trade> lst = new ArrayList<>();
        trades.forEach((Trade t) -> {
            if ((t.getUsername(0).equals(username) || t.getUsername(1).equals(username)) &&
                    (t.getStatus().equals(tradeStatus) || tradeStatus == TradeStatus.NONE)) lst.add(t);
        });
        return lst;
    }

    /**
     * Return a collection of all current <code>trades</code> stored.
     *
     * @return a list of <code>trades</code>, which is all the all the <code>trades</code> currently stored
     */
    public List<Trade> getTrades() {
        return trades;
    }

    /**
     * Finds all information of <code>Trade</code> that match the User with <code>username</code> involved.
     *
     * @param username the name of <code>User</code>.
     * @return a List of String which contain the information of <code>Trade</code>s.
     */
    public List<String[]> getTradesInfo(String username) {
        ArrayList<String[]> lst = new ArrayList<>();
        int size = getTrades(username, TradeStatus.NONE).size();
        for (int i = 0; i < 3; i++) lst.add(new String[size]);
        int counter = 0;
        for (Trade trade : getTrades(username, TradeStatus.NONE)) {
            lst.get(0)[counter] = Integer.toString(trade.getTradeId());
            lst.get(1)[counter] = trade.simplifiedInfo();
            lst.get(2)[counter] = trade.toString();
            counter++;
        }
        return lst;
    }

    /**
     * Gets the <code>meetingId</code> of the currently ongoing <code>meeting</code> about this <code>Trade</code>.
     *
     * @param tradeId the id of <code>Trade</code>.
     * @return null if <code>Trade</code> not exists, or return <code>meetingId</code> of the currently
     * ongoing <code>meeting</code> about his <code>Trade</code>. If no <code>meeting</code>
     * is ongoing, return -1.
     */
    public Integer getCurrentMeetingRelated(int tradeId) {
        Trade trade = getTrade(tradeId);
        if (trade != null) return trade.getCurrentMeetingRelated();
        return null;
    }
}