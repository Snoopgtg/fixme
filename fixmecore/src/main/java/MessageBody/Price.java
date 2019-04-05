package MessageBody;

import java.util.Random;

public class Price extends MessageParent{

    public Price() {

        super(44);
        super.setValue(setPrice());
    }

    private String setPrice() {
        float generatedFloat = new Random().nextFloat() * (99 - 1);
        return String.format("%.02f",generatedFloat);
    }
}

