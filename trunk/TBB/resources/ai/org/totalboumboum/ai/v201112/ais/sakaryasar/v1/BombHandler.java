package org.totalboumboum.ai.v201112.ais.sakaryasar.v1;

import java.util.HashMap;

import org.totalboumboum.ai.v201112.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201112.adapter.agent.AiMode;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;

/**
 * Classe gérant l'action de déposer une bombe pour l'agent. 
 * Cf. la documentation de {@link AiBombHandler} pour plus de détails.
 * 
 * 
 * @author Cahide Sakar
 * @author Abdurrahman Yaşar
 */
public class BombHandler extends AiBombHandler<SakarYasar>
{	
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected BombHandler(SakarYasar ai) throws StopRequestException
    {	super(ai);
    	ai.checkInterruption();
		
		// on règle la sortie texte pour ce gestionnaire
		verbose = false;
    	
    	// 
	}

    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected boolean considerBombing() throws StopRequestException
	{	ai.checkInterruption();
		boolean result = false;
		
		// 
		HashMap<AiTile,Float> hm = ai.utilityHandler.getUtilitiesByTile();
		
		if(ai.modeHandler.getMode()==AiMode.COLLECTING){
			if(hm.get(ai.getZone().getOwnHero().getTile())==6 || hm.get(ai.getZone().getOwnHero().getTile())==7)
				result =true;
		}
		else{
			if(hm.get(ai.getZone().getOwnHero().getTile())==6 || hm.get(ai.getZone().getOwnHero().getTile())==7 || hm.get(ai.getZone().getOwnHero().getTile())==8)
				result =true;
		}
		
		return result;
	}


	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected void updateOutput() throws StopRequestException
	{	
		ai.checkInterruption();

		//  à compléter, si vous voulez afficher quelque chose
	}
}
