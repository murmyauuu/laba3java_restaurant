package restaurant.model;

import restaurant.staff.Waiter;

public class Order {
    private final int orderId;
    private final DishType dishType;
    private final Waiter waiter;
    private volatile boolean ready = false;

    public Order(int orderId, DishType dishType, Waiter waiter) {
        this.orderId = orderId;
        this.dishType = dishType;
        this.waiter = waiter;
    }

    public int getOrderId() {
        return orderId;
    }

    public DishType getDishType() {
        return dishType;
    }

    public Waiter getWaiter() {
        return waiter;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady() {
        this.ready = true;
    }

    @Override
    public String toString() {
        return "Заказ №" + orderId + ": " + dishType.getDisplayName() + " официанта " + waiter.getName();
    }
}