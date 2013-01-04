package org.totalboumboum.ai.v201213.ais.oralozugur.v1;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.totalboumboum.ai.v201213.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201213.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201213.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityHandler;
import org.totalboumboum.ai.v201213.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiBlock;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.adapter.model.full.AiSimBomb;
import org.totalboumboum.ai.v201213.adapter.model.full.AiSimZone;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe principale de votre agent, que vous devez compléter. Cf. la
 * documentation de {@link ArtificialIntelligence} pour plus de détails.
 * 
 * 
 * @author Buğra Oral
 * @author Ceyhun Özuğur
 */
public class OralOzugur extends ArtificialIntelligence {
	/**
	 * Instancie la classe principale de l'agent.
	 */

	public AiTile objective = null;
	/** */
	private ArrayList<AiTile> accessibleTiles;

	/**
	 * 
	 */
	public OralOzugur() { // active/désactive la sortie texte
		verbose = false;
	}

	@Override
	protected void initOthers() throws StopRequestException {
		checkInterruption();

	}

	// ///////////////////////////////////////////////////////////////
	// PERCEPTS /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected void initPercepts() throws StopRequestException {
		checkInterruption();

		this.accessibleTiles = new ArrayList<AiTile>();
	}

	@Override
	protected void updatePercepts() throws StopRequestException {
		checkInterruption();

		this.accessibleTiles.clear();
	}

	// ///////////////////////////////////////////////////////////////
	// HANDLERS /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	/** gestionnaire chargé de calculer le mode de l'agent */
	protected ModeHandler modeHandler;
	/** gestionnaire chargé de calculer les valeurs d'utilité de l'agent */
	protected UtilityHandler utilityHandler;
	/** gestionnaire chargé de décider si l'agent doit poser une bombe ou pas */
	protected BombHandler bombHandler;
	/** gestionnaire chargé de décidé de la direction de déplacement de l'agent */
	protected MoveHandler moveHandler;

	@Override
	protected void initHandlers() throws StopRequestException {
		checkInterruption();

		// création des gestionnaires standard (obligatoires)
		modeHandler = new ModeHandler(this);
		utilityHandler = new UtilityHandler(this);
		bombHandler = new BombHandler(this);
		moveHandler = new MoveHandler(this);

	}

	@Override
	protected AiModeHandler<OralOzugur> getModeHandler()
			throws StopRequestException {
		checkInterruption();
		return modeHandler;
	}

	@Override
	protected AiUtilityHandler<OralOzugur> getUtilityHandler()
			throws StopRequestException {
		checkInterruption();
		return utilityHandler;
	}

	@Override
	protected AiBombHandler<OralOzugur> getBombHandler()
			throws StopRequestException {
		checkInterruption();
		return bombHandler;
	}

	@Override
	protected AiMoveHandler<OralOzugur> getMoveHandler()
			throws StopRequestException {
		checkInterruption();
		return moveHandler;
	}

	// ///////////////////////////////////////////////////////////////
	// OUTPUT /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected void updateOutput() throws StopRequestException {
		checkInterruption();

		// ici, par défaut on affiche :
		// les chemins et destinations courants
		moveHandler.updateOutput();
		// les utilités courantes
		utilityHandler.updateOutput();

		// cf. la Javadoc dans ArtificialIntelligence pour une description de la
		// méthode
	}

	// OUT METHODS
	/**
	 * Le recherhe de la case ou l'utilité est maximal
	 * 
	 * 
	 * 
	 * @return AiTile -> case qui a l'utilité maximal
	 * @throws StopRequestException
	 */
	public AiTile getBiggestTile() throws StopRequestException {
		this.checkInterruption();
		Map<AiTile, Float> hashmap;
		hashmap = getUtilityHandler().getUtilitiesByTile();

		List<AiTile> biggestTiles = new ArrayList<AiTile>();

		AiTile biggestTile = null;
		float value = -10;

		for (AiTile currentTile : hashmap.keySet()) {
			this.checkInterruption();
			if (getUtilityHandler().getUtilitiesByTile().get(currentTile) > value) {

				value = getUtilityHandler().getUtilitiesByTile().get(
						currentTile);
				biggestTile = currentTile;
				biggestTiles.clear();
				biggestTiles.add(currentTile);
			} else if (getUtilityHandler().getUtilitiesByTile()
					.get(currentTile) == value) {
				biggestTiles.add(currentTile);
			}
		}
		biggestTile = biggestTiles.get((int) (Math.random() * (biggestTiles.size()-1)));
		return biggestTile;

	}

	/**
	 * Recursive method to fill a list of accessible tiles.
	 * 
	 * @param sourceTile
	 *            The tile to start looking from. If not crossable, list will
	 *            not be populated.
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	private void fillAccessibleTilesBy(AiTile sourceTile)
			throws StopRequestException {
		this.checkInterruption();
		if (sourceTile.isCrossableBy(this.getZone().getOwnHero())) {
			this.accessibleTiles.add(sourceTile);
			if (sourceTile.getNeighbor(Direction.UP).isCrossableBy(
					this.getZone().getOwnHero())
					&& !this.accessibleTiles.contains(sourceTile
							.getNeighbor(Direction.UP)))
				fillAccessibleTilesBy(sourceTile.getNeighbor(Direction.UP));
			if (sourceTile.getNeighbor(Direction.DOWN).isCrossableBy(
					this.getZone().getOwnHero())
					&& !this.accessibleTiles.contains(sourceTile
							.getNeighbor(Direction.DOWN)))
				fillAccessibleTilesBy(sourceTile.getNeighbor(Direction.DOWN));
			if (sourceTile.getNeighbor(Direction.LEFT).isCrossableBy(
					this.getZone().getOwnHero())
					&& !this.accessibleTiles.contains(sourceTile
							.getNeighbor(Direction.LEFT)))
				fillAccessibleTilesBy(sourceTile.getNeighbor(Direction.LEFT));
			if (sourceTile.getNeighbor(Direction.RIGHT).isCrossableBy(
					this.getZone().getOwnHero())
					&& !this.accessibleTiles.contains(sourceTile
							.getNeighbor(Direction.RIGHT)))
				fillAccessibleTilesBy(sourceTile.getNeighbor(Direction.RIGHT));
		}
	}

	/**
	 * To get the accessible tiles from a given source tile. (TESTED, WORKS)
	 * 
	 * @param sourceTile
	 *            Tile to start looking from.
	 * @return List of all tiles that accessible from a given source tile.
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	public ArrayList<AiTile> getAccessibleTilesFrom(AiTile sourceTile)
			throws StopRequestException {
		this.checkInterruption();

		fillAccessibleTilesBy(sourceTile);

		return this.accessibleTiles;
	}

	/**
	 * To get destructed tiles from a given source tile.
	 * 
	 * @param sourceTile
	 *            Tile to start looking from.
	 * @return value of NbMurDetruit of the given tile
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	public int getNbMurDetruitofTile(AiTile sourceTile)
			throws StopRequestException {
		this.checkInterruption();
		int result = 0;
		AiZone zone = this.getZone();
		int x = sourceTile.getRow();
		int y = sourceTile.getCol();
		int i = 1, bomb_range = zone.getOwnHero().getBombRange();
		// obstacles sont pour terminer les recherce de murs quand on se
		// rencontre des murs.
		boolean[] obstacle = { true, true, true, true, true };

		while (obstacle[4] && (i <= bomb_range)) {
			this.checkInterruption();
			List<AiBlock> blocks;
			if (obstacle[0]) {
				blocks = zone.getTile(x + i, y).getBlocks();
				if (!blocks.isEmpty()) {
					for (AiBlock block : blocks) {
						this.checkInterruption();
						if (block.isDestructible())
							result++;
						obstacle[0] = false;
					}
				}
			}
			if (obstacle[1]) {
				blocks = zone.getTile(x - i, y).getBlocks();
				if (!blocks.isEmpty()) {
					for (AiBlock block : blocks) {
						this.checkInterruption();
						if (block.isDestructible())
							result++;
						obstacle[1] = false;
					}
				}
			}
			if (obstacle[2]) {
				blocks = zone.getTile(x, y + i).getBlocks();
				if (!blocks.isEmpty()) {
					for (AiBlock block : blocks) {
						this.checkInterruption();
						if (block.isDestructible())
							result++;
						obstacle[2] = false;
					}
				}
			}
			if (obstacle[3]) {
				blocks = zone.getTile(x, y - i).getBlocks();
				if (!blocks.isEmpty()) {
					for (AiBlock block : blocks) {
						this.checkInterruption();
						if (block.isDestructible())
							result++;
						obstacle[3] = false;
					}
				}
			}
			if ((!obstacle[0]) && (!obstacle[1])
					&& (!obstacle[2] && (!obstacle[3])))
				obstacle[4] = false;

			i++;

		}
		return result;
	}

	/**
	 * To get the PeutTuerEnnemi value from a given source tile.
	 * 
	 * @param sourceTile
	 *            Tile to start looking from.
	 * @return PeutTuerEnnemi value of the tile
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	public int getPeutTuerEnnemiofTile(AiTile sourceTile)
			throws StopRequestException {
		this.checkInterruption();
		int result = 0;
		if (isSimInBlastSight(sourceTile, getNearestEnemy()))
			return 2;
		for (AiTile neighbor : sourceTile.getNeighbors()) {
			this.checkInterruption();
			if (neighbor.getBlocks().isEmpty()) {
				if (isSimInBlastSight(neighbor, getNearestEnemy()))
					return 1;
			}
		}
		return result;
	}

	/**
	 * Similation pour voir si on peut tuer un adversaire en posant une bombe
	 * 
	 * 
	 * @param tile
	 *            Le tile on considere poser la bombe
	 * @param enemy
	 *            ennemi qu'on veut tuer
	 * @return vrai si on peut.
	 * @throws StopRequestException
	 */
	public boolean isSimInBlastSight(AiTile tile, AiHero enemy)
			throws StopRequestException {
		checkInterruption();
		AiZone zone = this.getZone();
		AiSimZone simzone = new AiSimZone(zone);
		AiSimBomb simbomb = simzone.createBomb(tile, simzone.getOwnHero());
		List<AiTile> indanger_tiles = simbomb.getBlast();
		if (indanger_tiles != null && enemy!=null) {
			if (indanger_tiles.contains(enemy.getTile()))
				return true;
		}
		return false;

	}

	/**
	 * fonction pour trouver l'ennemi plus proche
	 * 
	 * @return AiHero de l'ennemi plus proche
	 * @throws StopRequestException
	 */
	public AiHero getNearestEnemy() throws StopRequestException {
		checkInterruption();
		AiZone zone = this.getZone();
		AiHero hero = zone.getOwnHero();
		AiHero target = null;
		int distance = 5000;

		for (AiHero enemy : zone.getRemainingOpponents()) {
			this.checkInterruption();
			if ((zone.getTileDistance(hero, enemy) < distance)
					&& (this.accessibleTiles.contains(enemy.getTile())))
				target = enemy;
		}
		return target;
	}

	/**
	 * @return vrai, s'il y a un adversaire accesible dans la zone
	 * @throws StopRequestException
	 */
	public boolean isEnemiesAccessible() throws StopRequestException {
		checkInterruption();
		for (AiHero hero : getZone().getHeroes()) {
			checkInterruption();
			if (accessibleTiles.contains(hero.getTile())
					&& (hero.getId() != getZone().getOwnHero().getId()))
				return true;
		}
		return false;

	}
}
