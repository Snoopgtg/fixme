package message;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class RouterNio {

    public static void main(String[] args) throws InterruptedException {
       RouterNio routerNio = new RouterNio(5000);
       RouterNio routerMarket = new RouterNio(5001);

        Thread threadRouterBroker = new Thread();
        routerNio.run();
        Thread threadRouterMarket = new Thread();
        routerMarket.run();
//        Thread.currentThread().join();
    }

    private final int port;

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

            bootstrap.bind(port).sync().channel().closeFuture().sync();
        }
        finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
