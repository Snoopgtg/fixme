package message;

public enum ClientPort {
    BROKERPORT(5000),
    MARKETPORT(5001);

    private int port;

    ClientPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }


}
