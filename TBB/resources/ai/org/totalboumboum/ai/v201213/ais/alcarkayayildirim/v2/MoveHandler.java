package org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v2;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201213.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiBomb;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.adapter.path.AiLocation;
import org.totalboumboum.ai.v201213.adapter.path.AiPath;
import org.totalboumboum.ai.v201213.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201213.adapter.path.cost.ApproximateCostCalculator;
import org.totalboumboum.ai.v201213.adapter.path.cost.CostCalculator;
import org.totalboumboum.ai.v201213.adapter.path.cost.TimeCostCalculator;
import org.totalboumboum.ai.v201213.adapter.path.heuristic.HeuristicCalculator;
import org.totalboumboum.ai.v201213.adapter.path.heuristic.TimeHeuristicCalculator;
import org.totalboumboum.ai.v201213.adapter.path.search.Astar;
import org.totalboumboum.ai.v201213.adapter.path.search.Dijkstra;
import org.totalboumboum.ai.v201213.adapter.path.successor.ApproximateSuccessorCalculator;
import org.totalboumboum.ai.v201213.adapter.path.successor.SuccessorCalculator;
import org.totalboumboum.ai.v201213.adapter.path.successor.TimePartialSuccessorCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Our move handler class.
 * 
 * @author Ekin Alçar
 * @author Ulaş Kaya
 * @author Yağmur Yıldırım
 */
public class MoveHandler extends AiMoveHandler<AlcarKayaYildirim> {
	/** */
	protected AiZone zone = null;
	/** */
	protected AiHero ownHero = null;
	/** */
	protected AiTile currentTile = null;
	/** */
	protected AiTile nextTile = null;
	/** */
	protected AiTile safeTile = null;
	/** */
	protected Astar astarPrecise = null;
	/** */
	protected Astar astarApproximation = null;
	/** */
	protected Dijkstra dijkstra = null;
	/** */
	protected AiTile safeTileControl = null;

	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai
	 *            l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected MoveHandler(AlcarKayaYildirim ai) throws StopRequestException {
		super(ai);
		ai.checkInterruption();

		// on règle la sortie texte pour ce gestionnaire
		verbose = false;

		zone = ai.getZone();
		ownHero = zone.getOwnHero();

		{
			CostCalculator costCalculator = new TimeCostCalculator(ai, ownHero);
			costCalculator.setOpponentCost(1000); // on assimile la traversée
													// d'un adversaire à un
													// détour de 1 seconde
			HeuristicCalculator heuristicCalculator = new TimeHeuristicCalculator(
					ai, ownHero);
			SuccessorCalculator successorCalculator = new TimePartialSuccessorCalculator(
					ai, TimePartialSuccessorCalculator.MODE_NOTREE);
			astarPrecise = new Astar(ai, ownHero, costCalculator,
					heuristicCalculator, successorCalculator);
		}

		{
			CostCalculator costCalculator = new ApproximateCostCalculator(ai,
					ownHero);
			HeuristicCalculator heuristicCalculator = new TimeHeuristicCalculator(
					ai, ownHero);
			SuccessorCalculator successorCalculator = new ApproximateSuccessorCalculator(
					ai);
			astarApproximation = new Astar(ai, ownHero, costCalculator,
					heuristicCalculator, successorCalculator);
		}

		{
			CostCalculator costCalculator = new TimeCostCalculator(ai, ownHero);
			costCalculator.setOpponentCost(1000); // on assimile la traversée
													// d'un adversaire à un
													// détour de 1 seconde
			SuccessorCalculator successorCalculator = new TimePartialSuccessorCalculator(
					ai, TimePartialSuccessorCalculator.MODE_NOBRANCH);
			dijkstra = new Dijkstra(ai, ownHero, costCalculator,
					successorCalculator);
		}

	}

