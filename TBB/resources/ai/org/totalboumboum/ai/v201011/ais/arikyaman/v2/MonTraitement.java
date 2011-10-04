package org.totalboumboum.ai.v201011.ais.arikyaman.v2;

import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;


/**
 * >> ce texte est à remplacer par votre propre description de votre classe
 * >> remplacez aussi le nom de l'auteur.
 * 
 * @author Vincent Labatut
 *
 */
@SuppressWarnings("deprecation")
public class MonTraitement
{	/** classe principale de l'IA, permet d'accéder à checkInterruption() */
	private ArikYaman monIa;
	
	/** une méthode bidon pour l'exemple */
	public MonTraitement(ArikYaman monIa) throws StopRequestException
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