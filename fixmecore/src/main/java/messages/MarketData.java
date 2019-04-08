package messages;

import MessageBody.OrderQty;
import MessageBody.Price;
import MessageBody.Symbol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

public class MarketData {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private Symbol symbol = new Symbol();
    private OrderQty orderQty = new OrderQty();
    private int marketID;
    private Price price = new Price();

    public boolean isOrderQtyAndCalculated(OrderQty orderQty) {

        int currentOrderQty = Integer.parseInt(this.orderQty.getValue().toString());
        int receivedOrderQty = Integer.parseInt(orderQty.getValue().toString());
        if (currentOrderQty >= receivedOrderQty) {
            currentOrderQty -= receivedOrderQty;
            if (currentOrderQty < 0) {
                logger.info("OrderQty is over\n");
                return false;
            }
            return true;
        }
        else {
            logger.info("OrderQty is over\n");
            return false;
        }
    }

    public boolean isSumbolAndCalculated(Symbol recSymbol) {

        String currentSymbol = symbol.getValue().toString();
        String receivedSymbol = recSymbol.getValue().toString();
        if (currentSymbol.equals(receivedSymbol)) {
            logger.info("current Symbol : {} equals received Symbol : {}\n", currentSymbol, receivedSymbol);
            return true;
        }
        logger.warn("Symbol : {} doesn't exist\n", receivedSymbol);
        return false;
    }

    public boolean isPiceAndCalculated(Price recPrice) {

        int currentPrice = Integer.parseInt(this.price.getValue().toString());
        int receivedPrice = Integer.parseInt(recPrice.getValue().toString());
        if (currentPrice >= receivedPrice) {
            logger.info("Price is equal market price\n");
                return true;
        }
        logger.info("Price too low\n");
        return false;

    }

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
