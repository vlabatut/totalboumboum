package org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v2;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v201213.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201213.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201213.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityHandler;
import org.totalboumboum.ai.v201213.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiBlock;
import org.totalboumboum.ai.v201213.adapter.data.AiBomb;
import org.totalboumboum.ai.v201213.adapter.data.AiFire;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.adapter.model.full.AiSimZone;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * The main class of our AI. includes needed methods and our field/methods.
 *
 * @author Ekin Alçar
 * @author Ulaş Kaya
 * @author Yağmur Yıldırım
 */
public class AlcarKayaYildirim extends ArtificialIntelligence
{
	/** */
	AiZone zone;
	/** */
	AiHero ourhero;
	/** */
	AiTile ourtile;

	/**
	 * Represents this AI's hero.
	 */
	private AiHero hero;
	/**
	 * Instancie la classe principale de l'agent.
	 */
	public AlcarKayaYildirim()
	{	
		// active/désactive la sortie texte
		verbose = false;
	}
	
	@Override
	protected void initOthers() throws StopRequestException
	{	checkInterruption();
	}
	
	/////////////////////////////////////////////////////////////////
	// PERCEPTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void initPercepts() throws StopRequestException
	{	checkInterruption();
	}
	
	@Override
	protected void updatePercepts() throws StopRequestException
	{	checkInterruption();
	}
	
	/////////////////////////////////////////////////////////////////
	// HANDLERS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** gestionnaire chargé de calculer le mode de l'agent */
	public ModeHandler modeHandler;
	/** gestionnaire chargé de calculer les valeurs d'utilité de l'agent */
	protected UtilityHandler utilityHandler;
	/** gestionnaire chargé de décider si l'agent doit poser une bombe ou pas */
	protected BombHandler bombHandler;
	/** gestionnaire chargé de décidé de la direction de déplacement de l'agent */
	protected MoveHandler moveHandler;
	
	@Override
	protected void initHandlers() throws StopRequestException
	{	checkInterruption();
		
		// création des gestionnaires standard (obligatoires)
		modeHandler = new ModeHandler(this);
		utilityHandler = new UtilityHandler(this);
		bombHandler = new BombHandler(this);
		moveHandler = new MoveHandler(this);
	}

	@Override
	protected AiModeHandler<AlcarKayaYildirim> getModeHandler() throws StopRequestException
	{	checkInterruption();
		return modeHandler;
	}

	@Override
	protected AiUtilityHandler<AlcarKayaYildirim> getUtilityHandler() throws StopRequestException
	{	checkInterruption();
		if(utilityHandler == null)
			utilityHandler = new UtilityHandler(this);
		return utilityHandler;
	}

	@Override
	protected AiBombHandler<AlcarKayaYildirim> getBombHandler() throws StopRequestException
	{	checkInterruption();
		return bombHandler;
	}

