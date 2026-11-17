package decorator;

public class BasicOrder implements OrderCost {
    @Override
    public double getCost() {
        return 100.0;
    }
}