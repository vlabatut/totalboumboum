package org.totalboumboum.ai.v201112.ais.demireloz.v1.criterion;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiItemType;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.ais.demireloz.v1.DemirelOz;

/**
 * Cette classe représente est un simple exemple de 
 * critère binaire. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin.
 * 
 * @author Enis Demirel
 * @author Berke Öz
 */
@SuppressWarnings("deprecation")
public class Convenience extends AiUtilityCriterionBoolean
{	/** Nom de ce critère */
	public static final String NAME = "Convenience";
	
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Convenience(DemirelOz ai) throws StopRequestException
	{	// init nom
		super(NAME);
		
		// init agent
		this.ai = ai;
	}
	
    /////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected DemirelOz ai;

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@SuppressWarnings("unused")
	@Override
	
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	
	
	boolean result = false;
	Convenience convenience = new Convenience(this.ai);
	
	if(this.ai.getZone().getOwnHero().getBombNumberMax() <2)
	{
	if(tile.getItems().contains(AiItemType.EXTRA_BOMB))
	{
		result= true;
	}
	
	}
	
	
	if(this.ai.getZone().getOwnHero().getBombRange() <3)
	{
	if(tile.getItems().contains(AiItemType.EXTRA_FLAME))
	{
		result= true;
	}
	}
	return result;
	
}
}
