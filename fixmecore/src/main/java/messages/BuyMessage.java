package messages;

import MessageBody.*;

public class BuyMessage {
    private StandardMessageHeader standardMessageHeader;
    private ClOrdID clOrdID;
    private final Symbol symbol;
    private Side side;
    private OrdType ordType;
    private final OrderQty orderQty;
    private final Price price;

    //private StandardMessageTrailer standardMessageTrailer;

    public BuyMessage(StandardMessageHeader standardMessageHeader, ClOrdID clOrdID, Symbol symbol, Side side, OrdType ordType, OrderQty orderQty, Price price) {
        this.standardMessageHeader = standardMessageHeader;
        this.clOrdID = clOrdID;
        this.symbol = symbol;
        this.side = side;
        this.ordType = ordType;
        this.orderQty = orderQty;
        //this.standardMessageTrailer = standardMessageTrailer;
        this.price = price;
    }

    public String getMessage() {
        return standardMessageHeader.toString() + clOrdID.toString() + symbol.toString() + side.toString() + ordType.toString() + orderQty.toString() + price.toString();
    }
}

//same that sell