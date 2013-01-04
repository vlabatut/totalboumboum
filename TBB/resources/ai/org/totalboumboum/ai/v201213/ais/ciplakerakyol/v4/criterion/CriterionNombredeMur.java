package org.totalboumboum.ai.v201213.ais.ciplakerakyol.v4.criterion;

/*
 import java.util.Arrays;
 import java.util.Set;
 import java.util.TreeSet;
 */
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionInteger;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiBlock;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.ciplakerakyol.v4.CiplakErakyol;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Cette criteria est pour trouver les nombre des murs destructibles
 * 
 * @author Hazal Çıplak
 * @author Şebnem Erakyol
 */
public class CriterionNombredeMur extends
		AiUtilityCriterionInteger<CiplakErakyol> {
	/** Nom de ce critère */
	public static final String NAME = "NombreDesMurs";

	/** max nombre pour les murs */
	private final static int MAX = 4;
	/**
	 * @param ai
	 *            l'agent concerné.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public CriterionNombredeMur(CiplakErakyol ai) throws StopRequestException {
		super(ai, NAME, 0, 4);
		ai.checkInterruption();
		this.ai = ai;
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public Integer processValue(AiTile tile) throws StopRequestException {
		ai.checkInterruption();

		int result = 0;
		for (Direction direction : Direction.getPrimaryValues())
		{	ai.checkInterruption();
			for (AiBlock currentBlock : tile.getNeighbor(direction).getBlocks())
			{	ai.checkInterruption();
				
				if (currentBlock.isDestructible())
					result++;
			}
		}
		if ( result < 0 )
			result = 0;
		if ( result > MAX )
			result = MAX;
		return result;
	}
}