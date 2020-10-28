package core.trade.reverter;

import core.reverter.ActionReverter;
import core.trade.Trade;
import core.trade.TradeStatus;

/**
 * Reverts the action of a <code>User</code> deny a <code>Trade</code>.
 */
public class DenyTradeReverter extends ActionReverter {
    private final Trade trade;

    /**
     * Constructs a DenyTradeReverter with <code>trade</code> and <code>username</code>.
     *
     * @param trade    the <code>Trade</code> the <code>User</code> with <code>username</code> denied.
     * @param username the username of the <code>User</code> who did the action.
     */
    public DenyTradeReverter(Trade trade, String username) {
        super(username);
        this.trade = trade;
    }

    /**
     * Undo the action of <code>User</code> with <code>username</code> deny the <code>trade</code>.
     *
     * @return whether the undo is successful or not.
     */
    @Override
    public String execute() {
        trade.setStatus(TradeStatus.NOT_STARTED);
        //note that user cannot revert this process themselves.
        return "Revert user denying the trade successful!";
    }

    /**
     * Gets the type of action it will undo.
     *
     * @return the type of action it will undo.
     */
    @Override
    public String getActionType() {
        return "Deny Trade";
    }

    /**
     * Gets a description of action it will undo.
     *
     * @return a description of action it will undo.
     */
    @Override
    public String getActionDescriptionReverted() {
        return getAssociatedUsername() + " denied a trade with " + trade.getUsername(0) + " with id " + trade.getTradeId() + ".";
    }
}
