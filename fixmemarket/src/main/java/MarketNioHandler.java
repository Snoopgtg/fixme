import MessageBody.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import messages.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

public class MarketNioHandler extends ChannelInboundMessageHandlerAdapter<String> {

    private RejectedMessage rejectedMessage;
    private ExecutedMessage executedMessage;
    private String marketId;
    private String stringFromRouter;
    private ChannelHandlerContext ctx;
    private MarketData marketData;
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public void messageReceived(ChannelHandlerContext ctx, String stringFromRouter) throws Exception {
        //System.out.println(arg1);
        this.stringFromRouter = stringFromRouter;
        this.ctx = ctx;
        handler();

    }

    private void messageValidator() {

        OrderQty orderQty = new OrderQty();
        Symbol symbol = new Symbol();
        Price price = new Price();
       // Thread.currentThread().join();
        price.getAndSetValueFromString(stringFromRouter);
        symbol.getAndSetValueFromString(stringFromRouter);
        orderQty.getAndSetValueFromString(stringFromRouter);
        if (this.marketData.isPiceAndCalculated(price)) {
            if (this.marketData.isOrderQtyAndCalculated(orderQty)) {
                if (this.marketData.isPiceAndCalculated(price)) {
                    executedMessage = MessageFactory.createExecutedMessage(marketId, stringFromRouter);
                    ctx.channel().write(executedMessage + "\n");
                    ctx.channel().flush();
                    logger.info(stringFromRouter + ": executed");
                }
                else {
                    rejectedMessage = MessageFactory.createRejectedMessage(marketId);
                    ctx.channel().write(rejectedMessage + "\n");
                }
            }
            else {
                rejectedMessage = MessageFactory.createRejectedMessage(marketId);
                ctx.channel().write(rejectedMessage + "\n");
            }
        }
        else {
            rejectedMessage = MessageFactory.createRejectedMessage(marketId);
            ctx.channel().write(rejectedMessage + "\n");
        }


    }

    private void handler() {
        if (stringFromRouter.contains("id")) {
            this.marketId = stringFromRouter.substring(stringFromRouter.indexOf("id") + 2);
            marketData = new MarketData();
            logger.info("Market received id - {} from Router", this.marketId);
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