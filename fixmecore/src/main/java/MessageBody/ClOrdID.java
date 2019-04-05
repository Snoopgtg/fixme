package MessageBody;

import java.util.Random;

public class ClOrdID extends MessageParent{

    public ClOrdID() {

        super(11);
        super.setValue(setID());
    }

    private String setID() {
        int random = new Random().nextInt(9999);
        return "ATOMNOCCC" + Integer.toString(random);
    }
}

