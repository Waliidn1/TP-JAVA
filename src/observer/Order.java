package observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Order {
    private final List<OrderObserver> observers = new ArrayList<>();
    private String status;

    public void addObserver(OrderObserver observer) {
        Objects.requireNonNull(observer);
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeObserver(OrderObserver observer) {
        observers.remove(observer);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        Objects.requireNonNull(status);
        if (status.equals(this.status)) {
            return;
        }
        this.status = status;
        System.out.println("Order status changed: " + status);
        for (OrderObserver observer : new ArrayList<>(observers)) {
            observer.update(status);
        }
    }
}