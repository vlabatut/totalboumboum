package org.totalboumboum.ai.v200910.ais._monia;

import org.totalboumboum.ai.v200910.adapter.communication.AiAction;
import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;


/**
 * >> ce texte est � remplacer par votre propre description de votre classe
 */
public class MonTraitement
{	/** classe principale de l'IA, permet d'acc�der � checkInterruption() */
	private MonIA monIa;
	
	/** une m�thode bidon pour l'exemple */
	public MonTraitement(MonIA monIa) throws StopRequestException
	{	// avant tout : test d'interruption
		monIa.checkInterruption();
		
		// initialisation du champ permettant d'appeler checkInterruption 
		this.monIa = monIa;	
	}
	
	/** une autre m�thode bidon */
	public AiAction gagneRound() throws StopRequestException
	{	// avant tout : test d'interruption
		monIa.checkInterruption();
		
		// traitement qui fait gagner le round
		AiAction result = null;
		return result;
	}
}
