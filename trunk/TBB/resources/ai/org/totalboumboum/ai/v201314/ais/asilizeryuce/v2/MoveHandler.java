package org.totalboumboum.ai.v201314.ais.asilizeryuce.v2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.totalboumboum.ai.v201314.adapter.data.AiBlock;
import org.totalboumboum.ai.v201314.adapter.path.cost.ApproximateCostCalculator;
import org.totalboumboum.ai.v201314.adapter.path.cost.TimeCostCalculator;
import org.totalboumboum.ai.v201314.adapter.path.heuristic.TimeHeuristicCalculator;
import org.totalboumboum.ai.v201314.adapter.path.successor.ApproximateSuccessorCalculator;
import org.totalboumboum.ai.v201314.adapter.path.successor.SearchMode;
import org.totalboumboum.ai.v201314.adapter.path.successor.TimePartialSuccessorCalculator;
import org.totalboumboum.ai.v201314.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.adapter.path.AiLocation;
import org.totalboumboum.ai.v201314.adapter.path.AiPath;
import org.totalboumboum.ai.v201314.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201314.adapter.path.search.Astar;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe gérant le déplacement de l'agent. 
 * 
 * @author Emre Asıl
 * @author Tülin İzer
 * @author Miray Yüce
 */
public class MoveHandler extends AiMoveHandler<Agent> {

	/** c'est une case qui est sure pour notre agent*/
	AiTile safeDestination = null;

	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai
	 *            l'agent que cette classe doit gérer.
	 */
	protected MoveHandler(Agent ai) {
		super(ai);
		ai.checkInterruption();

		// dans cette classe, on aura généralement besoin d'un objet de type
		// Astar.
		// à titre d'exemple, on construit ici un objet Astar très simple (pas
		// forcément très efficace)
		// pour des raisons de rapidité, il est recommandé de créer l'objet
		// Astar une seule fois,
		// et non pas à chaque itération. Cela permet aussi d'éviter certains
		// problèmes de mémoire.
		zone = ai.getZone();
		ownHero = zone.getOwnHero();

		ApproximateCostCalculator approximateCostCalculator = new ApproximateCostCalculator(
				ai, ownHero);
		ApproximateSuccessorCalculator approximateSuccessorCalculator = new ApproximateSuccessorCalculator(
				ai);

		TimeCostCalculator timeCostCalculator = new TimeCostCalculator(ai,
				ownHero);
		TimeHeuristicCalculator heuristicCalculator = new TimeHeuristicCalculator(
				ai, ownHero);
		TimePartialSuccessorCalculator timeSuccessorCalculator = new TimePartialSuccessorCalculator(
				ai, SearchMode.MODE_ONEBRANCH);

		astar = new Astar(ai, ownHero, timeCostCalculator, heuristicCalculator,
				timeSuccessorCalculator);
		astarApproximate = new Astar(ai, ownHero, approximateCostCalculator,
				heuristicCalculator, approximateSuccessorCalculator);

		astar.setVerbose(false);
		astarApproximate.setVerbose(false);
		// this.astar.getSuccessorCalculator().setConsiderOpponents(true);
		this.astar.getCostCalculator().setMalusCost(10000);
		this.astar.getCostCalculator().setOpponentCost(1000);

	}

	// ///////////////////////////////////////////////////////////////
	// DATA /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	/** A titre d'exemple, je stocke la zone de jeu, car on en a souvent besoin */
	private AiZone zone = null;
	/**
	 * A titre d'exemple, je stocke le sprite controlé par cet agent, car on en
	 * a aussi souvent besoin
	 */
	private AiHero ownHero = null;
	/**
	 * Il est nécessaire de stocker l'objet Astar, si on ne veut pas devoir le
	 * re-créer à chaque itération
	 */
	private Astar astar = null;
	/** Il est nécessaire de stocker l'objet Astar approximate, si on ne veut pas devoir le
	 * re-créer à chaque itération*/
	private Astar astarApproximate = null;
	/** c'est un obstacle dans notre premier chemin indirect*/
	AiTile blockedTile = null;
	/** le premier chemin indirect qu'on a trouve*/
	boolean firstIndirectPath = false;
	/** Controle si la case qu'on se trouve est sure?*/
	boolean controlSecurity=false;
	/** controle la case qui nous bloque contient une bombe*/
	boolean controlBomb=false;
	/** controle la case qui nous bloque contient de flamme*/
	boolean controlFire=false;
	

	// public Boolean nextTileHasBlock = false;

	/**
	 * Location initial
	 * */
	private Boolean initalLocation = true;

	// ///////////////////////////////////////////////////////////////
	// DESTINATION /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected AiTile processCurrentDestination() {
		ai.checkInterruption();
		AiTile result = null;

