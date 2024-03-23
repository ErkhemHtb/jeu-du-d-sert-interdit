// Groupe : MAILLE Gabriel et HATANBAATAR VAN ATARTUGS Erkhembileg

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Collections;
import java.util.Arrays;

enum TypeCase {
    NORMAL(null),
    OEIL_TEMPETE(null),
    HELICOPTERE_CRASH(null),
    PISTE_DECOLLAGE(null),
    OASIS(null),
    TUNNEL(null),
    MIRAGE(null),
    MOTEUR(Piece.MOTEUR),
    GOUVERNAIL(Piece.GOUVERNAIL),
    HELICE(Piece.HELICE),
    CAPTEUR(Piece.CAPTEUR),
    LIGNE_MOTEUR(Piece.MOTEUR),
    COLONNE_MOTEUR(Piece.MOTEUR),
    LIGNE_HELICE(Piece.HELICE),
    COLONNE_HELICE(Piece.HELICE),
    LIGNE_GOUVERNAIL(Piece.GOUVERNAIL),
    COLONNE_GOUVERNAIL(Piece.GOUVERNAIL),
    LIGNE_CAPTEUR(Piece.CAPTEUR),
    COLONNE_CAPTEUR(Piece.CAPTEUR);

    private final Piece piece;

    TypeCase(Piece piece) {
        this.piece = piece;
    }

    public Piece getPiece() {
        return piece;
    }

    @Override
    public String toString() {
        switch (this) {
            case NORMAL:
                return "Desert";
            case OEIL_TEMPETE:
                return "";
            case HELICOPTERE_CRASH:
                return "Crash";
            case PISTE_DECOLLAGE:
                return "Piste";
            case OASIS:
                return "Oasis";
            case MIRAGE:
                return "Mirage";
            case TUNNEL:
                return "Tunnel";
            case MOTEUR:
                return "Moteur";
            case HELICE:
                return "Helice";
            case GOUVERNAIL:
                return "Gouvernail";
            case CAPTEUR:
                return "Capteur";
            case LIGNE_MOTEUR:
                return "Ligne Moteur";
            case COLONNE_MOTEUR:
                return "Colonne Moteur";
            case LIGNE_HELICE:
                return "Ligne Helice";
            case COLONNE_HELICE:
                return "Colonne Helice";
            case LIGNE_GOUVERNAIL:
                return "Ligne Gouvernail";
            case COLONNE_GOUVERNAIL:
                return "Colonne Gouvernail";
            case LIGNE_CAPTEUR:
                return "Ligne Capteur";
            case COLONNE_CAPTEUR:
                return "Colonne Capteur";
            default:
                throw new IllegalArgumentException("Type de case non reconnu");
        }
    }
    

}

enum Piece {
    MOTEUR,
    HELICE,
    GOUVERNAIL,
    CAPTEUR;
}

interface Observer {

    public void update();

}

abstract class Observable {

    private ArrayList<Observer> observers;

    public Observable() {
        this.observers = new ArrayList<Observer>();
    }

    public void addObserver(Observer o) {
        observers.add(o);
    }

    public void notifyObservers() {
        for (Observer o : observers) {
            o.update();
        }
    }
}

class proj {

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {

            Desert plateau = new Desert(5); // Creation du plateau de jeu
            plateau.addTabJoueur(); // Creation des Joueurs
            CVue vue = new CVue(plateau); // Creation de la vue du plateau - aspect graphique
        });
    }
}

class Joueur {
    private String nom;
    private int niveau_eau;
    private List<Piece> pieces;
    private int x;
    private int y;
    private Desert desert;
    private int actionsRestantes;
    private Color color;

    public Joueur(String nom, Desert desert) {
        this.nom = nom;
        this.niveau_eau = 5;
        this.desert = desert;
        this.actionsRestantes = 4;
        this.color=null;
        this.pieces = new ArrayList<>();
    }

    public String getNom() {
        return nom;
    }

    public int getNiveauEau() {
        return niveau_eau;
    }

    public Color getColor(){
        return color;
    }

    public void setColor(Color c){
        color=c;
    }

    public ArrayList<Piece> getPieces() {
        return (ArrayList<Piece>) pieces;
    }
    public void setPiece(ArrayList<Piece> p){    // POUR DEV 
        pieces=(List<Piece>) p;
    }

    public String getStringPieces(){
        String res="";
        for (Piece p:pieces){
            res=res+p.toString()+"/";
        }
        return res;
    }

    public void ramasserPiece() {
        Case caseActuelle = desert.getCase(x, y);
        Piece piece = caseActuelle.getPiece();
        if (piece != null && caseActuelle.isRevealed() && actionsRestantes > 0) {
            this.pieces.add(piece);
            caseActuelle.setPiece(null);
            actionsRestantes--;
            desert.notifyObservers();
        } else if (actionsRestantes <= 0) {
            System.out.println("Vous n'avez plus d'action.");
            desert.notifyObservers();
        } else {
            // Afficher un message indiquant que la pièce n'est pas encore révélée
            desert.notifyObservers(); // Notifier les observateurs même si la pièce n'est pas révélée
        }
    }
    

    public boolean possedePiece(Piece piece) {
        return pieces.contains(piece);
    }

    public void boireEau(int quantite) {
        niveau_eau -= quantite;
        if (niveau_eau < 0) {
            niveau_eau = 0;
        }
    }

