package message;

import io.netty.channel.EventLoopGroup;


public class RouterStopper {
    private static RouterStopper ourInstance = new RouterStopper();
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    public static RouterStopper getInstance() {
        return ourInstance;
    }

    private RouterStopper() {
    }

    public void stop() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    public void setBossGroup(EventLoopGroup bossGroup) {
        this.bossGroup = bossGroup;
    }

    public void setWorkerGroup(EventLoopGroup workerGroup) {
        this.workerGroup = workerGroup;
    }
}
