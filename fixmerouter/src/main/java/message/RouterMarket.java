/*
package message;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RouterMarket implements Runnable {
    public RouterMarket() {

    }

    private final Logger logger = LoggerFactory.getLogger(RouterMarket.class.getSimpleName());

    @Override
    public void run()  {
        AsynchronousServerSocketChannel server = null;//w w w  .  j  a  v  a2s .com
        try {
            server = AsynchronousServerSocketChannel
                    .open();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String host = "localhost";
        int port = 5001;
        InetSocketAddress sAddr = new InetSocketAddress(host, port);
        try {
            server.bind(sAddr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("side started. Accepting connections. Listening at {}", sAddr);
        //System.out.format("Server - Market is listening at %s%n", sAddr);
        AttachmentMarket attach = new AttachmentMarket();
        attach.server = server;
        server.accept(attach, new ConnectionHandlerMarket());
       */
/* try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*//*

    }
}
class AttachmentMarket {
    AsynchronousServerSocketChannel server;
    AsynchronousSocketChannel client;
    ByteBuffer buffer;
    SocketAddress clientAddr;
    boolean isRead;
}

class ConnectionHandlerMarket implements
        CompletionHandler<AsynchronousSocketChannel, AttachmentMarket> {
    @Override
    public void completed(AsynchronousSocketChannel client, AttachmentMarket attach) {
        try {
            final Logger log = LoggerFactory.getLogger(RouterMarket.class.getSimpleName());

            SocketAddress clientAddr = client.getRemoteAddress();
            log.info("Accepted a  connection from  {}", clientAddr);
            //System.out.format("Router - Market : Accepted a  connection from  %s%n", clientAddr);
            attach.server.accept(attach, this);
            ReadWriteHandlerMarket rwHandler = new ReadWriteHandlerMarket();
            AttachmentMarket newAttach = new AttachmentMarket();
            newAttach.server = attach.server;
            newAttach.client = client;
            newAttach.buffer = ByteBuffer.allocate(2048);
            newAttach.isRead = true;
            newAttach.clientAddr = clientAddr;
            client.read(newAttach.buffer, newAttach, rwHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void failed(Throwable e, AttachmentMarket attach) {
        final Logger log = LoggerFactory.getLogger(RouterMarket.class.getSimpleName());
        log.error("Failed to accept a connection.");
        //System.out.println("Failed to accept a  connection.");
        e.printStackTrace();
    }
}

class ReadWriteHandlerMarket implements CompletionHandler<Integer, AttachmentMarket> {
    @Override
    public void completed(Integer result, AttachmentMarket attach) {
        final Logger log = LoggerFactory.getLogger(RouterMarket.class.getSimpleName());

        if (result == -1) {
            try {
                attach.client.close();
                log.info("Stop listening to the client {}", attach.clientAddr);
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
            log.info("Market at {} says: {}", attach.clientAddr, msg);
            //System.out.format("Market at  %s  says: %s%n", attach.clientAddr, msg);
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
    public void failed(Throwable e, AttachmentMarket attach) {
        e.printStackTrace();
    }
}*/
