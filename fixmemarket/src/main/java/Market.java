import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import static MessageBody.EnumMessageBody.ClientPort.MARKETPORT;

public class Market {

    public static void main(String[] args) throws Exception {
        new Market("localhost", MARKETPORT.getPort()).run();
    }

    private final String host;
    private final int port;

    public Market(String host, int port) {

        this.host = host;
        this.port = port;
    }

    public void run() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new MarketInitializer());

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
