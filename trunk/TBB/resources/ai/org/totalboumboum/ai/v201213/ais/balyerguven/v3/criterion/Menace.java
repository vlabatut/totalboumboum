package org.totalboumboum.ai.v201213.ais.balyerguven.v3.criterion;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.ais.balyerguven.v3.BalyerGuven;

/**
 * our menace class.
 * 
 * @author Leman Sebla Balyer
 * @author Ecem Güven
 */
public class Menace extends AiUtilityCriterionBoolean<BalyerGuven>
{	/** danger */
	public static final String NAME = "Menace";
	

	
	/**
	 * Crée un nouveau critère boolean.
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
		
		List<AiHero> heroes = new ArrayList<AiHero>(gameZone.getHeroes());
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
