package org.totalboumboum.ai.v201213.ais.balyerguven.v3;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

import org.totalboumboum.ai.v201213.adapter.data.AiBomb;
import org.totalboumboum.ai.v201213.adapter.data.AiFire;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201213.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201213.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityHandler;
import org.totalboumboum.ai.v201213.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;


/**
 * our BalyerGuven class
 *
 * @author Leman Sebla Balyer
 * @author Ecem Güven
 */

public class BalyerGuven extends ArtificialIntelligence
{
	
	
	/**
	 * Stores the utility map.
	 */
	private HashMap<AiTile, Float>	utilityMap;
	
	/** represent hero */
	AiHero hero;
	/** represent zone*/
	AiZone zone;
	
	/** represent stuck utility tiles*/
	public HashSet<AiTile>			stuckUtilityTiles;
	
	/**
	 * represent last utility tiles
	 */
	public Queue<AiTile>			lastUtilityTiles;
	
	/**
	 * la methode de la sortie texte
	 */
	public BalyerGuven()
	{	// active/désactive la sortie texte
		verbose = false;
		
		this.lastUtilityTiles = new LinkedList<AiTile>();
		
		this.stuckUtilityTiles = new HashSet<AiTile>();
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
		zone=this.getZone();
		hero=zone.getOwnHero();
		
		return hero;
	}
	



	/**
	 * This method looks for the tile with the biggest utility value found in
	 * the list.
	 * 
	 * 
	 * @throws StopRequestException
	 *             In case where the game engine requests an end to the game
	 * 
	 * @return biggest tile
	 */
	protected AiTile getBiggestTile() throws StopRequestException {

		checkInterruption();
		AiTile result = this.getZone().getOwnHero().getTile();
		for ( AiTile currentTile : utilityMap.keySet() )
		{
			checkInterruption();
			if ( utilityMap.get( currentTile ) > utilityMap.get( result ) )
			{
				result = currentTile;
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
	 */
	public AiHero getNearestEnemy() throws StopRequestException {
		checkInterruption();
		int enemydist = 100000;
		AiHero nearestEnemy = null;
		zone = getZone();
		AiHero myHero=zone.getOwnHero();
		myHero = zone.getOwnHero();
		AiTile mytile = myHero.getTile();
		for (AiHero enemy : zone.getRemainingOpponents()) {
			checkInterruption();
			if (enemy.hasEnded())
				continue;

			int dist = getDist(enemy.getTile(), mytile);
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
	
	/** method for current dangerous tiles
	 * @return ArrayList<AiTile> dangerous tiles
	 * @throws StopRequestException
	 */
	public ArrayList<AiTile> getCurrentDangerousTiles() throws StopRequestException
	{
		this.checkInterruption();
		zone=this.getZone();
		ArrayList<AiTile> dangerousTiles = new ArrayList<AiTile>();
		for ( AiBomb currentBomb : zone.getBombs() )
		{
			this.checkInterruption();
			dangerousTiles.add( currentBomb.getTile() );
			for ( AiTile currentTile : currentBomb.getBlast() )
			{
				this.checkInterruption();
				dangerousTiles.add( currentTile );
			}
		}
		for ( AiFire currentFire : zone.getFires() )
		{
			this.checkInterruption();
			dangerousTiles.add( currentFire.getTile() );
		}
		return dangerousTiles;
	}
	
	/** method of endless loop
	 * @return boolean
	 * @throws StopRequestException
	 */
	public boolean inLoop() throws StopRequestException
	{
		this.checkInterruption();
		boolean result = false;
		if ( this.lastUtilityTiles.size() > 190 )
		{
			HashSet<AiTile> set = new HashSet<AiTile>( this.lastUtilityTiles);
			if ( set.size() == 2 )
			{
				result = true;
				
				this.stuckUtilityTiles= set;
			}
		}
		return result;
	}
	
	
	/** method for adding to last utility tiles
	 * @param toAdd
	 * @throws StopRequestException
	 */
	protected void addToLastUtilityTiles( AiTile toAdd ) throws StopRequestException
	{
		checkInterruption();
		this.lastUtilityTiles.add( toAdd );
		if ( this.lastUtilityTiles.size() > 200 )
		{
			this.lastUtilityTiles.remove();
		}
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
	
		// cf. la Javadoc dans ArtificialIntelligence pour une description de la méthode
	}
}
