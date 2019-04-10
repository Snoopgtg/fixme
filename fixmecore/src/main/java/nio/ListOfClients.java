package nio;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.Random;
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

    public Map<Integer, Channel> getMarketMap() {
        return marketMap;
    }
    
    public String getRandomKeyFromMarketMap() {
        String[] keys = new String[marketMap.size()];

        int index = 0;
        for (Map.Entry<Integer,Channel> mapEntry : marketMap.entrySet()) {
            keys[index] = mapEntry.getKey().toString();
            index++;
        }
        Random r = new Random();
        return keys[r.nextInt(keys.length)];

    }
}
