package messages;

import MessageBody.*;
import singlton.MessageSinglton;


public class MessageFactory {
    /*public	Message newMessage(*//*TODO fill message*//*) {

        *//*Coordinates coordinates = new Coordinates(longitude, latitude, height);

        switch (type.toLowerCase()) {

            case "jetplane" : return new JetPlane(name, coordinates);
            case "helicopter" : return new Helicopter(name, coordinates);
            case "baloon" : return new Baloon(name, coordinates);
            default : throw new Error(type + " is not valid aircraft");
        }*//*
    }*/
    public static SellMessage createSellMessage(String id) {

        StandardMessageHeader standardMessageHeader = new StandardMessageHeader(new BeginString("FIX.4.0"),
                new MsgType('D'), new SenderCompID(id), new MsgSeqNum(), new SendingTime());
        ClOrdID clOrdID = new ClOrdID();
        Symbol symbol = new Symbol();
        Side side = new Side('2');
        OrdType ordType = new OrdType();
        OrderQty orderQty = new OrderQty();
        Price price = new Price();
        return new SellMessage(standardMessageHeader, clOrdID, symbol,side, ordType, orderQty, price);
    }

    public static BuyMessage createBuyMessage(String id) {

        StandardMessageHeader standardMessageHeader = new StandardMessageHeader(new BeginString("FIX.4.0"),
                new MsgType('D'), new SenderCompID(id), new MsgSeqNum(), new SendingTime());
        ClOrdID clOrdID = new ClOrdID();
        Symbol symbol = new Symbol();
        Side side = new Side('1');
        OrdType ordType = new OrdType();
        OrderQty orderQty = new OrderQty();
        Price price = new Price();
        return new BuyMessage(standardMessageHeader, clOrdID, symbol, side, ordType, orderQty, price);
    }
}