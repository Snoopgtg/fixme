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
    private Price price = new Price();

    public boolean isOrderQtyAndCalculated(OrderQty orderQty) {

        int currentOrderQty = Integer.parseInt(this.orderQty.getValue().toString());
        int receivedOrderQty = Integer.parseInt(orderQty.getValue().toString());
        if (currentOrderQty >= receivedOrderQty) {
            currentOrderQty -= receivedOrderQty;
            if (currentOrderQty < 0) {
                logger.info("OrderQty is over");
                return false;
            }
            return true;
        }
        else {
            logger.info("OrderQty is over");
            return false;
        }
    }

    public boolean isSumbolAndCalculated(Symbol recSymbol) {

        String currentSymbol = symbol.getValue().toString();
        String receivedSymbol = recSymbol.getValue().toString();
        if (currentSymbol.equals(receivedSymbol)) {
            logger.info("current Symbol : {} equals received Symbol : {}", currentSymbol, receivedSymbol);
            return true;
        }
        logger.warn("Symbol : {} doesn't exist", receivedSymbol);
        return false;
    }

    public boolean isPriceAndCalculated(Price recPrice) {

        float currentPrice = Float.parseFloat((this.price.getValue().toString()));
        float receivedPrice = Float.parseFloat(recPrice.getValue().toString());
        if (currentPrice >= receivedPrice) {
            logger.info("Price is equal market price");
                return true;
        }
        logger.info("Price too low");
        return false;

    }

    public MarketData() {

    }
}
