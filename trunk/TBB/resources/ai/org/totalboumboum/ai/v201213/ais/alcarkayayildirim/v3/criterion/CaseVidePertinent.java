package org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v3.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v3.AlcarKayaYildirim;

/**
 * 
 * @author Ekin Alçar
 * @author Ulaş Kaya
 * @author Yağmur Yıldırım
 */
public class CaseVidePertinent extends AiUtilityCriterionBoolean<AlcarKayaYildirim>
{	/** Nom de ce critère */
	public static final String NAME = "CASE_VİDE_PERTİNENCE";

	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 * 		?	
	 * @throws StopRequestException	
	 * Au cas où le moteur demande la terminaison de l'agent.
	 */
	public CaseVidePertinent(AlcarKayaYildirim ai) throws StopRequestException
	{	// init nom
		super(ai,NAME);
		ai.checkInterruption();
		// init agent
		this.ai1 = ai;
	}

	

	/////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** */
	protected AlcarKayaYildirim ai1;

	/////////////////////////////////////////////////////////////////
	// PROCESS	 /////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	
	ai1.checkInterruption();
	boolean result = false;
	
	AiZone zone = ai1.getZone();
	AiHero ownHero = zone.getOwnHero();

	if(tile.getBlocks().isEmpty() && tile.getBombs().isEmpty() && tile.getFires().isEmpty() && (tile.getHeroes().isEmpty() || tile.getHeroes().contains(ownHero)))
		result = true;

	return result;
	}
}