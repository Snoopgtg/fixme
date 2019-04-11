package message;


import MessageBody.EnumMessageBody.ClientPort;
import MessageBody.TargetCompID;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import nio.ListOfClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import validator.CheckSumValidate;
import validator.DestinationValidator;
import validator.ParentValidator;

import java.lang.invoke.MethodHandles;
import java.net.InetSocketAddress;

public class RouterNioHandler extends ChannelInboundMessageHandlerAdapter<String> {

    private static Logger getLogger() {
        return logger;
    }

    private static int brokerIndex;
    private static int marketIndex;
    private ParentValidator parentValidator;
    private static int stopWork = 0;
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final ChannelGroup BrokerChannels = new DefaultChannelGroup("brokers");
    private static final ChannelGroup MarketChannels = new DefaultChannelGroup("markets");

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {

        Channel incoming = ctx.channel();

        if (RouterNioHandler.checkIncomingClient(ctx)) {
            if (ListOfClients.getInstance().getMarketMap().isEmpty()) {
                incoming.write("market does not exist\n");
                incoming.flush();
            }
            else {
                getLogger().info("Accepted a connection from {} - Broker client", incoming.remoteAddress());
                for (Channel channel : MarketChannels) {
                    channel.write("New Broker - has joined!\n");
                    channel.flush();
                }
                BrokerChannels.add(ctx.channel());
                registerBroker(incoming);
                getLogger().info("add Broker to broker group");
            }

        }
        else {
            getLogger().info("Accepted a connection from {} - Market client", incoming.remoteAddress());
            for (Channel channel : BrokerChannels) {
                channel.write("New Market - has joined!\n");
                channel.flush();
            }
            MarketChannels.add(ctx.channel());
            registerMarket(incoming);
            getLogger().info("add Market to market group");

        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        Channel incoming = ctx.channel();
        if (RouterNioHandler.checkIncomingClient(ctx)) {
            for (Channel channel : MarketChannels) {
                channel.write("Server - Broker : " + incoming.remoteAddress() + " has disconnected!\n");
            }
            ListOfClients.getInstance().getBrokerMap().values().removeIf(v -> v.equals(incoming));
            BrokerChannels.remove(ctx.channel());
        }
        else {
            for (Channel channel : BrokerChannels) {
                channel.write("Server - Market : " + incoming.remoteAddress() + " has disconnected!\n");
            }
            ListOfClients.getInstance().getMarketMap().values().removeIf(v -> v.equals(incoming));
            MarketChannels.remove(ctx.channel());
        }


    }

    @Override
    public void messageReceived(ChannelHandlerContext arg0, String msg) {

        TargetCompID targetCompID = new TargetCompID();
        this.parentValidator = new CheckSumValidate(msg);
        targetCompID.getAndSetValueFromString(msg);
        Integer targetValue = Integer.parseInt(targetCompID.getValue().toString());
        if (RouterNioHandler.checkIncomingClient(arg0)) {
            logger.info("received message from Broker {}", msg);
            parentValidator.linkWith(new DestinationValidator(ListOfClients.getInstance().getBrokerMap(), ListOfClients.getInstance().getMarketMap()));
            if (ListOfClients.getInstance().getMarketMap().containsKey(targetValue)) {
                Channel channel = ListOfClients.getInstance().getMarketMap().get(targetValue);
                channel.write(msg + "\n");
                channel.flush();
            }
            else { //TODO delete this else and correct validate in parentValidator.linkWith(new DestinationValidator(brokerMap, marketMap));

                arg0.channel().write("{} ID doesn't exist" + targetCompID.getValue() + "\n");
                logger.warn("Market with ID : {} doesn't exist", targetCompID.getValue());
                arg0.close();
            }

        }
        else {
            logger.info("received message from Market {}", msg);
            if (ListOfClients.getInstance().getBrokerMap().containsKey(targetValue)) {
                Channel channel = ListOfClients.getInstance().getBrokerMap().get(targetValue);
                channel.write(msg + "\n");
                channel.flush();
            }

        }
        isStopWork();
    }

    private void isStopWork() {
        if (stopWork == 100) {
            RouterStopper.getInstance().stop();
        }
        else {
            stopWork++;
        }
    }

    private void registerBroker(Channel incoming) {

        /*if (ListOfClients.getInstance().getBrokerMap().containsKey(brokerIndex)) {
            getLogger().info("Broker connected with id : {}", brokerIndex);
            return;
        }*/
        brokerIndex++;
        ListOfClients.getInstance().getBrokerMap().put(brokerIndex, incoming);
        getLogger().info("Router assigned Broker with ID = {}", String.format("%06d", brokerIndex));
        incoming.write("id" + Integer.toString(brokerIndex) + " tar" + ListOfClients.getInstance().getRandomKeyFromMarketMap() + "\n");
        incoming.flush();

    }

    private void registerMarket(Channel incoming) {

        /*if (ListOfClients.getInstance().getMarketMap().containsKey(marketIndex)) {
            getLogger().info("Market connected with id : {}", marketIndex);
            return;
        }*/
        marketIndex++;
        ListOfClients.getInstance().getMarketMap().put(marketIndex, incoming);
        getLogger().info("Router assigned Market with ID = {}", String.format("%06d", marketIndex));
        incoming.write("id" + Integer.toString(marketIndex) + "\n");
        incoming.flush();
    }

    private static boolean checkIncomingClient(ChannelHandlerContext ctx) {

        Channel incoming = ctx.channel();
        InetSocketAddress incomingPort = (InetSocketAddress) incoming.localAddress();
        return incomingPort.getPort() == ClientPort.BROKERPORT.getPort();
    }
}
