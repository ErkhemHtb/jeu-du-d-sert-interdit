			Rapport de projet
	
1. Parties du sujet trait�es :
- Cr�ation d'un plateau de jeu avec diff�rentes cases sp�ciales (Oasis, Mirage, Tunnel, etc.).
- Placement al�atoire des pi�ces et indices sur le plateau de jeu.
- Gestion des actions des joueurs : se d�placer (il faut cliquer sur ��Bouger�� puis utiliser les fl�ches directionnelles), explorer, d�blayer, r�v�ler les pi�ces et les indices.
- Interaction avec les cases sp�ciales, telles que les Oasis et les Tunnels.
- Une pi�ce se r�v�le quand ses deux indices sont trouv�s
- Possibilit� de r�cup�rer les pi�ces
- Gestion de la fin du jeu lorsque toutes les pi�ces sont trouv�es.
- Gestion du niveau d'eau des joueurs et des conditions de victoire et d�faite.
- Actions du d�sert
- Affichage et mise � jour du plateau de jeu en temps r�el.
- Gestion des temp�tes et des d�placements des cases.

2. Choix d'architecture :
- La classe Desert repr�sente le plateau de jeu et g�re les actions globales (placement des pi�ces, gestion des cases, etc.).
- La classe Case repr�sente une case sur le plateau, avec ses propri�t�s (type de case, pi�ce, sable, etc.) et ses actions (explorer, ajouter du sable, etc.).
- La classe Joueur repr�sente un joueur et g�re les actions propres aux joueurs (d�placement, interaction avec les cases, gestion du niveau d'eau, etc.).
- Les classes TypeCase et Piece sont utilis�es pour d�finir les types de cases et les pi�ces du jeu.
- Nous avons utilis� le pattern Observateur pour mettre � jour l'affichage du jeu en fonction des actions des joueurs.
- Nous avons �galement utilis� des exceptions personnalis�es pour g�rer les erreurs et faciliter le d�bogage.

3. Probl�mes connus :
- Dans certaines circonstances, il �tait possible que les pi�ces soient plac�es sur des cases o� d'autres �l�ments sont d�j� pr�sents (par exemple, un Oasis). Cela pouvait emp�cher la r�v�lation de la pi�ce m�me si les indices ont �t� trouv�s. Nous avons r�solu ce probl�me en modifiant la m�thode placerPieces(). Nous nous sommes assur�s que les pi�ces sont plac�es sur des cases vides en v�rifiant que le type de case est TypeCase.NORMAL et qu'aucune autre pi�ce n'est d�j� pr�sente sur la case.

- Il �tait possible que le nombre de tunnels pr�sents sur le plateau soit inf�rieur � deux. Un correctif a �t� propos� pour r�soudre ce probl�me, nous avons cr�� une m�thode qui place les tunnels et nous l�appelons dans le constructeur de D�sert, mais il convient de tester davantage pour s'assurer que le probl�me est compl�tement r�solu.
