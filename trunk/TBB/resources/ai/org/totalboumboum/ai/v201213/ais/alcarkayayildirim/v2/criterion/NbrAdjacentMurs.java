package org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v2.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionInteger;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiBlock;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v2.AlcarKayaYildirim;
import org.totalboumboum.engine.content.feature.Direction;


/**
 * The criteria that will evaluate the tile for the number of destructible walls
 * surrounding it.
 * 
 * @author Ekin Alçar
 * @author Ulaş Kaya
 * @author Yağmur Yıldırım
 */
public class NbrAdjacentMurs extends AiUtilityCriterionInteger<AlcarKayaYildirim>
{	/** Nom de ce critère */
	public static final String NAME = "NBR_ADJACENT_MURS";
	
	/**
	 * Bottom value of the utility.
	 */
	public static final int  VALUE1 = 1;
	/**
	 * Middle value of the utility.
	 */
	public static final int VALUE2 = 2;
	/**
	 * Top value of the utility.
	 */
	public static final int VALUE3 = 3;
	/**
	 * Symbolises empty.
	 *
	 */
	public static final int VALUE4 = 0;

	
	/**
	 * Crée un nouveau critère entier.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public NbrAdjacentMurs(AlcarKayaYildirim ai) throws StopRequestException
	{	// init nom + bornes du domaine de définition
		super(ai,NAME,1,3);
		ai.checkInterruption();
	}

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Integer processValue( AiTile tile ) throws StopRequestException
	{
		ai.checkInterruption();
		/*int result = 0;


		// We basically count the number of destructible walls
			for (AiTile currentNeighbors : tile.getNeighbors()) {
				ai.checkInterruption();
				for (AiBlock currentblock : currentNeighbors.getBlocks()) {
					ai.checkInterruption();

					if (currentblock.isDestructible()) {
						result = result + 1;
					}
				}
			} */

		
		int count = VALUE4, result = VALUE4;

		for ( AiBlock currentBlock : tile.getNeighbor( Direction.UP ).getBlocks() )
		{
			ai.checkInterruption();
			if ( currentBlock.isDestructible() ) count++;
		}
		for ( AiBlock currentBlock : tile.getNeighbor( Direction.DOWN ).getBlocks() )
		{
			ai.checkInterruption();
			if ( currentBlock.isDestructible() ) count++;
		}
		for ( AiBlock currentBlock : tile.getNeighbor( Direction.LEFT ).getBlocks() )
		{
			ai.checkInterruption();
			if ( currentBlock.isDestructible() ) count++;
		}
		for ( AiBlock currentBlock : tile.getNeighbor( Direction.RIGHT ).getBlocks() )
		{
			ai.checkInterruption();
			if ( currentBlock.isDestructible() ) count++;
		}

		if ( count == VALUE4 )
			result = VALUE1;
		if ( count == VALUE1 ) result = VALUE1;
		else if ( count == VALUE2 ) result = VALUE2;
		else if ( count == VALUE3 ) result = VALUE3;

		return result;
	}
}
