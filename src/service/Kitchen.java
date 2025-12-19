package restaurant.service;

import restaurant.model.Order;
import restaurant.staff.Chef;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Kitchen {
    private final BlockingQueue<Order> orderQueue = new LinkedBlockingQueue<>();
    private final ExecutorService chefExecutor;
    private volatile boolean acceptingOrders = true;

    public Kitchen(int chefCount) {
        // Создаём ThreadFactory, чтобы имена потоков были вида "Повар-1", "Повар-2", ...
        ThreadFactory namedThreadFactory = new ThreadFactory() {
            private final AtomicInteger counter = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r, "Повар-" + counter.getAndIncrement());
                return t;
            }
        };

        this.chefExecutor = Executors.newFixedThreadPool(chefCount, namedThreadFactory);

        // Запускаем фоновый поток для обработки заказов из очереди
        Thread kitchenThread = new Thread(this::processOrders, "Кухня-Менеджер");
        kitchenThread.setDaemon(false);
        kitchenThread.start();
    }

    public void submitOrder(Order order) {
        if (acceptingOrders) {
            try {
                orderQueue.put(order);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Не удалось добавить заказ в очередь: " + order);
            }
        }
    }

    private void processOrders() {
        try {
            while (acceptingOrders || !orderQueue.isEmpty()) {
                Order order = orderQueue.take(); // блокирующий вызов
                chefExecutor.submit(new Chef(order));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Обработка заказов на кухне прервана.");
        }
    }

    public void shutdown() {
        acceptingOrders = false;
        chefExecutor.shutdown();
        try {
            if (!chefExecutor.awaitTermination(10, TimeUnit.SECONDS)) {
                chefExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            chefExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}