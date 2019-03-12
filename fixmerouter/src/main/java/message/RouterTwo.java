
package message;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
public class RouterTwo implements Runnable {
    public RouterTwo() {

    }
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
        int port = 5000;
        InetSocketAddress sAddr = new InetSocketAddress(host, port);
        try {
            server.bind(sAddr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.format("Server - Broker is listening at %s%n", sAddr);
        Attachment attach = new Attachment();
        attach.server = server;
        server.accept(attach, new ConnectionHandler());
//        Thread.currentThread().join();
    }
}
class Attachment {
    AsynchronousServerSocketChannel server;
    AsynchronousSocketChannel client;
    ByteBuffer buffer;
    SocketAddress clientAddr;
    boolean isRead;
}

class ConnectionHandler implements
        CompletionHandler<AsynchronousSocketChannel, Attachment> {
    @Override
    public void completed(AsynchronousSocketChannel client, Attachment attach) {
        try {
            SocketAddress clientAddr = client.getRemoteAddress();
            System.out.format("Server - Broker : Accepted a  connection from  %s%n", clientAddr);
            attach.server.accept(attach, this);
            ReadWriteHandler rwHandler = new ReadWriteHandler();
            Attachment newAttach = new Attachment();
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
    public void failed(Throwable e, Attachment attach) {
        System.out.println("Failed to accept a  connection.");
        e.printStackTrace();
    }
}

class ReadWriteHandler implements CompletionHandler<Integer, Attachment> {
    @Override
    public void completed(Integer result, Attachment attach) {
        if (result == -1) {
            try {
                attach.client.close();
                System.out.format("Stopped   listening to the   client %s%n",
                        attach.clientAddr);
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
            System.out.format("Client at  %s  says: %s%n", attach.clientAddr,
                    msg);
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
    public void failed(Throwable e, Attachment attach) {
        e.printStackTrace();
    }
}
/*
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
public class Router {
    public static void main(String[] args) throws Exception {
        AsynchronousServerSocketChannel serverBroker = AsynchronousServerSocketChannel.open();
        AsynchronousServerSocketChannel serverMarket = AsynchronousServerSocketChannel
                .open();//w w w  .  j  a  v  a2s .co// m
        String host = "localhost";
        int portBroker = 5000;
        int portMarket = 5001;
        InetSocketAddress sAddrBroker = new InetSocketAddress(host, portBroker);
        InetSocketAddress sAddrMarket = new InetSocketAddress(host, portMarket);
        serverBroker.bind(sAddrBroker);
        serverMarket.bind(sAddrMarket);
        System.out.format("Server is listening at %s%n", sAddrBroker);
        System.out.format("Server is listening at %s%n", sAddrMarket);

        AttachmentBroker attachBroker = new AttachmentBroker();
        AttachmentMarket attachMarket = new AttachmentMarket();
        attachBroker.server = serverBroker;
        attachMarket.server = serverMarket;
        serverBroker.accept(attachBroker, new ConnectionHandler());
        serverMarket.accept(attachMarket, new ConnectionHandler());
        Thread.currentThread().join();
    }
}
class AttachmentBroker {
    AsynchronousServerSocketChannel server;
    AsynchronousSocketChannel client;
    ByteBuffer buffer;
    SocketAddress clientAddr;
    boolean isRead;
}

class Attachment {
    AsynchronousServerSocketChannel server;
    AsynchronousSocketChannel client;
    ByteBuffer buffer;
    SocketAddress clientAddr;
    boolean isRead;
}

class ConnectionHandler implements
        CompletionHandler<AsynchronousSocketChannel, Attachment> {
    @Override
    public void completed(AsynchronousSocketChannel client, AttachmentBroker attach) {
        try {
            SocketAddress clientAddr = client.getRemoteAddress();
            System.out.format("Accepted a  connection from  %s%n", clientAddr);
            attach.server.accept(attach, this);
            ReadWriteHandler rwHandler = new ReadWriteHandler();
            AttachmentBroker newAttach = new AttachmentBroker();
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
}

    class ConnectionHandler implements
            CompletionHandler<AsynchronousSocketChannel, Attachment> {
        @Override
        public void completed(AsynchronousSocketChannel client, AttachmentMarket attach) {
            try {
                SocketAddress clientAddr = client.getRemoteAddress();
                System.out.format("Accepted a  connection from  %s%n", clientAddr);
                attach.server.accept(attach, this);
                ReadWriteHandler rwHandler = new ReadWriteHandler();
                Attachment newAttach = new Attachment();
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
}

        @Override
    public void failed(Throwable e, Attachment attach) {
        System.out.println("Failed to accept a  connection.");
        e.printStackTrace();
    }
}

class ReadWriteHandler implements CompletionHandler<Integer, Attachment> {
    @Override
    public void completed(Integer result, Attachment attach) {
        if (result == -1) {
            try {
                attach.client.close();
                System.out.format("Stopped   listening to the   client %s%n",
                        attach.clientAddr);
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
            System.out.format("Client at  %s  says: %s%n", attach.clientAddr,
                    msg);
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
    public void failed(Throwable e, Attachment attach) {
        e.printStackTrace();
    }
}}*/
