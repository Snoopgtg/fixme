package MessageBody;

public class MsgSeqNum extends MessageParent {

    private static int messageNumber;
    public MsgSeqNum() {

        super(56);
        super.setValue(messageNumber++);
    }
}

//TODO imlements from message?