    public void remplirGourde(int quantite) {
        niveau_eau += quantite;
        if (niveau_eau > 5) {
            niveau_eau = 5;
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getActions(){
        return actionsRestantes;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void deplacer(int newX, int newY) {
        if ((Math.abs(newX - x) == 1 && newY == y) || (Math.abs(newY - y) == 1 && newX == x)) {
            if (newX>=0 && newX<5 && newY>=0 && newY<5){
                Case nouvelleCase = desert.getCase(newX, newY);
                if (!nouvelleCase.estBloquee()&&nouvelleCase.getType()!=TypeCase.OEIL_TEMPETE) {
                    this.x = newX;
                    this.y = newY;
                    actionsRestantes--;
                    desert.notifyObservers();
                }
            }
        }
    }

    public void creuser(int x, int y) { // On peut creuser dans l'oeil de la tempete, c'est inutile mais on peut
        if ((Math.abs(x - this.x) <= 1) && (Math.abs(y - this.y) <= 1)) {
            if (x>=0 && x<5 && y>=0 && y<5){
                Case caseACreuser = desert.getCase(x, y);
                if (caseACreuser.getSable()>0){
                    caseACreuser.enleverSable(1);
                    actionsRestantes--;
                    desert.notifyObservers();
                }
            }
        }
    }

    public void resetActions() {
        actionsRestantes = 4;
    }
    public void explorer() {
        int x = this.getX();
        int y = this.getY();
        Case currentCase = desert.getCase(x, y);
        
        if (!currentCase.estExploree()) {
            if (actionsRestantes >= 1) {
                currentCase.explorer();
                actionsRestantes -= 1;
            }
            
            if (currentCase.getType() == TypeCase.OASIS) {
                for (Joueur j : desert.getJoueur()) {
                    if (j.getX() == x && j.getY() == y) {
                        j.niveau_eau += 2;
                        if (j.niveau_eau > 5) {
                            j.niveau_eau = 5;
                        }
                    }
                }
            }
    
            TypeCase type = currentCase.getType();
            if (type != null && type.getPiece() != null) {
                Piece piece = type.getPiece();
                if (desert.checkIndicesRevealed(piece)) {
                    System.out.println("Les deux indices de la piece " + piece + " ont ete trouves, la piece est maintenant revelee.");
                    desert.revealPiece(piece);
                }
            }
            desert.notifyObservers();
        }
    }
    
    

    public void donnerEau(Joueur autreJoueur, int quantiteEau) {
        if (niveau_eau>1&&autreJoueur.getNiveauEau()<5){
            if (this.x == autreJoueur.x && this.y == autreJoueur.y) {
                int quantiteEauDisponible = Math.min(quantiteEau, Math.min(this.niveau_eau, 5 - autreJoueur.niveau_eau));
                this.niveau_eau -= quantiteEauDisponible;
                autreJoueur.niveau_eau += quantiteEauDisponible;
            }
            desert.notifyObservers();
        }
    }

    public void prendreEau(Joueur autreJoueur, int quantiteEau) {
        if (this.x == autreJoueur.x && this.y == autreJoueur.y) {
            int quantiteEauRecue = Math.min(quantiteEau,
                    Math.min(autreJoueur.niveau_eau, this.niveau_eau - this.niveau_eau));
            this.niveau_eau += quantiteEauRecue;
            autreJoueur.niveau_eau -= quantiteEauRecue;
        }
    }
    public void passageTunnel(){
        for (int i=0;i<desert.getTaille();i++){
            for (int j=0;j<desert.getTaille();j++){
                if (desert.getCase(i, j).getType()==TypeCase.TUNNEL && (i!=x || j!=y)){
                    x=i;
                    y=j;
                }
            }
        }
        desert.notifyObservers();
    }

    public int getNombrePieces() {
        return this.pieces.size();
    }
    

}

class Case {
    private int x;
    private int y;
    private boolean exploree;
    private int sable;
    private boolean bloquee;
    private TypeCase type;
    private Piece piece;
    private boolean revealed;

    public Case(int x, int y, TypeCase type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.exploree = false;
        this.sable = 0;
        this.bloquee = false;
        this.revealed = false;

    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public TypeCase getType() {
        return type;
    }

    public void setType(TypeCase type) {
        this.type = type;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public boolean estExploree() {
        return exploree;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }

    public void explorer() {
        if (!exploree && !estBloquee()) {
            exploree = true;
            // rappel : mettre des actions supplémentaires ici (révéler des informations sur
            // la case ou mettre à jour l'interface utilisateur)
        }
    }

    public int getSable() {
        return this.sable;
    }

    public void ajouterSable(int sable) {
        this.sable += sable;
        if (this.sable >= 2) {
            this.bloquee = true;
        }
    }

    public void enleverSable(int sable) {
        this.sable -= sable;
        if (this.sable < 0) {
            this.sable = 0;
        }
        if (this.sable < 2) {
            this.bloquee = false;
        }
    }

    public boolean estBloquee() {
        return bloquee;
    }
    public void setBloquee(boolean b) {
        bloquee=b;
    }

    @Override
    public String toString() {
        return "Case{" +
                "exploree=" + exploree +
                ", sable=" + sable +
                ", bloquee=" + bloquee +
                '}';
    }
}


    class Desert extends Observable {
        private Case[][] cases;
        private int taille;
        private List<Case> caseDesert = new ArrayList<>();
        private Case coordOeil;
        private int niveauTempete = 2;
        private Joueur[] joueurs;
        private int joueurActuel = 0;
        private Case pisteDecollage;

    
        public Desert(int taille) {
            this.taille = taille;
            this.joueurs = new Joueur[4];
            this.coordOeil = new Case(taille / 2, taille / 2, TypeCase.OEIL_TEMPETE);
            List<TypeCase> casesSpeciales = Arrays.asList(
                    TypeCase.LIGNE_HELICE, TypeCase.COLONNE_HELICE,
                    TypeCase.LIGNE_CAPTEUR, TypeCase.COLONNE_CAPTEUR,
                    TypeCase.LIGNE_GOUVERNAIL, TypeCase.COLONNE_GOUVERNAIL,
                    TypeCase.LIGNE_MOTEUR, TypeCase.COLONNE_MOTEUR,
                    TypeCase.OASIS, TypeCase.OASIS, TypeCase.MIRAGE,
                    TypeCase.HELICOPTERE_CRASH,
                    TypeCase.PISTE_DECOLLAGE
            );
        
            // Pour calculer le nombre de cases normales pour compléter le plateau
            int numberOfNormalCases = (taille * taille) - casesSpeciales.size() - 4; // -4 pour les 4 pièces
        
            // On ajoute les cases normales à la liste
            List<TypeCase> extendedCasesSpeciales = new ArrayList<>(casesSpeciales);
            for (int i = 0; i < numberOfNormalCases; i++) {
                extendedCasesSpeciales.add(TypeCase.NORMAL);
            }
        
            // On crée une liste modifiable à partir de extendedCasesSpeciales
            List<TypeCase> modifiableCasesSpeciales = new ArrayList<>(extendedCasesSpeciales);
            Collections.shuffle(modifiableCasesSpeciales);
        
            this.cases = new Case[taille][taille];
            for (int i = 0; i < taille; i++) {
                for (int j = 0; j < taille; j++) {
                    if (!modifiableCasesSpeciales.isEmpty()) {
                        TypeCase type = modifiableCasesSpeciales.remove(0);
                        cases[i][j] = new Case(i, j, type);
                        caseDesert.add(cases[i][j]);
        
                        if (type == TypeCase.PISTE_DECOLLAGE) {
                            this.pisteDecollage = cases[i][j];
                        }
                    } else {
                        cases[i][j] = new Case(i, j, TypeCase.NORMAL);
                        caseDesert.add(cases[i][j]);
                    }
                }
            }
        
            // Oeil de la tempête
            cases[taille / 2][taille / 2] = coordOeil;
        
            cases[0][2].ajouterSable(1);
            cases[1][1].ajouterSable(1);
            cases[1][3].ajouterSable(1);
            cases[2][0].ajouterSable(1);
            cases[2][4].ajouterSable(1);
            cases[3][1].ajouterSable(1);
            cases[3][3].ajouterSable(1);
            cases[4][2].ajouterSable(1);
        
            placerPieces();
            placerTunnels();
        }
        
        
        
    public int getTaille() {
        return taille;
    }
    public int getTempete() {
        return (int)niveauTempete;
    }
    public Joueur[] getJoueur(){
        return joueurs;
    }
    public int getJoueurActuel(){
        return joueurActuel;
    }
    public void next(){
        if ((joueurs.length-1)<=joueurActuel){
            joueurActuel=0;
        } else {
            joueurActuel+=1;
        }
    }

    public int getPisteDecollageX() {
        return pisteDecollage.getX();
    }

    public int getPisteDecollageY() {
        return pisteDecollage.getY();
    }

    public void addTabJoueur(){
        Joueur[] joueurs_temp = new Joueur[5]; // Creation des joueurs RAPPEL : Fonction a deplacer ? // A changer pour laisser les utilisateur les creer
        Joueur j = new Joueur("squelette", this);
        Joueur ja = new Joueur("squelette2", this);
        Joueur jb = new Joueur("squelette3", this);
        Joueur jc = new Joueur("squelette4", this);
        Joueur jd = new Joueur("squelette5", this);
        int posX = (int) (Math.random() * 5);
        int posY = (int) (Math.random() * 5);
        while (posX==2 && posY==2){
            posX = (int) (Math.random() * 5);
            posY = (int) (Math.random() * 5);
        }
        j.setX(posX);
        j.setY(posY);
        posX = (int) (Math.random() * 5);
        posY = (int) (Math.random() * 5);
        while (posX==2 && posY==2){
            posX = (int) (Math.random() * 5);
            posY = (int) (Math.random() * 5);
        }
        ja.setX(posX);
        ja.setY(posY);
        posX = (int) (Math.random() * 5);
        posY = (int) (Math.random() * 5);
        while (posX==2 && posY==2){
            posX = (int) (Math.random() * 5);
            posY = (int) (Math.random() * 5);
        }
        jb.setX(posX);
        jb.setY(posY);
        jc.setX(posX);
        jc.setY(posY);
        jd.setX(posX);
        jd.setY(posY);
        ArrayList<Piece> p=new ArrayList<>(4);
        p.add(Piece.MOTEUR);
        p.add(Piece.CAPTEUR);
        p.add(Piece.GOUVERNAIL);
        p.add(Piece.HELICE);
        // j.setPiece(p);                  POUR VERIFIER VICTOIRE
        j.setColor(Color.RED);
        ja.setColor(new Color(181,151,53));
        jb.setColor(Color.BLACK);
        jc.setColor(Color.GREEN);
        jd.setColor(new Color(226,53,191));
        joueurs_temp[0] = j;
        joueurs_temp[1] =ja;
        joueurs_temp[2] =jb;
        joueurs_temp[3] =jc;
        joueurs_temp[4] =jd;
        joueurs=joueurs_temp;
    }

    public Case getCase(int i, int j) {
        return cases[i][j];
    }

    public void show() {
        for (int i = 0; i < taille; i++) {
            for (int j = 0; j < taille; j++) {
                System.out.print("#");
            }
            System.out.println();
        }
    }

    public void actionDesert(Joueur[] joueurs) {
        System.out.println(); ////////////////////////////////// AIDE POUR DEV à enlever à la fin
        int nombreActions = (int) niveauTempete;
        for (int i = 0; i < nombreActions; i++) {
            // Choisir une action au hasard parmi les 3
            int action = (int) (Math.random() * 3);

            switch (action) {
                case 0:
                    System.out.println("action:VentSouffle"); // AIDE POUR DEV à enlever à la fin
                    ventSouffle();
                    break;
                case 1:
                    System.out.println("action:CHALEUR"); // AIDE POUR DEV à enlever à la fin
                    vagueDeChaleur(joueurs);
                    break;
                case 2:
                    System.out.println("action:Dechainement"); // AIDE POUR DEV à enlever à la fin
                    tempeteSeDechaine();
                    break;
            }
        } // On vérifie le sable total du desert pour arrêter la partie à 43 tonnes
        this.notifyObservers();
    }

    private void ventSouffle() {
        // Choisir une direction aléatoire (0: haut, 1: droite, 2: bas, 3: gauche)
        int direction = (int) (Math.random() * 4);

        // Choisir une force aléatoire entre 1 et 3
        int force = (int) (Math.random() * 3) + 1;
        System.out.println("direction :" + direction + " / force :" + force);
        // Calculer les déplacements en x et y en fonction de la direction
        int dx = 0;
        int dy = 0;
        switch (direction) {
            case 0:
                dy = -1;
                break;
            case 1:
                dx = 1;
                break;
            case 2:
                dy = 1;
                break;
            case 3:
                dx = -1;
                break;
        }

        // Trouver l'œil de la tempête
        int oeilX = -1;
        int oeilY = -1;
        for (int x = 0; x < taille; x++) {
            for (int y = 0; y < taille; y++) {
                if (cases[x][y].getType() == TypeCase.OEIL_TEMPETE) {
                    oeilX = x;
                    oeilY = y;
                    break;
                }
            }
        }

        // Boucle pour déplacer les tuiles
        for (int i = 0; i < force; i++) {
            int newX = oeilX - dx;
            int newY = oeilY - dy;

            // Vérifier si la nouvelle position est dans les limites du désert
            if (newX >= 0 && newX < taille && newY >= 0 && newY < taille) {

                // Déplacer la tuile et ajouter une tonne de sable
                cases[newX][newY].ajouterSable(1);

                // Échanger l'oeil de la tempête et la case adjacente
                Case temp = cases[newX][newY];
                cases[newX][newY] = cases[oeilX][oeilY];
                cases[oeilX][oeilY] = temp;

                // Mettre à jour la position de l'oeil de la tempête
                oeilX = newX;
                oeilY = newY;
            }
        }
    }

    private void vagueDeChaleur(Joueur[] joueurs) {
        for (Joueur joueur : joueurs) {
            if (!(cases[joueur.getX()][joueur.getY()].getType()==TypeCase.TUNNEL)){
                joueur.boireEau(1);
            }
        }
        notifyObservers();
    }

    private void tempeteSeDechaine() {
        niveauTempete += 0.5;
    }

    public boolean partieGagnee() {
        int piecesRamassees = 0;
        boolean tousLesJoueursSurPiste = false;
        for (Joueur joueur : getJoueur()) {
            piecesRamassees += joueur.getNombrePieces();
            if (joueur.getX() == getPisteDecollageX() && joueur.getY() == getPisteDecollageY()) {
                tousLesJoueursSurPiste = true;
            }
        }
    
        if (piecesRamassees == 4 && tousLesJoueursSurPiste && pisteDecollage.getSable() == 0) {
            return true;
        }
        return false;
    }

    public void placerPieces() {
        List<Piece> pieces = Arrays.asList(Piece.MOTEUR, Piece.GOUVERNAIL, Piece.HELICE, Piece.CAPTEUR);
        Collections.shuffle(pieces);
    
        List<TypeCase> indices = Arrays.asList(
                TypeCase.LIGNE_MOTEUR, TypeCase.COLONNE_MOTEUR,
                TypeCase.LIGNE_GOUVERNAIL, TypeCase.COLONNE_GOUVERNAIL,
                TypeCase.LIGNE_HELICE, TypeCase.COLONNE_HELICE,
                TypeCase.LIGNE_CAPTEUR, TypeCase.COLONNE_CAPTEUR);
        Collections.shuffle(indices);
    
        int nombreDePiecesPlacees = 0;
        int nombreDIndicesPlaces = 0;
    
        while (nombreDePiecesPlacees < pieces.size() && nombreDIndicesPlaces < 2 * pieces.size()) {
            int randomX = (int) (Math.random() * taille);
            int randomY = (int) (Math.random() * taille);
    
            Case randomCase = this.getCase(randomX, randomY);
            TypeCase caseType = randomCase.getType();
    
            if (caseType == TypeCase.NORMAL) {
                if (randomCase.getPiece() == null && nombreDePiecesPlacees < pieces.size()) {
                    randomCase.setPiece(pieces.get(nombreDePiecesPlacees));
                    nombreDePiecesPlacees++;
                } else if (randomCase.getPiece() == null && nombreDIndicesPlaces < indices.size()) {
                    randomCase.setType(indices.get(nombreDIndicesPlaces));
                    nombreDIndicesPlaces++;
                }
            }
        }
    }
    
        public void placerTunnels() {
            int tunnelCount = 0;
    
            while (tunnelCount < 2) {
                int randomX = (int) (Math.random() * taille);
                int randomY = (int) (Math.random() * taille);
    
                Case randomCase = this.getCase(randomX, randomY);
    
                if (randomCase.getType() == TypeCase.NORMAL && randomCase.getPiece() == null) {
                    randomCase.setType(TypeCase.TUNNEL);
                    tunnelCount++;
                }
            }
        }
    
    
    
    
    
    
    

    public boolean checkIndicesRevealed(Piece piece) {
        int indicesCount = 0;
        for (int i = 0; i < taille; i++) {
            for (int j = 0; j < taille; j++) {
                TypeCase type = getCase(i, j).getType();
                if (type != null && type.getPiece() == piece && getCase(i, j).estExploree()) {
                    indicesCount++;
                }
            }
        }
        return indicesCount >= 2;
    }
    
    public void revealPiece(Piece piece) {
        boolean found = false;
        for (int i = 0; i < taille && !found; i++) {
            for (int j = 0; j < taille && !found; j++) {
                Case currentCase = getCase(i, j);
                if (currentCase.getPiece() == piece) {
                    currentCase.setRevealed(true);
                    found = true;
                }
            }
        }
    }
    
    
    
    
    
    
}
    
    


class CVue {

    private JFrame frame;

    private VueGrille grille;
    private VueCommandes commandes;
    private VueJoueurs joueurs;

    public CVue(Desert plateau) {
        frame = new JFrame();
        frame.setTitle("Jeu du Desert Interdit");

        frame.setLayout(new FlowLayout());

        grille = new VueGrille(plateau);
        frame.add(grille);
        commandes = new VueCommandes(plateau);
        frame.add(commandes);
        joueurs=new VueJoueurs(plateau);
        frame.add(joueurs);
        
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

class VueGrille extends JPanel implements Observer {
    private Desert plateau;
    private final static int TAILLE = 100;

    public VueGrille(Desert plateau) {
        this.plateau = plateau;
        plateau.addObserver(this);
        Dimension dim = new Dimension(800, 900); // 1366,768
        this.setPreferredSize(dim);
    }

    public void update() {
        int sable_tot=0;
        for (int i = 0; i < plateau.getTaille(); i++) {
            for (int j = 0; j < plateau.getTaille(); j++) {
                sable_tot=sable_tot+(plateau.getCase(i,j).getSable());
            }
        }
        if (!(sable_tot>=43 || plateau.getTempete()>=7)){
            repaint();
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        removeAll();
        setLayout(null);
        int sable_tot = 0;
        for (int i = 0; i < plateau.getTaille(); i++) { // Affichage Du Desert + Sable
            for (int j = 0; j < plateau.getTaille(); j++) {
                paint(g, plateau.getCase(i, j),
                        ((getWidth() / 2) - (plateau.getTaille() * (TAILLE + 5) / 2)) + i * (TAILLE + 5),
                        ((getHeight() / 2) - (plateau.getTaille() * (TAILLE + 5) / 2)) + j * (TAILLE + 5));
                // paint(g, plateau.getCase(i, j),((TAILLE+5)*plateau.getTaille())*2+i *
                // (TAILLE+5),((TAILLE+5)*plateau.getTaille())/2+j * (TAILLE+5));
                if (plateau.getCase(i, j).getType() != TypeCase.OEIL_TEMPETE) {
                    JLabel sand = new JLabel();
                    sand.setText("Sable : " + plateau.getCase(i, j).getSable());
                    sable_tot = sable_tot + plateau.getCase(i, j).getSable();
                    sand.setBounds(
                            ((getWidth() / 2) - (plateau.getTaille() * (TAILLE + 5) / 2) + TAILLE / 4)
                                    + i * (TAILLE + 5),
                            ((getHeight() / 2) - (plateau.getTaille() * (TAILLE + 5) / 2) + TAILLE / 8)
                                    + j * (TAILLE + 5)-20,
                            100, 50);
                    add(sand);
                    //if (plateau.getCase(i, j).estExploree()) { // Modifier ici pour afficher le contenu des cases
                        JLabel type = new JLabel();
                        type.setText(plateau.getCase(i, j).getType().toString());
                        sable_tot = sable_tot + plateau.getCase(i, j).getSable();
                        type.setBounds(
                                ((getWidth() / 2) - (plateau.getTaille() * (TAILLE + 5) / 2) + 4)
                                        + i * (TAILLE + 5),
                                ((getHeight() / 2) - (plateau.getTaille() * (TAILLE + 5) / 2) + TAILLE / 6)
                                        + j * (TAILLE + 5) - 20 + 10,
                                100, 50);
                        add(type);
                    
                        if (plateau.getCase(i, j).getPiece() != null && plateau.getCase(i, j).isRevealed()) {
                            JLabel pieceLabel = new JLabel();
                            pieceLabel.setText(plateau.getCase(i, j).getPiece().toString());
                            pieceLabel.setBounds(
                                    ((getWidth() / 2) - (plateau.getTaille() * (TAILLE + 5) / 2) + 4) + i * (TAILLE + 5),
                                    ((getHeight() / 2) - (plateau.getTaille() * (TAILLE + 5) / 2) + TAILLE / 6) + j * (TAILLE + 5) - 20 + 10 + 20,
                                    100, 50);
                            add(pieceLabel);
                        }
                    //}
                    
                    
                }
            }
        }
        JLabel sand = new JLabel();
        sand.setText("Sable total : " + sable_tot);
        sand.setBounds(150, 150, 100, 50);
        add(sand);
        // AFFICHAGE des Joueurs 
        int taille_joueur=20;
        int matricule=0;
        for (Joueur j:plateau.getJoueur()){
            g.setColor(j.getColor()); //// Couleur symbolisant le joueur actuelle (jouant)
            int o=10;
            if (plateau.getJoueur().length>=5){
                o=0;
            }
            g.fillOval(
                        ((getWidth() / 2) - (plateau.getTaille() * (TAILLE + 5) / 2)) + j.getX()* (TAILLE + 5)+o+((taille_joueur)*matricule),
                        ((getHeight() / 2) - (plateau.getTaille() * (TAILLE + 5) / 2)) + j.getY() * (TAILLE + 5)+((TAILLE-taille_joueur)/2)+20,taille_joueur,taille_joueur);
            matricule+=1;
        }
        JLabel actionrest = new JLabel();
        actionrest.setText("Actions restantes : "+plateau.getJoueur()[plateau.getJoueurActuel()].getActions());
        actionrest.setBounds(150,130,150,50);
        add(actionrest);
        JLabel jactuel = new JLabel();
        jactuel.setText("A votre tour : "+plateau.getJoueur()[plateau.getJoueurActuel()].getNom());
        jactuel.setForeground(plateau.getJoueur()[plateau.getJoueurActuel()].getColor());
        jactuel.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 30));
        jactuel.setBounds(150,110,500,50);
        add(jactuel);
        
    }

    private void paint(Graphics g, Case c, int x, int y) {
        if (c.getSable() >= 1) {
            Color col = new Color(246, 145, 30);
            g.setColor(col);
        } else {
            g.setColor(Color.ORANGE);
        }
        if (c.estBloquee()) {
            g.setColor(Color.RED);
        }
        if (c.getType()==TypeCase.OASIS||(c.getType()==TypeCase.MIRAGE&&(!c.estExploree()))){
            g.setColor(new Color(82,195,218));
        } 
        if (c.estExploree()) {
            if (c.getType()==TypeCase.HELICOPTERE_CRASH){
                g.setColor(Color.GRAY);
            }
        }
        if (c.getType() == TypeCase.OEIL_TEMPETE) {
            g.setColor(Color.WHITE);
        }
        g.fillRect(x, y, TAILLE, TAILLE);
    }
}

class VueCommandes extends JPanel implements Observer {

    private Desert plateau;
    private JButton Findetour;
    private JButton Bouger;
    private KeyControleur key;
    private KeyControleur key2;
    private JButton Creuser;
    private JButton Exploration;
    private JButton Ramasser;
    private JButton Enfuir;



    public VueCommandes(Desert plateau) {
        this.plateau = plateau;
        plateau.addObserver(this);
        Dimension dim = new Dimension(200, 900); // 1366,768
        this.setPreferredSize(dim);
        paintComponent();
    }
    public void paintComponent(){
        setLayout(null);
        Findetour = new JButton("Fin de tour"); // Bouton fin de tour 
        Findetour.setBounds(0, 300, 100, 25); 
        add(Findetour);
        Controleur FinTour = new Controleur(plateau);
        Findetour.addActionListener(FinTour); 
        Bouger = new JButton("Bouger"); // Bouton Exploration (une fois presse permet d'entrer une action au clavier (haut,bas,droite,gauche) avec les flèches)= deplacement.
        Bouger.setBounds(0, 330, 100, 25);
        add(Bouger);
        key=new KeyControleur(plateau,Mode.DEPLACER);
        Bouger.addKeyListener(key);
        Bouger.addActionListener( new ActionListener () { // Déclaration interne de la reaction à la pression du bouton
            public void actionPerformed(ActionEvent e){
                key.setEnabled(!(key.isEnabled()));
            }
        });
        Creuser = new JButton("Creuser"); // bouton action de creuser
        Creuser.setBounds(0,360,100,25);
        add(Creuser);
        key2=new KeyControleur(plateau,Mode.CREUSER);
        Creuser.addKeyListener(key2);
        Creuser.addActionListener( new ActionListener () { // Déclaration interne de la reaction à la pression du bouton
            public void actionPerformed(ActionEvent e){
                key2.setEnabled(!(key2.isEnabled()));
            }
        });
        Exploration = new JButton("Exploration"); // bouton action d'explorer la case actuelle
        Exploration.setBounds(0,390,100,25);
        add(Exploration);
        Exploration.addActionListener( new ActionListener () { // Déclaration interne de la reaction à la pression du bouton
            public void actionPerformed(ActionEvent e){
                plateau.getJoueur()[plateau.getJoueurActuel()].explorer();
            }
        });
        
        Ramasser = new JButton("Ramasser");
        Ramasser.setBounds(0, 420, 100, 25);
        add(Ramasser);
        Ramasser.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            plateau.getJoueur()[plateau.getJoueurActuel()].ramasserPiece();
        }
        });
        Enfuir = new JButton("S'Enfuir");
        Enfuir.setBounds(0, 450, 100, 25);
        add(Enfuir);
        Enfuir.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (plateau.partieGagnee()) {
                Findetour.setEnabled(false);
                Creuser.setEnabled(false);
                Bouger.setEnabled(false);
                Enfuir.setEnabled(false);
                Exploration.setEnabled(false);
                Ramasser.setEnabled(false);
                JLabel end = new JLabel();  // Texte en cas de Victoire
                end.setBounds(0,425,300,150);
                end.setText("Vous vous êtes enfuis");
                add(end);
                repaint();
            }
        }
        });
        
    }

