package org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v1.criterion;

import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v1.AlcarKayaYildirim;

/**
 * The criteria that will evaluate the tile for attack suitability.
 * 
 * @author Ekin Alçar
 * @author Ulaş Kaya
 * @author Yağmur Yıldırım
 */
public class AttaquePertinence extends AiUtilityCriterionBoolean<AlcarKayaYildirim>
{	/** Nom de ce critère */
	public static final String NAME = "ATTAQUEPERTINENCE";
	
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public AttaquePertinence(AlcarKayaYildirim ai) throws StopRequestException
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
	
		List<AiHero> heroes = ai.getZone().getHeroes();
		Iterator<AiHero> itHeroes = heroes.iterator();
		AiHero temp;
		AiHero ownHero = ai.getZone().getOwnHero();
		
		while(itHeroes.hasNext() && !result){
			ai.checkInterruption();
			temp = itHeroes.next();
			if(temp != ownHero && (temp.getTile().getCol() == tile.getCol() || temp.getTile().getRow() == tile.getRow()))
				result = true;
		}
	
		return result;
	}
}
