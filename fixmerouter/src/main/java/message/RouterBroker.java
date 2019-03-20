/*

package message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;

public class RouterBroker implements Runnable {
    public RouterBroker() {

    }

    private static final Logger logger = LoggerFactory.getLogger(RouterBroker.class.getName());

    public static Logger getLogger() {
        return logger;
    }

    @Override
    public void run()  {
        AsynchronousServerSocketChannel server = null;
        try {
            server = AsynchronousServerSocketChannel.open();
        } catch (IOException e) {
            logger.error("Messaging error with open server", e);
        }
        String host = "localhost";
        int port = 5000;
        InetSocketAddress sAddr = new InetSocketAddress(host, port);
        try {
            server.bind(sAddr);
        } catch (IOException e) {
            logger.error("Messaging error with bind server", e);
        }
        logger.info("side started. Accepting connections. Listening at {}", sAddr);
        //System.out.format("Server - Broker is listening at %s%n", sAddr);
        ConnectionAttachment attach = new ConnectionAttachment();
        attach.server = server;
        server.accept(attach, new BrokerConnectionHandler());
       */
/* try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*//*

    }
}
class ConnectionAttachment {
    AsynchronousServerSocketChannel server;
    AsynchronousSocketChannel client;
    ByteBuffer buffer;
    SocketAddress clientAddr;
    boolean isRead;
}

class BrokerConnectionHandler implements
        CompletionHandler<AsynchronousSocketChannel, ConnectionAttachment> {
    @Override
    public void completed(AsynchronousSocketChannel client, ConnectionAttachment attach) {
        try {
            SocketAddress clientAddr = client.getRemoteAddress();
            RouterBroker.getLogger().info("Accepted a connection from", clientAddr);
            //System.out.format("Server - Broker : Accepted a  connection from  %s%n", clientAddr);
            attach.server.accept(attach, this);
            //TODO registerBroker(client);
            ReadWriteHandler rwHandler = new ReadWriteHandler();
            ConnectionAttachment newAttach = new ConnectionAttachment();
            newAttach.server = attach.server;
            newAttach.client = client;
            newAttach.buffer = ByteBuffer.allocate(2048);
            newAttach.isRead = true;
            newAttach.clientAddr = clientAddr;
            client.read(newAttach.buffer, newAttach, rwHandler);
        } catch (IOException e) {
            RouterBroker.getLogger().error("Could not establish connection with client", e);
        }
    }

    @Override
    public void failed(Throwable e, ConnectionAttachment attach) {
        RouterBroker.getLogger().info("Failed to accept a connection", e);
    }
}

class ReadWriteHandler implements CompletionHandler<Integer, ConnectionAttachment> {
    @Override
    public void completed(Integer result, ConnectionAttachment attach) {
        if (result == -1) {
            try {
                attach.client.close();
                RouterBroker.getLogger().info("Stopped listening to the client ", attach.clientAddr);
//                System.out.format("Stopped   listening to the   client %s%n", attach.clientAddr);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return;
        }

        if (attach.isRead) {
            attach.buffer.flip();
            int limits = attach.buffer.limit();
            byte bytes[] = new byte[limits];
            attach.buffer.get(bytes, 0, limits);
            Charset cs = Charset.forName("UTF-8");
            String msg = new String(bytes, cs);
            RouterBroker.getLogger().info("Client at {} says: {}", attach.clientAddr, msg);
//            System.out.format("Client at  %s  says: %s%n", attach.clientAddr, msg);
            attach.isRead = false; // It is a write
            attach.buffer.rewind();

        } else {
            // Write to the client
            attach.client.write(attach.buffer, attach, this);
            attach.isRead = true;
            attach.buffer.clear();
            attach.client.read(attach.buffer, attach, this);
        }
    }

    @Override
    public void failed(Throwable e, ConnectionAttachment attach) {
        RouterBroker.getLogger().error("Messaging error", e);
    }
}*/