    public void update(){
        int sable_tot=0;
        boolean dead=false;
        for (int i = 0; i < plateau.getTaille(); i++) {
            for (int j = 0; j < plateau.getTaille(); j++) {
                sable_tot=sable_tot+(plateau.getCase(i,j).getSable());
            }
        }
        for (Joueur j:plateau.getJoueur()){
            if(j.getNiveauEau()<=0){
                dead=true;
            }
        }
        if (sable_tot>=43 || plateau.getTempete()>=7 || dead){
            JLabel end = new JLabel();  // Texte en cas de defaite 
            JLabel how = new JLabel();  // Texte en cas de defaite 
            end.setBounds(0,425,300,150);
            end.setText("Vous avez perdu,");
            add(end);
            if (sable_tot>=43){
                Findetour.setEnabled(false);
                Creuser.setEnabled(false);
                Bouger.setEnabled(false);
                Enfuir.setEnabled(false);
                Exploration.setEnabled(false);
                Ramasser.setEnabled(false);
                how.setText("par ensablement !");
                how.setBounds(0,450,300,150);
            } else if (plateau.getTempete()>=7){
                Findetour.setEnabled(false);
                Creuser.setEnabled(false);
                Bouger.setEnabled(false);
                Enfuir.setEnabled(false);
                Exploration.setEnabled(false);
                Ramasser.setEnabled(false);
                how.setText("a cause de la tempete!");
                how.setBounds(0,450,300,150);
            } else if (dead){
                Findetour.setEnabled(false);
                Creuser.setEnabled(false);
                Bouger.setEnabled(false);
                Enfuir.setEnabled(false);
                Exploration.setEnabled(false);
                Ramasser.setEnabled(false);
                how.setText("l'un d'entre vous est mort !");
                how.setBounds(0,450,300,150);
            }
            add(how);
            repaint();
        }
        }
    }

