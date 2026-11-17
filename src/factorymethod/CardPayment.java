package factorymethod;

public class CardPayment implements Payment {
    @Override
    public void pay(double amount) {
        System.out.println("Paiement par carte bancaire : " + amount + " EUR");
    }
}
