package org.totalboumboum.ai.v201112.ais.coskunozdemir.v1;

import java.util.ArrayList;
import java.util.HashMap;

import org.totalboumboum.ai.v201112.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201112.adapter.agent.AiMode;
import org.totalboumboum.ai.v201112.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201112.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityHandler;
import org.totalboumboum.ai.v201112.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiBomb;
import org.totalboumboum.ai.v201112.adapter.data.AiFire;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.path.AiLocation;
import org.totalboumboum.ai.v201112.adapter.path.AiPath;
import org.totalboumboum.ai.v201112.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201112.adapter.path.cost.TileCostCalculator;
import org.totalboumboum.ai.v201112.adapter.path.heuristic.NoHeuristicCalculator;
import org.totalboumboum.ai.v201112.adapter.path.search.Astar;
import org.totalboumboum.ai.v201112.adapter.path.successor.BasicSuccessorCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe principale de votre agent, que vous devez compléter. Cf. la
 * documentation de {@link ArtificialIntelligence} pour plus de détails.
 * 
 * @author Doruk Coşkun
 * @author Utku Özdemir
 */
@SuppressWarnings("deprecation")
public class CoskunOzdemir extends ArtificialIntelligence
{
	private ArrayList<AiTile> accessibleTiles;
	private AiHero myHero;
	private AiTile myCurrentTile;
	private AiLocation myCurrentLocation;
	private HashMap<AiTile, Float> utilityMap;

	@Override
	protected void init() throws StopRequestException
	{
		checkInterruption();

		super.init();
		verbose = false;

		//  à compléter si vous voulez créer des objets
		// particuliers pour réaliser votre traitement, et qui
		// ne sont ni des gestionnaires (initialisés dans initHandlers)
		// ni des percepts (initialisés dans initPercepts).
		// Par exemple, ici on surcharge init() pour initialiser
		// verbose, qui est la variable controlant la sortie
		// texte de l'agent (true -> debug, false -> pas de sortie)

		// cf. la java doc dans ArtificialIntelligence pour une description de
		// la méthode
	}

	// ///////////////////////////////////////////////////////////////
	// PERCEPTS /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected void initPercepts() throws StopRequestException
	{
		checkInterruption();

		this.accessibleTiles = new ArrayList<AiTile>();
		this.myHero = this.getZone().getOwnHero();
		this.myCurrentTile = this.myHero.getTile();
		this.myCurrentLocation = new AiLocation( this.myCurrentTile );

	}

