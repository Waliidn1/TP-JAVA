package decorator;

public class GiftWrappingDecorator extends OrderCostDecorator {
    public GiftWrappingDecorator(OrderCost order) {
        super(order);
    }

    @Override
    public double getCost() {
        return order.getCost() + 5.0;
    }
}