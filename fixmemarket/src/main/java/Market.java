import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.log4j.BasicConfigurator;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import static MessageBody.EnumMessageBody.ClientPort.MARKETPORT;

public class Market {

    public static void main(String[] args) {
        BasicConfigurator.configure();
        try {
            new Market("localhost", MARKETPORT.getPort()).run();
        } catch (Exception e) {
            System.out.println("ERROR: You have first start router\n" + e);
            System.exit(0);
        }
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
            futureMarket.sync().channel().closeFuture().sync();
        }
        finally {
            group.shutdownGracefully();
        }
    }
}
