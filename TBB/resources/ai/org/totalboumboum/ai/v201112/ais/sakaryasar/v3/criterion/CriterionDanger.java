package org.totalboumboum.ai.v201112.ais.sakaryasar.v3.criterion;

import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiBomb;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.ais.sakaryasar.v3.SakarYasar;

/**
 * @author Cahide Sakar
 * @author Abdurrahman Yaşar
 */
@SuppressWarnings("deprecation")
public class CriterionDanger extends AiUtilityCriterionBoolean{
	/** */
	public static final String NAME = "DANGER";
	
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai 
	 * 		?
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public CriterionDanger(SakarYasar ai) throws StopRequestException
	{	// init nom
		super(NAME);
		ai.checkInterruption();
		// init agent
		this.ai = ai;
	}
	
    /////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected SakarYasar ai;

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
		boolean result = false;

		List<AiBomb> bombs = this.ai.getZone().getBombs();
		Iterator<AiBomb> itBombs = bombs.iterator();
		
		AiBomb temp;
		while(itBombs.hasNext() && !result){
			ai.checkInterruption();
			temp = itBombs.next();
			if(temp.getCol() == tile.getCol() && Math.abs(temp.getCol() - tile.getCol()) < temp.getRange()+1)
				result = true;
			if(temp.getRow() == tile.getRow() && Math.abs(temp.getRow() - tile.getRow()) < temp.getRange()+1)
				result = true;
		}
		if(!tile.getFires().isEmpty())
			result = true;
		return result;
	}
}
