package MessageBody;

public class SenderCompID extends MessageParent{

    public SenderCompID(String senderId) {

        super(senderId, 49);
    }

    public SenderCompID() {

        super(0, 49);
    }
    @Override
    public String toString() {
        return (this.tag.toString() + "=" + String.format("%06d", Integer.parseInt(this.value.toString())) + (char)1).replace((char)1, '\u2401');
    }
}

