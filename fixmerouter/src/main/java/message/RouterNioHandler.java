package message;


import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.net.InetSocketAddress;

public class RouterNioHandler extends ChannelInboundMessageHandlerAdapter<String> {

    private static Logger getLogger() {
        return logger;
    }

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
            BrokerChannels.add(ctx.channel());
            getLogger().info("add Broker to broker group");

        }
        else {
            for (Channel channel : MarketChannels) {
                channel.write("Server - " + incoming.remoteAddress() + " has joined!\n");
                channel.write("Server a\n");
            }
            MarketChannels.add(ctx.channel());
            getLogger().info("add Market to market group");
            System.out.println("id = " + ctx.channel().id());

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
    private static boolean checkIncomingClient(ChannelHandlerContext ctx) {

        Channel incoming = ctx.channel();
        InetSocketAddress incomingPort = (InetSocketAddress) incoming.localAddress();
        return incomingPort.getPort() == ClientPort.BROKERPORT.getPort();
    }
}
