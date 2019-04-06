package messages;

import MessageBody.OrderQty;
import MessageBody.Price;
import MessageBody.Symbol;

public class MarketData {

    private Symbol symbol = new Symbol();
    private OrderQty orderQty = new OrderQty();
    private int marketID;
    private Price price = new Price();

    public MarketData(int marketID) {
        this.marketID = marketID;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
    }

    public OrderQty getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(OrderQty orderQty) {
        this.orderQty = orderQty;
    }

    public int getMarketID() {
        return marketID;
    }

    public void setMarketID(int marketID) {
        this.marketID = marketID;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }
}
