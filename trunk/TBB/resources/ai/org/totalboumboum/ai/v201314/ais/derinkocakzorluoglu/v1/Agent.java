package org.totalboumboum.ai.v201314.ais.derinkocakzorluoglu.v1;

import java.util.ArrayList;

import org.totalboumboum.ai.v201314.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201314.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.agent.ArtificialIntelligence;
//import org.totalboumboum.engine.content.feature.Direction;
//import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
//import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionString;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.derinkocakzorluoglu.v1.Agent;
/**
 * Cf. la documentation de {@link ArtificialIntelligence} pour plus de détails.
 * 
 *
 * @author Emre Derin
 * @author Oktay Koçak
 * @author Emin Can Zorluoğlu
 */
public class Agent extends ArtificialIntelligence
{
	/**
	 * Instancie la classe principale de l'agent.
	 */
	public boolean enemy_accessible;
	
	/** Liste de tile accessible */

	public ArrayList<AiTile> accessibleTiles;
	
	/**
	 * getter pour accessibleTiles
	 * 
	 * @return liste de tiles accessible
	 * @throws StopRequestException
	 * 		description manquante !	
	 * */
	public ArrayList<AiTile> getAccessibleTiles() throws StopRequestException {
		checkInterruption();
		return accessibleTiles;
	}
	
	/**
	 * Constructeur de la classe Agent
	 */
	public Agent()
	{	checkInterruption();
		
		// active/désactive la sortie texte
		verbose = false;
	}
	
	@Override
	protected void initOthers()
	{	checkInterruption();
	this.enemy_accessible = false;
	
	// cf. la Javadoc dans ArtificialIntelligence pour une description de la méthode
	}
	
	/////////////////////////////////////////////////////////////////
	// PERCEPTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void initPercepts()
	{	checkInterruption();
	

		
		this.accessibleTiles = new ArrayList<AiTile>();
		
		
	}
		
		// cf. la Javadoc dans ArtificialIntelligence pour une description de la méthode
	
	
	@Override
	protected void updatePercepts()
	{	
		checkInterruption();
	this.accessibleTiles.clear();

		this.enemy_accessible = isEnemiesAccessible();
	
		
	
		// cf. la Javadoc dans ArtificialIntelligence pour une description de la méthode
	}
	
	/////////////////////////////////////////////////////////////////
	// HANDLERS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Gestionnaire chargé de calculer le mode de l'agent */
	protected ModeHandler modeHandler;
	/** Gestionnaire chargé de calculer les valeurs de préférence de l'agent */
	protected PreferenceHandler preferenceHandler;
	/** Gestionnaire chargé de décider si l'agent doit poser une bombe ou pas */
	protected BombHandler bombHandler;
	/** Gestionnaire chargé de décidé de la direction de déplacement de l'agent */
	protected MoveHandler moveHandler;
	
	@Override
	protected void initHandlers()
	{	checkInterruption();
		
		// création des gestionnaires standard (obligatoires)
		modeHandler = new ModeHandler(this);
		preferenceHandler = new PreferenceHandler(this);
		bombHandler = new BombHandler(this);
		moveHandler = new MoveHandler(this);
		
		
		
		// cf. la Javadoc dans ArtificialIntelligence pour une description de la méthode
	}

	@Override
	protected AiModeHandler<Agent> getModeHandler()
	{	checkInterruption();
		return modeHandler;
	}

	@Override
	protected AiPreferenceHandler<Agent> getPreferenceHandler()
	{	checkInterruption();
		return preferenceHandler;
	}

	@Override
	protected AiBombHandler<Agent> getBombHandler()
	{	checkInterruption();
		return bombHandler;
	}

	@Override
	protected AiMoveHandler<Agent> getMoveHandler()
	{	checkInterruption();
		return moveHandler;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void updateOutput()
	{	checkInterruption();

		

		// ici, par défaut on affiche :
			// les chemins et destinations courants
			moveHandler.updateOutput();
			// les preferences courantes
			preferenceHandler.updateOutput();
	
		// cf. la Javadoc dans ArtificialIntelligence pour une description de la méthode
	}
	
	/**
	 * Controle si un ennemi est accesible ou pas
	 * 
	 * @return vrai, s'il y a un adversaire accesible dans la zone
	 * @throws StopRequestException
	 * 		desc
	 */
public boolean isEnemiesAccessible() throws StopRequestException {
		checkInterruption();
		for (AiHero hero : getZone().getRemainingOpponents()) {
			checkInterruption();
			if (accessibleTiles.contains(hero.getTile())
					&& (hero.getId() != getZone().getOwnHero().getId()))
				return true;
		}
		return false;

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
/*
private void fillAccessibleTilesBy(AiTile sourceTile)
		throws StopRequestException {
	checkInterruption();
	AiHero hero=getZone().getOwnHero();
	if ( sourceTile.isCrossableBy(hero ) )
	{
		this.accessibleTiles.add( sourceTile );
		if ( sourceTile.getNeighbor( Direction.UP ).isCrossableBy( hero ) && !this.accessibleTiles.contains( sourceTile.getNeighbor( Direction.UP ) ) )
			fillAccessibleTilesBy( sourceTile.getNeighbor( Direction.UP ) );
		if ( sourceTile.getNeighbor( Direction.DOWN ).isCrossableBy( hero ) && !this.accessibleTiles.contains( sourceTile.getNeighbor( Direction.DOWN ) ) )
			fillAccessibleTilesBy( sourceTile.getNeighbor( Direction.DOWN ) );
		if ( sourceTile.getNeighbor( Direction.LEFT ).isCrossableBy( hero ) && !this.accessibleTiles.contains( sourceTile.getNeighbor( Direction.LEFT ) ) ) 
			fillAccessibleTilesBy( sourceTile.getNeighbor( Direction.LEFT ) );
		if ( sourceTile.getNeighbor( Direction.RIGHT ).isCrossableBy( hero ) && !this.accessibleTiles.contains( sourceTile.getNeighbor( Direction.RIGHT ) ) )
			fillAccessibleTilesBy( sourceTile.getNeighbor( Direction.RIGHT ) );
	}
}
*/
}