enum Perso{
    UN,
    DEUX,
    TROIS,
    QUATRE,
    CINQ;

}

class Controleur implements ActionListener {

    Desert plateau;
    Perso perso;

    public Controleur(Desert plateau) {
        this.plateau = plateau;
        this.perso=null;
    }
    public Controleur(Desert plateau,Perso p) {
        this.plateau = plateau;
        this.perso=p;
    }

    public void actionPerformed(ActionEvent e) {
        if (perso==null){
            if (plateau.getJoueurActuel()==(plateau.getJoueur().length-1)){
                plateau.actionDesert(plateau.getJoueur());
            }
            plateau.getJoueur()[plateau.getJoueurActuel()].resetActions();
            plateau.next();
            plateau.notifyObservers();
        } else if (perso==Perso.UN){
            plateau.getJoueur()[plateau.getJoueurActuel()].donnerEau(plateau.getJoueur()[0],1);
        } else if (perso==Perso.DEUX){
            plateau.getJoueur()[plateau.getJoueurActuel()].donnerEau(plateau.getJoueur()[1],1);
        } else if (perso==Perso.TROIS){
            plateau.getJoueur()[plateau.getJoueurActuel()].donnerEau(plateau.getJoueur()[2],1);
        } else if (perso==Perso.QUATRE){
            plateau.getJoueur()[plateau.getJoueurActuel()].donnerEau(plateau.getJoueur()[3],1);
        } else if (perso==Perso.CINQ){
            plateau.getJoueur()[plateau.getJoueurActuel()].donnerEau(plateau.getJoueur()[4],1);
        }
    }
}

