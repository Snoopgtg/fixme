package MessageBody;

import java.util.Random;

public class RefSeqNum extends MessageParent{

    public RefSeqNum() {

        super(45);
        super.setValue(setRefSeqNum());
    }

    private String setRefSeqNum() {
        int random = new Random().nextInt(3);
        return Integer.toString(random);
    }
}

