package org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v1;

import java.util.ArrayList;

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
	/** */
	private AlcarKayaYildirim ai;
	
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
	protected ModeHandler modeHandler;
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
			modeHandler.updateOutput();
			
	}
	
	
	// NOTRE METHODE
	
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
		for ( AiBomb currentBomb : this.ai.getZone().getBombs() )
		{
			ai.checkInterruption();
			dangerousTiles.add( currentBomb.getTile() );
			for ( AiTile currentTile : currentBomb.getBlast() )
			{
				ai.checkInterruption();
				dangerousTiles.add( currentTile );
			}
		}
		for ( AiFire currentFire : this.ai.getZone().getFires() )
		{
			ai.checkInterruption();
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
		this.checkInterruption();

		zone = getZone();
		ourhero = zone.getOwnHero();
		ourtile = ourhero.getTile();
		boolean result = false;
		if (!aitile.getBlocks().isEmpty()) {
			for (AiBlock currentBlock : aitile.getBlocks()) {
				this.checkInterruption();
				if (currentBlock.isDestructible()) {
					for (AiBomb currentBomb : zone.getBombs()) {
						this.checkInterruption();
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
	public boolean getAnEnemyInMyRange(AiTile aitile, Direction direction, int i)
			throws StopRequestException {
		this.checkInterruption();
		boolean result = false;
		zone = getZone();
		ourhero = zone.getOwnHero();
		ourtile = ourhero.getTile();
		int currentrange = ourhero.getBombRange();

		if (!zone.getRemainingOpponents().isEmpty()) {

			while (i < currentrange) {
				this.checkInterruption();
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
		this.checkInterruption();

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
		this.checkInterruption();
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
		this.checkInterruption();
		boolean result = false;
		for (AiBlock currentblock : aitile.getNeighbor(direction).getBlocks()) {
			this.checkInterruption();
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
		this.checkInterruption();
		int enemydist = 10000;
		AiHero nearestEnemy = null;
		zone = getZone();
		ourhero = zone.getOwnHero();
		ourtile = ourhero.getTile();
		for (AiHero enemy : zone.getRemainingOpponents()) {
			this.checkInterruption();
			if (enemy.hasEnded())
				continue;

			int dist = this.getDist(enemy.getTile(), ourtile);
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
		this.checkInterruption();
		int distance = Math.abs(aitile.getCol() - aitile1.getCol())
				+ Math.abs(aitile.getRow() - aitile1.getRow());
		return distance;
	}


}
