package messages;

import MessageBody.*;


public class MessageFactory {

    public static SellMessage createSellMessage(String senderCompID, Integer targetCompID) {

        StandardMessageHeader standardMessageHeader = new StandardMessageHeader(new BeginString("FIX.4.0"),
                new MsgType('D'), new SenderCompID(senderCompID), new TargetCompID(targetCompID), new MsgSeqNum(), new SendingTime());
        ClOrdID clOrdID = new ClOrdID();
        Symbol symbol = new Symbol();
        Side side = new Side('2');
        OrdType ordType = new OrdType();
        OrderQty orderQty = new OrderQty();
        Price price = new Price();

        return new SellMessage(standardMessageHeader, clOrdID, symbol,side, ordType, orderQty, price);
    }

    public static BuyMessage createBuyMessage(String senderCompID, Integer targetCompID) {

        StandardMessageHeader standardMessageHeader = new StandardMessageHeader(new BeginString("FIX.4.0"),
                new MsgType('D'), new SenderCompID(senderCompID), new TargetCompID(targetCompID), new MsgSeqNum(), new SendingTime());
        ClOrdID clOrdID = new ClOrdID();
        Symbol symbol = new Symbol();
        Side side = new Side('1');
        OrdType ordType = new OrdType();
        OrderQty orderQty = new OrderQty();
        Price price = new Price();
        return new BuyMessage(standardMessageHeader, clOrdID, symbol, side, ordType, orderQty, price);
    }

    public static ExecutedMessage createExecutedMessage(String senderCompID, Integer targetCompID, String receivedMessage) {
        StandardMessageHeader standardMessageHeader = new StandardMessageHeader(new BeginString("FIX.4.0"),
                new MsgType('8'), new SenderCompID(senderCompID), new TargetCompID(targetCompID), new MsgSeqNum(), new SendingTime());

        OrderID orderID = new OrderID();
        ExecID execID = new ExecID();
        ExecTransType execTransType = new ExecTransType('2');
        OrdStatus ordStatus = new OrdStatus('B');
        Symbol symbol = new Symbol();
        symbol.getAndSetValueFromString(receivedMessage);
        Side side = new Side();
        side.getAndSetValueFromString(receivedMessage);
        OrderQty orderQty = new OrderQty();
        orderQty.getAndSetValueFromString(receivedMessage);
        CumQty cumQty = new CumQty();
        cumQty.setValue(orderQty.getValue()); // same that orderQty
        AvgPx avgPx = new AvgPx();
        avgPx.getAndSetValueFromString(receivedMessage);



        return new ExecutedMessage(standardMessageHeader, orderID, execID, execTransType, ordStatus, symbol, side, orderQty,
                cumQty, avgPx);
    }

    public static RejectedMessage createRejectedMessage(String senderCompID, Integer targetCompID) {
        StandardMessageHeader standardMessageHeader = new StandardMessageHeader(new BeginString("FIX.4.0"),
                new MsgType('3'), new SenderCompID(senderCompID), new TargetCompID(targetCompID), new MsgSeqNum(), new SendingTime());

        RefSeqNum refSeqNum = new RefSeqNum();

        return new RejectedMessage(standardMessageHeader, refSeqNum);

    }
}