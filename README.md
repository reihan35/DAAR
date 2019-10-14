# Grep Clone

* Exemple d'utilisation

Dans l'exemple 1 on cherche une expression régulier dans un fichier.
![alt text](resultsRapport/exemple1.png)

Dans l'exemple 2 on cherche une expression régulier "hel" suivi d'un caractère et qui se trouve a la fin de la ligne dans un fichier.
![alt text](resultsRapport/exemple1.png)


Egrep implementer s'utilise de la manière suivant :

* Name :
RegEx

* Commande :
java RegEx [ OPTIONS ] PATTERN [ FILE ]

* Description :
Notre programme recherche dans l'entrée du nom FILE les lignes contenant un match avec le PATTERN entrée. Par défaut, il va affiche les lignes correspondantes.

* Les options :
-h : affiche les options
-1: -l: affiche le nom du fichier
-i : Ignorer les distinctions de casse dans les fichiers PATTERN et le fichier d'entrée
-w : Sélectionnez uniquement les lignes contenant des correspondances qui forment des mots entiers.
-c : Supprimer la sortie normale; à la place, imprimez un nombre de mots correspondantes pour chaque fichier d'entrée. (nb mots trouver)
-y : Supprimer la sortie normale; à la place, imprimez un nombre de ligne correspondantes pour chaque fichier d'entrée. (nb ligne où y a des match)
-m[n : nb ligne] : arrete l'affichage des linge matcher à la n-ème ligne
-o : print que les matching trouver
-n : affiche le numéro de ligne suivie de la sorti par defaut


* Caractère spécial accepté :
\textasciicircum : PATTERN rechercher est au début de la ligne.
$: PATTERN rechercher se trouve à la fin de la ligne.
*: L'élément précédent sera apparié zéro ou plusieurs fois.
|: Soit L'élément précédent ou L'élément suivant de "|" doit être présent.
( & ): L'opérateur qui suit ce caractère sera appliqué à tout ce qui a entre parentes.
?: L'élément précédent est facultatif et correspond au maximum une fois. (cependant la recherche avec ce caractère peut ne pas fonctionne, des erreurs sont malheureusement encore présente)

* Expressions régulières basiques
Dans les expressions rationnelles de base, les méta-caractères . , * , | , ( , ) et ? perdent leur signification particulière; utilisez plutôt les versions anti-slashées \. , \*, \| , \( , \) et \? ou utilisez le PATTERN avec `[PATTERN]`.
