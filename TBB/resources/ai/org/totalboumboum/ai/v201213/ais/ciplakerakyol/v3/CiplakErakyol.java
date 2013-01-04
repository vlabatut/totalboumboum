package org.totalboumboum.ai.v201213.ais.ciplakerakyol.v3;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.totalboumboum.ai.v201213.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201213.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201213.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityHandler;
import org.totalboumboum.ai.v201213.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.path.AiLocation;
import org.totalboumboum.ai.v201213.adapter.path.AiPath;
import org.totalboumboum.ai.v201213.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201213.adapter.path.search.Astar;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe principale de notre agent
 *
 * @author Hazal Çıplak
 * @author Şebnem Erakyol
 */
public class CiplakErakyol extends ArtificialIntelligence
{
	
	/** */
	Astar AStar;
	/**
	 * Instancie la classe principale de l'agent.
	 */
	public CiplakErakyol()
	{	// active/désactive la sortie texte
		verbose = true;
	}

	@Override
	protected void initOthers() throws StopRequestException
	{	
		checkInterruption();
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
	{
		checkInterruption();
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

		modeHandler = new ModeHandler(this);
		utilityHandler = new UtilityHandler(this);
		bombHandler = new BombHandler(this);
		moveHandler = new MoveHandler(this);
	}

	@Override
	protected AiModeHandler<CiplakErakyol> getModeHandler() throws StopRequestException
	{	checkInterruption();
		return modeHandler;
	}

	@Override
	protected AiUtilityHandler<CiplakErakyol> getUtilityHandler() throws StopRequestException
	{	checkInterruption();
		return utilityHandler;
	}

	@Override
	protected AiBombHandler<CiplakErakyol> getBombHandler() throws StopRequestException
	{	checkInterruption();
		return bombHandler;
	}

	@Override
	protected AiMoveHandler<CiplakErakyol> getMoveHandler() throws StopRequestException
	{	checkInterruption();
		return moveHandler;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void updateOutput() throws StopRequestException
	{
		checkInterruption();
		// les chemins et destinations courants
		moveHandler.updateOutput();
		// les utilités courantes
		utilityHandler.updateOutput();

	}

	/** */
	private Set<AiTile>	accessibleTiles;

	/**
	 * @param sourceTile
	 * @return accessibleTiles
	 * @throws StopRequestException
	 */
	protected Set<AiTile> getAccessibleTiles( AiTile sourceTile ) throws StopRequestException
	{
		checkInterruption();
		this.accessibleTiles = new HashSet<AiTile>();
		fillAccessibleTilesBy( sourceTile );
		return this.accessibleTiles;
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
		checkInterruption();
		if ( sourceTile.isCrossableBy( getZone().getOwnHero() ) )
		{
			this.accessibleTiles.add( sourceTile );
			if ( sourceTile.getNeighbor( Direction.UP ).isCrossableBy( getZone().getOwnHero() ) && !this.accessibleTiles.contains( sourceTile.getNeighbor( Direction.UP ) ) ) fillAccessibleTilesBy( sourceTile.getNeighbor( Direction.UP ) );
			if ( sourceTile.getNeighbor( Direction.DOWN ).isCrossableBy( getZone().getOwnHero() ) && !this.accessibleTiles.contains( sourceTile.getNeighbor( Direction.DOWN ) ) ) fillAccessibleTilesBy( sourceTile.getNeighbor( Direction.DOWN ) );
			if ( sourceTile.getNeighbor( Direction.LEFT ).isCrossableBy( getZone().getOwnHero() ) && !this.accessibleTiles.contains( sourceTile.getNeighbor( Direction.LEFT ) ) ) fillAccessibleTilesBy( sourceTile.getNeighbor( Direction.LEFT ) );
			if ( sourceTile.getNeighbor( Direction.RIGHT ).isCrossableBy( getZone().getOwnHero() ) && !this.accessibleTiles.contains( sourceTile.getNeighbor( Direction.RIGHT ) ) ) fillAccessibleTilesBy( sourceTile.getNeighbor( Direction.RIGHT ) );
		}
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
		checkInterruption();
		AiTile result = getZone().getOwnHero().getTile();
		Float utilityValue = Float.MIN_VALUE;
		Map<AiTile, Float> utilityMap = getUtilityHandler().getUtilitiesByTile();
		
		for ( AiTile tempTile : utilityMap.keySet() )
		{
			checkInterruption();
			if ( utilityMap.get( tempTile ) > utilityValue )
			{
				utilityValue = utilityMap.get( result );
				result = tempTile;
			}
		}
		return result;
	}
	
	/**
	* Utilise A* method pour calculer la chemin plus court d'un case dans
	* laquelle notre IA se trouvent et une case de la destination.
	*
	* @param targetTile
	* Case de destination.
	* @return La chemin plus courte
	*
	* @throws StopRequestException
	* Au cas où le moteur demande la terminaison de l'agent.
	*/
	public AiPath getShortestPathToAccessibleTile( AiTile targetTile ) throws StopRequestException
	{
		AiHero ownHero =getZone().getOwnHero();
		checkInterruption();
		if ( targetTile != ownHero.getTile() )
		{
			AiLocation startLocation = new AiLocation( ownHero );
			try
			{
				return AStar.startProcess( startLocation, targetTile );
			}
			catch ( LimitReachedException e )
			{
				// e.printStackTrace();
			}
		}
		return null;
	}


}
