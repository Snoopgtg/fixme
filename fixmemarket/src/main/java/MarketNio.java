import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import messages.*;
import nio.RouterClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.Future;

public class MarketNio extends RouterClient {

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//    private SellMessage sellMessage = MessageFactory.createSellMessage("2");
//    private BuyMessage buyMessage = MessageFactory.createBuyMessage("1");
//    String test = "[/127.0.0.1:34060] 8=FIX.4.0␁9=89␁35=D␁49=1␁56=1␁52=20190405-18:24:11.675␁11=ATOMNOCCC4655␁55=MSFT␁54=1␁40=2␁38=3␁44=54.87␁10=107␁";
//    private ExecutedMessage executedMessage = MessageFactory.createExecutedMessage("2", test);
//    private RejectedMessage rejectedMessage = MessageFactory.createRejectedMessage("1");
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void main(String[] args) throws Exception {
        new MarketNio("localhost", 5001).run();
    }

    private final String host;
    private final int port;

    public MarketNio(String host, int port) {

        this.host = host;
        this.port = port;
    }

    public void run() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new MarketNioInitializer());

            ChannelFuture futureMarket = bootstrap.connect(host, port);
//            Channel channel = bootstrap.connect(host, port).sync().channel();
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
//            channel.write(sellMessage.getMessage() + " waiting for masseges \r\n");
//            channel.flush();
            futureMarket.sync().channel().closeFuture().sync();

           /* while (true) {
                //channel.write(in.readLine() + " waiting for masseges \r\n");
                *//*channel.write(sellMessage.getMessage() + "\r\n");
                channel.write(buyMessage.getMessage() + "\r\n");
                channel.write(executedMessage.getMessage() + "\r\n");
                channel.write(rejectedMessage.getMessage() + "\r\n");
                channel.flush();*//*
            }*/
        }
        finally {
            group.shutdownGracefully();
        }
    }
}
