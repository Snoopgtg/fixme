package message;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RouterMain {

    public static void main(String[] args) {
        RouterMarket routerMarket = new RouterMarket();
        RouterBroker routerBroker = new RouterBroker();

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(routerMarket);
        executorService.submit(routerBroker);
    }
}
