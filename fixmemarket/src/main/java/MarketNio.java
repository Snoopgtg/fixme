import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import messages.BuyMessage;
import messages.MessageFactory;
import messages.SellMessage;
import nio.RouterClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MarketNio extends RouterClient {

    private SellMessage sellMessage = MessageFactory.createSellMessage("2");
    private BuyMessage buyMessage = MessageFactory.createBuyMessage("1");

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

            Channel channel = bootstrap.connect(host, port).sync().channel();
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
//            channel.write(sellMessage.getMessage() + " waiting for masseges \r\n");
//            channel.flush();

            while (true) {
                //channel.write(in.readLine() + " waiting for masseges \r\n");
                channel.write(sellMessage.getMessage() + "\r\n");
                channel.write(buyMessage.getMessage() + "\r\n");
                channel.flush();
            }
        }
        finally {
            group.shutdownGracefully();
        }
    }
}
