package adapter;

import factorymethod.Payment;

public class ExternalPaymentAdapter implements Payment {
    private final ExternalPaymentGateway gateway;

    public ExternalPaymentAdapter(ExternalPaymentGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public void pay(double amount) {
        gateway.makeTransaction(amount);
    }
}