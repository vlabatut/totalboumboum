package org.totalboumboum.ai.v201213.ais.balyerguven.v2;





import java.util.HashMap;

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
 * Classe principale de votre agent, que vous devez compléter.
 * Cf. la documentation de {@link ArtificialIntelligence} pour plus de détails.

 *
 * @author Leman Sebla Balyer
 * @author Ecem Güven
 */

public class BalyerGuven extends ArtificialIntelligence
{
	
	/**
	 * Instancie la classe principale de l'agent.
	 */
	private HashMap<AiTile, Float>	utilityMap;
	
	/** */
	AiHero hero;
	/** */
	AiZone myZone;
	
	
	/**
	 * 
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
	 * @return AiHero
	 * @throws StopRequestException
	 */
	public AiHero getHero() throws StopRequestException
	{	checkInterruption();
		myZone=this.getZone();
		hero=myZone.getOwnHero();
		
		return hero;
	}
	


	/**
	 * @return AiTile
	 * @throws StopRequestException
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
	 * @return AiHero
	 * @throws StopRequestException
	 */
	public AiHero getNearestEnemy() throws StopRequestException {
		checkInterruption();
		int enemydist = 10000;
		AiHero nearestEnemy = null;
		myZone = getZone();
		AiHero myHero=myZone.getOwnHero();
		myHero = myZone.getOwnHero();
		AiTile mytile = myHero.getTile();
		for (AiHero enemy : myZone.getRemainingOpponents()) {
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
