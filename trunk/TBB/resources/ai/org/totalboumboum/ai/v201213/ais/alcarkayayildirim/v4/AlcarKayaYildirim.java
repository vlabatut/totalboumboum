package org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.totalboumboum.ai.v201213.adapter.data.AiItem;
import org.totalboumboum.ai.v201213.adapter.data.AiItemType;
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
@SuppressWarnings("deprecation")
public class AlcarKayaYildirim extends ArtificialIntelligence
{
	/** */
	AiZone zone;
	/** */
	AiHero ourhero;
	/** */
	AiTile ourtile;
	/**
	 * Stores the utility map.
	 */
	private HashMap<AiTile, Float> utilityMap;
	/** */
	private ArrayList<AiTile> accessibleTiles;
	/**
	 * Range's bottom limit, used in determining the potentially dangerous tiles
	 * on bomb put.
	 */
	private final int RANGE_BOTTOM_LIMIT = 0;
	/**
	 * Distance's bottom limit, if distance drops to this limit, the fire's edge
	 * is reached in {@link #getDangerousTilesOnBombPut()}.
	 */
	private final int DISTANCE_BOTTOM_LIMIT	= 0;
	/**
	 * Represents this AI's hero.
	 */
	private AiHero hero; 
	/** */
//	List<AiTile> nextSuddenTile;
	/**
	 * Used to determine if hero can reach safety if he puts a bomb on his
	 * location. IF safe tile count drops to this limit, there is nowhere to
	 * run.
	 */
	private final int SAFETY_TILE_COUNT_LIMIT = 0;
	
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
		this.accessibleTiles = new ArrayList<AiTile>();
//		this.nextSuddenTile = zone.getNextSuddenDeathEvent().getTiles();
		hero = this.getZone().getOwnHero();
	}
	
	@Override
	protected void updatePercepts() throws StopRequestException
	{	checkInterruption();
		this.accessibleTiles.clear();
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
				this.checkInterruption();
				dangerousTiles.add( currentTile );
			}
		}
		for ( AiFire currentFire : getZone().getFires() )
		{
			checkInterruption();
			dangerousTiles.add( currentFire.getTile() );
		}
		for(AiItem currentItem : getZone().getItems() ){
			this.checkInterruption();
			if(currentItem.getType().equals(AiItemType.NO_BOMB) ||
					currentItem.getType().equals(AiItemType.NO_FLAME) ||
					currentItem.getType().equals(AiItemType.NO_SPEED) ||
					currentItem.getType().equals(AiItemType.ANTI_BOMB) ||
					currentItem.getType().equals(AiItemType.ANTI_FLAME) ||
					currentItem.getType().equals(AiItemType.ANTI_SPEED) ||
					currentItem.getType().equals(AiItemType.RANDOM_NONE)){
				dangerousTiles.add(currentItem.getTile());
			}
		}
