package exceptions;

public class TransferServiceTest {
    @FunctionalInterface
    private interface TransferAction {
        void run() throws BankingException;
    }

    public static void main(String[] args) throws BankingException {
        TransferService service = new TransferService();
        BankAccount source = new BankAccount("FR001", 2_000);
        BankAccount destination = new BankAccount("FR002", 100);
        reject(InsufficientFundsException.class, () -> service.transfer(source, destination, 3_000), source, destination);
        reject(TransferLimitExceededException.class, () -> service.transfer(source, destination, 12_000), source, destination);
        for (double amount : new double[] {-100, 0, Double.NaN, Double.POSITIVE_INFINITY}) {
            reject(IllegalArgumentException.class, () -> service.transfer(source, destination, amount), source, destination);
        }
        reject(InvalidAccountException.class, () -> service.transfer(source, source, 500), source, destination);
        reject(InvalidAccountException.class, () -> service.transfer(null, destination, 500), source, destination);
        reject(InvalidAccountException.class, () -> service.transfer(source, null, 500), source, destination);
        BankAccount sameNumber = new BankAccount("FR001", 0);
        reject(InvalidAccountException.class, () -> service.transfer(source, sameNumber, 500), source, sameNumber);

        service.transfer(source, destination, 500);
        balances(source, destination, 1_500, 600);
        BankAccount rich = new BankAccount("FR003", 10_000);
        service.transfer(rich, destination, 10_000);
        balances(rich, destination, 0, 10_600);

        for (boolean failAfterCredit : new boolean[] {false, true}) {
            RuntimeException failure = new IllegalStateException("Panne simulee");
            BankAccount failing = new BankAccount("FR004", 100) {
                @Override public void credit(double amount) {
                    if (failAfterCredit) { super.credit(amount); }
                    throw failure;
                }
            };
            double before = source.getBalance();
            try {
                service.transfer(source, failing, 500);
                throw new AssertionError("Panne attendue");
            } catch (BankingException e) {
                if (e.getCause() != failure) { throw new AssertionError("Cause perdue", e); }
            }
            balances(source, failing, before, 100);
        }
        System.out.println("TransferServiceTest : OK (validation, limites, compensation, cause conservee)");
    }

    private static void reject(Class<? extends Exception> type, TransferAction action,
            BankAccount source, BankAccount destination) {
        double sourceBefore = source.getBalance();
        double destinationBefore = destination.getBalance();
        try {
            action.run();
            throw new AssertionError("Rejet attendu");
        } catch (BankingException | RuntimeException e) {
            if (!type.isInstance(e)) { throw new AssertionError("Exception incorrecte", e); }
        }
        balances(source, destination, sourceBefore, destinationBefore);
    }

    private static void balances(BankAccount source, BankAccount destination, double s, double d) {
        if (source.getBalance() != s || destination.getBalance() != d) {
            throw new AssertionError("Soldes incorrects");
        }
    }
}