	@Override
	protected void updatePercepts() throws StopRequestException
	{
		checkInterruption();

		//  à compléter si vous avez des objets
		// à mettre à jour à chaque itération, e.g.
		// des objets créés par la méthode initPercepts().

		// cf. la java doc dans ArtificialIntelligence pour une description de
		// la méthode
		
		this.myCurrentTile = this.myHero.getTile();
		this.myCurrentLocation = new AiLocation( this.myCurrentTile );
		this.accessibleTiles.clear();
		fillAccessibleTiles( this.myCurrentTile, this.accessibleTiles );

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
	protected void initHandlers() throws StopRequestException
	{
		checkInterruption();

		// création des gestionnaires standard (obligatoires)
		modeHandler = new ModeHandler( this );
		utilityHandler = new UtilityHandler( this );
		bombHandler = new BombHandler( this );
		moveHandler = new MoveHandler( this );

		//  à compléter si vous utilisez d'autres gestionnaires
		// (bien sûr ils doivent aussi être déclarés ci-dessus)

		// cf. la java doc dans ArtificialIntelligence pour une description de
		// la méthode
	}

	@Override
	protected AiModeHandler<CoskunOzdemir> getModeHandler()
			throws StopRequestException
	{
		checkInterruption();
		return modeHandler;
	}

	@Override
	protected AiUtilityHandler<CoskunOzdemir> getUtilityHandler()
			throws StopRequestException
	{
		checkInterruption();
		return utilityHandler;
	}

	@Override
	protected AiBombHandler<CoskunOzdemir> getBombHandler()
			throws StopRequestException
	{
		checkInterruption();
		return bombHandler;
	}

	@Override
	protected AiMoveHandler<CoskunOzdemir> getMoveHandler()
			throws StopRequestException
	{
		checkInterruption();
		return moveHandler;
	}

	// ///////////////////////////////////////////////////////////////
	// OUTPUT /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected void updateOutput() throws StopRequestException
	{
		checkInterruption();

		//  à compléter si vous voulez modifier l'affichage
		// ici, par défaut on affiche :
		// les chemins et destinations courants
		moveHandler.updateOutput();
		
		// les utilités courantes
		utilityHandler.updateOutput();
		 modeHandler.updateOutput();

		// cf. la java doc dans ArtificialIntelligence pour une description de
		// la méthode
	}
	
	/////////////////Notre Methods////////////////////////////////

	
	/**
	 *  return all dangerous tiles
	 *   
	 *  
	 *  	
	 *  
	 *  
	 *  @return	
	 *  list of AiTiles which contain a bomb,a fire or threathened by a bomb	
	 */
	public ArrayList<AiTile> getCurrentDangerousTiles()
			throws StopRequestException
	{
		ArrayList<AiTile> dangerousTiles = new ArrayList<AiTile>();
		for ( AiBomb currentBomb : this.getZone().getBombs() )
		{
			checkInterruption();
			for ( AiTile currentTile : currentBomb.getBlast() )
			{
				checkInterruption();
				dangerousTiles.add( currentTile );
			}
		}
		for ( AiFire currentFire : this.getZone().getFires() )
		{
			checkInterruption();
			dangerousTiles.add( currentFire.getTile() );
		}
		return dangerousTiles;
	}

	/**
	 *  Returns true if our heros current tile is in danger
	 *   
	 *  
	 *  	
	 *  
	 *  
	 *  @return	
	 *  Returns a boolean value	
	 */
	protected boolean isInDanger() throws StopRequestException
	{
		return ( getCurrentDangerousTiles().contains( this.myCurrentTile ) );
	}
	
	
	
	/**
	 *  Return the threatened tiles by the bomb on the given tile
	 *   
	 *  
	 *  @param givenTile
	 *           Any tile which we want to put a bomb	
	 *  
	 *  
	 *  @return	
	 *  Returns a list of AiTiles
	 */
	public ArrayList<AiTile> getDangerousTilesOnPut( AiTile givenTile )
			throws StopRequestException
	{
		ArrayList<AiTile> dangerousTilesOnPut = new ArrayList<AiTile>();
		int myRow = givenTile.getRow(), myCol = givenTile.getCol();
		int myRange = this.myHero.getBombRange();
		dangerousTilesOnPut.add( givenTile );

		int currentRow = myRow - 1, currentCol = myCol;
		int distance = 0;
		while ( currentRow >= 0
				&& this.getZone().getTile( currentRow, currentCol )
						.isCrossableBy( this.getZone().getOwnHero() )
				&& distance < myRange )
		{
			checkInterruption();
			dangerousTilesOnPut.add( this.getZone().getTile( currentRow,
					currentCol ) );
			currentRow--;
			distance++;
		}
		currentRow = myRow + 1;
		currentCol = myCol;
		distance = 0;

		while ( currentRow < this.getZone().getWidth()
				&& this.getZone().getTile( currentRow, currentCol )
						.isCrossableBy( this.getZone().getOwnHero() )
				&& distance < myRange )
		{
			checkInterruption();
			dangerousTilesOnPut.add( this.getZone().getTile( currentRow,
					currentCol ) );
			currentRow++;
			distance++;
		}
		currentRow = myRow;
		currentCol = myCol - 1;
		distance = 0;

		while ( currentCol >= 0
				&& this.getZone().getTile( currentRow, currentCol )
						.isCrossableBy( this.getZone().getOwnHero() )
				&& distance < myRange )
		{
			checkInterruption();
			dangerousTilesOnPut.add( this.getZone().getTile( currentRow,
					currentCol ) );
			currentCol--;
			distance++;
		}

		currentRow = myRow;
		currentCol = myCol + 1;
		distance = 0;

		while ( currentCol < this.getZone().getHeight()
				&& this.getZone().getTile( currentRow, currentCol )
						.isCrossableBy( this.getZone().getOwnHero() )
				&& distance < myRange )
		{
			checkInterruption();
			dangerousTilesOnPut.add( this.getZone().getTile( currentRow,
					currentCol ) );
			currentCol++;
			distance++;
		}

		currentRow = myRow;
		currentCol = myCol;
		distance = 0;

		return dangerousTilesOnPut;
	}

	
	
	/**
	 *  Returns true if there is an enemy in my bomb blast zone
	 *   
	 *  
	 *  	
	 *  
	 *  
	 *  @return	
	 *  Returns a boolean value
	 */
	protected boolean enemyInRange() throws StopRequestException
	{
		for ( AiHero currentEnemy : this.getZone().getRemainingOpponents() )
		{
			checkInterruption();
			if ( getDangerousTilesOnPut( this.myHero.getTile() ).contains(
					currentEnemy.getTile() ) )
				return true;
		}
		return false;
	}

	
	
	
	
	/**
	 * Fills the Arraylist accessibleTiles with the crossable tiles that I can
	 * reach by just walking by now.
	 * <br/>Note: Recursive, works but can cause memory problems in large maps with
	 * many accessible tiles.
	 * <br/>Can be optimized by considering the incoming direction.
	 * 
	 *  @param currentTile 
	 * 		My current tile
	 * 
	 *  @param accessibleTiles
	 * 		The tile which we are going to fill
	 *   
	 */	
	protected void fillAccessibleTiles( AiTile currentTile,
			ArrayList<AiTile> accessibleTiles ) throws StopRequestException
	{
		if ( currentTile.isCrossableBy( this.getZone().getOwnHero() ) )
		{
			accessibleTiles.add( currentTile );
			if ( currentTile.getNeighbor( Direction.UP ).isCrossableBy(
					this.getZone().getOwnHero() )
					&& !accessibleTiles.contains( currentTile
							.getNeighbor( Direction.UP ) ) )
				fillAccessibleTiles( currentTile.getNeighbor( Direction.UP ),
						accessibleTiles );
			if ( currentTile.getNeighbor( Direction.DOWN ).isCrossableBy(
					this.getZone().getOwnHero() )
					&& !accessibleTiles.contains( currentTile
							.getNeighbor( Direction.DOWN ) ) )
				fillAccessibleTiles( currentTile.getNeighbor( Direction.DOWN ),
						accessibleTiles );
			if ( currentTile.getNeighbor( Direction.LEFT ).isCrossableBy(
					this.getZone().getOwnHero() )
					&& !accessibleTiles.contains( currentTile
							.getNeighbor( Direction.LEFT ) ) )
				fillAccessibleTiles( currentTile.getNeighbor( Direction.LEFT ),
						accessibleTiles );
			if ( currentTile.getNeighbor( Direction.RIGHT ).isCrossableBy(
					this.getZone().getOwnHero() )
					&& !accessibleTiles.contains( currentTile
							.getNeighbor( Direction.RIGHT ) ) )
				fillAccessibleTiles(
						currentTile.getNeighbor( Direction.RIGHT ),
						accessibleTiles );
		}
	}

	
	/**
	 *  Returns true if I can reach safety if I put a bomb here
	 *   
	 *  
	 *  	
	 *  
	 *  
	 *  @return	
	 *  Returns a boolean value
	 */ 
	protected boolean canReachSafety() throws StopRequestException
	{
		ArrayList<AiTile> accessibleTiles = new ArrayList<AiTile>();
		this.fillAccessibleTiles( this.getZone().getOwnHero().getTile(),
				accessibleTiles );
		int safeTileCount = accessibleTiles.size();
		for ( AiTile currentTile : accessibleTiles )
		{
			if ( getDangerousTilesOnPut( this.myHero.getTile() ).contains(
					currentTile )
					|| getCurrentDangerousTiles().contains( currentTile ) )
			{
				safeTileCount--;
			}
		}

		return ( safeTileCount > 0 );
	}

	
	/**
	 *  Returns the shortest path to given tile using A*
	 *   
	 *  
	 *   @param dest
	 * 		The tile which we want to calculate the shortest path between our current tile and destination tile
	 *  
	 *  
	 *  @return	
	 *  Returns a AiPath
	 */ 
	protected AiPath shortestTo( AiTile dest ) throws StopRequestException,
			LimitReachedException
	{
		// calculators
		TileCostCalculator tcc = new TileCostCalculator( this );
		NoHeuristicCalculator nhc = new NoHeuristicCalculator( this );
		BasicSuccessorCalculator bsc = new BasicSuccessorCalculator( this );

		Astar astar = new Astar( this, this.myHero, tcc, nhc, bsc );

		// astar init
		AiPath result = astar
				.processShortestPath( this.myCurrentLocation, dest );
		//System.out.println( "----Path----\n" + result );

		return result;
	}
	
	
	/**
	 *  Finds the closest accessible safe tile. Does not use A* (too slow)
	 *   
	 *  
	 *  	
	 *  
	 *  
	 *  @return	
	 *  Returns a AiTile
	 */ 
	protected AiTile closestSafeTile() throws StopRequestException,
			LimitReachedException
	{
		int distance = 500;
		AiTile result = this.myCurrentTile;
		for ( AiTile currTile : listByPerimeter( 6 ) )
		{

			// if ( !getCurrentDangerousTiles().contains( currTile ) && distance
			// > this.getZone().getTileDistance( this.myCurrentTile, currTile )
			// )
			// {
			// distance = this.getZone().getTileDistance( this.myCurrentTile,
			// currTile );
			// result = currTile;
			// System.out.println( " Tile : " + currTile.getRow() + "," +
			// currTile.getCol() + " Distance : " + distance );
			//
			// }
			
			if ( !getCurrentDangerousTiles().contains( currTile )
					&& distance > this.getZone().getTileDistance(
							this.myCurrentTile, currTile ) )
			{
				distance = this.getZone().getTileDistance( this.myCurrentTile,
						currTile );
				result = currTile;
//				System.out.println( " Tile : " + currTile.getRow() + ","
//						+ currTile.getCol() + " Distance : " + distance );

			}
		}
		return result;
	}
	
	/**
	 *  Returns a list of AiTiles defined by a perimeter 
	 *  </br>
	 *  EX: if the perimeter value is 1 and my current location is (5,5), the returning list contains
	 *  </br>
	 *  {(4,5)  , (5,4) , (5,6) , (6,5) }
	 *   
	 *  
	 *  @param perimeter
	 *           The perimeter value
	 *  
	 *  
	 *  @return	
	 *  Returns a list of AiTiles
	 */
	ArrayList<AiTile> listByPerimeter( int perimeter )
			throws StopRequestException
	{
		ArrayList<AiTile> result = new ArrayList<AiTile>();
		for ( AiTile currentTile : getAccessibleTiles() )
		{
			checkInterruption();
			if ( this.getZone().getTileDistance( currentTile,
					this.myCurrentTile ) <= perimeter )
			{
				result.add( currentTile );
			}
		}

		return result;
	}
	
	/**
	 *  Returns the tile which has the biggest utility value
	 *   
	 *  
	 *  	
	 *  
	 *  
	 *  @return	
	 *  Returns a AiTile
	 */ 
	protected AiTile getTileWithBiggestUtility() throws StopRequestException
	{
		AiTile result = this.myCurrentTile;
		for ( AiTile currentTile : utilityMap.keySet() )
		{
			checkInterruption();
			if ( utilityMap.get( currentTile ) > utilityMap.get( result )  )
			{
				result = currentTile;
			}
		}
		return result;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////

	// GETTERS--------------------------------------------
	protected AiHero getMyHero()
	{
		return myHero;
	}

	protected AiTile getMyCurrentTile()
	{
		return myCurrentTile;
	}

	protected AiLocation getMyCurrentLocation()
	{
		return myCurrentLocation;
	}

	protected ArrayList<AiTile> getAccessibleTiles()
	{
		return accessibleTiles;
	}

	protected boolean inModeAttack()
	{
		if ( this.MODE == AiMode.ATTACKING.toString() )
			return true;
		return false;
	}
	
	protected HashMap<AiTile, Float> getUtilityMap()
	{
		return utilityMap;
	}

	protected void setUtilityMap( HashMap<AiTile, Float> utilityMap )
	{
		this.utilityMap = utilityMap;
	}
	
}
