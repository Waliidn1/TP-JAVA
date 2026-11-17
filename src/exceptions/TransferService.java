package exceptions;

import java.util.Objects;

public class TransferService {
    private static final double MAX_AMOUNT = 10_000.0;

    public void transfer(BankAccount source, BankAccount destination, double amount)
            throws BankingException {
        if (source == null || destination == null) {
            throw new InvalidAccountException("Les deux comptes sont obligatoires.");
        }
        if (source == destination || (source.getAccountNumber() != null
                && Objects.equals(source.getAccountNumber(), destination.getAccountNumber()))) {
            throw new InvalidAccountException("Les comptes doivent etre differents.");
        }
        if (!Double.isFinite(amount) || amount <= 0) {
            throw new IllegalArgumentException("Le montant doit etre positif et fini.");
        }
        if (amount > MAX_AMOUNT) {
            throw new TransferLimitExceededException("Plafond de 10 000 EUR depasse.");
        }
        if (amount > source.getBalance()) {
            throw new InsufficientFundsException("Fonds insuffisants.");
        }

        double sourceBefore = source.getBalance();
        double destinationBefore = destination.getBalance();
        source.debit(amount);
        // Seul le credit est protege : le debit a deja reussi.
        try {
            destination.credit(amount);
        } catch (RuntimeException cause) {
            // Restauration exacte, meme si credit a modifie le solde avant d'echouer.
            source.restoreBalance(sourceBefore);
            destination.restoreBalance(destinationBefore);
            throw new BankingException("Echec du credit : soldes restaures.", cause);
        }
    }
}