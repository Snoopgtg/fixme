package messages;

import MessageBody.RefSeqNum;
import MessageBody.StandardMessageHeader;

public class RejectedMessage {
    private StandardMessageHeader standardMessageHeader;
    private RefSeqNum refSeqNum;

    public RejectedMessage(StandardMessageHeader standardMessageHeader, RefSeqNum refSeqNum) {
        this.standardMessageHeader = standardMessageHeader;
        this.refSeqNum = refSeqNum;
    }
}
