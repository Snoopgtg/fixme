package message;


import MessageBody.TargetCompID;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import nio.RouterClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import validator.CheckSumValidate;
import validator.DestinationValidator;
import validator.ParentValidator;

import java.lang.invoke.MethodHandles;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RouterNioHandler extends ChannelInboundMessageHandlerAdapter<String> {

    private static Logger getLogger() {
        return logger;
    }

    private static int brokerIndex;
    private static int marketIndex;
    private ParentValidator parentValidator;
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final ChannelGroup BrokerChannels = new DefaultChannelGroup("brokers");
    private static final ChannelGroup MarketChannels = new DefaultChannelGroup("markets");

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        System.out.println("");
        Channel incoming = ctx.channel();
        getLogger().info("Accepted a connection from {}", incoming.remoteAddress());
        if (RouterNioHandler.checkIncomingClient(ctx)) {
            for (Channel channel : MarketChannels) {
//                channel.write("Broker - " + incoming.remoteAddress() + " has joined!\n");
//                channel.flush();
            }
            BrokerChannels.add(ctx.channel());
            registerBroker(incoming);
            getLogger().info("add Broker to broker group");

        }
        else {
            for (Channel channel : BrokerChannels) {
//                channel.write("Market - " + incoming.remoteAddress() + " has joined!\n");
//                channel.flush();
            }
            MarketChannels.add(ctx.channel());
            registerMarket(incoming);
            System.out.println("TARGET = " + ListOfClients.getInstance().getMarketMap().size());

            getLogger().info("add Market to market group");

        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        Channel incoming = ctx.channel();
        for (Channel channel : BrokerChannels) {
            channel.write("Server - " + incoming.remoteAddress() + " has disconnected!\n");
        }
        BrokerChannels.remove(ctx.channel());
    }

    @Override
    public void messageReceived(ChannelHandlerContext arg0, String msg) throws InterruptedException {

        TargetCompID targetCompID = new TargetCompID();
        this.parentValidator = new CheckSumValidate(msg);
        //Thread.currentThread().join();
        targetCompID.getAndSetValueFromString(msg);
        Channel incoming = arg0.channel();
        logger.info("Received message {}", msg);
        if (RouterNioHandler.checkIncomingClient(arg0)) {
            parentValidator.linkWith(new DestinationValidator(ListOfClients.getInstance().getBrokerMap(), ListOfClients.getInstance().getMarketMap()));

            if (ListOfClients.getInstance().getMarketMap().containsKey(0)) {

                Channel channel = ListOfClients.getInstance().getMarketMap().get(0);
                channel.write("[" + incoming.remoteAddress() + "] " + msg + "\n");
                channel.flush();
            /*for (Channel chanel : BrokerChannels) {
                if (chanel != incoming) {
                    chanel.write("[" + incoming.remoteAddress() + "] " + msg + "\n");
                    chanel.flush();
                }
            }*/
            }
            else { //TODO delete this else and correct validate in parentValidator.linkWith(new DestinationValidator(brokerMap, marketMap));

                logger.warn("Market with ID : {} doesn't exist", targetCompID.getValue());
            }

        }
        else {
            parentValidator.linkWith(new DestinationValidator(ListOfClients.getInstance().getMarketMap(), ListOfClients.getInstance().getBrokerMap()));//TODO return bool
            Channel channel = ListOfClients.getInstance().getBrokerMap().get(targetCompID.getValue());
            //Thread.sleep(1000);
            channel.write("[" + incoming.remoteAddress() + "] " + msg + "\n");
            channel.flush();

            /*
            for (Channel chanel : MarketChannels) {
                if (chanel != incoming) {
                    chanel.write("[" + incoming.remoteAddress() + "] " + msg + "\n");
                    chanel.flush();
                }
            }*/
        }

    }

    private void registerBroker(Channel incoming) {

        if (ListOfClients.getInstance().getBrokerMap().containsKey(brokerIndex)) {
            getLogger().info("Broker connected with id : {}", brokerIndex);
            return;
        }
        ListOfClients.getInstance().getBrokerMap().put(brokerIndex, incoming);
        getLogger().info("Router assigned Broker with ID = {}", brokerIndex);
        brokerIndex++;
        incoming.write("id" + Integer.toString(brokerIndex) + "\n");
        incoming.flush();

    }

    private void registerMarket(Channel incoming) {

        if (ListOfClients.getInstance().getMarketMap().containsKey(marketIndex)) {
            getLogger().info("Market connected with id : {}", marketIndex);
            return;
        }
        ListOfClients.getInstance().getMarketMap().put(marketIndex, incoming);
        getLogger().info("Router assigned Market with ID = {}", marketIndex);
        marketIndex++;
        incoming.write("id" + Integer.toString(marketIndex) + "\n");
        incoming.flush();
    }

    private static boolean checkIncomingClient(ChannelHandlerContext ctx) {

        Channel incoming = ctx.channel();
        InetSocketAddress incomingPort = (InetSocketAddress) incoming.localAddress();
        return incomingPort.getPort() == ClientPort.BROKERPORT.getPort();
    }
}
