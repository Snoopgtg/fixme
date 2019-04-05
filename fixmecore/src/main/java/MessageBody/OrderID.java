package MessageBody;

import java.util.Random;

public class OrderID extends MessageParent{

    public OrderID() {

        super(37);
        super.setValue(setOrderID());
    }

    private String setOrderID() {
        int random = new Random().nextInt(99);
        return Integer.toString(random);
    }
}