enum Mode{
    CREUSER,
    DEPLACER;
}

class KeyControleur implements KeyListener {

    Desert plateau;
    boolean enable;
    private final Mode mode;

    public KeyControleur(Desert plateau,Mode m) {
        this.plateau = plateau;
        this.mode=m;
        enable=false;
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) { // Action du joueur avec les fleches du clavier
        if (enable){
            Joueur j=plateau.getJoueur()[plateau.getJoueurActuel()];
            if (mode==Mode.DEPLACER){ // On verifie l'action a faire
                    if (j.getActions()>0){
                        int k=e.getKeyCode();
                        if (k==37){
                            j.deplacer(j.getX()-1, j.getY());  // GAUCHE
                        }
                        if (k==38){
                            j.deplacer(j.getX(), j.getY()-1);    //HAUT
                        }
                        if (k==39){
                            j.deplacer(j.getX()+1, j.getY());   //DROITE
                        }
                        if (k==40){
                            j.deplacer(j.getX(), j.getY()+1);   //BAS
                        }
                        if (k==17){
                            if (plateau.getCase(j.getX(), j.getY()).getType()==TypeCase.TUNNEL){
                                j.passageTunnel();   //PASSAGE TUNNEL
                            }
                        }
                    }
                this.setEnabled(!(this.isEnabled()));
            } else if (mode==Mode.CREUSER){  // On verifie l'action a faire
                if (j.getActions()>0){
                    int k=e.getKeyCode();
                    if (k==37){
                        j.creuser(j.getX()-1, j.getY());  // GAUCHE
                    }
                    if (k==38){
                        j.creuser(j.getX(), j.getY()-1);    //HAUT
                    }
                    if (k==39){
                        j.creuser(j.getX()+1, j.getY());   //DROITE
                    }
                    if (k==40){
                        j.creuser(j.getX(), j.getY()+1);   //BAS
                    }
                    if (k==17){
                        j.creuser(j.getX(), j.getY());   //CONTROLE ou CTRL pour creuser la case actuelle
                    }
                }
                this.setEnabled(!(this.isEnabled()));
            }
        }
    }

