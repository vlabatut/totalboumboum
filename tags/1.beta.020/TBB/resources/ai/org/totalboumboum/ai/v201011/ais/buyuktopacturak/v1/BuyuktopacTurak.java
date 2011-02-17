package org.totalboumboum.ai.v201011.ais.buyuktopacturak.v1;

import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;

public class BuyuktopacTurak extends ArtificialIntelligence
{	
	/** m�thode appel�e par le moteur du jeu pour obtenir une action de votre IA */
	public AiAction processAction() throws StopRequestException
	{	// avant tout : test d'interruption
		checkInterruption();
		
		AiAction result = new AiAction(AiActionName.NONE);
		uneMethode();
		return result;
	}
	
	/** une m�thode bidon pour l'exemple */
	private AiAction uneMethode() throws StopRequestException
	{	// avant tout : test d'interruption
		checkInterruption();
		
		// je cr�e un objet impl�mentant mon traitement
		CollecteMatrix mt = new CollecteMatrix(this);
		//mt.matrice();
		// j'appelle la m�thode qui effectue le traitement
		AiAction result = mt.gagneRound();
		return result;
	}
}
