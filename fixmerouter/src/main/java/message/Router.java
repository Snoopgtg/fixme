package message;

//TODO Use the java executor framework for message handling.
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Router {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static Logger getLogger() {
        return logger;
    }
    private final int portForBroker;
    private final int portForMarket;

    public static void main(String[] args) throws InterruptedException {

        /*String ttt = "[/127.0.0.1:57220] 8=FIX.4.0␁35=A␁49=SERVER␁56=CLIENT␁34=177␁52=20090107-18:15:16␁98=0␁108=30␁10=062␁";
        ttt = ttt.replace('␁', '|');
        ttt = ttt.substring(ttt.indexOf("35"));
        ttt = ttt.substring(0, ttt.lastIndexOf("10"));
        byte[] b = ttt.getBytes();
        System.out.println(ttt.getBytes().length);*/
        new Router(5000, 5001).run();
    }


    public Router(int portForBroker, int portForMarket) {
        this.portForBroker = portForBroker;
        this.portForMarket = portForMarket;
    }


    public void run() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup(2);
        EventLoopGroup workerGroup = new NioEventLoopGroup(10);

        try {
            ServerBootstrap bootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new RouterNioInitializer());
            ChannelFuture futureBrocker = bootstrap.bind(portForBroker);
            logger.info("- Broker side started. Accepting connections. Listening at {}", portForBroker);
            ChannelFuture futureMarket = bootstrap.bind(portForMarket);
            logger.info("- Market side started. Accepting connections. Listening at {}", portForMarket);
            futureBrocker.sync().channel().closeFuture().sync();
            futureMarket.sync().channel().closeFuture().sync();
        }
        finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}

/*
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.awt.*;
import java.util.ArrayList;

public class RouterNio {

    public static void main(String[] args) */
/*throws InterruptedException*//*
 {
       RouterNio routerNio = new RouterNio(5000);
       RouterNio routerMarket = new RouterNio(5001);

    }

    private final int port;
    private List<ChannelFuture> futureBrocker = new ArrayList<Ch>();

    public RouterNio(int port) {
        this.port = port;
    }

    public void run() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup(2);
        EventLoopGroup workerGroup = new NioEventLoopGroup(10);

        try {
            ServerBootstrap bootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new RouterNioInitializer());

            System.out.println("The server at port " + port + " is ready.");
            bootstrap.bind(port).sync().channel().closeFuture().sync();
        }
        finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
*/
