package tournament200809.monia;

import fr.free.totalboumboum.ai.adapter200809.AiAction;
import fr.free.totalboumboum.ai.adapter200809.AiActionName;
import fr.free.totalboumboum.ai.adapter200809.ArtificialIntelligence;
import fr.free.totalboumboum.ai.adapter200809.StopRequestException;

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
