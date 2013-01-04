package org.totalboumboum.ai.v201213.ais.oralozugur.v4.criterion;



import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.oralozugur.v4.OralOzugur;

/**
 * Il retourne vrai pour les cases qui sont à côte des mur destructibles et l’autre face de
mur est un tile libre.
 * 
 * @author Buğra Oral
 * @author Ceyhun Özuğur
 */
public class BestWall extends AiUtilityCriterionBoolean<OralOzugur>
{	/** Nom de ce critère */
	public static final String NAME = "BestWall";
	
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public BestWall(OralOzugur ai) throws StopRequestException
	{	super(ai,NAME);
		ai.checkInterruption();
	}
	
    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
	if(this.ai.bestWallTile!=null){
		if (ai.bestWallTile.equals(tile)) {
			return true;
		}
	}
	return false;	
	}
		
	
	
}
