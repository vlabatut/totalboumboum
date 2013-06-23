package org.totalboumboum.ai.v201011.ais.isbirkoyas.v4;

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
	public final int BONUS = 1000;
	public final int ATTAQUE = 1;
	public int bonusInit;
	public int DESTRUCTIBLEINIT = 200;
	public boolean modeCollect = false;
	public boolean modeAttaque = false;
	public boolean plein;
	public boolean senfuire = false;
	public boolean senfuire2 = false;
	public boolean poserBombe;// poserbombe2=true;
	public boolean attaqueBombe;
	public boolean collectBombe;
	public boolean print = false, print2 = false;
	public boolean courir;
	int i = 0, k = 1;
	double t;

	/**
	 * méthode appelée par le moteur du jeu pour obtenir une action de notre IA
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
		// la longueur de la zone
		int width = gameZone.getWidth();
		// la largeur de la zone
		int height = gameZone.getHeight();
		AiAction resultat = new AiAction(AiActionName.NONE);
		// Notre hero dans cette zone
		this.ourHero = gameZone.getOwnHero();
		// la matrice de collecte
		int[][] matriceCollect = new int[height][width];
		// la matrice d'attaque
		double[][] matriceAttaque = new double[height][width];
		plein = false;
		// AiOutput a;

		// Détermination du mode
		mode(gameZone);
		// si le mode: collecte
		if (modeCollect) {
			checkInterruption();
			bonusInit = BONUS;
			if (print)
				System.out.println("le mode collecte");

			// La desicion du posage de bombe
			posageBombe.deciderPoserBombeCollect(gameZone);
			// Initialise la matrice collecte
			traitementCollect.initialiseMatrice(matriceCollect, gameZone);
			// Calcul de la matrice collecte
			traitementCollect.collectMatrice(matriceCollect, gameZone);
			resultat = deplacementCollecte.algorithmCollect(matriceCollect,
					gameZone, resultat);
			// Affiche la matrice collecte
			if (print2)
				traitementCollect.affiche(matriceCollect, gameZone);
		}
		// si le mode : attaque
		if (modeAttaque) {
			checkInterruption();
			if (print)
				System.out.println("le mode attaque");

			// La desicion du posage de bombe
			posageBombe.deciderPoserBombeAttaque(gameZone);
			// Initialise la matrice attaque
			traitementAttaque.initialiseMatrice(matriceAttaque, gameZone);
			// Calcul de la matrice attaque
			traitementAttaque.attaqueMatrice(matriceAttaque, gameZone);
			resultat = deplacementAttaque.algorithmAttaque(matriceAttaque,
					gameZone, resultat);
			// Affiche la matrice collecte
			if (print2)
				traitementAttaque.affiche(matriceAttaque, gameZone);
		}
		return resultat;
	}

	/**
	 * Methode qui fait la decision du mode collecte ou attaque?
	 * 
	 * @param gameZone
	 *            la zone du jeu
	 * @return true or false
	 * @throws StopRequestException
	 */
	private void mode(AiZone gameZone) throws StopRequestException {
		checkInterruption();

		if (ourHero.getBombNumberCurrent() < 3) {
			if (!gameZone.getItems().isEmpty()) {
				if (print)
					System.out
							.println("IA possede moins de 3 bombe et il y a des bonus sur le terrain donc mode collecte");
				modeCollect = true;
				modeAttaque = false;

			} else {
				if (print)
					System.out
							.println("IA possede moins de 3 bombe et il y n'a pas des bonus sur le terrain donc mode attaque");
				modeAttaque = true;
				modeCollect = false;

			}
		} else {
			if (print)
				System.out
						.println("IA possede plus de de 2 bombe donc mode attaque.");
			modeAttaque = true;
			modeCollect = false;

		}
	}

	// LES METHODES D'ACCES

	public Securite Securite() throws StopRequestException {
		checkInterruption();
		return securite;

	}

	public PosageBombe PosageBombe() throws StopRequestException {
		checkInterruption();
		return posageBombe;

	}

	public DeplacementCommune DeplacementCommune() throws StopRequestException {
		checkInterruption();
		return deplacementCommune;
	}

	public DeplacementCollecte DeplacementCollecte()
			throws StopRequestException {
		checkInterruption();
		return deplacementCollecte;
	}

	public DeplacementAttaque DeplacementAttaque() throws StopRequestException {
		checkInterruption();
		return deplacementAttaque;
	}

	public TraitementAttaque TraitementAttaque() throws StopRequestException {
		checkInterruption();
		return traitementAttaque;
	}

	public TraitementCollect TraitementCollect() throws StopRequestException {
		checkInterruption();
		return traitementCollect;
	}

}
