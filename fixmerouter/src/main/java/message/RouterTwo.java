package message;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class RouterTwo implements Runnable{
    public RouterTwo() {
    }


    @Override
    public void run() {
        try (AsynchronousServerSocketChannel server =
                     AsynchronousServerSocketChannel.open()) {
            Thread router = new Thread();
            router.start();
            String host = "localhost";
            int port = 5001;
            InetSocketAddress sAddr = new InetSocketAddress(host, port);
            server.bind(sAddr);
            System.out.format("Router - Market side started. Accepting connections\n");
            Future<AsynchronousSocketChannel> acceptCon =
                    server.accept();
            AsynchronousSocketChannel client = acceptCon.get(60,
                    TimeUnit.SECONDS);
            if ((client!= null) && (client.isOpen())) {
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                Future<Integer> readval = client.read(buffer);
                System.out.println("Received from client: "
                        + new String(buffer.array()).trim());
                readval.get();
                buffer.flip();
                String str= "I'm Router - Market. Thank you!";
                Future<Integer> writeVal = client.write(
                        ByteBuffer.wrap(str.getBytes()));
                System.out.println("Writing back to client: "
                        +str);
                writeVal.get();
                buffer.clear();
            }
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
