import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import messages.BuyMessage;
import messages.MessageFactory;
import messages.SellMessage;
import nio.ListOfClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Random;

public class BrokerHandler extends ChannelInboundMessageHandlerAdapter<String> {

    private SellMessage sellMessage;
    private BuyMessage buyMessage;
    private String brokerId;
    private String targetId;
    private static Integer rejectedCounter = 0;
    private String stringFromRouter;
    private ChannelHandlerContext ctx;
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public void messageReceived(ChannelHandlerContext arg0, String stringFromRouter) throws Exception {
        this.ctx = arg0;
        this.stringFromRouter = stringFromRouter;
        handler();
    }

    private void messageCreator() {

        Random random = new Random();
        if (random.nextBoolean()) {
            sellMessage = MessageFactory.createSellMessage(brokerId, Integer.parseInt(targetId));
            ctx.channel().write(sellMessage.getMessage() + "\n");
            ctx.channel().flush();

            logger.info("Sell message is generated : " + sellMessage.getMessage());
        }
        else {
            buyMessage = MessageFactory.createBuyMessage(brokerId, Integer.parseInt(targetId));
            ctx.channel().write(buyMessage.getMessage() + "\n");
            ctx.channel().flush();
            logger.info("Buy message is generated : " + buyMessage.getMessage());
        }
    }

    private void handler() {
        if (stringFromRouter.contains("id")) {
            this.brokerId = stringFromRouter.substring(stringFromRouter.indexOf("id") + 2, stringFromRouter.indexOf(" "));
            logger.info("Broker received id - {} from Router", String.format("%06d", Integer.parseInt(this.brokerId)));
            targetId = stringFromRouter.substring(stringFromRouter.indexOf("tar") + 3);
            messageCreator();
        }
        else if (stringFromRouter.contains("35=3")){
            logger.warn("rejected incoming " + this.stringFromRouter);
            rejectedCounter++;
            isRejectedOver();
        }
        else if (stringFromRouter.contains("market")) {
            logger.info("Broker received message: {}", this.stringFromRouter);
            logger.error("You have to start market client");
            System.exit(0);
        }
        else if (stringFromRouter.contains("doesn't exist")) {
            logger.error("Broker say GOOD BYE, because market doesn't exist");
        }
        else {
            logger.info("Broker received message: {}", this.stringFromRouter);
            messageCreator();
        }

    }

    private void isRejectedOver() {

        if (rejectedCounter == 4) {
            logger.warn("Broker say GOOD BYE");
            ctx.close();
        }
        else {
            messageCreator();
        }

    }
}
