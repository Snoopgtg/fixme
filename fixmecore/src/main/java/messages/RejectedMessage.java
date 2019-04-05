package messages;

import MessageBody.RefSeqNum;
import MessageBody.StandardMessageHeader;
import MessageBody.StandardMessageTrailer;

public class RejectedMessage {
    private StandardMessageHeader standardMessageHeader;
    private RefSeqNum refSeqNum;
    private StandardMessageTrailer standardMessageTrailer;

    public RejectedMessage(StandardMessageHeader standardMessageHeader, RefSeqNum refSeqNum, StandardMessageTrailer standardMessageTrailer) {
        this.standardMessageHeader = standardMessageHeader;
        this.refSeqNum = refSeqNum;
        this.standardMessageTrailer = standardMessageTrailer;
    }
}
