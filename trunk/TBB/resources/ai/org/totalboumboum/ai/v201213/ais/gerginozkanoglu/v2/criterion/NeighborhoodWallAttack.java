package org.totalboumboum.ai.v201213.ais.gerginozkanoglu.v2.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.gerginozkanoglu.v2.GerginOzkanoglu;
import org.totalboumboum.ai.v201213.ais.gerginozkanoglu.v2.TileCalculation;

/**
 * this criterion will be used in case null of attacking mode.
 * 
 * @author Tuğçe Gergin
 * @author Seçil Özkanoğlu
 */
public class NeighborhoodWallAttack extends AiUtilityCriterionBoolean<GerginOzkanoglu>
{	/** Nom de ce critère */
	public static final String NAME = "NeighborhoodWallAttack";
	
	/**
	 * maximum wall count.
	 */
	public static int MAX_WALL_LIMIT = 1;
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public NeighborhoodWallAttack(GerginOzkanoglu ai) throws StopRequestException
	{	super(ai,NAME);
		ai.checkInterruption();
	}
	
    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
		boolean result = true;
	
		TileCalculation calculate = new TileCalculation(this.ai);
		if(calculate.neighborhoodOfDestructibleWalls(tile) < MAX_WALL_LIMIT)
		   result = false;
		
	
		return result;
	}
}
