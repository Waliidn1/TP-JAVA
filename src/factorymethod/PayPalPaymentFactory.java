package factorymethod;

public class PayPalPaymentFactory extends PaymentFactory {
    @Override
    public Payment createPayment() {
        return new PayPalPayment();
    }
}
