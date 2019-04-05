package messages;

import MessageBody.*;

public class ExecutedMessage {
    private StandardMessageHeader standardMessageHeader;
    private OrderID orderID;
    private ExecID execID;
    private ExecTransType execTransType;
    private OrdStatus ordStatus;
    private Symbol symbol;
    private Side side;
    private OrderQty orderQty;
    private CumQty cumQty;
    private AvgPx avgPx;
    private StandardMessageTrailer standardMessageTrailer;

    public ExecutedMessage(StandardMessageHeader standardMessageHeader, OrderID orderID, ExecID execID, ExecTransType execTransType, OrdStatus ordStatus, Symbol symbol, Side side, OrderQty orderQty, CumQty cumQty, AvgPx avgPx, StandardMessageTrailer standardMessageTrailer) {
        this.standardMessageHeader = standardMessageHeader;
        this.orderID = orderID;
        this.execID = execID;
        this.execTransType = execTransType;
        this.ordStatus = ordStatus;
        this.symbol = symbol;
        this.side = side;
        this.orderQty = orderQty;
        this.cumQty = cumQty;
        this.avgPx = avgPx;
        this.standardMessageTrailer = standardMessageTrailer;
    }
}
