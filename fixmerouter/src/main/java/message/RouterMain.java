package message;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RouterMain {

    public static void main(String[] args) {
        RouterOne routerOne = new RouterOne();
        RouterTwo routerTwo = new RouterTwo();

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(routerOne);
        executorService.submit(routerTwo);
    }
}
