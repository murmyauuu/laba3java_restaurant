package restaurant.staff;

import restaurant.model.Order;

public class Chef implements Runnable {
    private final Order order;

    public Chef(Order order) {
        this.order = order;
    }

    @Override
    public void run() {
        String chefName = Thread.currentThread().getName();
        try {
            System.out.println("[" + chefName + "] Начал готовить " + order);
            Thread.sleep(order.getDishType().getCookTimeMs());
            System.out.println("[" + chefName + "] Готово: " + order);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("[" + chefName + "] Прерван при готовке " + order);
        }

        order.setReady();
        synchronized (order) {
            order.notifyAll();
        }
    }
}