package message;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ListOfClients {

    private Map<Integer,Channel> brokerMap = new ConcurrentHashMap<>();
    private Map<Integer,Channel> marketMap = new ConcurrentHashMap<>();

    private static ListOfClients ourInstance;

    public static synchronized ListOfClients getInstance() {
        if (ourInstance == null) {
            ourInstance = new ListOfClients();
        }
        return ourInstance;
    }

    private ListOfClients() {
    }

    public Map<Integer, Channel> getBrokerMap() {
        return brokerMap;
    }

    public void setBrokerMap(Map<Integer, Channel> brokerMap) {
        this.brokerMap = brokerMap;
    }

    public Map<Integer, Channel> getMarketMap() {
        return marketMap;
    }

    public void setMarketMap(Map<Integer, Channel> marketMap) {
        this.marketMap = marketMap;
    }
}
