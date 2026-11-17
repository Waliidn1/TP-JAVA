# Partie 4 : analyse avant compensation
Pour un solde source de 2 000 EUR et un virement de 500 EUR, une panne apres debit et avant credit laisse la source a 1 500 EUR et le destinataire a son solde initial (100 EUR dans Main).
try/catch n'annule aucune modification automatiquement. Le total des soldes a diminue de 500 EUR : le systeme est incoherent.
Il faut restaurer les valeurs anterieures ou utiliser une transaction atomique. Ici, les deux soldes sont sauvegardes et restaures si le credit leve une RuntimeException, meme apres une modification partielle.
La cause initiale est transmise au constructeur de BankingException. Les erreurs fatales de la JVM ne sont pas interceptees.

# Partie 6 : choix des catch
Pour l'utilisateur, choisir B : proposer un message et une action adaptes a chaque erreur (changer le montant, choisir un compte, etc.).
Pour une couche de journalisation, A peut suffire : journaliser la classe concrete et la pile avec sa cause, puis propager si necessaire sans dupliquer les logs.
Pour un controleur API, traduire les sous-types en codes d'erreur stables et reponses adaptees ; garder les details techniques dans les logs.

# Questions de discussion
1. Une exception specifique exprime la cause metier et permet un traitement cible ; Exception seule n'indique pas ce qui a echoue.
2. Avec RuntimeException, BankingException devient non verifiee : catch ou throws ne sont plus imposes par le compilateur.
3. Lever : throw new ... ; declarer : throws dans la signature ; gerer : catch avec une reaction appropriee.
4. catch(Exception e) peut masquer des bugs et confondre erreurs metier et techniques, surtout si l'exception est ignoree.
5. Gerer les erreurs au niveau capable de reagir : regles metier dans le service, message utilisateur dans l'interface, panne technique dans la couche adaptee. Traduire avec la cause si besoin.
6. Intercepter ne restaure rien : une compensation ou un rollback explicite reste necessaire.
7. Une transaction ACID de la base regroupe debit et credit : commit si tout reussit, rollback sinon.

# Finally et ressources
finally convient pour annoncer la fin d'une tentative, succes ou echec, sans dupliquer le message dans chaque catch. Un arret brutal peut empecher son execution.
Utiliser de preference try-with-resources pour liberer connexions, fichiers et flux. Fermer Scanner ferme aussi System.in : le faire seulement si ce programme en est proprietaire.