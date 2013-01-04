package org.totalboumboum.ai.v201213.ais.besnilikangal.v1.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionInteger;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.besnilikangal.v1.BesniliKangal;

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

	/**
	 * 
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public NombreDesMurs( BesniliKangal ai ) throws StopRequestException
	{
		super( ai, NAME, 0, 3 );
		ai.checkInterruption();
	}

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
    public Integer processValue( AiTile tile ) throws StopRequestException
    {
		ai.checkInterruption();
		return ai.getTileOperation().getAccessibleDestructibleTiles().get( tile );
    }
}
