package decorator;

public class ExpressDeliveryDecorator extends OrderCostDecorator {
    public ExpressDeliveryDecorator(OrderCost order) {
        super(order);
    }

    @Override
    public double getCost() {
        return order.getCost() + 15.0;
    }
}