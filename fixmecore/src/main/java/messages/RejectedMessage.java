package messages;

import MessageBody.CheckSum;
import MessageBody.RefSeqNum;
import MessageBody.StandardMessageHeader;

public class RejectedMessage {
    private StandardMessageHeader standardMessageHeader;
    private RefSeqNum refSeqNum;
    private CheckSum checkSum = new CheckSum();

    public RejectedMessage(StandardMessageHeader standardMessageHeader, RefSeqNum refSeqNum) {
        this.standardMessageHeader = standardMessageHeader;
        this.refSeqNum = refSeqNum;
        this.standardMessageHeader.getBodyLength().calculateAndSetLengthOfBytesInMessage(this.getMessage());
        this.checkSum.calculateAndSetCheckSumInMessage(this.getMessage());

    }

    public String getMessage() {
        return standardMessageHeader.toString() + refSeqNum.toString() + checkSum.toString();
    }
}
