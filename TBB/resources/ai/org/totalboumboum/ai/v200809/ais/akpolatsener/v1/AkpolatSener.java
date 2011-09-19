package org.totalboumboum.ai.v200809.ais.akpolatsener.v1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

import org.totalboumboum.ai.v200809.adapter.AiAction;
import org.totalboumboum.ai.v200809.adapter.AiActionName;
import org.totalboumboum.ai.v200809.adapter.AiBomb;
import org.totalboumboum.ai.v200809.adapter.AiHero;
import org.totalboumboum.ai.v200809.adapter.AiTile;
import org.totalboumboum.ai.v200809.adapter.AiZone;
import org.totalboumboum.ai.v200809.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200809.adapter.StopRequestException;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * 
 * @author Cem Akpolat
 * @author Emre Sener
 *
 */
public class AkpolatSener extends ArtificialIntelligence {
	
	/** indique la minimum distance necessaire pour poser une bombe */
	int distanceToEnemy = 3;
	
	/** les cases actuel, precedent et prochain */
	AiTile currentTile = null;
	AiTile previousTile = null;
	AiTile nextTile = null;
	
	/** zone du jeu */	
	AiZone zone = null;
	
	/** les personnages de soi-meme et l'enemie */	
	AiHero ownHero;
	AiHero enemy;
	
	/** objet pour acceder les champs et 
	 * les methodes concernant les personnages */	
	Hero hero;
	
	/** objet pour acceder les champs et 
	 * les methodes concernant les cas voisins */
	Neighbors neighbors;
	
	/** direction du cas actuel vers le prochain*/
	Direction direction;
	
	/** liste des cas visit�s avec ses champs*/
	List<XTile> xTiles = new ArrayList<XTile>();
	
	/** action de resultat à renvoyer*/
	AiAction result = new AiAction(AiActionName.NONE);
	
	/**
	 * methode obligatoire
	 * @return	action d'IA
	 */
	public AiAction processAction() throws StopRequestException {
		checkInterruption();

		// initialisation des zone, personnage et case actuelle
		zone = getPercepts();
		ownHero = zone.getOwnHero();
		currentTile = ownHero.getTile();

		// obtenir l'enemie la plus proche
		hero = new Hero(this, zone, ownHero);
		enemy = hero.getNearHero();

		if (nextTile == null)
			nextTile = currentTile;

		// si aucune case n'est pas visit�e, case actuelle est ajout�. Sinon les champs de la case de type XTile sont actualis�s
		if (xTiles.isEmpty()) {
			XTile xCurrentTile = new XTile(this, currentTile);
			xCurrentTile.visits = 1;
			xTiles.add(xCurrentTile);
		} else {
			boolean currentTileAdded = false;
			for (int i = 0; i < xTiles.size(); i++) {
				checkInterruption();
				
				if (xTiles.get(i).tile == currentTile) {
					xTiles.get(i).visits++;
					currentTileAdded = true;
				}
			}

			if (!currentTileAdded) {
				XTile XCurrentTile = new XTile(this, currentTile);
				XCurrentTile.visits = 1;

				xTiles.add(XCurrentTile);
			}
		}

		// si IA n'est pas mort, il va selectionner la case possible
		if (ownHero != null) {
			try {
				selectTile(currentTile);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else
			result = new AiAction(AiActionName.NONE);

		return result;
	}

	/**
	 * determine l'action la plus logique
	 * @param tile la case dont on cherche les voisines
	 */
	public void selectTile(AiTile tile) throws Exception {
		checkInterruption();

		Hero ownHero = new Hero(this, this.zone, this.ownHero);

		Iterator<AiBomb> bombs = zone.getBombs().iterator();

		int purpleBombs = 0;

		while (bombs.hasNext()) {
			checkInterruption();
			
			if (bombs.next().getColor() == PredefinedColor.PURPLE)
				purpleBombs++;
		}

		// s'il n'existe pas de probleme et IA a seulement une bombe dans la zone
		if (ownHero.canPutBomb(distanceToEnemy) && purpleBombs < 1) {
			result = new AiAction(AiActionName.DROP_BOMB);
			distanceToEnemy--;

			if (distanceToEnemy < 1)
				distanceToEnemy = 3;

		} 
		else //si IA ne peut pas poser la bombe, il va essayer de selectionner la case la plus possible pour aller 
		{
			nextTile = findPath();
			direction = zone.getDirection(currentTile, nextTile);

			if (direction != null)
				result = new AiAction(AiActionName.MOVE, direction);
			else
				result = new AiAction(AiActionName.NONE);
		}

	}

	/**
	 * renvoie le cas la plus possible pour aller
	 * @return	le cas la plus propre
	 */
	public AiTile findPath() throws Exception {
		checkInterruption();
		
		List<AiTile> possibleTiles;

		AiTile bestTile = null;

		neighbors = new Neighbors(this, zone, currentTile);

		// les cases voisines propres qui ne sont pas dans la port�e d'une bombe
		possibleTiles = neighbors.findNeighborsNotInBombRange();

		// s'il n y a pas de voisins propres qui ne sont pas dans la port�e d'une bombe 
		if (possibleTiles.size() == 0)
		{
			if (currentTile.getBombs().isEmpty() && currentTile.getFires().isEmpty() && !neighbors.isInBombRange(currentTile, 10)) 
				bestTile = currentTile;
			else 
			{
				possibleTiles = neighbors.findCleanNeighbors();
				
				// s'il n y a pas de voisins propres /
				if (possibleTiles.size() == 0)
					bestTile = currentTile;
				else
					bestTile = selectMostPrior(possibleTiles);

			}
		} 
		else
			bestTile = selectMostPrior(possibleTiles);

		previousTile = currentTile;

		return bestTile;

	}

	
	/**
	 * renvoie la bonne case parmi des cas voisins propres
	 * 
	 * @param tiles
	 * @return la case ayant la plus haute priorité dans la liste
	 * @throws StopRequestException
	 */	
	public AiTile selectMostPrior(List<AiTile> tiles)	throws StopRequestException 
	{
		checkInterruption();
		
		XAStarComparator comp = new XAStarComparator();
		PriorityQueue<XTile> prtXTile = new PriorityQueue<XTile>(1, comp);
		
		boolean isInXTiles = false;
		
		// on ajoute les cases qui ne sont pas dans la liste des cases visit�s à la liste XTiles et PriorityQueue
		for (int cntTiles = 0; cntTiles < tiles.size(); cntTiles++) 
		{
			checkInterruption();
			
			for (int cntXTiles = 0; cntXTiles < xTiles.size(); cntXTiles++)
			{
				checkInterruption();
				
				if (xTiles.get(cntXTiles).tile == tiles.get(cntTiles)) 
				{
					prtXTile.offer(xTiles.get(cntXTiles));
					isInXTiles = true;
				}

			}

			if (!isInXTiles) 
			{
				XTile xNeighborTile = new XTile(this, tiles.get(cntTiles));
				xNeighborTile.visits = 1;
				xTiles.add(xNeighborTile);
				prtXTile.offer(xNeighborTile);
			}

		}

		return prtXTile.poll().tile;

	}

}
