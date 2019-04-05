package singlton;

public class MessageSinglton {
    private String id;

    private static MessageSinglton messageSinglton = new MessageSinglton();

    public static MessageSinglton getMessageSinglton() {
        return messageSinglton;
    }

    private MessageSinglton() {
    }

    public String getId() {
        return id;
    }

    public void setId(String  id) {
        this.id = id;
    }
}
