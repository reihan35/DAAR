Egrep implementer s'utilise de la manière suivant :

Name :
RegEx

Commande :
RegEx [ OPTIONS ] PATTERN [ FILE ]

Description :
Notre programme recherche dans l'entrée du nom FILE les lignes contenant un match avec le PATTERN entrée. Par défaut, il va affiche les lignes correspondantes.

Les options :
-l: affiche le nom du fichier
-i: Ignorer les distinctions de casse dans les fichiers PATTERN et le fichier d'entrée
-c : Supprimer la sortie normale; à la place, imprimez le nombre de match correspondantes pour chaque fichier d'entrée. (le nb de ligne où y a des match)
-m[n] : Arrête l'affichage des linge matcher à la n-ème ligne
-o : Affiche que les match trouver

Caractère spécial accepté :
\textasciicircum : PATTERN rechercher est au début de la ligne.
\$: PATTERN rechercher se trouve à la fin de la ligne.
$*$: L'élément précédent sera apparié zéro ou plusieurs fois.
$|$: Soit L'élément précédent ou L'élément suivant de $\vert$ doit être présent.
$($ $)$: L'opérateur qui suit ce caractère sera appliqué à tout ce qui a entre parentes.
?: L'élément précédent est facultatif et correspond au maximum une fois. (cependant la recherche avec ce caractère peut ne pas fonctionne, des erreurs sont malheureusement encore présente)

Expressions régulières basiques
Dans les expressions rationnelles de base, les méta-caractères ~ . , $*$ , $|$ , $($ , $)$ et ? perdent leur signification particulière; utilisez plutôt les versions anti-slashées $\backslash.$ , $\backslash*$, $\backslash|$ , $\backslash($ , $\backslash)$ et $\backslash?$ ou utilisez le PATTERN avec `[PATTERN]`.
