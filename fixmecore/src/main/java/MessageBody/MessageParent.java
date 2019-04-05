package MessageBody;

public abstract class MessageParent<T> {
    protected T value;
    protected Integer tag;

    public MessageParent(T value, Integer tag) {
        this.value = value;
        this.tag = tag;
    }

    public MessageParent(Integer tag) {
        this.tag = tag;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public String toString() {
        return (this.tag.toString() + "=" + this.value.toString() + (char)1).replace((char)1, '\u2401');
    }
}
//TODO counter for message method
