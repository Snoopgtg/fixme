import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.concurrent.Future;
//from   w w  w  . j av a 2 s  .  c om
public class Market {
    public static void main(String[] args) throws Exception {
        AsynchronousSocketChannel channel = AsynchronousSocketChannel.open();
        SocketAddress serverAddr = new InetSocketAddress("localhost", 5000);
        Future<Void> result = channel.connect(serverAddr);
        result.get();
        System.out.println("Connected");
        Attachmente attach = new Attachmente();
        attach.channel = channel;
        attach.buffer = ByteBuffer.allocate(2048);
        attach.isRead = false;
        attach.mainThread = Thread.currentThread();

        Charset cs = Charset.forName("UTF-8");
        String msg = "Hello";
        byte[] data = msg.getBytes(cs);
        attach.buffer.put(data);
        attach.buffer.flip();

        ReadWriteHandlere readWriteHandler = new ReadWriteHandlere();
        channel.write(attach.buffer, attach, readWriteHandler);
        attach.mainThread.join();
    }
}
class Attachmente {
    AsynchronousSocketChannel channel;
    ByteBuffer buffer;
    Thread mainThread;
    boolean isRead;
}
class ReadWriteHandlere implements CompletionHandler<Integer, Attachmente> {
    @Override
    public void completed(Integer result, Attachmente attach) {
        if (attach.isRead) {
            attach.buffer.flip();
            Charset cs = Charset.forName("UTF-8");
            int limits = attach.buffer.limit();
            byte bytes[] = new byte[limits];
            attach.buffer.get(bytes, 0, limits);
            String msg = new String(bytes, cs);
            System.out.format("Server Responded: "+ msg);
            try {
                msg = this.getTextFromUser();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (msg.equalsIgnoreCase("bye")) {
                attach.mainThread.interrupt();
                return;
            }
            attach.buffer.clear();
            byte[] data = msg.getBytes(cs);
            attach.buffer.put(data);
            attach.buffer.flip();
            attach.isRead = false; // It is a write
            attach.channel.write(attach.buffer, attach, this);
        }else {
            attach.isRead = true;
            attach.buffer.clear();
            attach.channel.read(attach.buffer, attach, this);
        }
    }
    @Override
    public void failed(Throwable e, Attachmente attach) {
        e.printStackTrace();
    }
    private String getTextFromUser() throws Exception{
        System.out.print("Please enter a  message  (Bye  to quit):");
        BufferedReader consoleReader = new BufferedReader(
                new InputStreamReader(System.in));
        String msg = consoleReader.readLine();
        return msg;
    }
}