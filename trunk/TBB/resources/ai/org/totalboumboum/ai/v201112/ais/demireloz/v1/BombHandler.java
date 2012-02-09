package org.totalboumboum.ai.v201112.ais.demireloz.v1;

import org.totalboumboum.ai.v201112.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;

/**
 * Classe gérant l'action de déposer une bombe pour l'agent. 
 * Cf. la documentation de {@link AiBombHandler} pour plus de détails.
 * 
 * 
 * @author Enis Demirel
 * @author Berke Öz
 */
@SuppressWarnings("deprecation")
public class BombHandler extends AiBombHandler<DemirelOz>
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
	
	/*private static BombHandler instance = null;
	private static DemirelOz monAI = null; 
	private static AiDataZone zone = null;
	
	*/
	
	
	protected BombHandler(DemirelOz ai) throws StopRequestException
    {	super(ai);
    	ai.checkInterruption();
 
		// on règle la sortie texte pour ce gestionnaire
		verbose = false;
		
    
	}

    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected boolean considerBombing() throws StopRequestException
	{	
		
		ai.checkInterruption();
	
	
	if(this.ai.getZone().getOwnHero().getBombNumberCurrent()<this.ai.getZone().getOwnHero().getBombNumberMax()&&this.ai.getZone().getOwnHero().getTile().getBombs().size()==0)
		{
		
		return true;
			
		}
	else 
		return false;
	}


	
	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected void updateOutput() throws StopRequestException
	{	ai.checkInterruption();
		
		//  à compléter, si vous voulez afficher quelque chose
	}
}
