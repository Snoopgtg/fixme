import MessageBody.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import messages.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

public class MarketHandler extends ChannelInboundMessageHandlerAdapter<String> {

    private RejectedMessage rejectedMessage;
    private ExecutedMessage executedMessage;
    private String marketId;
    private String stringFromRouter;
    private ChannelHandlerContext ctx;
    private MarketData marketData;
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public void messageReceived(ChannelHandlerContext ctx, String stringFromRouter) throws Exception {
        this.stringFromRouter = stringFromRouter;
        this.ctx = ctx;
        handler();

    }

    private void messageValidator() {

        OrderQty orderQty = new OrderQty();
        Symbol symbol = new Symbol();
        Price price = new Price();
        SenderCompID senderCompID = new SenderCompID();
        price.getAndSetValueFromString(stringFromRouter);
        symbol.getAndSetValueFromString(stringFromRouter);
        orderQty.getAndSetValueFromString(stringFromRouter);
        senderCompID.getAndSetValueFromString(stringFromRouter);
        Integer targetValue = Integer.parseInt(senderCompID.getValue().toString());
        if (this.marketData.isSumbolAndCalculated(symbol)) {
            if (this.marketData.isOrderQtyAndCalculated(orderQty)) {
                if (this.marketData.isPriceAndCalculated(price)) {
                    executedMessage = MessageFactory.createExecutedMessage(marketId, targetValue, stringFromRouter);
                    ctx.channel().write(executedMessage.getMessage() + "\n");
                    ctx.channel().flush();
                    logger.info(executedMessage.getMessage() + ": executed send");
                }
                else {
                    rejectedMessage = MessageFactory.createRejectedMessage(marketId, targetValue);
                    ctx.channel().write(rejectedMessage.getMessage() + "\n");
                    logger.info(rejectedMessage.getMessage() + ": rejected send");
                }
            }
            else {
                rejectedMessage = MessageFactory.createRejectedMessage(marketId, targetValue);
                ctx.channel().write(rejectedMessage.getMessage() + "\n");
                logger.info(rejectedMessage.getMessage() + ": rejected send");

            }
        }
        else {
            rejectedMessage = MessageFactory.createRejectedMessage(marketId, targetValue);
            ctx.channel().write(rejectedMessage.getMessage() + "\n");
            logger.info(rejectedMessage.getMessage() + ": rejected send");

        }


    }

    private void handler() {
        if (stringFromRouter.contains("id")) {
            this.marketId = stringFromRouter.substring(stringFromRouter.indexOf("id") + 2);
            marketData = new MarketData();
            logger.info("Market received id - {} from Router", String.format("%06d", Integer.parseInt(this.marketId)));
        }
        else if (stringFromRouter.contains("8=FIX")){
            logger.info("Market received message: {}", this.stringFromRouter);
            messageValidator();
        }
        else {
            logger.info("Market received message: {}", this.stringFromRouter);
        }
    }
}