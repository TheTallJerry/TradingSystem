package core.trade.reverter;

import core.reverter.ActionReverter;
import core.trade.Trade;

import java.util.List;

/**
 * Reverts the action of a <code>User</code> requests a <code>Trade</code>.
 */
public class RequestTradeReverter extends ActionReverter {
    private final Trade trade;
    private final List<Trade> trades;

    /**
     * Constructs a RequestTradeReverter with <code>trade</code> and <code>username</code>.
     *
     * @param trade    the <code>Trade</code> the <code>User</code> with <code>username</code> requested.
     * @param username the username of the <code>User</code> who did the action.
     * @param trades   a collection of all the existing <code>Trade</code>s
     */
    public RequestTradeReverter(Trade trade, String username, List<Trade> trades) {
        super(username);
        this.trade = trade;
        this.trades = trades;
    }

    /**
     * Undo the action of <code>User</code> with <code>username</code> requesting <code>trade</code>.
     *
     * @return whether the undo is successful or not.
     */
    @Override
    public String execute() {
        if (trade.getCurrentMeetingRelated() == -1) {
            trades.remove(trade);
            return ("reverting trade request successful!");
        }
        return "The trade has started meeting arrangement. Undo failed.";
    }

    /**
     * Gets the type of action it will undo.
     *
     * @return the type of action it will undo.
     */
    @Override
    public String getActionType() {
        return "Request Trade";
    }

    /**
     * Gets a description of action it will undo.
     *
     * @return a description of action it will undo.
     */
    @Override
    public String getActionDescriptionReverted() {
        return getAssociatedUsername() +
                " requested a trade with " + trade.getUsername(1) + " of id " + trade.getTradeId();
    }
}
