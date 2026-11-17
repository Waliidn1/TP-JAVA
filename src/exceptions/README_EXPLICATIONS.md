# Comprendre les exceptions simplement

Une exception signale qu'un problème empêche une opération de se dérouler normalement. Par exemple, un virement ne peut pas être effectué si le compte ne contient pas assez d'argent. Au lieu de continuer avec des données incorrectes, le programme signale le problème à la partie du code qui l'a appelé.

## Les exceptions de notre projet

`BankingException` est la classe mère des exceptions bancaires. Elle regroupe les problèmes liés au virement. Elle hérite de `Exception` : le compilateur oblige donc le code appelant à traiter ces erreurs avec `catch`, ou à les transmettre avec `throws`.

`InsufficientFundsException` signifie que le solde est insuffisant. Par exemple, demander un virement de 3 000 euros depuis un compte qui contient 2 000 euros déclenche cette exception.

`TransferLimitExceededException` signifie que le montant dépasse le plafond autorisé de 10 000 euros. Même si le compte possède assez d'argent, un virement de 12 000 euros est refusé.

`InvalidAccountException` signifie qu'un compte est absent (`null`) ou que le compte source et le compte destinataire sont identiques. Notre service compare aussi les numéros de compte.

`IllegalArgumentException` est une exception standard de Java. Nous l'utilisons quand le montant transmis n'est pas acceptable : zéro, un nombre négatif ou une valeur non finie. Elle hérite de `RuntimeException` : Java n'impose pas de la déclarer avec `throws`.

`InputMismatchException` est levée par `Scanner` quand la saisie ne correspond pas au type attendu. Par exemple, l'utilisateur écrit « abc » alors que le programme attend un nombre.

## throw et throws : quelle différence ?

`throw` déclenche réellement une exception dans le corps d'une méthode :

```java
if (amount > source.getBalance()) {
    throw new InsufficientFundsException("Fonds insuffisants.");
}
```

`throws` annonce dans la signature qu'une méthode peut transmettre une exception à son appelant :

```java
public void transfer(BankAccount source, BankAccount destination, double amount)
        throws BankingException {
    // Validation et virement
}
```

Déclarer `throws` ne résout pas le problème : cela prévient simplement le code appelant qu'il devra le gérer ou le transmettre à son tour.

## try, catch et finally

`try` contient l'opération que l'on veut tenter. Si une exception survient, la suite du bloc est interrompue.

`catch` permet de réagir à une exception. Dans `Main`, chaque erreur métier a son propre message pour que l'utilisateur comprenne ce qu'il doit corriger.

```java
try {
    service.transfer(source, destination, 500);
} catch (InsufficientFundsException e) {
    System.out.println("Vous n'avez pas assez d'argent.");
} catch (BankingException e) {
    System.out.println("Le virement a échoué : " + e.getMessage());
} finally {
    System.out.println("Traitement de la transaction terminé.");
}
```

Les variables `service`, `source` et `destination` doivent avoir été créées avant cet extrait.

Un seul `catch` est exécuté : le premier compatible avec l'exception. Il faut donc placer les exceptions précises avant leur classe mère, comme `BankingException`.

`finally` s'exécute à la sortie du try/catch, que le virement réussisse ou échoue. Ici, il annonce la fin du traitement. Il ne garantit pas la réussite du virement et peut ne pas s'exécuter si le processus est arrêté brutalement.

Le `try-with-resources` de `Main` ferme automatiquement le `Scanner`. Dans une application réelle, ce mécanisme sert aussi à fermer des fichiers ou des connexions à une base de données.

## Pourquoi conserver la cause d'une erreur ?

Lorsqu'un problème technique survient pendant le crédit, nous créons une `BankingException` en lui passant l'exception d'origine :

```java
throw new BankingException("Echec du credit : soldes restaures.", cause);
```

Le message explique le problème au niveau du virement. La cause garde les détails techniques utiles pour comprendre la panne. On peut la retrouver avec `getCause()` ; `printStackTrace()` affiche aussi cette cause.

## Attraper une exception ne remet pas l'argent automatiquement

Si on débite 500 euros d'un compte qui contient 2 000 euros, son solde devient 1 500 euros. Si une panne survient avant le crédit du destinataire, un simple `catch` ne remet pas les 500 euros sur le compte source.

Notre service vérifie d'abord toutes les règles avant de toucher aux soldes. Il sauvegarde ensuite les deux soldes. Si le crédit échoue avec une `RuntimeException`, il restaure les valeurs sauvegardées et transmet une `BankingException` contenant la cause initiale.

Cette solution est pédagogique : elle suppose des opérations en mémoire, sans accès simultanés ni arrêt brutal. Pour une application bancaire avec une base de données, on utiliserait une transaction : soit le débit et le crédit sont tous les deux validés, soit les modifications sont annulées par un rollback.

## Essayer le programme

Lancer `Main.java` dans VS Code, puis saisir un montant :

- `500` : le virement réussit depuis le compte initial de 2 000 euros.
- `3000` : les fonds sont insuffisants.
- `12000` : le plafond est dépassé.
- `-100` : le montant est invalide.
- `abc` : une erreur de saisie est affichée.

Lancer `TransferServiceTest.java` pour vérifier aussi les comptes invalides et les pannes simulées lors du crédit.