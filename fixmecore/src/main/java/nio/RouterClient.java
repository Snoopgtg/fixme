package nio;

public class RouterClient {

    protected		long id;
    static private	long idCounter;
    protected String name;

    public RouterClient() {

        this.id = nextId();
    }

    public void setName(String name) {
        this.name = name + Long.toString(id);
    }

    private	long nextId() {

        return idCounter++;
    }

    public String getName() {
        return name;
    }
}
