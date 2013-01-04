package org.totalboumboum.ai.v201213.ais.gerginozkanoglu.v2.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionInteger;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.gerginozkanoglu.v2.GerginOzkanoglu;
import org.totalboumboum.ai.v201213.ais.gerginozkanoglu.v2.TileCalculation;

/**
 * This criteria will be 
 * evaluated by number of destructible walls that can be destroyed by a single bomb.
 * 
 *({@code super(nom,inf,sup)}).
 * 
 * @author Tuğçe Gergin
 * @author Seçil Özkanoğlu
 */
public class NumberOfDestructibleWalls extends AiUtilityCriterionInteger<GerginOzkanoglu>
{	/** Criterion's name */
	public static final String NAME = "NumberOfDestructibleWalls";
	
	/**
	 * in case of being trapped, our limit is 2 for destructible walls.
	 */
	private static int MAX_DESTWALL = 2;
	/**
	 * @param ai
	 * 
	 * @throws StopRequestException	
	 */
	public NumberOfDestructibleWalls(GerginOzkanoglu ai) throws StopRequestException
	{	super(ai,NAME,0,2);
		ai.checkInterruption();
	}

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Integer processValue(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
		int result = 2;
		TileCalculation calcul = new TileCalculation(this.ai);
		int counter = calcul.numberOfDestructibleWalls(tile);
		if(counter == MAX_DESTWALL)
			result = 1;
		else if(counter > MAX_DESTWALL)
			result = 2;
		else
			result = 0;
		return result;
	}
}
