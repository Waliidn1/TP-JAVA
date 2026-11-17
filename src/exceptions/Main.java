package exceptions;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        BankAccount source = new BankAccount("FR001", 2_000.0);
        BankAccount destination = new BankAccount("FR002", 100.0);

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Montant du virement : ");
            double amount = scanner.nextDouble();
            new TransferService().transfer(source, destination, amount);
            System.out.println("Virement effectue.");}

         catch (InputMismatchException e) {
            System.out.println("Saisie invalide : entrez un nombre.");
        } 
        catch (InsufficientFundsException e) {
            System.out.println("Fonds insuffisants : " + e.getMessage());
        } 
        catch (TransferLimitExceededException e) {
            System.out.println("Plafond depasse : " + e.getMessage());
        } 
        catch (InvalidAccountException e) {
            System.out.println("Compte invalide : " + e.getMessage());
        } 
        catch (IllegalArgumentException e) {
            System.out.println("Montant invalide : " + e.getMessage());
        } 
        catch (BankingException e) {
            System.out.println("Echec technique du virement : " + e.getMessage());
            e.printStackTrace(System.err); // Conserve aussi la cause pour le diagnostic.
        } 
        finally 
        {
            System.out.println("Traitement de la transaction terminé.");
        }
        
        System.out.println("Solde source : " + source.getBalance() + " EUR");
        System.out.println("Solde destinataire : " + destination.getBalance() + " EUR");
    }
}