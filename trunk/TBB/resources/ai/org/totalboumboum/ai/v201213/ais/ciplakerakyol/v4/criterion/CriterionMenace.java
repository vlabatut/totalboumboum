package org.totalboumboum.ai.v201213.ais.ciplakerakyol.v4.criterion;

import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiBomb;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.ciplakerakyol.v4.CiplakErakyol;

/**
 *Cette critere est pour decide est-qu'il y a une menace par une bombe ou pas.
 *s'il y a menace il va retourner true, sinon il va retourner false.
 * 
 * @author Hazal Çıplak
 * @author Şebnem Erakyol
 */
public class CriterionMenace extends AiUtilityCriterionBoolean<CiplakErakyol>
{	/** Nom de ce critère */
	public static final String NAME = "Securite";
	
	/**
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public CriterionMenace(CiplakErakyol ai) throws StopRequestException
	{	super(ai,NAME);
		ai.checkInterruption();
	}
	
    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	
		ai.checkInterruption();
		boolean result = true;
		List<AiBomb> bombs = this.ai.getZone().getBombs();
		Iterator<AiBomb> itBombs = bombs.iterator();
		AiBomb temp;
		
		while(itBombs.hasNext() && result)
		{
			ai.checkInterruption();
			temp = itBombs.next();
			
			if(temp.getCol() == tile.getCol() && Math.abs(temp.getCol() - tile.getCol()) < temp.getRange()+1)
				result = false;
			if(temp.getRow() == tile.getRow() && Math.abs(temp.getRow() - tile.getRow()) < temp.getRange()+1)
				result = false;
		}
		
		if(!tile.getFires().isEmpty())
			result = false;
		
		return result;
	}
}
