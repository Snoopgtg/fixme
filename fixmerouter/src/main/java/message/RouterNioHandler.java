package message;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;

public class RouterNioHandler extends ChannelInboundMessageHandlerAdapter<String> {

    private static final ChannelGroup channels = new DefaultChannelGroup();
    private static final ChannelGroup channelsMarket = new DefaultChannelGroup();

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        Channel incomingM = ctx.channel();
        for (Channel channel : channels) {
            channel.write("Server - " + incoming.remoteAddress() + " has joined!\n");
            channel.flush();
        }
        for (Channel channel : channelsMarket) {
            channelsMarket.write("Server - " + incoming.remoteAddress() + " has joined!\n");
            channelsMarket.flush();
        }
        channels.add(ctx.channel());
        channelsMarket.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        for (Channel channel : channels) {
            channel.write("Server - " + incoming.remoteAddress() + " has disconnected!\n");
            channel.flush();

        }
        channels.remove(ctx.channel());
    }

    @Override
    public void messageReceived(ChannelHandlerContext arg0, String msg) throws Exception {

        Channel incoming = arg0.channel();
        for (Channel channel : channels) {
            if (channel != incoming) {
                channel.write("[" + incoming.remoteAddress() + "] " + msg + "\n");
                channel.flush();
            }
        }

    }
}
