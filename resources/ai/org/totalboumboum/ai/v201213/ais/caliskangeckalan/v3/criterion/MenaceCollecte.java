package org.totalboumboum.ai.v201213.ais.caliskangeckalan.v3.criterion;

import java.util.List;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiBomb;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.ais.caliskangeckalan.v3.CaliskanGeckalan;

/**
 * Cette classe représente est un simple exemple de 
 * critère binaire. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin.
 * 
 * @author Mustafa Çalışkan
 * @author Gökhan Geçkalan
 */
@SuppressWarnings("deprecation")
public class MenaceCollecte extends AiUtilityCriterionBoolean<CaliskanGeckalan>
{	/** Nom de ce critère */
	public static final String NAME = "MENACECOLLECTE";
	
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	
	/** initialize the zone	 */
	protected AiZone zone = null;
	/** initialize the ownHero	 */
	protected AiHero ownHero = null;
	/** initialize the tile of ownHero	 */
	protected AiTile currentTile = null;
	/** initialize the astar for direct path */
	
	/**
	 * @param ai 
	 * 		information manquante !?	
	 * @throws StopRequestException
	 * 		information manquante !?	
	 */
	public MenaceCollecte(CaliskanGeckalan ai) throws StopRequestException
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
		boolean result = false;
		List<AiBomb> bombs = zone.getBombs();
		for(int i=0;i<bombs.size();i++){
			ai.checkInterruption();
			if(bombs.get(i).getBlast().contains(tile)){
				result = true;
				break;
			}
		}
		return result;
	}
}
