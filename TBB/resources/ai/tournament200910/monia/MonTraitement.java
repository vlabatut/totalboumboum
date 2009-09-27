package tournament200910.monia;

import fr.free.totalboumboum.ai.adapter200910.communication.AiAction;
import fr.free.totalboumboum.ai.adapter200910.communication.StopRequestException;


public class MonTraitement
{
	private MonIA monIa;
	
	public MonTraitement(MonIA monIa) throws StopRequestException
	{	// avant tout : test d'interruption
		monIa.checkInterruption();
		// initialisation du champ permettant d'appeler checkInterruption 
		this.monIa = monIa;	
	}
	
	public AiAction gagneRound() throws StopRequestException
	{	// avant tout : test d'interruption
		monIa.checkInterruption();
		// traitement qui fait gagner le round
		AiAction result = null;
		return result;
	}
}
