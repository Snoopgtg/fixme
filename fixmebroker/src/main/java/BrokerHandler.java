import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import messages.BuyMessage;
import messages.MessageFactory;
import messages.SellMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Random;

public class BrokerHandler extends ChannelInboundMessageHandlerAdapter<String> {

    private SellMessage sellMessage;
    private BuyMessage buyMessage;
    private String brokerId;
    //TODO formatting to 6 digits ID
    private String stringFromRouter;
    ChannelHandlerContext ctx;
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    @Override
    public void messageReceived(ChannelHandlerContext arg0, String stringFromRouter) throws Exception {
        System.out.println(stringFromRouter);
        this.ctx = arg0;
        this.stringFromRouter = stringFromRouter;
        handler();
        messageCreator();
    }

    private void messageCreator() {

        Random random = new Random();
        if (random.nextBoolean()) {
            sellMessage = MessageFactory.createSellMessage(brokerId);
            ctx.channel().write(sellMessage.getMessage() + "\n");
            ctx.channel().flush();

            logger.info("Sell message is generated : " + sellMessage.getMessage());
        }
        else {
            buyMessage = MessageFactory.createBuyMessage(brokerId);
            ctx.channel().write(buyMessage.getMessage() + "\n");
            ctx.channel().flush();
            logger.info("Buy message is generated : " + buyMessage.getMessage());
        }
    }

    private void handler() {
        if (stringFromRouter.contains("id")) {
            this.brokerId = stringFromRouter.substring(stringFromRouter.indexOf("id") + 2);
            logger.info("Broker received id - {} from Router", this.brokerId);
        }
        else {
            logger.info("Broker received message: {}", this.stringFromRouter);
        }
    }
}
