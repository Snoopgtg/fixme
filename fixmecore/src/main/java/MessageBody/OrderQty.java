package MessageBody;

import java.util.Random;

public class OrderQty extends MessageParent{

    public OrderQty() {

        super(38);
        super.setValue(setOrderQty());
    }

    private String setOrderQty() {
        int random = new Random().nextInt(15);
        return Integer.toString(random);
    }
}

