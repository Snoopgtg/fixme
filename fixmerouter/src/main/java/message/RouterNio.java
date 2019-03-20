package message;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class RouterNio {

    private final int portForBrocker;
    private final int portForMarket;

    public static void main(String[] args) throws InterruptedException {
        new RouterNio(5000, 5001).run();

    }


    public RouterNio(int portForBrocker, int portForMarket) {
        this.portForBrocker = portForBrocker;
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
            System.out.println("The server at port " + portForBrocker + " is ready.");
            ChannelFuture futureBrocker = bootstrap.bind(portForBrocker);
            System.out.println("The server at port " + portForMarket + " is ready.");
            ChannelFuture futureMarket = bootstrap.bind(portForMarket);
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
