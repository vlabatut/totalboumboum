package org.totalboumboum.ai.v200910.ais.demirciduzokergok.v1;

import org.totalboumboum.ai.v200910.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200910.adapter.communication.AiAction;
import org.totalboumboum.ai.v200910.adapter.communication.AiActionName;
import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;

/**
 * >> ce texte est à remplacer par votre propre description de votre IA
 * 
 * classe principale de l'IA, qui définit son comportement.
 * n'hésitez pas à décomposer le traitement en plusieurs classes,
 * plus votre programme est modulaire et plus il sera facile à
 * débugger, modifier, relire, comprendre, etc.
 */
public class DemirciDuzokErgok extends ArtificialIntelligence
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
//		MonTraitement mt = new MonTraitement(this);
		// j'appelle la méthode qui effectue le traitement
		AiAction result = new AiAction(AiActionName.NONE)/*mt.gagneRound()*/;
		return result;
	}
}
