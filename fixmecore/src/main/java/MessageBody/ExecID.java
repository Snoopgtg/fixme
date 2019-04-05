package MessageBody;

import java.util.Random;

public class ExecID extends MessageParent{

    public ExecID() {

        super(17);
        super.setValue(setExecID());
    }

    private String setExecID() {
        int random = new Random().nextInt(99);
        return Integer.toString(random);
    }
}

