package decorator;

public abstract class OrderCostDecorator implements OrderCost {
    protected final OrderCost order;

    protected OrderCostDecorator(OrderCost order) {
        this.order = order;
    }
}