		// ici, à titre d'exemple, on se contente de prendre la case dont la
		// préférence est maximale
		// c'est une approche simpliste, ce n'est pas forcément la meilleure
		// (sûrement pas, d'ailleurs)
		// c'est seulement pour montrer un exemple en termes de programmation
		// (et non pas de conception d'agent)
		AiPreferenceHandler<Agent> preferenceHandler = ai
				.getPreferenceHandler();
		Map<Integer, List<AiTile>> preferences = preferenceHandler
				.getPreferencesByValue();
		int minPref = Collections.min(preferences.keySet()); // ATTENTION : ici
																// il faudrait
																// tester qu'il
																// y a au moins
																// une valeur
																// dans la map
																// (sinon :
																// NullPointerException
																// !)

		AiTile previousTile = getCurrentDestination();
		List<AiTile> tiles = preferences.get(minPref); // on récupère la liste
														// de cases qui ont la
														// meilleure préférence

		if (initalLocation) {
			initalLocation = false;
			result = tiles.get(0); // on prend la première de la liste
									// (arbitrairement)
		} else {
			// if next tiles preference is not better than previous tiles don't
			// change destination
			if (tiles.contains(previousTile)) {
				result = previousTile;
			} else
				result = tiles.get(0);
		}
		return result;
	}

	// ///////////////////////////////////////////////////////////////
	// PATH /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected AiPath processCurrentPath() {
		ai.checkInterruption();
		AiPath result = null;
		AiLocation startLocation = new AiLocation(ownHero);

		/*
		 * cette case correspond à celle sélectionnée dans la méthode
		 * processCurrentDestination
		 */
		AiTile endTile = getCurrentDestination();

		if (ai.accessibleTiles.contains(endTile)) {
			try {
				result = astar.startProcess(startLocation, endTile);
				if (result == null)
					result = null;// result =
									// dijkstra.startProcess(startLocation);
				// Dijkstra kullan
			} catch (LimitReachedException e) { // e.printStackTrace(); // il ne
												// faut PAS afficher la trace
												// (cf.

				// System.out.println("Exceptioon");
				result = new AiPath();
			}
		}

		else {
			if (blockedTile != null) {
				controlSecurity = ownHero.getTile().equals(safeDestination);
				controlBomb = blockedTile.getBombs().isEmpty();
				controlFire = blockedTile.getFires().isEmpty();
			}
			if (firstIndirectPath == false || controlSecurity && controlBomb && controlFire) 
			{
				try {
					result = astarApproximate.startProcess(startLocation,
							endTile);

					// Dijkstra kullan
				} catch (LimitReachedException e) {
					result = new AiPath();
				}
				firstIndirectPath = true;

				Iterator<AiLocation> it = result.getLocations().iterator();
				AiTile previousTile = null;

				while (it.hasNext()) {
					ai.checkInterruption();

					AiLocation location = it.next();
					AiTile tile = location.getTile();
					List<AiBlock> blocks = tile.getBlocks();
					
					if (!blocks.isEmpty()) {
						blockedTile = previousTile;
						break;
					}
					previousTile = tile;
				}
				if(blockedTile!=null)
				{
					if (blockedTile.equals(ownHero.getTile()))
						ai.nextTileHasBlock = true;
				}
			}
			
			else {
				for (AiTile tile : ai.accessibleTiles) {
					ai.checkInterruption();
					ArrayList<AiTile> dTiles = ai.dangerousTiles();
					if (!dTiles.contains(tile)) {
						safeDestination = tile;

						try {
							result = astar.startProcess(startLocation,
									safeDestination);
						} catch (LimitReachedException e) { //
							result=new AiPath();
						}

						if (result == null)
							print("No path could be found! duration=");
						break;
					}
				}
			}
		}

		return result;
	}

	// ///////////////////////////////////////////////////////////////
	// DIRECTION /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected Direction processCurrentDirection() {
		ai.checkInterruption();
		Direction result = Direction.NONE;

		AiPath currentPath = getCurrentPath();
		long wait = 0;

		// cas où le chemin est vide, ou bien ne contient que la case courante
		if (currentPath == null || currentPath.getLength() < 2)
			result = Direction.NONE;

		else {
			if (currentPath != null)
				wait = currentPath.getFirstPause();
			if (wait > 0)
				return Direction.NONE;
			else {
				AiLocation nextLocation = currentPath.getLocation(1);
				this.ai.nextTile = nextLocation.getTile();
				AiTile currentTile = ownHero.getTile();
				this.ai.lastDirection = zone.getDirection(currentTile,
						this.ai.nextTile);
				//
				// if (!ai.nextTile.isCrossableBy(ownHero))
				// ai.nextTileHasBlock = true;

				result = this.ai.lastDirection;
			}
		}

		return result;
	}

	// ///////////////////////////////////////////////////////////////
	// OUTPUT /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public void updateOutput() {
		ai.checkInterruption();

		// ici on se contente de faire le traitement par défaut
		super.updateOutput();

	}
}