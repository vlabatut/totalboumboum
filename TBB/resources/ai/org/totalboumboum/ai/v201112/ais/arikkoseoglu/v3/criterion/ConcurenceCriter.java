package org.totalboumboum.ai.v201112.ais.arikkoseoglu.v3.criterion;

import java.util.List;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.ais.arikkoseoglu.v3.ArikKoseoglu;

/**
 * Cette classe est un simple exemple de 
 * critère binaire. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin.
 * 
 * @author Furkan Arık
 * @author Aksel Köseoğlu
 */
@SuppressWarnings("deprecation")
public class ConcurenceCriter extends AiUtilityCriterionBoolean
{	/** Nom de ce critère */
	public static final String NAME = "CONCURENCE";
	
	/**
	 * Crée un nouveau critère binaire.
	 * @param ai 
	 * 		?
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public ConcurenceCriter(ArikKoseoglu ai) throws StopRequestException
	{	// init nom
		super(NAME);
		ai.checkInterruption();
		// init agent
		this.ai = ai;
	}
	
    /////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** */
	protected ArikKoseoglu ai;

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
		boolean retour = false;
		AiZone gameZone = ai.getZone();
		AiHero ownHero = gameZone.getOwnHero();
		List<AiHero> heros = gameZone.getRemainingOpponents();
		for(int i = 0 ; i< heros.size(); i++){
			ai.checkInterruption();
			if(((ownHero.getPosX() - tile.getPosX())+(ownHero.getPosY() - tile.getPosY()))>((heros.get(i).getPosX() - tile.getPosX())+(heros.get(i).getPosY() - tile.getPosY())))
				retour = true;//rakip var
		}
		return retour;
	}
}
