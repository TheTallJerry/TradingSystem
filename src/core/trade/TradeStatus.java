package core.trade;

/**
 * Status of a Trade. NONE cannot be a Trade status, but can be used to indicate Trades with any Status.
 */
public enum TradeStatus {
    NOT_STARTED, DENIED, ONGOING, COMPLETED, CANCELLED, ABANDONED, NONE
}
