package org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v1.criterion;

import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiBomb;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v1.AlcarKayaYildirim;

/**
 * 
 * @author Ekin Alçar
 * @author Ulaş Kaya
 * @author Yağmur Yıldırım
 */
public class Menace extends AiUtilityCriterionBoolean<AlcarKayaYildirim>
{	/** Nom de ce critère */
	public static final String NAME = "MENACE";
	
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Menace(AlcarKayaYildirim ai) throws StopRequestException
	{	super(ai,NAME);
		ai.checkInterruption();
	}
	
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
