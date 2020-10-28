package core.trade;

import core.user.User;
import core.user.UserAccountManager;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class that manages the trade thresholds and related queries.
 */
public class TradeThresholdManager {
    private int maxIncompleteTrades;
    private int maxWeeklyTransactions;
    private int minLendBorrowDifference;
    private LocalDateTime time1;
    private LocalDateTime time2;

    /**
     * Constructs an instance of <code>AdminTradeManager</code>.
     *
     * @param maxIncompleteTrades     the max num of incomplete trades a <code>user</code> can have.
     * @param maxWeeklyTransactions   the max num of <code>transaction</code>s a user can have per week.
     * @param minLendBorrowDifference the max number the user can borrow more than lend.
     */
    public TradeThresholdManager(int maxIncompleteTrades, int maxWeeklyTransactions, int minLendBorrowDifference) {
        this.maxIncompleteTrades = maxIncompleteTrades;
        this.maxWeeklyTransactions = maxWeeklyTransactions;
        this.minLendBorrowDifference = minLendBorrowDifference;
        time1 = LocalDateTime.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        time2 = LocalDateTime.now().with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
        LocalDateTime now = LocalDateTime.now();
        /* if today is Sunday */
        if (now.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
            /* time1 becomes sunday */
            time1 = now;
            /* time2 becomes next sunday */
            time2 = now.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
        }
    }

    /**
     * Gets all number of incomplete <code>Trade</code> of <code>User</code>s which over <code>maxIncompleteTrade</code>.
     *
     * @param ua           a <code>UserAccountManager</code> for accessing user information.
     * @param tradeManager a <code>TradeManager</code>  for get trades related to <code>users</code>
     * @return a Map of <code>username</code> and their number of incomplete <code>Trade</code> over
     * <code>maxIncompleteTrade</code> which key is username, value is number of incomplete <code>Trade</code>.
     */
    public Map<String, Integer> getUserOverMaxIncompleteTrade(UserAccountManager ua, TradeInfoManager tradeManager) {
        Map<String, Integer> temp = new HashMap<>();
        for (User user : ua.getAccounts((User u) -> true)) {
            int incompleteCount = tradeManager.getTrades(user.getUsername(), TradeStatus.ONGOING).size();
            incompleteCount += tradeManager.getTrades(user.getUsername(), TradeStatus.ABANDONED).size();
            incompleteCount += tradeManager.getTrades(user.getUsername(), TradeStatus.CANCELLED).size();
            if (incompleteCount > maxIncompleteTrades)
                temp.put(user.getUsername(), incompleteCount - maxIncompleteTrades);
        }
        return temp;
    }

    /**
     * Gets a map of <code>username</code>s of <code>user</code>s that exceeded <code>minLendBorrowDifference</code>.
     * <code>minLendBorrowDifference</code> is the difference between number of items lend and items borrowed,
     * with items lend being the larger one.
     *
     * @param ua a UserAccountManager for accessing user information.
     * @return a map that maps <code>username</code> of <code>user</code>s to pair of number of lending and
     * borrowing of the <code>user</code>.
     */
    public Map<String, Integer> getUsersNotUpToMinLentBorrowDifference(UserAccountManager ua) {
        Map<String, Integer> map = new HashMap<>();
        for (User user : ua.getAccounts((User u) ->
                (u.getNumLent() - u.getNumBorrowed() < minLendBorrowDifference && !u.getUsername().equals("GUEST"))))
            map.put(user.getUsername(), Math.abs(user.getNumLent() - user.getNumBorrowed()));
        return map;
    }

    /**
     * Gets all number of completed <code>Trade</code> of <code>User</code>s which over <code>maxWeeklyTransaction</code>.
     *
     * @param trades a collection of all <code>trades</code> that will be checked
     * @return a Map of <code>username</code> and their number of completed <code>Trade</code> over
     * <code>maxWeeklyTransaction</code> which key is username,
     * value is number of completed <code>trade</code>.
     */
    public HashMap<String, Integer> getUserOverWeeklyTransMaxMap(List<Trade> trades) {
        HashMap<String, Integer> userOverWeeklyTransMax = new HashMap<>();
        for (Trade trade : trades)
            /*
             * Trade's time should be between time1 exclusive and time2 inclusive.
             * time1.compareTo(time2) return 0 if same, positive is time1 is after time2, negative otherwise.
             */
            if (trade.getStatus() == TradeStatus.COMPLETED && time1.compareTo(trade.getTradeCompletionTime()) < 0 &&
                    time2.compareTo(trade.getTradeCompletionTime()) >= 0) {
                updateUserOverWeeklyTransMaxMap(userOverWeeklyTransMax, trade.getUsername(0));
                updateUserOverWeeklyTransMaxMap(userOverWeeklyTransMax, trade.getUsername(1));
            }
        for (Map.Entry<String, Integer> entry : userOverWeeklyTransMax.entrySet())
            if (entry.getValue() <= maxWeeklyTransactions)
                userOverWeeklyTransMax.remove(entry.getKey());
            else
                entry.setValue(entry.getValue() - maxWeeklyTransactions);
        return userOverWeeklyTransMax;
    }

    /* Called by getUserOverWeeklyTransMaxMap to update user's information of over weekly trans */
    private void updateUserOverWeeklyTransMaxMap(HashMap<String, Integer> input, String username) {
        input.put(username, !input.containsKey(username) ? 1 : input.get(username) + 1);
    }

    /**
     * Gets the trade related threshold of <code>type</code>.
     * <p>
     * Precondition: <code>type</code> must be either <code>MAX_INCOMPLETE_TRADES</code>,
     * <code>MAX_WEEKLY_TRANSACTIONS</code> or <code>MIN_LEND_BORROW_DIFF</code>.
     *
     * @param type the type of threshold to be shown.
     * @return the value of the threshold <code>type</code>.
     */
    public int getThreshold(TradeThresholdType type) {
        switch (type) {
            case MAX_INCOMPLETE_TRADES:
                return maxIncompleteTrades;
            case MAX_WEEKLY_TRANSACTIONS:
                return maxWeeklyTransactions;
            case MIN_LEND_BORROW_DIFF:
                return minLendBorrowDifference;
        }
        return 0;
    }

    /**
     * Sets the trade related threshold of <code>type</code> to <code>value</code>.
     * <p>
     * Precondition: <code>type</code> must be either <code>MAX_INCOMPLETE_TRADES</code>,
     * <code>MAX_WEEKLY_TRANSACTIONS</code> or <code>MIN_LEND_BORROW_DIFF</code>.
     *
     * @param type  the type of threshold to be set.
     * @param value the new value of <code>type</code> to be set.
     */
    public void setThreshold(TradeThresholdType type, int value) {
        switch (type) {
            case MAX_INCOMPLETE_TRADES:
                maxIncompleteTrades = value;
                break;
            case MAX_WEEKLY_TRANSACTIONS:
                maxWeeklyTransactions = value;
                break;
            case MIN_LEND_BORROW_DIFF:
                minLendBorrowDifference = value;
                break;
        }
    }
}
