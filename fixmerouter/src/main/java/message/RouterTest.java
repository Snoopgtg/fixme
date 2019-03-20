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

public class RouterTest {
    public static void main(String[] args) throws InterruptedException {
        RouterBroker routerBroker = new RouterBroker();
        RouterMarket routerMarket = new RouterMarket();
//        routerBroker.run();
//        routerMarket.run();

//        ExecutorService executorService = Executors.newFixedThreadPool(2);
//        executorService.submit(routerMarket);
//        executorService.submit(routerBroker);

        Thread threadRouterBroker = new Thread(routerBroker);
        routerBroker.run();
        Thread threadRouterMarket = new Thread(routerMarket);
        routerMarket.run();
        Thread.currentThread().join();


    }

    private static final Logger logger = LoggerFactory.getLogger(RouterTest.class.getName());

    public static Logger getLogger() {
        return logger;
    }

    static class RouterBroker implements Runnable {

        public RouterBroker() {
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
       /* try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        }
    }
    static class ConnectionAttachment {
        AsynchronousServerSocketChannel server;
        AsynchronousSocketChannel client;
        ByteBuffer buffer;
        SocketAddress clientAddr;
        boolean isRead;
    }

    static class BrokerConnectionHandler implements
            CompletionHandler<AsynchronousSocketChannel, ConnectionAttachment> {
        @Override
        public void completed(AsynchronousSocketChannel client, ConnectionAttachment attach) {
            try {
                SocketAddress clientAddr = client.getRemoteAddress();
                RouterTest.getLogger().info("Accepted a connection from", clientAddr);
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
                RouterTest.getLogger().error("Could not establish connection with client", e);
            }
        }

        @Override
        public void failed(Throwable e, ConnectionAttachment attach) {
            RouterTest.getLogger().info("Failed to accept a connection", e);
        }
    }

    static class ReadWriteHandler implements CompletionHandler<Integer, ConnectionAttachment> {
        @Override
        public void completed(Integer result, ConnectionAttachment attach) {
            if (result == -1) {
                try {
                    attach.client.close();
                    RouterTest.getLogger().info("Stopped listening to the client ", attach.clientAddr);
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
                RouterTest.getLogger().info("Client at {} says: {}", attach.clientAddr, msg);
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
            RouterTest.getLogger().error("Messaging error", e);
        }
    }

    static class RouterMarket implements Runnable {

        public RouterMarket() {
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
       /* try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        }
    }
    static class AttachmentMarket {
        AsynchronousServerSocketChannel server;
        AsynchronousSocketChannel client;
        ByteBuffer buffer;
        SocketAddress clientAddr;
        boolean isRead;
    }

    static class ConnectionHandlerMarket implements
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

    static class ReadWriteHandlerMarket implements CompletionHandler<Integer, AttachmentMarket> {
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
    }
}

