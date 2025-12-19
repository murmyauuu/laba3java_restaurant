package restaurant.staff;

import restaurant.model.DishType;
import restaurant.model.Order;
import restaurant.service.Kitchen;

import java.util.Random;

public class Waiter implements Runnable {
    private final String name;
    private final Kitchen kitchen;
    private static int nextOrderId = 1;
    private static final Random random = new Random();

    public Waiter(String name, Kitchen kitchen) {
        this.name = name;
        this.kitchen = kitchen;
    }

    public String getName() {
        return name;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 5; i++) {
                DishType dish = DishType.values()[random.nextInt(DishType.values().length)];
                Order order;
                synchronized (Waiter.class) {
                    order = new Order(nextOrderId++, dish, this);
                }

                System.out.println("[" + name + "] Принят " + order);
                kitchen.submitOrder(order);

                synchronized (order) {
                    while (!order.isReady()) {
                        order.wait();
                    }
                }

                System.out.println("[" + name + "] Доставлен " + order);
                Thread.sleep(300);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("[" + name + "] прерван.");
        }
    }
}