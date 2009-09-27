package tournament200910.monia;

import fr.free.totalboumboum.ai.adapter200910.ArtificialIntelligence;
import fr.free.totalboumboum.ai.adapter200910.communication.AiAction;
import fr.free.totalboumboum.ai.adapter200910.communication.AiActionName;
import fr.free.totalboumboum.ai.adapter200910.communication.StopRequestException;


public class MonIA extends ArtificialIntelligence
{
	public AiAction processAction() throws StopRequestException
	{	// avant tout : test d'interruption
		checkInterruption();
		AiAction result = new AiAction(AiActionName.NONE);
		uneMethode();
		return result;
	}
	
	private AiAction uneMethode() throws StopRequestException
	{	// avant tout : test d'interruption
		checkInterruption();
		// je crée un objet implémentant mon traitement
		MonTraitement mt = new MonTraitement(this);
		// j'appelle la méthode qui effectue le traitement
		AiAction result = mt.gagneRound();
		return result;
	}
}
