package org.totalboumboum.ai.v201011.ais.isbirkoyas.v5;

import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;

/**
 * @author Ela Koyas
 * @author Goksu Isbir
 * 
 */
public class IsbirKoyas extends ArtificialIntelligence {

	private Securite securite = null;
	private DeplacementCommune deplacementCommune = null;
	private DeplacementAttaque deplacementAttaque = null;
	private DeplacementCollecte deplacementCollecte = null;
	private TraitementAttaque traitementAttaque = null;
	private TraitementCollect traitementCollect = null;
	private PosageBombe posageBombe = null;
	public AiPath nextMove = null;
	public AiPath nextMove2 = null;
	public AiPath nextMove3 = null;
	public AiHero ourHero;

	public final double DEPLACEMENT = 1000;
	public final int ATTAQUE = 1;
	public int bonusInit;
	public boolean modeCollect = false;
	public boolean modeAttaque = false;
	public boolean senfuire = false;
	public boolean senfuire2 = false;
	public boolean poserBombe;// poserbombe2=true;
	public boolean attaqueBombe;
	public boolean collectBombe;
	public boolean print = false, print2 = false;
	public boolean courir;
	public boolean jeu = true;
	int i = 0, k = 1, compteur = 0;
	double t;

	/**
	 * mCette méthode appelée par le moteur du jeu pour obtenir une action de
	 * notre IA .Ici on fait appelle aux autres méthodes des autres classes.
	 * 
	 * @return resultat
	 * */
	public AiAction processAction() throws StopRequestException {
		// avant tout: test d'interruption
		checkInterruption();
		DeplacementAttaque deplacementAttaque = new DeplacementAttaque(this);
		DeplacementCollecte deplacementCollecte = new DeplacementCollecte(this);
		TraitementAttaque traitementAttaque = new TraitementAttaque(this);
		TraitementCollect traitementCollect = new TraitementCollect(this);
		PosageBombe posageBombe = new PosageBombe(this);

		// La perception instantanement de l'environnement
		AiZone gameZone = getPercepts();
		// la largeur de la zone
		int height = gameZone.getHeight();
		// la longueur de la zone
		int width = gameZone.getWidth();
		AiAction resultat = new AiAction(AiActionName.NONE);
		// Notre hero dans cette zone
		this.ourHero = gameZone.getOwnHero();
		// la matrice de collecte
		int[][] matriceCollect = new int[height][width];
		// la matrice d'attaque
		double[][] matriceAttaque = new double[height][width];
		if (!ourHero.getTile().getItems().isEmpty()) {
			compteur++;
			if (print)
				System.out.println("Notre IA a collecté un bonus! Compteur: "
						+ compteur);
		}
		// DÃ©termination du mode
		mode(gameZone);
		if (gameZone.getRemainingHeroes().contains(ourHero))
			jeu = true;
		else
			jeu = false;
		// si le mode: collecte
		if (modeCollect && jeu) {
			checkInterruption();
			bonusInit = traitementCollect.BONUS;
			if (print)
				System.out.println("le mode collecte");

			// La desicion du posage de bombe
			posageBombe.deciderPoserBombeCollect(gameZone);
			// Initialise la matrice collecte
			traitementCollect.initialiseMatrice(matriceCollect, gameZone);
			// Calcul de la matrice collecte
			traitementCollect.collectMatrice(matriceCollect, gameZone);
			// Affiche la matrice collecte
			if (print2)
				traitementCollect.affiche(matriceCollect, gameZone);
			resultat = deplacementCollecte.algorithmCollect(matriceCollect,
					gameZone, resultat);
			if (print)
				System.out
						.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

		}
		// si le mode : attaque
		if (modeAttaque && jeu) {
			checkInterruption();
			if (print)
				System.out.println("le mode attaque");

			// La desicion du posage de bombe
			posageBombe.deciderPoserBombeAttaque(gameZone);
			// Initialise la matrice attaque
			traitementAttaque.initialiseMatrice(matriceAttaque, gameZone);
			// Calcul de la matrice attaque
			traitementAttaque.attaqueMatrice(matriceAttaque, gameZone);
			// Affiche la matrice attaque
			if (print2)
				traitementAttaque.affiche(matriceAttaque, gameZone);
			resultat = deplacementAttaque.algorithmAttaque(matriceAttaque,
					gameZone, resultat);

			if (print)
				System.out
						.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		}
		return resultat;
	}

	/**
	 * Cette méthode fait la décision du mode collecte ou attaque en fonction du
	 * nombre de bombes de notre IA et la zone du jeu. Elle prend un seul
	 * argument qui est la zone du jeu.
	 * 
	 * @param gameZone
	 *            la zone du jeu
	 * @return true ou false
	 * @throws StopRequestException
	 */
	private void mode(AiZone gameZone) throws StopRequestException {
		checkInterruption();
		if (print)
			System.out.println("Nombre de la bombe:" + ourHero.getBombRange());
		if (!gameZone.getItems().isEmpty() && compteur < 2) {
			if (print)
				System.out
						.println("IA possede moins de 3 bombe et il y a des bonus sur le terrain donc mode collecte");
			modeCollect = true;
			modeAttaque = false;

		} else {
			if (print)
				System.out
						.println("IA possede plus de de 2 bombe donc mode attaque.");
			modeAttaque = true;
			modeCollect = false;

		}
	}

	// LES METHODES D'ACCES
	/**
	 * METHODE D'ACCES a la classe DeplacementCommune
	 * 
	 * @throws StopRequestException
	 * @return deplacementcommune
	 */
	public Securite Securite() throws StopRequestException {
		checkInterruption();
		return securite;

	}

	/**
	 * METHODE D'ACCES a la classe DeplacementCommune
	 * 
	 * @throws StopRequestException
	 * @return deplacementcommune
	 */
	public PosageBombe PosageBombe() throws StopRequestException {
		checkInterruption();
		return posageBombe;

	}

	/**
	 * METHODE D'ACCES a la classe DeplacementCommune
	 * 
	 * @throws StopRequestException
	 * @return deplacementcommune
	 */
	public DeplacementCommune DeplacementCommune() throws StopRequestException {
		checkInterruption();
		return deplacementCommune;
	}

	/**
	 * METHODE D'ACCES a la classe DeplacementCollecte
	 * 
	 * @throws StopRequestException
	 * @return deplacementcollecte
	 */
	public DeplacementCollecte DeplacementCollecte()
			throws StopRequestException {
		checkInterruption();
		return deplacementCollecte;
	}

	/**
	 * METHODE D'ACCES a la classe DeplacementAttaque
	 * 
	 * @throws StopRequestException
	 * @return deplacementAttaque
	 */
	public DeplacementAttaque DeplacementAttaque() throws StopRequestException {
		checkInterruption();
		return deplacementAttaque;
	}

	/**
	 * METHODE D'ACCES a la classe TraitementAttaque
	 * 
	 * @throws StopRequestException
	 * @return traitementAttaque
	 */
	public TraitementAttaque TraitementAttaque() throws StopRequestException {
		checkInterruption();
		return traitementAttaque;
	}

	/**
	 * METHODE D'ACCES a la classe TraitementCollect
	 * 
	 * @throws StopRequestException
	 * @return traitementCollect
	 */
	public TraitementCollect TraitementCollect() throws StopRequestException {
		checkInterruption();
		return traitementCollect;
	}

}
