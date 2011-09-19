package org.totalboumboum.ai.v201011.ais.buyuktopacturak.v1;

import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;

public class BuyuktopacTurak extends ArtificialIntelligence
{	
	/** méthode appelée par le moteur du jeu pour obtenir une action de votre IA */
	public AiAction processAction() throws StopRequestException
	{	// avant tout : test d'interruption
		checkInterruption();
		
		AiAction result = new AiAction(AiActionName.NONE);
		uneMethode();
		return result;
	}
	
	/** une méthode bidon pour l'exemple */
	private AiAction uneMethode() throws StopRequestException
	{	// avant tout : test d'interruption
		checkInterruption();
		
		// je crée un objet implémentant mon traitement
		CollecteMatrix mt = new CollecteMatrix(this);
		//mt.matrice();
		// j'appelle la méthode qui effectue le traitement
		AiAction result = mt.gagneRound();
		return result;
	}
}
