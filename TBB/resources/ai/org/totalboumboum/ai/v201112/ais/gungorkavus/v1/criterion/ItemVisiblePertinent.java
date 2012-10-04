package org.totalboumboum.ai.v201112.ais.gungorkavus.v1.criterion;

import java.util.List;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiItem;
import org.totalboumboum.ai.v201112.adapter.data.AiItemType;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.ais.gungorkavus.v1.GungorKavus;

/**
 * Cette classe représente est un simple exemple de 
 * critère binaire. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin.
 * 
 * @author Eyüp Burak Güngör
 * @author Umit Kavus
 */
@SuppressWarnings("deprecation")
public class ItemVisiblePertinent extends AiUtilityCriterionBoolean
{	/** Nom de ce critère */
	public static final String NAME = "Pertinente";

	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai 
	 * 		?
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public ItemVisiblePertinent(GungorKavus ai) throws StopRequestException
	{	// init nom
		super(NAME);

		// init agent
		this.ai = ai;
	}

	/////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected GungorKavus ai;

	/////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	boolean result = false;



	AiHero ownHero = ai.getZone().getOwnHero();
	int ourBombeNbr = ownHero.getBombNumberMax();
	int ourFlameNbr = ownHero.getBombRange();
	List<AiItem> item = tile.getItems();

	//S'il y a des bonus dans la zone
	if(item.size()>0){
		//Quelle bonus dont on a besoin	
		if(ourBombeNbr<100){
			for(int i=0;i<item.size();i++)
			{
				ai.checkInterruption();
				if(item.get(i).getType().equals(AiItemType.EXTRA_BOMB))
					result = true;

			}
		}
		if(ourFlameNbr<10){
			for(int i=0;i<item.size();i++)
			{	
				ai.checkInterruption();
				if(item.get(i).getType().equals(AiItemType.EXTRA_FLAME))
					result = true;

			}
		}

	}
	return result;
	}
}
