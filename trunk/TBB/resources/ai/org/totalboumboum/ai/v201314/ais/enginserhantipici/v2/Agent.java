package org.totalboumboum.ai.v201314.ais.enginserhantipici.v2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.totalboumboum.ai.v201314.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201314.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;

/**
 * Classe principale de votre agent, que vous devez compléter.
 * Cf. la documentation de {@link ArtificialIntelligence} pour plus de détails.
 * 
 *
 * @author Gözde Engin
 * @author Barış Serhan
 * @author Garip Tipici
 */
public class Agent extends ArtificialIntelligence
{
	/**
	 * Instancie la classe principale de l'agent.
	 */

	
	public Agent()
	{	checkInterruption();
	}
	
	@Override
	protected void initOthers()
	{	checkInterruption();

	
		// cf. la Javadoc dans ArtificialIntelligence pour une description de la méthode
	}
	
	/////////////////////////////////////////////////////////////////
	// PERCEPTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void initPercepts()
	{	checkInterruption();
	
	
		// cf. la Javadoc dans ArtificialIntelligence pour une description de la méthode
	}
	
	@Override
	protected void updatePercepts()
	{	checkInterruption();
	
		// active/désactive la sortie texte
		verbose = false;
		modeHandler.verbose = false;
		preferenceHandler.verbose = false;
		bombHandler.verbose = false;
		moveHandler.verbose = false;

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
	
	/**
	 * This method detects if the tile is in danger or not
	 * @param tile AiTile
	 * @return result boolean
	 */
	public boolean isInDanger(AiTile tile){
		checkInterruption();
		AiZone zone = getZone();
		boolean result;
		List<AiTile> dangertiles = new ArrayList<AiTile>();
		Map<Long, List<AiBomb>> allbombs = zone.getBombsByDelays();
		for(long l : allbombs.keySet()){
			checkInterruption();
			for(AiBomb bomb : allbombs.get(l)){
				checkInterruption();
				if(l < 1500){
					dangertiles.addAll(bomb.getBlast());
				}
			}
		}
		
		if(dangertiles.contains(tile)){
			result = true;
		}else{
			result = false;
		}
		return result;
	}
	
	
	/**
	 * if the enemy is near the selected tile, for criterion "DistanceAdversaire"
	 */
	public boolean b = false;
	
	/**
	 * if there is a pattern for applying triangle attack strategy
	 */
	public boolean PatternExist = false;
	
	
	/**
	 * list for holding the selected tile for triangle attack strategy
	 * myTiles(0) n myTiles(1) hold the corners and
	 * myTiles(2) holds the middle tile
	 * if there is specific pattern, myTiles(0) also shows the tile where there is enemy is staying near of it(inside of the trap)  
	 */
	public List<AiTile> myTiles = null;
	
	/**
	 * getter for myTiles
	 * @return myTiles
	 */
	public List<AiTile> getMyTiles() {
		checkInterruption();
		return myTiles;
	}

	/**
	 * setter for myTiles
	 * @param myTiles list of AiTiles
	 */
	public void setMyTiles(List<AiTile> myTiles) {
		checkInterruption();
		this.myTiles = myTiles;
	}

	/**
	 * 
	 * @return true if the pattern exist
	 */
	public boolean isPatternExist() {
		checkInterruption();
		return PatternExist;
	}

	/**
	 * setter for PatternExist
	 * @param patternExist true if pattern exist
	 */
	public void setPatternExist(boolean patternExist) {
		checkInterruption();
		PatternExist = patternExist;
	}

	@Override
	protected void initHandlers()
	{	checkInterruption();
		
		// création des gestionnaires standard (obligatoires)
		modeHandler = new ModeHandler(this);
		preferenceHandler = new PreferenceHandler(this);
		bombHandler = new BombHandler(this);
		moveHandler = new MoveHandler(this);
		b = false;
		// b = preferenceHandler.isThereATileNearByEnemy;
		
		
		// cf. la Javadoc dans ArtificialIntelligence pour une description de la méthode
	}

	
	/**
	 * getter for boolean b
	 * @return b
	 */
	public boolean isB() {
		checkInterruption();
		return b;
	}
	/**
	 * setter for b
	 * @param b true if Patterns.isThereATileNearByEnemy is true
	 */
	public void setB(boolean b) {
		checkInterruption();
		this.b = b;
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
	
		// vous pouvez changer la taille du texte affiché, si nécessaire
		// attention: il s'agit d'un coefficient multiplicateur
		AiOutput output = getOutput();
		output.setTextSize(2);


		// ici, par défaut on affiche :
			// les chemins et destinations courants
			moveHandler.updateOutput();
			// les preferences courantes
			preferenceHandler.updateOutput();
	
		// cf. la Javadoc dans ArtificialIntelligence pour une description de la méthode
	}

	/**
	 * @return
	 */
	
}
