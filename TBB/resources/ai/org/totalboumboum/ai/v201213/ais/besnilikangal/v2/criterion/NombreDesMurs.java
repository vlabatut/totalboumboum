package org.totalboumboum.ai.v201213.ais.besnilikangal.v2.criterion;

import java.util.List;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionInteger;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiBlock;
import org.totalboumboum.ai.v201213.adapter.data.AiFire;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.besnilikangal.v2.BesniliKangal;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Cette critere a été utilisé pour determiner combien de murs il y en a autour d'une case.
 * C'est-a-dire que GAUCHE,DROITE,BAS,HAUT
 * 
 * @author Doruk Kangal
 * @author Mustafa Besnili
 */
public class NombreDesMurs extends AiUtilityCriterionInteger<BesniliKangal>
{
	/** Nom de ce critère */
	public static final String NAME = "NombreDesMures";
	/** Valeur maximale pour ce critère */
	public static final int DESTRUCTION_LIMIT = 4;

	/**
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public NombreDesMurs( BesniliKangal ai ) throws StopRequestException
	{
		super( ai, NAME, 0, DESTRUCTION_LIMIT );
		ai.checkInterruption();
	}

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Integer processValue( AiTile tile ) throws StopRequestException
	{
		ai.checkInterruption();
		int result = 0;
		int range = ai.ownHero.getBombRange();
		AiFire fire = ai.ownHero.getBombPrototype().getFirePrototype();

		for ( Direction d : Direction.getPrimaryValues() )
		{
			ai.checkInterruption();
			AiTile neighbor = tile;
			int i = 1;
			boolean blocked = false;
			while ( i <= range && !blocked )
			{
				ai.checkInterruption();
				neighbor = neighbor.getNeighbor( d );
				blocked = !neighbor.isCrossableBy( fire );
				List<AiBlock> blocks = neighbor.getBlocks();
				if ( !blocks.isEmpty() && blocks.get( 0 ).isDestructible() )
					result++;
				i++;
			}
		}
		return ( result > DESTRUCTION_LIMIT ) ? DESTRUCTION_LIMIT : result;
	}
}
