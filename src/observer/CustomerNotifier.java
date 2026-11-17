package observer;

public class CustomerNotifier implements OrderObserver {
    @Override
    public void update(String status) {
        System.out.println("Customer notified: " + status);
    }
}