    public void keyTyped(KeyEvent e) {
    }
    public boolean isEnabled(){
        return enable;
    }
    public void setEnabled(boolean b){
        this.enable=b;
    }
}

class VueJoueurs extends JPanel implements Observer {

    private Desert plateau;
    private int width_card=200;
    private int height_card=150;
    private int gap=3; // pixels
    private int startx =0;
    private int starty =200;
    private JButton Give;


    public VueJoueurs(Desert plateau) {
        this.plateau = plateau;
        plateau.addObserver(this);
        Dimension dim = new Dimension(600, 900); // 1366,768
        this.setPreferredSize(dim);
        paintComponent();
    }

    public void update(){
        removeAll();
        paintComponent();
        repaint();
    }

    public void paintComponent(){ // Creation des cartes de chaques joueurs
        int compt=0;
        int compt_lignes=0;
        int matricule=0;
        for (Joueur h:plateau.getJoueur()){
            if (compt*(width_card+gap)+startx+width_card>=600){
                compt_lignes+=1;
                compt=0;
            }
     
            int posX=compt*(width_card+gap);
            int posY=compt_lignes*(height_card+gap);
            JLabel name=new JLabel(h.getNom());
            name.setBounds(((startx+posX+gap)+width_card/3),(starty+posY+gap)+gap,150,20);
            add(name);
            JLabel water=new JLabel("Niveau de Gourde : "+ h.getNiveauEau());
            water.setBounds(((startx+posX+gap)+width_card/5),(starty+posY+gap)+height_card/6,150,10);
            add(water);
            JLabel pieces=new JLabel("Pieces : "+ h.getStringPieces());
            pieces.setBounds(((startx+posX+gap)+width_card/5),(starty+posY+gap)+height_card/6+15,150,10);
            add(pieces);
            Give = new JButton("Donner Eau"); // bouton action d'explorer la case actuelle
            Give.setBounds(((startx+posX+gap)+width_card/5),(starty+posY+gap)+height_card/6+30,100,25);
            add(Give);
            Perso p=Perso.UN;
            if (matricule==0){
                p=Perso.UN;
            }if (matricule==1){
                p=Perso.DEUX;
            }if (matricule==2){
                p=Perso.TROIS;
            }if (matricule==3){
                p=Perso.QUATRE;
            }if (matricule==4){
                p=Perso.CINQ;
            }
            Controleur con=new Controleur(plateau,p);
            Give.addActionListener(con);
            JLabel inside=new JLabel("");
            inside.setBounds(startx+posX+gap,starty+posY+gap,width_card-(gap*2),height_card-(gap*2));
            inside.setBackground(Color.WHITE);
            inside.setOpaque(true);
            add(inside);
            setLayout(null);
            JLabel outline=new JLabel("");
            outline.setBounds(startx+posX,starty+posY,width_card,height_card);
            outline.setBackground(h.getColor());
            outline.setOpaque(true);
            add(outline);
            compt+=1;
            matricule+=1;
        }
    }
    }

    /**class CarteJoueurs extends JPanel implements Observer {

        private Desert plateau;
        private Joueur joueur;
        private int card_height=150;
        private int card_width=200;
    
    
        public CarteJoueurs(Desert plateau,Joueur j) {
            this.plateau = plateau;
            plateau.addObserver(this);
            Dimension dim = new Dimension(card_width, card_height); // 1366,768
            setBackground(j.getColor());
            this.setPreferredSize(dim);
            paintComponent();
        }
        public void paintComponent(){
 
        }
        public int getHeight(){
            return card_height;
        }
        public int getWidth(){
            return card_width;
        }
    
        public void update(){
                repaint();
            }
        }
        **/
