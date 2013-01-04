package org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v3.criterion;


import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v3.AlcarKayaYildirim;

/**
 * The criteria that will evaluate the tile if it's the best tile to advance
 * through a non-accessible enemy in attack mode.
 * 
 * @author Ekin Alçar
 * @author Ulaş Kaya
 * @author Yağmur Yıldırım
 */
public class AttaqueBestPertinence extends AiUtilityCriterionBoolean<AlcarKayaYildirim>
{	/** Nom de ce critère */
	public static final String NAME = "ATTAQUE_BEST_PERTINENCE";

	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public AttaqueBestPertinence(AlcarKayaYildirim ai) throws StopRequestException
	{	super(ai,NAME);
		ai.checkInterruption();
		this.ai1 = ai;
	}
	
	
	/** */
	protected AlcarKayaYildirim ai1;
    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	
		ai1.checkInterruption();
		boolean result = false;

		if ( tile.equals( this.ai.getClosestAttPertinentTile() ) )
		{
			result = true;
		}
	
		return result;
	}
	

	
}
