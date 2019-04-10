package MessageBody;

import nio.ListOfClients;

import java.util.Random;

public class TargetCompID extends MessageParent{

    public TargetCompID(Integer value) {

        super(value, 34);
    }

    public TargetCompID() {

        super(0,34);

    }

    @Override
    public String toString() {
        return (this.tag.toString() + "=" + String.format("%06d", Integer.parseInt(this.value.toString())) + (char)1).replace((char)1, '\u2401');
    }
}
