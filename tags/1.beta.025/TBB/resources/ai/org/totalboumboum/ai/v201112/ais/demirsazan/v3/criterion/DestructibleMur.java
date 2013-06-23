package org.totalboumboum.ai.v201112.ais.demirsazan.v3.criterion;

import java.util.Set;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionInteger;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.ais.demirsazan.v3.CommonTools;
import org.totalboumboum.ai.v201112.ais.demirsazan.v3.DemirSazan;

/**
 * Cette classe est un simple exemple de 
 * critère binaire. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin.
 * 
 * @author Serdil Demir
 * @author Gokhan Sazan
 */
@SuppressWarnings("deprecation")
public class DestructibleMur extends AiUtilityCriterionInteger
{	/** Nom de ce critère */
	public static final String NAME = "DESTRUCTIBLE_MURS";
	/** */
	public static final int MUR_LIMIT = 3;
	/** common tools */
	CommonTools commonTools;
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai 
	 * 		?
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public DestructibleMur(DemirSazan ai) throws StopRequestException
	{
       
		// init nom
		super(NAME,0,MUR_LIMIT);
		ai.checkInterruption();
		// init agent
		this.ai = ai;
		
		this.commonTools = new CommonTools(this.ai);
	}
	
    /////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** */
	protected DemirSazan ai;
	
    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Integer processValue(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
		Set<AiTile> murs = commonTools.getThreatenedSoftwallTiles(tile);
		if(murs.size()> MUR_LIMIT){
			return  MUR_LIMIT;
		}
		return murs.size();
	}
}
