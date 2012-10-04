package org.totalboumboum.ai.v201112.ais.demirsazan.v3.criterion;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.ais.demirsazan.v3.CommonTools;
import org.totalboumboum.ai.v201112.ais.demirsazan.v3.DemirSazan;

/**
 * Cette classe représente est un simple exemple de 
 * critère binaire. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin.
 * 
 * @author Serdil Demir
 * @author Gokhan Sazan
 */
@SuppressWarnings("deprecation")
public class Menace extends AiUtilityCriterionBoolean
{	/** Nom de ce critère */
	public static final String NAME = "MENACE";

	//common tools
	CommonTools commonTools;
	/**
	 * Crée un nouveau critère binaire.
	 * @param ai 
	 * 		?
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Menace(DemirSazan ai) throws StopRequestException
	{
		
		// init nom
		super(NAME);
		ai.checkInterruption();
		// init agent
		this.ai = ai;
		
		this.commonTools = new CommonTools(this.ai);
		
		
	}
	
    /////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected DemirSazan ai;

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
		return commonTools.isPossibleCrossAdv(tile);
	}
}
