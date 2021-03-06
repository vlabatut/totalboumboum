package org.totalboumboum.ai.v200910.ais.aldanmazyenigun.v5c;

import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.ai.v200910.adapter.path.AiPath;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * 
 * @version 5.c
 * 
 * @author Cansın Aldanmaz
 * @author Yalçın Yenigün
 * 
 */
@SuppressWarnings("deprecation")
public class BombController {

	/**
	 * controle si on peut acceder a une case sur, si on met une bombe.
	 * 
	 * @param ai
	 *            Description manquante !
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	public BombController(AldanmazYenigun ai)
			throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE

		this.ai = ai;
		zone = ai.getZone();

		// init destinations
		arrived = false;
		AiHero ownHero = ai.getOwnHero();
		possibleDest = ai.findAbstractSafeTiles(ownHero.getTile());
		updatePath();
	}

	// ///////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE /////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	/** l'IA concernée par ce gestionnaire de chemin */
	private AldanmazYenigun ai;
	/** zone de jeu */
	private AiZone zone;

	// ///////////////////////////////////////////////////////////////
	// DESTINATION /////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	/** indique si le personnage est arrivé à destination */
	private boolean arrived;
	/** la case de destination sélectionnée pour la fuite */
	private AiTile tileDest;
	/** destinations potentielles */
	private List<AiTile> possibleDest;

	/**
	 * détermine si le personnage est arrivé dans la case de destination. S'il
	 * n'y a pas de case de destination, on considère que le personnage est
	 * arrivé.
	 * 
	 * @return Description manquante !
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	public boolean hasArrived() throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE

		if (!arrived) {
			if (tileDest == null)
				arrived = true;
			else {
				AiTile currentTile = ai.getActualTile();
				arrived = currentTile == tileDest;
			}
		}
		return arrived;
	}

	// ///////////////////////////////////////////////////////////////
	// PATH /////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	/** le chemin à suivre */
	private AiPath path;

	/**
	 * 
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	private void updatePath() throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE

		path = ai.astar.processShortestPath(ai.getActualTile(), possibleDest);
		tileDest = path.getLastTile();
	}

	/**
	 * vérifie que le personnage est bien sur le chemin pré-calculé, en
	 * supprimant si besoin les cases inutiles (car précedant la case courante).
	 * Si le personnage n'est plus sur le chemin, alors le chemin est vide après
	 * l'exécution de cette méthode.
	 * 
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	private void checkIsOnPath() throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE

		AiTile currentTile = ai.getActualTile();
		while (!path.isEmpty() && path.getTile(0) != currentTile) {
			ai.checkInterruption(); // APPEL OBLIGATOIRE
			path.removeTile(0);
		}
	}

	/**
	 * teste si le chemin est toujours valide, i.e. si aucun obstacle n'est
	 * apparu depuis la dernière itération. Contrairement au PathManager, ici
	 * pour simplifier on ne teste que l'apparition de nouveaux obstacles (feu,
	 * bombes, murs), et non pas les changement concernant la sûreté des cases.
	 * En d'autres termes, si une bombe apparait avant que le personnage d'ait
	 * atteint une case sure, elle ne sera pas prise en compte dans la
	 * trajectoire.
	 * 
	 * @return ? Description manquante !
	 * @throws StopRequestException
	 *             Description manquante !
	 * 
	 */
	private boolean checkPathValidity() throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE

		boolean result = true;
		Iterator<AiTile> it = path.getTiles().iterator();
		while (it.hasNext() && result) {
			ai.checkInterruption(); // APPEL OBLIGATOIRE
			AiTile tile = it.next();
			result = tile.isCrossableBy(ai.getOwnHero());
		}
		return result;
	}

	// ///////////////////////////////////////////////////////////////
	// COST CALCULATOR			 /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	/**
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	private void updateCostCalculator() throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE

		// calcul de la matrice de coût : on prend l'opposé du niveau de sûreté
		// i.e. : plus le temps avant l'explosion est long, plus le coût est
		// faible
		// double dangerMatrix[][] = ai.getZoneFormee().getMatrix();
		for (int line = 0; line < zone.getHeight(); line++) {
			ai.checkInterruption(); // APPEL OBLIGATOIRE
			for (int col = 0; col < zone.getWidth(); col++) {
				ai.checkInterruption(); // APPEL OBLIGATOIRE
				double cost = ai.getZoneFormee().getCaseLevel(line, col);
				ai.costCalculator.setCost(line, col, cost);
			}
		}
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	/**
	 * calcule la prochaine direction pour aller vers la destination (ou renvoie
	 * Direction.NONE si aucun déplacement n'est nécessaire)
	 * 
	 * @return Description manquante !
	 * @throws StopRequestException
	 *             Description manquante !
	 * */
	public Direction update() throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE

		// on met d'abord à jour la matrice de cout
		updateCostCalculator();

		Direction result = Direction.NONE;
		if (!hasArrived()) { // on vérifie que le joueur est toujours sur le
								// chemin
			checkIsOnPath();
			// si le chemin est vide ou invalide, on le recalcule.
			if (path.isEmpty() || !checkPathValidity())
				updatePath();
			// s'il reste deux cases au moins dans le chemin, on se dirige vers
			// la suivante
			AiTile tile = null;
			if (path.getLength() > 1)
				tile = path.getTile(1);
			// sinon, s'il ne reste qu'une seule case, on va au centre
			else if (path.getLength() > 0)
				tile = path.getTile(0);
			// on détermine la direction du prochain déplacement
			if (tile != null)
				result = zone.getDirection(ai.getOwnHero(), tile);
		}
		return result;
	}

	/**
	 * @return Description manquante !
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	public boolean isThereSafeTiles() throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE

		boolean result = false;
		// on met d'abord à jour la matrice de cout
		updateCostCalculator();
		if (!hasArrived()) { // on vérifie que le joueur est toujours sur le
								// chemin
			checkIsOnPath();

			// si le chemin est vide ou invalide, on le recalcule.
			if (path.isEmpty() || !checkPathValidity())
				updatePath();
			// s'il reste deux cases au moins dans le chemin, on se dirige vers
			// la suivante
			@SuppressWarnings("unused")
			AiTile tile = null;
			if (path.getLength() > 1) {
				if (ai.getDangerLevel(path.getTile(1)) == 0) {
					result = true;
					tile = path.getTile(1);
				}
			}
			// sinon, s'il ne reste qu'une seule case, on va au centre
			else if (path.getLength() > 0)
				tile = path.getTile(0);
			// on détermine la direction du prochain déplacement
			// if(tile!=null)
			// result = true;
			// else
			if (path.isEmpty())
				result = false;
		}
		return result;
	}

	/**
	 * 
	 * @return Description manquante !
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	public AiPath getPath() throws StopRequestException {
		ai.checkInterruption();
		return path;
	}
}