	/**
	 * @return result
	 * @throws StopRequestException
	 */
	protected boolean getSafeTile() throws StopRequestException {
		ai.checkInterruption();
		boolean result = false;
		AiBomb ownBomb = ownHero.getBombPrototype();
		AiPath pathControl = null;
		List<AiBomb> zoneBombList = null;
		Set<AiTile> list = new TreeSet<AiTile>();
		AiBomb otherBomb = null;

		int controlFirst = 0;
		int controlSecond = 0;

		// bomberman tarafından gidilebilecek olan (crossableby) tile leri
		// listeye atıyor
		for (int i = 0; i < 5; i++) {
			ai.checkInterruption();

			for (int j = 0; j < 5; j++) {
				ai.checkInterruption();

				if (ownHero.getRow() + i < zone.getHeight()
						&& ownHero.getCol() + j < zone.getWidth()) {
					if (zone.getTile(ownHero.getRow() + i, ownHero.getCol() + j)
							.isCrossableBy(ownHero, false, false, false, false,
									true, true))
						list.add(zone.getTile(ownHero.getRow() + i,
								ownHero.getCol() + j));
				}
				if (ownHero.getRow() - i > 0
						&& ownHero.getCol() + j < zone.getWidth()) {
					if (zone.getTile(ownHero.getRow() - i, ownHero.getCol() + j)
							.isCrossableBy(ownHero, false, false, false, false,
									true, true))
						list.add(zone.getTile(ownHero.getRow() - i,
								ownHero.getCol() + j));
				}

				if (ownHero.getRow() - i > 0 && ownHero.getCol() - j > 0) {
					if (zone.getTile(ownHero.getRow() - i, ownHero.getCol() - j)
							.isCrossableBy(ownHero, false, false, false, false,
									true, true))
						list.add(zone.getTile(ownHero.getRow() - i,
								ownHero.getCol() - j));
				}
				if (ownHero.getRow() + i < zone.getHeight()
						&& ownHero.getCol() - j > 0) {
					if (zone.getTile(ownHero.getRow() + i, ownHero.getCol() - j)
							.isCrossableBy(ownHero, false, false, false, false,
									true, true))
						list.add(zone.getTile(ownHero.getRow() + i,
								ownHero.getCol() - j));
				}
			}
		}

		Iterator<AiTile> it = list.iterator();
		AiTile firstTile = null;
		AiLocation ownHeroLocation = new AiLocation(ownHero);
		zoneBombList = zone.getBombs();

		while (it.hasNext()) {
			ai.checkInterruption();
			firstTile = it.next();

			try {
				pathControl = astarPrecise.startProcess(ownHeroLocation,
						firstTile);
			} catch (LimitReachedException e) {
				e.printStackTrace();
			}

			if (zoneBombList.isEmpty()) {
				if (pathControl != null
						&& !ownBomb.getBlast().contains(firstTile)) {
					controlSecond++;
				}
			}

		} // while

		while (it.hasNext()) {
			ai.checkInterruption();
			firstTile = it.next();

			try {
				pathControl = astarPrecise.startProcess(ownHeroLocation,
						firstTile);
			} catch (LimitReachedException e) {
				e.printStackTrace();
			}

			{
				if (!zoneBombList.isEmpty())
					for (int i = 0; i < zoneBombList.size(); i++) {
						ai.checkInterruption();
						otherBomb = zoneBombList.get(i);
						if (pathControl != null
								&& !ownBomb.getBlast().contains(firstTile)
								&& !otherBomb.getBlast().contains(firstTile)) {
							controlFirst++;
						}

					}
			}

		}

		if (controlFirst > 0 || controlSecond > 0) {
			result = true;
		}

		return result;

	}
	@Override
	protected AiTile updateCurrentDestination() throws StopRequestException {
		
		this.ai.checkInterruption();
		Map<AiTile, Float> hashmap;
		hashmap = ai.utilityHandler.getUtilitiesByTile();

		AiTile biggestTile = null;
		float value = -10;

		for (AiTile currentTile : hashmap.keySet()) {
			this.ai.checkInterruption();
			if (ai.utilityHandler.getUtilitiesByTile().get(currentTile) > value
					) {
				value = ai.utilityHandler.getUtilitiesByTile().get(
						currentTile);
				biggestTile = currentTile;

			}
		}
		
		/*for(int i=0;i<10;i++)
		{
			System.out.println("value " +biggestTile + ai.modeHandler.getMode());
		}*/
		
		return biggestTile;

	}

	// ///////////////////////////////////////////////////////////////
	// PATH /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected AiPath updateCurrentPath() throws StopRequestException {
		ai.checkInterruption();

		currentTile = ownHero.getTile();
		currentPath = null;
		AiLocation ownLocation = new AiLocation(ownHero);

		if (safeTile == null) {
			try {
				currentPath = dijkstra.processEscapePath(ownLocation);

			} catch (LimitReachedException e) {
				e.printStackTrace();
			}

			if (currentPath != null || !currentPath.isEmpty()) {

				AiLocation lastLocation = currentPath.getLastLocation();
				safeTile = lastLocation.getTile();
				safeTileControl = safeTile;

			}
		}

		if (safeTile != null) {

			if (currentTile.equals(safeTile))
				safeTile = null;
			else {
				try {
					currentPath = astarPrecise.startProcess(ownLocation, safeTile);
				} catch (LimitReachedException e) {
					e.printStackTrace();
				}
			}
		}

		if (safeTile == null) {
			try {
				currentPath = astarPrecise.startProcess(ownLocation,currentDestination);
			} catch (LimitReachedException e) {
				e.printStackTrace();
			}
			if (currentPath == null || currentPath.isEmpty()) {
				try {
					currentPath = astarApproximation.startProcess(ownLocation,currentDestination);
				} catch (LimitReachedException e) {
					e.printStackTrace();
				}
			}

		}

		return currentPath;
	}

	// ///////////////////////////////////////////////////////////////
	// DIRECTION /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected Direction updateCurrentDirection() throws StopRequestException {
		ai.checkInterruption();

		currentTile = ownHero.getTile();
		List<AiBomb> bombList = zone.getBombs();

		if (currentPath != null) {
			long wait = currentPath.getFirstPause();

			if (currentPath.getLength() >= 2 && wait <= 0) //
			{
				AiLocation sourceLocation = currentPath.getFirstLocation();
				AiLocation targetLocation = currentPath.getLocation(1);
				currentDirection = zone.getDirection(sourceLocation.getTile(),
						targetLocation.getTile());
				nextTile = targetLocation.getTile();
			}
		}

		int controlBombNextTile = 0;
		int controlBombCurrentTile = 0;

		for (int a = 0; a < bombList.size(); a++) {
			ai.checkInterruption();

			if (bombList.get(a).getBlast().contains(nextTile)) {
				controlBombNextTile++;
			}

		}

		for (int a = 0; a < bombList.size(); a++) {
			ai.checkInterruption();

			if (bombList.get(a).getBlast().contains(currentTile)) {
				controlBombCurrentTile++;
			}

		}

		if (controlBombNextTile >= 1 && controlBombCurrentTile == 0) {
			currentDirection = Direction.NONE;
		}

		if (!nextTile.getFires().isEmpty() || nextTile.equals(currentTile))
			currentDirection = Direction.NONE;

		return currentDirection;
	}

	// ///////////////////////////////////////////////////////////////
	// OUTPUT /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public void updateOutput() throws StopRequestException {
		ai.checkInterruption();

		// ici on se contente de faire le traitement par défaut
		super.updateOutput();
	}
}
