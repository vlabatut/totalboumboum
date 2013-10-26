package org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v4.criterion;


import java.util.List;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionInteger;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiBlock;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v4.AlcarKayaYildirim;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * The criteria that will evaluate the tile for the number of destructible walls
 * surrounding it.
 * 
 * @author Ekin Alçar
 * @author Ulaş Kaya
 * @author Yağmur Yıldırım
 */
@SuppressWarnings("deprecation")
public class NbrAdjacentMurs extends AiUtilityCriterionInteger<AlcarKayaYildirim>
{	/** Nom de ce critère */
	public static final String NAME = "NBR_ADJACENT_MURS";
    /** */
	AiZone zone;
	/** */
	AiHero ourhero;
	/** */
	AiTile ourtile;
	
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
		// init agent
	}

	/////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Integer processValue( AiTile tile ) throws StopRequestException
	{
		ai.checkInterruption();	
		zone = this.ai.getZone();
		ourhero = zone.getOwnHero();
		ourtile = ourhero.getTile();
		int result = 0;
		int i = 1;
		
		
		int bomb_range = ourhero.getBombRange();
		boolean[] obstacle = { true, true, true, true, true };
		
		AiTile up = tile.getNeighbor(Direction.UP);
		AiTile down = tile.getNeighbor(Direction.DOWN);
		AiTile left = tile.getNeighbor(Direction.LEFT);
		AiTile right = tile.getNeighbor(Direction.RIGHT);
		
		while (obstacle[4] && (i <= bomb_range)) {
			this.ai.checkInterruption();

			List<AiBlock> blocks;

			if (obstacle[0]) {
				blocks = up.getBlocks();
				if (!up.getItems().isEmpty())
					obstacle[0] = false;
				else if (!blocks.isEmpty()) {
					for (AiBlock block : blocks) {
						this.ai.checkInterruption();
						if (block.isDestructible())
							result++;
						obstacle[0] = false;
					}
				}

				up = up.getNeighbor(Direction.UP);
			}
			if (obstacle[1]) {
				blocks = down.getBlocks();
				if (!down.getItems().isEmpty())
					obstacle[1] = false;
				else if (!blocks.isEmpty()) {
					for (AiBlock block : blocks) {
						this.ai.checkInterruption();
						if (block.isDestructible())
							result++;
						obstacle[1] = false;
					}
				}
				down = down.getNeighbor(Direction.DOWN);
			}
			if (obstacle[2]) {
				blocks = left.getBlocks();
				if (!left.getItems().isEmpty())
					obstacle[2] = false;
				else if (!blocks.isEmpty()) {
					for (AiBlock block : blocks) {
						this.ai.checkInterruption();
						if (block.isDestructible())
							result++;
						obstacle[2] = false;
					}
				}
				left = left.getNeighbor(Direction.LEFT);
			}
			if (obstacle[3]) {
				blocks = right.getBlocks();
				if (!right.getItems().isEmpty())
					obstacle[3] = false;
				else if (!blocks.isEmpty()) {
					for (AiBlock block : blocks) {
						this.ai.checkInterruption();
						if (block.isDestructible())
							result++;
						obstacle[3] = false;
					}
				}
				right = right.getNeighbor(Direction.RIGHT);
			}
			if ((!obstacle[0]) && (!obstacle[1]) && (!obstacle[2] && (!obstacle[3])))
				obstacle[4] = false;

			i++;

		}
		if (result > 3)
			result = 3;
		if(result==0)
			result = 1;
		
		return result;	
	}
}