	@Override
	protected AiMoveHandler<AlcarKayaYildirim> getMoveHandler() throws StopRequestException
	{	checkInterruption();
		return moveHandler;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void updateOutput() throws StopRequestException
	{	checkInterruption();

			moveHandler.updateOutput();
			// les utilités courantes
			utilityHandler.updateOutput();
			
	}
	
	
	// NOTRE METHODE
	
	/**
	 * Method to get this AI's hero.
	 * 
	 * @return this AI's hero.
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	public AiHero getHero() throws StopRequestException
	{
		checkInterruption();
		return hero;
	}
	
	/**
	 * Populates a list of dangerous tiles of this AI's zone. <br />
	 * 
	 * @return List of the dangerous tiles.
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	public ArrayList<AiTile> getCurrentDangerousTiles() throws StopRequestException
	{
		checkInterruption();
		ArrayList<AiTile> dangerousTiles = new ArrayList<AiTile>();
		
		for ( AiBomb currentBomb : getZone().getBombs() )
		{
			checkInterruption();
			dangerousTiles.add( currentBomb.getTile() );
			for ( AiTile currentTile : currentBomb.getBlast() )
			{
				checkInterruption();
				dangerousTiles.add( currentTile );
			}
		}
		for ( AiFire currentFire : getZone().getFires() )
		{
			checkInterruption();
			dangerousTiles.add( currentFire.getTile() );
		}
		return dangerousTiles;
	}
	
	/**
	 * getWallInDanger controls if the given tile contains blocks and if it is
	 * in danger.
	 * 
	 * @param aitile
	 *            - The target aitile.
	 * @throws StopRequestException
	 *             In case where the game engine requests an end to the game
	 * @return It will return a boolean value.If the tile that contains a wall
	 *         is in the range of a bomb, the result will be true.
	 * */
	public boolean getWallInDanger(AiTile aitile) throws StopRequestException {
		checkInterruption();

		zone = getZone();
		ourhero = zone.getOwnHero();
		ourtile = ourhero.getTile();
		boolean result = false;
		if (!aitile.getBlocks().isEmpty()) {
			for (AiBlock currentBlock : aitile.getBlocks()) {
				checkInterruption();
				if (currentBlock.isDestructible()) {
					for (AiBomb currentBomb : zone.getBombs()) {
						checkInterruption();
						if (currentBomb.getBlast().contains(aitile)) {
							return result = true;
						}
					}
				}
			}
		}
		return result;
	}
	/**
	 * getAnEnemyInMyRange will look for an enemy in the given tile's neighbors
	 * for a specified direction.
	 * 
	 * @param aitile
	 *            - Our tile.
	 * @param i
	 *            - The number of times we will be cycling in the function, this
	 *            number corresponds to our current range.
	 * @param direction
	 *            - The given direction.In this direction we will be looking for
	 *            an enemy
	 * @throws StopRequestException
	 *             In case where the game engine requests an end to the game
	 * @return It will return a boolean value.If the tile's neighbors contains
	 *         an enemy in the given direction, it will be true
	 * */
	public boolean getAnEnemyInMyRange(AiTile aitile, Direction direction, int i) throws StopRequestException {
		checkInterruption();
		boolean result = false;
		
		zone = getZone();
		ourhero = zone.getOwnHero();
		ourtile = ourhero.getTile();
		int currentrange = ourhero.getBombRange();

		if (!zone.getRemainingOpponents().isEmpty()) {

			while (i < currentrange) {
				checkInterruption();
				if (controlOfBlocks(aitile, direction) == true
						&& aitile.getNeighbor(direction).getItems().isEmpty()) {
					if (controlEnemy(aitile, direction) == true) {
						return result = true;
					} else {
						i++;
						return result = getAnEnemyInMyRange(
								aitile.getNeighbor(direction), direction, i);
					}
				}

				else {
					return result = false;
				}
			}
		}
		return result;

	}
	/**
	 * controlEnemy will look for an enemy in the neighbor of the given tile,in
	 * the specified direction.
	 * 
	 * @param aitile
	 *            - The target.
	 * @param direction
	 *            - The direction that we will be looking at.
	 * @throws StopRequestException
	 *             In case where the game engine requests an end to the game
	 * @return It will return a boolean value.If the tile's neighbor in the
	 *         given direction has an enemy, it will return true "
	 * */
	public boolean controlEnemy(AiTile aitile, Direction direction)
			throws StopRequestException {
		checkInterruption();

		boolean result = false;

		if (getNearestEnemy().getTile().equals(aitile.getNeighbor(direction))) {
			result = true;
		}
		return result;
	}
	/**
	 * For a given tile's neighbor, it will control the existence of blocks.
	 * 
	 * @param aitile
	 *            - The target.
	 * @param direction
	 *            - The direction that we will be looking at.
	 * @throws StopRequestException
	 *             In case where the game engine requests an end to the game
	 * @return It will return a boolean value.If the tile in the given direction
	 *         has a block or wall on it, it will return false.
	 * */
	public boolean controlOfBlocks(AiTile aitile, Direction direction)
			throws StopRequestException {
		checkInterruption();
		boolean result = false;

		if (aitile.getNeighbor(direction).getBlocks().isEmpty()) {
			if (aitile.getNeighbor(direction).getBombs().isEmpty()) {
				result = true;

			}
		}
		return result;

	}
	/**
	 * controlOfDestructibleBlock will look if the given tile's neighbor
	 * contains a destructible block.
	 * 
	 * @param aitile
	 *            - The target.
	 * @param direction
	 *            - The direction that we will be looking at.
	 * @throws StopRequestException
	 *             In case where the game engine requests an end to the game
	 * @return It will return a boolean value.If the tile in the given direction
	 *         has a destructible wall, it will return true "
	 * */
	public boolean controlOfDestructibleBlock(AiTile aitile, Direction direction)
			throws StopRequestException {
		checkInterruption();
		boolean result = false;
		for (AiBlock currentblock : aitile.getNeighbor(direction).getBlocks()) {
			checkInterruption();
			if (currentblock.isDestructible()
					&& aitile.getNeighbor(direction).getItems().isEmpty()) {

				return result = true;
			}
		}

		return result;

	}
	/**
	 * 
	 * This method searches the zone and finds the enemy agent which is closest
	 * to our tile
	 * 
	 * 
	 * @throws StopRequestException
	 *             In case where the game engine requests an end to the game
	 * 
	 * @return An enemy AiHero closest to our agent is returned
	 * */
	public AiHero getNearestEnemy() throws StopRequestException {
		checkInterruption();
		int enemydist = 10000;
		AiHero nearestEnemy = null;
		zone = getZone();
		ourhero = zone.getOwnHero();
		ourtile = ourhero.getTile();
		for (AiHero enemy : zone.getRemainingOpponents()) {
			checkInterruption();
			if (enemy.hasEnded())
				continue;

			int dist = getDist(enemy.getTile(), ourtile);
			if (dist < enemydist) {
				nearestEnemy = enemy;
				enemydist = dist;
			}
		}
		return nearestEnemy;
	}
	/**
	 * 
	 * getDist is a method which returns the non cyclic distance between two
	 * tiles.
	 * 
	 * @param aitile
	 *            - The first given tile.
	 * 
	 * @param aitile1
	 *            - The second tile is our target.
	 * 
	 * @throws StopRequestException
	 *             In case where the game engine requests an end to the game
	 * 
	 * @return distance between the tiles
	 * */
	public int getDist(AiTile aitile, AiTile aitile1)
			throws StopRequestException {
		checkInterruption();
		int distance = Math.abs(aitile.getCol() - aitile1.getCol())
				+ Math.abs(aitile.getRow() - aitile1.getRow());
		return distance;
	}
	
	/**
	 * This method is used for determining the accessible tiles of the hero that
	 * must be given in the parameters list. In the mean time it simulates a
	 * bomb in the given tile, and in our own tile if the value of tile is null.
	 * 
	 * @param hero
	 *            - The agent which we will be looking for.
	 * @param tile
	 *            - The tile that we want to simulate our bomb on.If tile is
	 *            null then the bomb will be created on our tile.
	 * @param simulate
	 *            - If true getSafeTiles() will make a simulation of the given
	 *            hero in the given tile.
	 * @throws StopRequestException
	 *             In case where the game engine requests an end to the game
	 * @return The return of this method will be a list of aitile which are
	 *         accesible to a specific hero
	 * */
	public List<AiTile> getSafeTiles(AiHero hero, AiTile tile, boolean simulate)
			throws StopRequestException {
		this.checkInterruption();
		zone = getZone();
		ourhero = zone.getOwnHero();
		ourtile = ourhero.getTile();
		AiSimZone simzone = new AiSimZone(zone);
		ArrayList<AiTile> list = new ArrayList<AiTile>();
		ArrayList<AiTile> safe = new ArrayList<AiTile>();
		ArrayList<AiTile> blastflamelist = new ArrayList<AiTile>();

		list.add(hero.getTile());

		for (AiBomb b : simzone.getBombs()) {
			this.checkInterruption();

			if (b.getBlast().contains(hero.getTile()))
				continue;

			for (AiTile t : b.getBlast()) {
				this.checkInterruption();
				blastflamelist.add(t);
			}
		}

		for (AiFire aifire : simzone.getFires()) {
			this.checkInterruption();
			blastflamelist.add(aifire.getTile());
		}
		while (!list.isEmpty()) {
			this.checkInterruption();
			ArrayList<AiTile> list1 = new ArrayList<AiTile>();
			for (AiTile aitile : list) {
				this.checkInterruption();
				for (Direction direction : Direction.getPrimaryValues()) {
					this.checkInterruption();
					AiTile neighbor = aitile.getNeighbor(direction);

					if (neighbor.isCrossableBy(hero)
							&& !safe.contains(neighbor)) {
						if (!blastflamelist.contains(neighbor)) {
							list1.add(neighbor);
						}
					}

				}
				safe.add(aitile);
			}
			for (AiTile aitile : safe) {
				this.checkInterruption();
				if (list.contains(aitile)) {
					list.remove(aitile);
				}
			}
			for (AiTile aitile : list1) {
				this.checkInterruption();
				list.add(aitile);
			}
		}
		// simulates our bomb and removes blast range from the safe tiles.
		if (simulate) {
			AiBomb b = simzone.createBomb(tile, simzone.getOwnHero());
			safe.removeAll(b.getBlast());
		}
		return safe;
	}

}
