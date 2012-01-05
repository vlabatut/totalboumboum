package org.totalboumboum.ai.v201112.ais.arikkoseoglu.v3.criterion;

import java.util.List;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.ais.arikkoseoglu.v3.ArikKoseoglu;

/**
 * Cette classe représente est un simple exemple de 
 * critère binaire. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin.
 * 
 * @author Furkan Arık
 * @author Aksel Köseoğlu
 */
public class MenaceCriter extends AiUtilityCriterionBoolean
{	/** Nom de ce critère */
	public static final String NAME = "MENACE";
	
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public MenaceCriter(ArikKoseoglu ai) throws StopRequestException
	{	// init nom
		super(NAME);
		ai.checkInterruption();
		// init agent
		this.ai = ai;
	}
	
    /////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected ArikKoseoglu ai;

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
		List<AiHero> heros = gameZone.getHeroes();
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
