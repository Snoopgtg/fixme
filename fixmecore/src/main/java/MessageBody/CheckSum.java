package MessageBody;

public class CheckSum extends MessageParent{
    private int length;

    public CheckSum(Integer value) {

        super(value, 10);
    }

    @Override
    public String toString() {
        return this.tag.toString() + "=" + this.value.toString();
    }

    //TODO checkSumCounter(); checkSum of all message
}

