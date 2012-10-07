package org.totalboumboum.ai.v201112.ais.caliskangeckalanseven.v3;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import org.totalboumboum.ai.v201112.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiBlock;
import org.totalboumboum.ai.v201112.adapter.data.AiBomb;
import org.totalboumboum.ai.v201112.adapter.data.AiFire;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.adapter.path.search.Astar;
import org.totalboumboum.ai.v201112.adapter.path.search.Dijkstra;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe gérant l'action de déposer une bombe pour l'agent. Cf. la
 * documentation de {@link AiBombHandler} pour plus de détails.
 * 
 * @author Mustafa Çalışkan
 * @author Gökhan Geçkalan
 * @author Cihan Seven
 */
@SuppressWarnings("deprecation")
public class BombHandler extends AiBombHandler<CaliskanGeckalanSeven> {
	/** */
	protected AiZone zone = null;
	/** Le personnage contrôlé par l'agent */
	protected AiHero ownHero = null;
	/** La case courante */
	protected AiTile currentTile = null;
	/** L'objet a* utilisé pour le calcul des chemins directs */
	protected Astar astarPrecise = null;
	/** L'objet a* utilisé pour le calcul des chemins indirects */
	protected Astar astarApproximation = null;
	
	/** */
	protected Dijkstra dijkstra = null;
	
	/** */
	protected boolean secondaryBombing = false;
	/** Indique si l'agent doit poser une bombe sur l'objectif */
	protected boolean bombDestination = false;
	
	
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai
	 *            l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected BombHandler(CaliskanGeckalanSeven ai) throws StopRequestException {
		super(ai);
		ai.checkInterruption();

		// on règle la sortie texte pour ce gestionnaire
		verbose = true;
		zone = ai.getZone();
		ownHero = zone.getOwnHero();
		
		
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESSING /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected boolean considerBombing() throws StopRequestException {
		ai.checkInterruption();
		boolean result = false;
		CalculCommun calculCommun = new CalculCommun(ai);
		if(danger())
			return false;
		AiTile currentTile = ownHero.getTile();
		AiBomb bomb = ownHero.getBombPrototype();
		boolean bombAbsence = currentTile.isCrossableBy(bomb);
		if(bombAbsence)
		{	
			AiTile currentDestination = ai.moveHandler.getCurrentDestination();
			boolean bombPrimaryDestination = currentTile.equals(currentDestination) && ai.moveHandler.bombDestination;
			boolean bombSecondaryDestination = currentTile.equals(currentDestination) && ai.moveHandler.secondaryBombing;
			boolean canBomb = calculCommun.canBomb(); //control of virtual bomb. if we put a bomb is there any place to escape
			if(canBomb && (bombPrimaryDestination || bombSecondaryDestination))
			{	
				result = true;
			}
		}
	
		return result;
	}

	/**
	 * 
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	private boolean danger() throws StopRequestException {
		ai.checkInterruption();
		boolean danger = false;
		LinkedList<AiTile> dangerTiles = dangerZone();
		if (dangerTiles.contains(ai.getZone().getOwnHero().getTile())) {
			danger = true;
		}
		return danger;
	}

	
	/**
	 * 
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	private LinkedList<AiTile> dangerZone() throws StopRequestException {

		ai.checkInterruption();
		AiZone zone = ai.getZone();

		LinkedList<AiTile> dangerZone = new LinkedList<AiTile>();
		Collection<AiBomb> bombs = zone.getBombs();
		Collection<AiFire> fires = zone.getFires();
		Collection<AiBlock> blocks = zone.getBlocks();
		LinkedList<AiTile> blokTile = new LinkedList<AiTile>();
		Iterator<AiBlock> it_blocks = blocks.iterator();
		Iterator<AiFire> itfires = fires.iterator();
		while (it_blocks.hasNext()) {
			ai.checkInterruption();
			AiBlock blok = it_blocks.next();
			AiTile tile = blok.getTile();
			blokTile.add(tile);
		}
		if (fires.size() > 0) {
			while (itfires.hasNext()) {
				ai.checkInterruption();
				AiFire fire = itfires.next();
				AiTile temp = fire.getTile();
				dangerZone.add(temp);
			}
		}
		Iterator<AiBomb> it1 = bombs.iterator();
		if (bombs.size() > 0) {

			while (it1.hasNext()) {
				ai.checkInterruption();

				AiBomb bomb = it1.next();

				int k = bomb.getRange();
				int x = bomb.getCol();
				int y = bomb.getRow();

				AiTile tempTile = zone.getTile(y, x);
				dangerZone.add(tempTile);
				AiTile tile1 = tempTile;
				int i = 0;
				while (i < k && !blocks.contains(tempTile)) {
					ai.checkInterruption();
					AiTile tile = tempTile.getNeighbor(Direction.DOWN);

					tempTile = tile;
					if (!blokTile.contains(tile)) {

						dangerZone.add(tile);
					} else
						break;
					i++;
				}
				i = 0;
				tempTile = tile1;

				while (i < k && !blocks.contains(tempTile)) {
					ai.checkInterruption();
					AiTile tile = tempTile.getNeighbor(Direction.UP);

					tempTile = tile;
					if (!blokTile.contains(tile)) {

						dangerZone.add(tile);
					} else
						break;
					i++;
				}
				i = 0;
				tempTile = tile1;
				while (i < k && !blocks.contains(tempTile)) {
					ai.checkInterruption();
					AiTile tile = tempTile.getNeighbor(Direction.RIGHT);

					tempTile = tile;
					if (!blokTile.contains(tile)) {

						dangerZone.add(tile);
					} else
						break;
					i++;
				}
				i = 0;
				tempTile = tile1;
				while (i < k && !blocks.contains(tempTile)) {
					ai.checkInterruption();
					AiTile tile = tempTile.getNeighbor(Direction.LEFT);

					tempTile = tile;
					if (!blokTile.contains(tile)) {

						dangerZone.add(tile);
					} else
						break;
					i++;
				}
				i = 0;
				tempTile = tile1;
			}

		}
		return dangerZone;
	}


	// ///////////////////////////////////////////////////////////////
	// OUTPUT /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	/**
	 * @throws StopRequestException
	 */
	protected void updateOutput() throws StopRequestException {
		ai.checkInterruption();

		
	}
}
