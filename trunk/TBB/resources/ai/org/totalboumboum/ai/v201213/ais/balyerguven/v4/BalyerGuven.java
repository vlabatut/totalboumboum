package org.totalboumboum.ai.v201213.ais.balyerguven.v4;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.totalboumboum.ai.v201213.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201213.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201213.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityHandler;
import org.totalboumboum.ai.v201213.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiBomb;
import org.totalboumboum.ai.v201213.adapter.data.AiFire;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiSuddenDeathEvent;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * our BalyerGuven class
 *
 * @author Leman Sebla Balyer
 * @author Ecem Güven
 */
@SuppressWarnings("deprecation")
public class BalyerGuven extends ArtificialIntelligence
{
	/** represents hero */
	AiHero hero;
	/** represents zone */
	AiZone zone;

	/**
	 * la methode de la sortie texte
	 */
	public BalyerGuven()
	{	// active/désactive la sortie texte
		verbose = false;
	}
	
	@Override
	protected void initOthers() throws StopRequestException
	{	checkInterruption();
		// cf. la Javadoc dans ArtificialIntelligence pour une description de la méthode
	}
	
	/////////////////////////////////////////////////////////////////
	// PERCEPTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void initPercepts() throws StopRequestException
	{	checkInterruption();
		// cf. la Javadoc dans ArtificialIntelligence pour une description de la méthode
		zone=getZone();
		hero=zone.getOwnHero();
	}
	