//		for (AiTile suddenTile : nextSuddenTile) {
//			this.checkInterruption();
//			
//				dangerousTiles.add(suddenTile); 
//			
//		}
		
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
	public boolean controlEnemy(AiTile aitile, Direction direction) throws StopRequestException {
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
			this.checkInterruption();
			if (currentblock.isDestructible() /* && aitile.getNeighbor(direction).getItems().isEmpty()*/) {
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
		int enemydist = 5000;
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
	public int getDist(AiTile aitile, AiTile aitile1) throws StopRequestException {
		this.checkInterruption();
		
		int distance = Math.abs(aitile.getCol() - aitile1.getCol()) + Math.abs(aitile.getRow() - aitile1.getRow());
		
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
	
	/**
	 * Determines the closest destructible wall tile to the closest enemy to
	 * this AI's hero.
	 * 
	 * @return The closest destructible wall's tile to the closest enemy to this
	 *         AI's hero.
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	public AiTile getClosestAccDesWalltoEnemy() throws StopRequestException
	{
		this.checkInterruption();
		zone = getZone();
		ourhero = zone.getOwnHero();
		ourtile = ourhero.getTile();
		double distance = Double.MAX_VALUE;
		AiTile result = null;
		for ( AiTile currentTile : this.getAccessibleDestructibleWallTiles(ourtile ) )
		{	this.checkInterruption();
			AiHero cl = this.getClosestEnemy();
			if(cl!=null)
			{	AiTile clTile = cl.getTile();
				int distance0 = this.getDist( currentTile, clTile );
				if ( distance0 < distance )
				{
					result = currentTile;
					distance = distance0;
				}
			}
		}
		return result;
	}
	/**
	 * Determines the accessible tiles to a given hero which are neighbor of a
	 * destructible wall.
	 * 
	 * @param heroTile
	 *            Hero to process.
	 * @return List of accessible tiles which are neighbor of a destructible
	 *         wall.
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	public ArrayList<AiTile> getAccessibleDestructibleWallTiles( AiTile heroTile ) throws StopRequestException
	{
		this.checkInterruption();
		zone = getZone();
		ourhero = zone.getOwnHero();
		ourtile = ourhero.getTile();
		ArrayList<AiTile> accessibleDestructibleWallTiles = new ArrayList<AiTile>();
		
		for ( AiTile currentTile : this.getAccessibleTiles( heroTile ) )
		{
			this.checkInterruption();
			for ( AiTile neighborTile : currentTile.getNeighbors() )
			{
				this.checkInterruption();
				for ( AiBlock neighborBlock : neighborTile.getBlocks() )
				{
					this.checkInterruption();
					if ( neighborBlock.isDestructible() && !accessibleDestructibleWallTiles.contains( neighborBlock ) )
					{
						accessibleDestructibleWallTiles.add( neighborTile );
					}
				}
			}
		}
		return accessibleDestructibleWallTiles;
	}
	
	/**
	 * Determines the closest enemy hero to this AI's hero.
	 * 
	 * @return The closest enemy hero.
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	public AiHero getClosestEnemy() throws StopRequestException
	{
		this.checkInterruption();
		zone = getZone();
		ourhero = zone.getOwnHero();
		ourtile = ourhero.getTile();
		double distance = Double.MAX_VALUE;
		AiHero result = null;

		for ( AiHero currentHero : this.getZone().getRemainingOpponents() )
		{
			this.checkInterruption();
			if ( this.getDist( ourtile, currentHero.getTile() ) < distance )
			{

				distance = this.getDist( ourtile, currentHero.getTile() );
				result = currentHero;
			}
		}
		return result;
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
	private void fillAccessibleTilesBy( AiTile sourceTile ) throws StopRequestException
	{
		this.checkInterruption();
		zone = getZone();
		ourhero = zone.getOwnHero();
		ourtile = ourhero.getTile();
		if ( sourceTile.isCrossableBy( ourhero) )
		{
			this.accessibleTiles.add( sourceTile );
			if ( sourceTile.getNeighbor( Direction.UP ).isCrossableBy(ourhero) && !this.accessibleTiles.contains( sourceTile.getNeighbor( Direction.UP ) ) ) fillAccessibleTilesBy( sourceTile.getNeighbor( Direction.UP ) );
			if ( sourceTile.getNeighbor( Direction.DOWN ).isCrossableBy(ourhero) && !this.accessibleTiles.contains( sourceTile.getNeighbor( Direction.DOWN ) ) ) fillAccessibleTilesBy( sourceTile.getNeighbor( Direction.DOWN ) );
			if ( sourceTile.getNeighbor( Direction.LEFT ).isCrossableBy(ourhero) && !this.accessibleTiles.contains( sourceTile.getNeighbor( Direction.LEFT ) ) ) fillAccessibleTilesBy( sourceTile.getNeighbor( Direction.LEFT ) );
			if ( sourceTile.getNeighbor( Direction.RIGHT ).isCrossableBy(ourhero) && !this.accessibleTiles.contains( sourceTile.getNeighbor( Direction.RIGHT ) ) ) fillAccessibleTilesBy( sourceTile.getNeighbor( Direction.RIGHT ) );
		}
	}
	
	/**
	 * To get the Accessible tiles of this AI's own hero. (TESTED, WORKS)
	 * 
	 * @return List of all tiles that accessible by this object's AI's own hero.
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	public ArrayList<AiTile> getAccessibleTiles() throws StopRequestException
	{
		this.checkInterruption();
		zone = getZone();
		ourhero = zone.getOwnHero();
		ourtile = ourhero.getTile();
		this.accessibleTiles = new ArrayList<AiTile>();
		fillAccessibleTilesBy(ourtile);

		return this.accessibleTiles;
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
	public ArrayList<AiTile> getAccessibleTiles( AiTile sourceTile ) throws StopRequestException
	{
		this.checkInterruption();
		this.accessibleTiles = new ArrayList<AiTile>();
		fillAccessibleTilesBy( sourceTile );

		return this.accessibleTiles;
	}
	
	/**
	 * Populates a list of tiles which will become dangerous if this AI's hero
	 * puts a bomb to his location. <br/>
	 * (TESTED, WORKS)
	 * 
	 * 
	 * @return The list of potentially dangerous tiles on bomb put.
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	public ArrayList<AiTile> getDangerousTilesOnBombPut() throws StopRequestException
	{
		this.checkInterruption();
		zone = getZone();
		ourhero = zone.getOwnHero();
		ourtile = ourhero.getTile();
		return this.getDangerousTilesOnBombPut(ourtile, ourhero.getBombRange());
	}

	/**
	 * Populates a list of tiles which will become dangerous if a bomb is put on
	 * given tile. <br/>
	 * (TESTED, WORKS) <br/>
	 * At the beginning of the game, the ranges of the heroes can be
	 * uninitialized. So, be careful when giving range parameter from a hero's
	 * range. <br/>
	 * If range is 0, this method will return an empty list.
	 * 
	 * @param givenTile
	 *            The tile which will contain the potential bomb.
	 * @param range
	 *            The explosion range of the potential bomb.
	 * @return The list of potentially dangerous tiles on bomb put.
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	public ArrayList<AiTile> getDangerousTilesOnBombPut( AiTile givenTile, int range ) throws StopRequestException
	{
		this.checkInterruption();
		zone = getZone();
		ourhero = zone.getOwnHero();
		ourtile = ourhero.getTile();
		ArrayList<AiTile> dangerousTilesOnBombPut = new ArrayList<AiTile>();
		if ( givenTile.isCrossableBy(ourhero) && ( range > RANGE_BOTTOM_LIMIT ) )
		{
			dangerousTilesOnBombPut.add( givenTile );

			AiTile currentTile = givenTile.getNeighbor( Direction.LEFT );
			int distance = range;
			while ( currentTile.isCrossableBy(ourhero) && ( distance > DISTANCE_BOTTOM_LIMIT ) )
			{
				this.checkInterruption();
				dangerousTilesOnBombPut.add( currentTile );
				if ( !currentTile.getItems().isEmpty() ) break;
				currentTile = currentTile.getNeighbor( Direction.LEFT );
				distance--;
			}

			currentTile = givenTile.getNeighbor( Direction.RIGHT );
			distance = range;
			while ( currentTile.isCrossableBy(ourhero) && ( distance > DISTANCE_BOTTOM_LIMIT ) )
			{
				this.checkInterruption();
				dangerousTilesOnBombPut.add( currentTile );
				if ( !currentTile.getItems().isEmpty() ) break;
				currentTile = currentTile.getNeighbor( Direction.RIGHT );
				distance--;
			}

			currentTile = givenTile.getNeighbor( Direction.UP );
			distance = range;
			while ( currentTile.isCrossableBy(ourhero) && ( distance > DISTANCE_BOTTOM_LIMIT ) )
			{
				this.checkInterruption();
				dangerousTilesOnBombPut.add( currentTile );
				if ( !currentTile.getItems().isEmpty() ) break;
				currentTile = currentTile.getNeighbor( Direction.UP );
				distance--;
			}

			currentTile = givenTile.getNeighbor( Direction.DOWN );
			distance = range;
			while ( currentTile.isCrossableBy(ourhero) && ( distance > DISTANCE_BOTTOM_LIMIT ) )
			{
				this.checkInterruption();
				dangerousTilesOnBombPut.add( currentTile );
				if ( !currentTile.getItems().isEmpty() ) break;
				currentTile = currentTile.getNeighbor( Direction.DOWN );
				distance--;
			}
		}

		return dangerousTilesOnBombPut;
	}
	
	/**
	 * Indentifie l'ensemble des cases situées au plus à un rayon
	 * donné d'une case centrale, et dans lesquelles on pourra 
	 * poser une bombe.
	 * 
	 * @param center
	 * 		La case centrale.
	 * @param hero
	 * 		Le personnage à considérer.
	 * @return 
	 * 		L'ensemble des cases concernées.
	 * 
	 * @throws StopRequestException 
	 * 		Le moteur du jeu a demandé à l'agent de s'arrêter. 
	 */
	public List<AiTile> getTilesForRadius(AiTile center, AiHero hero) throws StopRequestException
	{	this.checkInterruption();
		// init
		List<AiTile> result = new ArrayList<AiTile>(); //TreeSet<AiTile>();
		int range = hero.getBombRange();
		AiFire fire = hero.getBombPrototype().getFirePrototype();
		
		// on ne teste pas la case de la cible, on la considère comme ok
		result.add(center);
		
		// par contre, on teste celles situées à porté de bombes
		for(Direction d: Direction.getPrimaryValues())
		{	this.checkInterruption();
			AiTile neighbor = center;
			int i = 1;
			boolean blocked = false;
			while(i<=range && !blocked)
			{	this.checkInterruption();
				neighbor = neighbor.getNeighbor(d);
				if(neighbor.isCrossableBy(fire))
					result.add(neighbor);
				else
					blocked = true;
				i++;
			}
		}
		
		return result;
	}
	
	/**
	 * Returns enemy situation in the range of this AI's own hero. <br />
	 * 
	 * @return If there's another hero in the range or not.
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	protected boolean isHeroInRange() throws StopRequestException
	{
		this.checkInterruption();
		zone = getZone();
		ourhero = zone.getOwnHero();
		
		return isHeroInRange( ourhero);
	}

	/**
	 * Returns enemy situation in the range of the given hero. <br />
	 * 
	 * @param givenHero
	 *            The hero to do the operation for.
	 * @return If there's another hero in the range or not.
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	protected boolean isHeroInRange( AiHero givenHero ) throws StopRequestException
	{
		this.checkInterruption();

		for ( AiHero currentHero : this.getZone().getRemainingHeroes() )
		{
			this.checkInterruption();
			if ( !currentHero.equals( givenHero ) )
			{
				if ( this.getDangerousTilesOnBombPut( givenHero.getTile(), givenHero.getBombRange() ).contains( currentHero.getTile() ) ) return true;
			}
		}
		return false;
	}
	/**
	 * Checks the danger situation of this AI's hero. <br />
	 * (TESTED, WORKS)
	 * 
	 * @return If this AI's hero is in danger (in a blast range or in a flame)
	 *         or not.
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	protected boolean isHeroInDanger() throws StopRequestException
	{
		this.checkInterruption();
		zone = getZone();
		ourhero = zone.getOwnHero();
		ourtile = ourhero.getTile();
		if ( this.getCurrentDangerousTiles().contains( ourtile ) ) return true;
		
		return false;
	}

	/**
	 * Checks a hero's danger situation. <br />
	 * 
	 * @param givenHero
	 *            The hero to be checked.
	 * @return If given hero is in danger (in a blast range or in a flame) or
	 *         not.
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	protected boolean isHeroInDanger( AiHero givenHero ) throws StopRequestException
	{
		this.checkInterruption();
		if ( this.getCurrentDangerousTiles().contains( givenHero.getTile() ) ) return true;
		return false;
	}
	
	/**
	 * Checks if this AI's hero can reach a safe tile if he puts a bomb to his
	 * tile. <br />
	 * 
	 * @return If given hero can access a safe tile or not.
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	protected boolean canReachSafety() throws StopRequestException
	{
		this.checkInterruption();
		zone = getZone();
		ourhero = zone.getOwnHero();
		return this.canReachSafety( ourhero );
	}

	/**
	 * Checks if the given hero can reach a safe tile if he puts a bomb to his
	 * tile. <br />
	 * 
	 * @param givenHero
	 *            The hero to process.
	 * @return If given hero can access a safe tile or not.
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	protected boolean canReachSafety( AiHero givenHero ) throws StopRequestException
	{
		this.checkInterruption();
		
		ArrayList<AiTile> accessibleTiles = this.getAccessibleTiles( givenHero.getTile() );
		int safeTileCount = accessibleTiles.size();
		
		for ( AiTile currentTile : accessibleTiles )
		{
			this.checkInterruption();
			if ( this.getDangerousTilesOnBombPut( givenHero.getTile(), givenHero.getBombRange() ).contains( currentTile ) || this.getCurrentDangerousTiles().contains( currentTile ) ) 
					safeTileCount--;
		}
		return ( safeTileCount > SAFETY_TILE_COUNT_LIMIT );
	}
	/**
	 * Returns the tile which has the biggest utility value.
	 * 
	 * @return Returns the AiTile with the biggest utility value.
	 * 
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	protected AiTile getTileWithBiggestUtility() throws StopRequestException
	{
		this.checkInterruption();
		zone = getZone();
		ourhero = zone.getOwnHero();
		ourtile = ourhero.getTile();
		AiTile result = ourtile;
		for ( AiTile currentTile : utilityMap.keySet() )
		{
			this.checkInterruption();
			if ( utilityMap.get( currentTile ) > utilityMap.get( result ) )
			{
				result = currentTile;
			}
		}
		return result;
	}
	/**
	 * Method to get Utility map.
	 * 
	 * @return Utility map. Can be empty at start of the game.
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	protected HashMap<AiTile, Float> getUtilityMap() throws StopRequestException
	{
		this.checkInterruption();
		return utilityMap;
	}
	/**
	 * Sets utility map to the field.
	 * 
	 * @param utilityMap
	 *            The given utility map.
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	protected void setUtilityMap( HashMap<AiTile, Float> utilityMap ) throws StopRequestException
	{
		this.checkInterruption();
		this.utilityMap = utilityMap;
	}
	/**
	 * Le recherhe de la case ou l'utilité est maximal
	 * 
	 * @return AiTile -> case qui a l'utilité maximal
	 * 
	 * @throws StopRequestException
	 * 		?	
	 */
	public AiTile getBiggestTile() throws StopRequestException {
		this.checkInterruption();
		Map<AiTile, Float> hashmap;
		hashmap = getUtilityHandler().getUtilitiesByTile();

		List<AiTile> biggestTiles = new ArrayList<AiTile>();

		AiTile biggestTile = null;
		float value = -10;
		int distance = 1000;
		if (!hashmap.isEmpty()) {
			for (AiTile currentTile : hashmap.keySet()) {
				this.checkInterruption();

				if (hashmap.get(currentTile) > value) {

					value = hashmap.get(currentTile);
					biggestTile = currentTile;
					biggestTiles.clear();
					biggestTiles.add(currentTile);
					distance = getZone().getTileDistance(
							getZone().getOwnHero().getTile(), currentTile);
				} else if (hashmap.get(currentTile) == value) {
					int distance2 = getZone().getTileDistance(
							getZone().getOwnHero().getTile(), currentTile);
					if (distance > distance2) {
						biggestTile = currentTile;
						biggestTiles.clear();
						biggestTiles.add(currentTile);
						distance = getZone().getTileDistance(
								getZone().getOwnHero().getTile(), currentTile);
					} else if (distance == distance2)
						biggestTiles.add(currentTile);
				}
			}
			biggestTile = biggestTiles.get((int) (Math.random() * (biggestTiles
					.size() - 1)));
		}
		return biggestTile;
	}
	/**
	 * Determines the closest suitable tile for attack mode to this AI's hero.
	 * 
	 * @return The closest suitable tile.
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	public AiTile getClosestAttPertinentTile() throws StopRequestException
	{
		this.checkInterruption();
		zone = getZone();
		ourhero = zone.getOwnHero();
		ourtile = ourhero.getTile();
		AiHero enemy = this.getClosestEnemy();
		ArrayList<AiTile> dangerTiles = new ArrayList<AiTile>();
		
		for ( AiTile currentTile : this.getAccessibleTiles() )
		{
			this.checkInterruption();
			if ( this.getDangerousTilesOnBombPut( currentTile, ourhero.getBombRange() ).contains( enemy.getTile() ) )
			{
				dangerTiles.add( currentTile );
			}
		}

		AiTile result = null;
		double distance = Double.MAX_VALUE;
		
		for ( AiTile currentTile : dangerTiles )
		{
			this.checkInterruption();
			if ( this.getDist(ourtile, currentTile ) < distance )
			{
				result = currentTile;
				distance = this.getDist(ourtile, currentTile );
			}
		}

		return result;
	}

}
