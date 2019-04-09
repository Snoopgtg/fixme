package MessageBody;

import messages.Message;

public class StandardMessageHeader implements Message {
    private BeginString beginString; //FIX.4.0
    private BodyLength bodyLength = new BodyLength(); //Valid values: '0' ... '9999'
    private MsgType msgType;
    private SenderCompID senderCompID;
    private TargetCompID targetCompID; //router will send target with sender
    private MsgSeqNum msgSeqNum;
    private SendingTime sendingTime;

    public StandardMessageHeader(BeginString beginString, MsgType msgType, SenderCompID senderCompID, TargetCompID targetCompID, MsgSeqNum msgSeqNum, SendingTime sendingTime) {
        this.beginString = beginString;
        this.msgType = msgType;
        this.senderCompID = senderCompID;
        this.targetCompID = targetCompID;
        this.msgSeqNum = msgSeqNum;
        this.sendingTime = sendingTime;
    }

    public String toString(){
       return beginString.toString() + bodyLength.toString() + msgType.toString() + senderCompID.toString() + targetCompID.toString() + msgSeqNum.toString() + sendingTime.toString();
    }

    public BodyLength getBodyLength() {
        return bodyLength;
    }
}
