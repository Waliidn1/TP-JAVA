package observer;

public class InventoryUpdater implements OrderObserver {
    @Override
    public void update(String status) {
        System.out.println("Inventory system notified: " + status);
    }
}