package org.totalboumboum.ai.v201213.ais.caliskangeckalan.v2.criterion;

import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.ais.caliskangeckalan.v2.CaliskanGeckalan;

/**
 * Cette classe représente est un simple exemple de 
 * critère binaire. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin.
 * 
 * @author Mustafa Çalışkan
 * @author Gökhan Geçkalan
 */
public class PuissanceRivals extends AiUtilityCriterionBoolean<CaliskanGeckalan>
{	/** Nom de ce critère */
	public static final String NAME = "PUISSANCE";
	
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	
	/** */
	protected AiZone zone = null;
	/** */
	protected AiHero ownHero = null;
	/** */
	protected AiTile currentTile = null;
	/**
	 * @param ai 
	 * @throws StopRequestException  */
	public PuissanceRivals(CaliskanGeckalan ai) throws StopRequestException
	{	// init nom
		super(ai,NAME);
		ai.checkInterruption();
		// init agent
		zone = ai.getZone();
		ownHero = zone.getOwnHero();
		currentTile = ownHero.getTile();
	}
	
    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
		boolean result = true;
		List<AiHero> heroes = tile.getHeroes();
		int ownRange	 	= ownHero.getBombRange();
		int ownBombNumber   = ownHero.getBombNumberMax();
		double ownSpeed     = ownHero.getWalkingSpeed();
		double ownTotal     = ownSpeed*70D + ((double)ownBombNumber)*60D + ownRange*40D; 
		if(heroes.size()>0) {
			Iterator<AiHero> heroesIt = heroes.iterator();
			while(heroesIt.hasNext() && result) {
				ai.checkInterruption();
				AiHero heroesNext = heroesIt.next();
				if(!heroesNext.equals(ownHero)) {
					int otherRange 		= heroesNext.getBombRange();
					int otherBombNumber = heroesNext.getBombNumberMax();
					double otherSpeed 	= heroesNext.getWalkingSpeed();
					double otherTotal 	= otherSpeed*70D + ((double)otherBombNumber)*60D + otherRange*40D;
					if(otherTotal>ownTotal)
						result = false;
				}
			}
		}
		return result;
	}
}
