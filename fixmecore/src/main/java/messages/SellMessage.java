package messages;


import MessageBody.*;

public class SellMessage {
    private StandardMessageHeader standardMessageHeader;
    private ClOrdID clOrdID;
    private Symbol symbol;
    private Side side;
    private OrdType ordType;
    private OrderQty orderQty;
    private Price price;
    private CheckSum checkSum = new CheckSum();

    public SellMessage(StandardMessageHeader standardMessageHeader, ClOrdID clOrdID, Symbol symbol, Side side, OrdType ordType, OrderQty orderQty, Price price) {
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
//same that buy

//8=FIX.4.2 | 9=178 | 35=D | 34=123123 | 49=BROKER11 | 56=PHLX | 52=20071123-05:30:00.000 | 11=ATOMNOCCC9990900 | 55=MSFT | 167=FUT | 54=1 | 38=15 | 40=2 | 44=15 | 59=0 | 10=128 |