package org.totalboumboum.ai.v201213.ais.balyerguven.v1.criterion;

import java.util.List;

import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.ais.balyerguven.v1.BalyerGuven;

/**
 * Cette classe est un simple exemple de 
 * critère binaire. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin.
 * 
 * @author Leman Sebla Balyer
 * @author Ecem Güven
 */
public class Menace extends AiUtilityCriterionBoolean<BalyerGuven>
{	/** Nom de ce critère */
	public static final String NAME = "Menace";
	

	
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Menace(BalyerGuven ai) throws StopRequestException
	{	super(ai,NAME);
		ai.checkInterruption();
		this.ai = ai;
	}
	
    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	
		ai.checkInterruption();
		AiZone gameZone = ai.getZone();
		AiHero myHero = gameZone.getOwnHero();
		int range = myHero.getBombRange();
		int posX = tile.getCol();
		int posY = tile.getRow();
		List<AiHero> heroes = gameZone.getHeroes();
		heroes.remove(myHero);
		if(heroes!=null){
			for(int i = 0 ; i< heroes.size(); i++){
				ai.checkInterruption();
				if((heroes.get(i).getCol()>= posX-range || heroes.get(i).getCol()<= posX+range)&& heroes.get(i).getRow()== posY &&  tile.isCrossableBy(heroes.get(i)) ){
					return true;
				}
				else if((heroes.get(i).getRow()>= posY-range || heroes.get(i).getRow()<= posY+range) && heroes.get(i).getCol()== posX  &&  tile.isCrossableBy(heroes.get(i))){
					return true;
				}
			}
		}
		return false;
	
		
	}
}
