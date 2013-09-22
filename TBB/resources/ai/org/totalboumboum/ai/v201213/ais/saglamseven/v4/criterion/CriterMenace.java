package org.totalboumboum.ai.v201213.ais.saglamseven.v4.criterion;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.ais.saglamseven.v4.SaglamSeven;

/**
 * Cette classe est un simple exemple de 
 * critère binaire. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin.
 * 
 * @author Esra Sağlam
 * @author Cihan Adil Seven
 */
@SuppressWarnings("deprecation")
public class CriterMenace extends AiUtilityCriterionBoolean<SaglamSeven>
{	/** Nom de ce critère */
	public static final String NAME = "MENACE";
	
	
	
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public CriterMenace(SaglamSeven ai) throws StopRequestException
	{	super(ai, NAME);
		ai.checkInterruption();
	}

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
		AiZone gameZone = ai.getZone();
		AiHero ownHero = gameZone.getOwnHero();
		int range = ownHero.getBombRange()
		, posX = tile.getCol()
		, posY = tile.getRow();
		List<AiHero> heros = new ArrayList<AiHero>(gameZone.getHeroes());
		heros.remove(ownHero);
		if(heros!=null){
			for(int i = 0 ; i< heros.size(); i++){
				ai.checkInterruption();
				if((heros.get(i).getCol()>= posX-range || heros.get(i).getCol()<= posX+range)&& heros.get(i).getRow()== posY &&  tile.isCrossableBy(heros.get(i)) ){
					return true;
				}
				else if((heros.get(i).getRow()>= posY-range || heros.get(i).getRow()<= posY+range) && heros.get(i).getCol()== posX  &&  tile.isCrossableBy(heros.get(i))){
					return true;
				}
			}
		}
		return false;
	}
}
