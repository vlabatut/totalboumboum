package org.totalboumboum.ai.v201112.ais.caliskangeckalanseven.v3.criterion;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.ais.caliskangeckalanseven.v3.CaliskanGeckalanSeven;

/**
 * Cette classe est un simple exemple de 
 * critère binaire. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin.
 * 
 * @author Mustafa Çalışkan
 * @author Gökhan Geçkalan
 * @author Cihan Seven
 */
@SuppressWarnings("deprecation")
public class PertinentItems extends AiUtilityCriterionBoolean
{	/** Nom de ce critère */
	public static final String NAME = "PERTINENTITEMS";
	
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai 
	 * 		?
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public PertinentItems(CaliskanGeckalanSeven ai) throws StopRequestException
	{	// init nom
		super(NAME);
		ai.checkInterruption();
		// init agent
		this.ai = ai;
		zone = ai.getZone();
		ownHero = zone.getOwnHero();
	}
	
    /////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** */
	protected CaliskanGeckalanSeven ai;

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
		boolean result = false;
		if(!tile.getItems().isEmpty())
			result = true;
		return result;
	}
	
	/** */
	protected AiZone zone = null;
	/** Le personnage contrôlé par l'agent */
	protected AiHero ownHero = null;
	/** La case courante */
	protected AiTile currentTile = null;
	
}
