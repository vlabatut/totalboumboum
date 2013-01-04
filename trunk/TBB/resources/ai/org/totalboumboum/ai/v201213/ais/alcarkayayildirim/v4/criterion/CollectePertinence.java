package org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v4.criterion;

import java.util.List;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiItemType;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v4.AlcarKayaYildirim;

/**
 * @author Ekin Alçar
 * @author Ulaş Kaya
 * @author Yağmur Yıldırım
 */
public class CollectePertinence extends AiUtilityCriterionBoolean<AlcarKayaYildirim>
{	/** Nom de ce critère */
	public static final String NAME = "COLLECTE_PERTINENCE";
	
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public CollectePertinence(AlcarKayaYildirim ai) throws StopRequestException
	{	super(ai,NAME);
		ai.checkInterruption();
		this.ai1 = ai;
	}
	
	/** */
	protected AlcarKayaYildirim ai1;
    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	
		ai1.checkInterruption();
		boolean result = false;
	
		AiHero ownHero = ai1.getZone().getOwnHero();
		int ourBmbNbr = ownHero.getBombNumberMax();
		int ourFlmNbr = ownHero.getBombRange();
		List<org.totalboumboum.ai.v201213.adapter.data.AiItem> item = tile.getItems();
		
		//S'il y a des bonus dans la zone
		if(!(item.isEmpty())){
			//Quelle bonus dont on a besoin
			if(ourBmbNbr < 2){
				for(int i=0;i<item.size();i++){
					ai1.checkInterruption();
					if(item.get(i).getType().equals(AiItemType.EXTRA_BOMB))
						result = true;
				}
			}
			
			if(ourFlmNbr<2){
				for(int i=0;i<item.size();i++){
					ai1.checkInterruption();
					if(item.get(i).getType().equals(AiItemType.EXTRA_FLAME))
						result = true;
				}
			}			
		}
		
		return result;
	}
}
