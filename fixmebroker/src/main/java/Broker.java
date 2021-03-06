import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.log4j.BasicConfigurator;

import static MessageBody.EnumMessageBody.ClientPort.BROKERPORT;

public class Broker {

    public static void main(String[] args) {
        BasicConfigurator.configure();
        try {
            new Broker("localhost", BROKERPORT.getPort()).run();
        } catch (Exception e) {
            System.out.println("ERROR: You have to start router and market\n" + e);
            System.exit(0);
        }
    }

    private final String host;
    private final int port;

    public Broker(String host, int port) {

        this.host = host;
        this.port = port;
    }

    public void run() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new BrokerInitializer());

            ChannelFuture futureBroker = bootstrap.connect(host, port);
            futureBroker.sync().channel().closeFuture().sync();
        }
        finally {
            group.shutdownGracefully();
        }
    }
}
