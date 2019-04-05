import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import messages.MessageFactory;
import messages.SellMessage;
import singlton.MessageSinglton;

public class MarketNioHandler extends ChannelInboundMessageHandlerAdapter<String> {

    private SellMessage sellMessage;
    private String marketIndex;
    private ChannelHandlerContext ctx;
    @Override
    public void messageReceived(ChannelHandlerContext ctx, String stringFromRouter) throws Exception {
        //System.out.println(arg1);
        marketIndex = stringFromRouter;
        this.ctx = ctx;
        setMarketIndex(stringFromRouter);


    }

    public void setMarketIndex(String index) {

        System.out.println(index);
        sellMessage = MessageFactory.createSellMessage(index);
//        ctx.channel().write(sellMessage.getMessage() + " waiting for masseges \r\n");
//        ctx.channel().flush();
         /*while (true) {
                //channel.write(in.readLine() + " waiting for masseges \r\n");
             ctx.channel().write(sellMessage.getMessage() + " waiting for masseges \r\n");
             ctx.channel().flush();
            }*/

    }



}