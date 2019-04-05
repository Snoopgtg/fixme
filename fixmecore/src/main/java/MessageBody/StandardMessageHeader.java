package MessageBody;

import messages.Message;

public class StandardMessageHeader implements Message {
    private BeginString beginString; //FIX.4.0
    private BodyLength bodyLength = new BodyLength(); //Valid values: '0' ... '9999'
    private MsgType msgType;
    private SenderCompID senderCompID;
//    private TargetCompID targetCompID;
    private MsgSeqNum msgSeqNum;
    private SendingTime sendingTime;

    public StandardMessageHeader(BeginString beginString, MsgType msgType, SenderCompID senderCompID/* TargetCompID targetCompID*/, MsgSeqNum msgSeqNum, SendingTime sendingTime) {
        this.beginString = beginString;
        this.msgType = msgType;
        this.senderCompID = senderCompID;
//        this.targetCompID = targetCompID;
        this.msgSeqNum = msgSeqNum;
        this.sendingTime = sendingTime;
    }

    public String toString(){
       return beginString.toString() + bodyLength.toString() + msgType.toString() + senderCompID.toString() + msgSeqNum.toString() + sendingTime.toString();
    }

    public BodyLength getBodyLength() {
        return bodyLength;
    }
}
