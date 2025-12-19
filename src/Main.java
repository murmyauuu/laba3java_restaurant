package restaurant;

import restaurant.service.Kitchen;
import restaurant.staff.Waiter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        final int NUM_WAITERS = 3;
        final int NUM_CHEFS = 2;

        System.out.println("Запуск ресторана...");

        Kitchen kitchen = new Kitchen(NUM_CHEFS);
        ExecutorService waiterExecutor = Executors.newFixedThreadPool(NUM_WAITERS);

        for (int i = 1; i <= NUM_WAITERS; i++) {
            waiterExecutor.submit(new Waiter("Официант-" + i, kitchen));
        }

        waiterExecutor.shutdown();
        try {
            if (!waiterExecutor.awaitTermination(30, TimeUnit.SECONDS)) {
                waiterExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            waiterExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        kitchen.shutdown();
        System.out.println("Работа ресторана завершена.");
    }
}