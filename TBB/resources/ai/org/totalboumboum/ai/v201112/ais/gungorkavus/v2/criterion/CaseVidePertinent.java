package org.totalboumboum.ai.v201112.ais.gungorkavus.v2.criterion;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.ais.gungorkavus.v2.GungorKavus;

/**
 * Cette classe représente est un simple exemple de 
 * critère binaire. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin.
 * 
 * @author Eyüp Burak Güngör
 * @author Umit Kavus
 */
@SuppressWarnings("deprecation")
public class CaseVidePertinent extends AiUtilityCriterionBoolean
{	/** Nom de ce critère */
	public static final String NAME = "Pertinent";

	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @throws StopRequestException	
	 * Au cas où le moteur demande la terminaison de l'agent.
	 */
	public CaseVidePertinent(GungorKavus ai) throws StopRequestException
	{	// init nom
		super(NAME);
		ai.checkInterruption();
		// init agent
		this.ai = ai;
	}

	

	/////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected GungorKavus ai;

	/////////////////////////////////////////////////////////////////
	// PROCESS	 /////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	
	ai.checkInterruption();
	boolean result = false;
	
	AiZone zone = ai.getZone();
	AiHero ownHero = zone.getOwnHero();

	if(tile.getBlocks().size()==0 && tile.getBombs().size()==0 && tile.getFires().size()==0 && (tile.getHeroes().size()==0 || tile.getHeroes().contains(ownHero)))
		result = true;
	
	

	return result;
	}
}