package messages;

import MessageBody.*;

public class BuyMessage {
    private StandardMessageHeader standardMessageHeader;
    private ClOrdID clOrdID;
    private Symbol symbol;
    private Side side;
    private OrdType ordType;
    private OrderQty orderQty;
    private Price price;
    private CheckSum checkSum = new CheckSum();

    public BuyMessage(StandardMessageHeader standardMessageHeader, ClOrdID clOrdID, Symbol symbol, Side side, OrdType ordType, OrderQty orderQty, Price price) {
        this.standardMessageHeader = standardMessageHeader;
        this.clOrdID = clOrdID;
        this.symbol = symbol;
        this.side = side;
        this.ordType = ordType;
        this.orderQty = orderQty;
        this.price = price;
        int lengtInByteOfMessage = this.standardMessageHeader.getBodyLength().calculateLengthOfBytesInMessage(this.getMessage());
        this.standardMessageHeader.getBodyLength().setValue(lengtInByteOfMessage);
        String resultOfcheckSum = this.checkSum.calculateCheckSumInMessage(this.getMessage());
        this.checkSum.setValue(resultOfcheckSum);
    }

    public String getMessage() {
        return standardMessageHeader.toString() + clOrdID.toString() + symbol.toString() + side.toString() + ordType.toString() + orderQty.toString() + price.toString() + checkSum.toString();
    }
}

//same that sell