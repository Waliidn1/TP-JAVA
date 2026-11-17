import adapter.ExternalPaymentAdapter;
import adapter.ExternalPaymentGateway;
import decorator.BasicOrder;
import decorator.ExpressDeliveryDecorator;
import decorator.GiftWrappingDecorator;
import decorator.OrderCost;
import factorymethod.CardPaymentFactory;
import factorymethod.PayPalPaymentFactory;
import factorymethod.Payment;
import factorymethod.PaymentFactory;
import observer.CustomerNotifier;
import observer.InventoryUpdater;
import observer.Order;
import singleton.AppConfig;

public class App {
    public static void main(String[] args) {
        effectuerPaiement(new CardPaymentFactory(), 100.0);
        effectuerPaiement(new PayPalPaymentFactory(), 50.0);
        effectuerPaiement(new PayPalPaymentFactory(), 75.0);

        System.out.println("Currency: " + AppConfig.getInstance().getCurrency());

        Payment externalPayment = new ExternalPaymentAdapter(new ExternalPaymentGateway());
        externalPayment.pay(100.0);

        OrderCost basic = new BasicOrder();
        OrderCost gift = new GiftWrappingDecorator(basic);
        OrderCost expressGift = new ExpressDeliveryDecorator(gift);
        System.out.println("Basic Order: " + basic.getCost() + " EUR");
        System.out.println("With Gift Wrapping: " + gift.getCost() + " EUR");
        System.out.println("With Gift Wrapping + Express Delivery: " + expressGift.getCost() + " EUR");

        Order order = new Order();
        CustomerNotifier customer = new CustomerNotifier();
        InventoryUpdater inventory = new InventoryUpdater();
        order.addObserver(customer);
        order.addObserver(inventory);
        order.setStatus("SHIPPED");
        order.removeObserver(customer);
        order.removeObserver(inventory);
    }

    public static void effectuerPaiement(PaymentFactory factory, double amount) {
        Payment payment = factory.createPayment();
        payment.pay(amount);
    }
}