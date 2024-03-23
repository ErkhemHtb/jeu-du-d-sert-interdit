			Rapport de projet
	
1. Parties du sujet traitées :
- Création d'un plateau de jeu avec différentes cases spéciales (Oasis, Mirage, Tunnel, etc.).
- Placement aléatoire des pièces et indices sur le plateau de jeu.
- Gestion des actions des joueurs : se déplacer (il faut cliquer sur « Bouger » puis utiliser les flèches directionnelles), explorer, déblayer, révéler les pièces et les indices.
- Interaction avec les cases spéciales, telles que les Oasis et les Tunnels.
- Une pièce se révèle quand ses deux indices sont trouvés
- Possibilité de récupérer les pièces
- Gestion de la fin du jeu lorsque toutes les pièces sont trouvées.
- Gestion du niveau d'eau des joueurs et des conditions de victoire et défaite.
- Actions du désert
- Affichage et mise à jour du plateau de jeu en temps réel.
- Gestion des tempêtes et des déplacements des cases.

2. Choix d'architecture :
- La classe Desert représente le plateau de jeu et gère les actions globales (placement des pièces, gestion des cases, etc.).
- La classe Case représente une case sur le plateau, avec ses propriétés (type de case, pièce, sable, etc.) et ses actions (explorer, ajouter du sable, etc.).
- La classe Joueur représente un joueur et gère les actions propres aux joueurs (déplacement, interaction avec les cases, gestion du niveau d'eau, etc.).
- Les classes TypeCase et Piece sont utilisées pour définir les types de cases et les pièces du jeu.
- Nous avons utilisé le pattern Observateur pour mettre à jour l'affichage du jeu en fonction des actions des joueurs.
- Nous avons également utilisé des exceptions personnalisées pour gérer les erreurs et faciliter le débogage.

3. Problèmes connus :
- Dans certaines circonstances, il était possible que les pièces soient placées sur des cases où d'autres éléments sont déjà présents (par exemple, un Oasis). Cela pouvait empêcher la révélation de la pièce même si les indices ont été trouvés. Nous avons résolu ce problème en modifiant la méthode placerPieces(). Nous nous sommes assurés que les pièces sont placées sur des cases vides en vérifiant que le type de case est TypeCase.NORMAL et qu'aucune autre pièce n'est déjà présente sur la case.

- Il était possible que le nombre de tunnels présents sur le plateau soit inférieur à deux. Un correctif a été proposé pour résoudre ce problème, nous avons créé une méthode qui place les tunnels et nous l’appelons dans le constructeur de Désert, mais il convient de tester davantage pour s'assurer que le problème est complètement résolu.
