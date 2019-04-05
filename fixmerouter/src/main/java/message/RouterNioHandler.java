package message;


import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import nio.RouterClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.net.InetSocketAddress;

public class RouterNioHandler extends ChannelInboundMessageHandlerAdapter<String> {

    private static Logger getLogger() {
        return logger;
    }

    private static int brokerIndex;
    private static int marketIndex;
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final ChannelGroup BrokerChannels = new DefaultChannelGroup("brokers");
    private static final ChannelGroup MarketChannels = new DefaultChannelGroup("markets");

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        getLogger().info("Accepted a connection from {}", incoming.remoteAddress());
        if (RouterNioHandler.checkIncomingClient(ctx)) {
            for (Channel channel : BrokerChannels) {
                channel.write("Server - " + incoming.remoteAddress() + " has joined!\n");
            }
            brokerIndex++;
            BrokerChannels.add(ctx.channel());
            registerBroker(incoming);
            getLogger().info("add Broker to broker group");

        }
        else {
            for (Channel channel : MarketChannels) {
                channel.write("Server - " + incoming.remoteAddress() + " has joined!\n");
            }
            marketIndex++;
            MarketChannels.add(ctx.channel());
            registerMarket(incoming);
            getLogger().info("add Market to market group");

        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        for (Channel channel : BrokerChannels) {
            channel.write("Server - " + incoming.remoteAddress() + " has disconnected!\n");
        }
        BrokerChannels.remove(ctx.channel());
    }

    @Override
    public void messageReceived(ChannelHandlerContext arg0, String msg) throws Exception {

        Channel incoming = arg0.channel();
        if (RouterNioHandler.checkIncomingClient(arg0)) {
            for (Channel chanel : BrokerChannels) {
                if (chanel != incoming) {
                    chanel.write("[" + incoming.remoteAddress() + "] " + msg + "\n");
                }
            }
        }
        else {
            for (Channel chanel : MarketChannels) {
                if (chanel != incoming) {
                    chanel.write("[" + incoming.remoteAddress() + "] " + msg + "\n");
                }
            }
        }

    }

    private void registerBroker(Channel incoming) {

        incoming.write(Integer.toString(brokerIndex) + "\n");
        incoming.flush();
        getLogger().info("Router assigned Broker with ID = {}", brokerIndex);
    }

    private void registerMarket(Channel incoming) {

        incoming.write(Integer.toString(marketIndex) + "\n");
        incoming.flush();
        getLogger().info("Router assigned Market with ID = {}", marketIndex);
    }

    private static boolean checkIncomingClient(ChannelHandlerContext ctx) {

        Channel incoming = ctx.channel();
        InetSocketAddress incomingPort = (InetSocketAddress) incoming.localAddress();
        return incomingPort.getPort() == ClientPort.BROKERPORT.getPort();
    }
}
