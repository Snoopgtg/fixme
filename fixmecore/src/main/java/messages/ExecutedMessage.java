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
    private CheckSum checkSum = new CheckSum();

    public ExecutedMessage(StandardMessageHeader standardMessageHeader, OrderID orderID, ExecID execID, ExecTransType
            execTransType, OrdStatus ordStatus, Symbol symbol, Side side, OrderQty orderQty, CumQty cumQty, AvgPx avgPx) {
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
        int lengtInByteOfMessage = this.standardMessageHeader.getBodyLength().calculateLengthOfBytesInMessage(this.getMessage());
        this.standardMessageHeader.getBodyLength().setValue(lengtInByteOfMessage);
        String resultOfcheckSum = this.checkSum.calculateCheckSumInMessage(this.getMessage());
        this.checkSum.setValue(resultOfcheckSum);
    }

    public String getMessage() {
        return standardMessageHeader.toString() + orderID.toString() + execID.toString() + execTransType.toString()
                + ordStatus.toString() + symbol.toString() + side.toString() + orderQty.toString() + cumQty.toString()
                + avgPx.toString() + checkSum.toString();
    }
}
