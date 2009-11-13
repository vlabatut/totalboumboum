package tournament200910.danesatir;

import fr.free.totalboumboum.ai.adapter200910.communication.AiAction;
import fr.free.totalboumboum.ai.adapter200910.communication.StopRequestException;


/**
 * >> ce texte est à remplacer par votre propre description de votre classe
 */
public class MonTraitement
{	/** classe principale de l'IA, permet d'accéder à checkInterruption() */
	private MonIA monIa;
	
	/** une méthode bidon pour l'exemple */
	public MonTraitement(MonIA monIa) throws StopRequestException
	{	// avant tout : test d'interruption
		monIa.checkInterruption();
		
		// initialisation du champ permettant d'appeler checkInterruption 
		this.monIa = monIa;	
	}
	
	/** une autre méthode bidon */
	public AiAction gagneRound() throws StopRequestException
	{	// avant tout : test d'interruption
		monIa.checkInterruption();
		
		// traitement qui fait gagner le round
		AiAction result = null;
		return result;
	}
}
