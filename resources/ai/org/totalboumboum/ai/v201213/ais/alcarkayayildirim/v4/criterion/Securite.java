package org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v4.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v4.AlcarKayaYildirim;

/**
 * 
 * @author Ekin Alçar
 * @author Ulaş Kaya
 * @author Yağmur Yıldırım
 */
@SuppressWarnings("deprecation")
public class Securite extends AiUtilityCriterionBoolean<AlcarKayaYildirim>
{	/** Nom de ce critère */
	public static final String NAME = "SECURITE";
	
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Securite(AlcarKayaYildirim ai) throws StopRequestException
	{	super(ai,NAME);
		ai.checkInterruption();
	}
	
    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
	
		if ( this.ai.getCurrentDangerousTiles().contains(tile)){
			return false;
		}
	
		return true;
	}
	

	
}