	@Override
	protected void updatePercepts() throws StopRequestException
	{	checkInterruption();
		// cf. la Javadoc dans ArtificialIntelligence pour une description de la méthode
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
	protected AiModeHandler<BalyerGuven> getModeHandler() throws StopRequestException
	{	checkInterruption();
		return modeHandler;
	}

	@Override
	protected AiUtilityHandler<BalyerGuven> getUtilityHandler() throws StopRequestException
	{	
		checkInterruption();
		return utilityHandler;
	}

	@Override
	protected AiBombHandler<BalyerGuven> getBombHandler() throws StopRequestException
	{	checkInterruption();
		return bombHandler;
	}

	@Override
	protected AiMoveHandler<BalyerGuven> getMoveHandler() throws StopRequestException
	{	checkInterruption();
		return moveHandler;
	}

	/** 
	 * Method to get this AI's hero.
	 * 
	 * @return this AI's hero.
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	public AiHero getHero() throws StopRequestException
	{	checkInterruption();
		return hero;
	}
	/**
	 * time for running
	 */
	private long TIME = 1000;
	/**
	 * list of reachable tiles in the zone
	 */
	private ArrayList<AiTile> reachableTiles;
	

	/**
	 * method for getting the reachable tiles.
	 * 
	 * @param tile
	 * 		?	
	 * @return 
	 * 		list of tiles reachable
	 * @throws StopRequestException
	 *      If the engine demands the termination of the agent.
	 */
	protected ArrayList<AiTile> getReachableTiles( AiTile tile ) throws StopRequestException
	{
		checkInterruption();
		reachableTiles = new ArrayList<AiTile>();
		fillAccessibleTilesBy(tile);

		return reachableTiles;
	}

	/** 
	 * 
	 * @param tile
	 *            The tile to start looking from. If not crossable, list will
	 *            not be populated.
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	private void fillAccessibleTilesBy( AiTile tile ) throws StopRequestException
	{
		checkInterruption();
		if ( tile.isCrossableBy( getHero() ) )
		{
			reachableTiles.add( tile );
			if ( tile.getNeighbor( Direction.UP ).isCrossableBy( getHero() ) && !reachableTiles.contains( tile.getNeighbor( Direction.UP ) ) ) fillAccessibleTilesBy( tile.getNeighbor( Direction.UP ) );
			if ( tile.getNeighbor( Direction.DOWN ).isCrossableBy( getHero() ) && !reachableTiles.contains( tile.getNeighbor( Direction.DOWN ) ) ) fillAccessibleTilesBy( tile.getNeighbor( Direction.DOWN ) );
			if ( tile.getNeighbor( Direction.LEFT ).isCrossableBy( getHero() ) && !reachableTiles.contains( tile.getNeighbor( Direction.LEFT ) ) ) fillAccessibleTilesBy( tile.getNeighbor( Direction.LEFT ) );
			if ( tile.getNeighbor( Direction.RIGHT ).isCrossableBy( getHero() ) && !reachableTiles.contains( tile.getNeighbor( Direction.RIGHT ) ) ) fillAccessibleTilesBy( tile.getNeighbor( Direction.RIGHT ) );
		}
	}

	/**
	 * 
	 * method for finding the biggest tile.
	 * 
	 * @throws StopRequestException
	 *             In case where the game engine requests an end to the game
	 * 
	 * @return biggest tile
	 */
	public AiTile getBiggestTile() throws StopRequestException {

		checkInterruption();
		Map<AiTile, Float> hashmap = utilityHandler.getUtilitiesByTile();
		AiTile result = hero.getTile();
		float value = Float.MIN_VALUE;
		for (Entry<AiTile, Float> entry : hashmap.entrySet())
		{
			checkInterruption();
			AiTile tempTile = entry.getKey();
			Float utility = entry.getValue();
			if (utility > value)
			{
				value = utility;
				result = tempTile;
			}
		}
		return result;
	}
	
	/** method for current dangerous tiles
	 * @return ArrayList<AiTile> dangerous tiles
	 * @throws StopRequestException
	 * 			In case where the game engine requests an end to the game
	 */
	public ArrayList<AiTile> getCurrentDangerousTiles() throws StopRequestException
	{
		checkInterruption();
		ArrayList<AiTile> dangerousTiles = new ArrayList<AiTile>();
		for ( AiBomb bomb : zone.getBombs() )
		{
			checkInterruption();
			dangerousTiles.add(bomb.getTile());
			for (AiTile tile : bomb.getBlast())
			{
				checkInterruption();
				dangerousTiles.add(tile);
			}
		}
		for (AiFire fire : zone.getFires())
		{
			checkInterruption();
			dangerousTiles.add(fire.getTile());
		}

		long time = zone.getTotalTime();
		List<AiSuddenDeathEvent> events = zone.getAllSuddenDeathEvents();
		for ( AiSuddenDeathEvent death : events )
		{
			checkInterruption();
			if ( time < death.getTime() && death.getTime() - time <= TIME )
				dangerousTiles.addAll(death.getTiles());
		}
		return dangerousTiles;
	}

	/** method for current dangerous tiles
	 * @return ArrayList<AiTile> dangerous tiles
	 * @throws StopRequestException
	 * 			In case where the game engine requests an end to the game
	 */
	public List<AiTile> getReachableSecureTiles() throws StopRequestException
	{
		checkInterruption();
		List<AiTile> reachableTiles = getReachableTiles(hero.getTile());
		reachableTiles.removeAll(getCurrentDangerousTiles());
		return reachableTiles;
	}

	/**
	 * method for finding closest safe tiles.
	 * @return secureTile
	 * 
	 * @throws StopRequestException
	 * 			In case where the game engine requests an end to the game
	 */
	public AiTile getClosestSecureTile() throws StopRequestException
	{
		checkInterruption();
		AiTile result = hero.getTile();
		List<AiTile> reachableSecureTiles = getReachableSecureTiles();
		int distance = Integer.MAX_VALUE;
		for ( AiTile secureTile : reachableSecureTiles )
		{
			checkInterruption();
			int dist = zone.getTileDistance(hero.getTile(), secureTile);
			if ( dist < distance )
			{
				distance = dist;
				result = secureTile;
			}
		}
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void updateOutput() throws StopRequestException {
		checkInterruption();
		moveHandler.updateOutput();
		// les utilités courantes
		utilityHandler.updateOutput();

		// cf. la Javadoc dans ArtificialIntelligence pour une description de la
		// méthode
	}
}
