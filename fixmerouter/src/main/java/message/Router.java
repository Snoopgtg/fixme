package message;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

import static MessageBody.EnumMessageBody.ClientPort.BROKERPORT;
import static MessageBody.EnumMessageBody.ClientPort.MARKETPORT;

public class Router {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static Logger getLogger() {
        return logger;
    }
    private final int portForBroker;
    private final int portForMarket;

    public static void main(String[] args) throws InterruptedException {
        BasicConfigurator.configure();
        new Router(BROKERPORT.getPort(), MARKETPORT.getPort()).run();
    }

    public Router(int portForBroker, int portForMarket) {
        this.portForBroker = portForBroker;
        this.portForMarket = portForMarket;
    }


    public void run() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup(2);
        EventLoopGroup workerGroup = new NioEventLoopGroup(10);

        RouterStopper.getInstance().setBossGroup(bossGroup);
        RouterStopper.getInstance().setWorkerGroup(workerGroup);
        try {
            ServerBootstrap bootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new RouterNioInitializer());
            ChannelFuture futureBroker = bootstrap.bind(portForBroker);
            logger.info("- Broker side started. Accepting connections. Listening at {}", portForBroker);
            ChannelFuture futureMarket = bootstrap.bind(portForMarket);
            logger.info("- Market side started. Accepting connections. Listening at {}", portForMarket);

            futureBroker.sync().channel().closeFuture().sync();
            futureMarket.sync().channel().closeFuture().sync();
        }
        finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
