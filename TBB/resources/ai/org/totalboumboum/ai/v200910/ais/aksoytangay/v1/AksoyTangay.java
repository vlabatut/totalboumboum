package org.totalboumboum.ai.v200910.ais.aksoytangay.v1;

import org.totalboumboum.ai.v200910.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200910.adapter.communication.AiAction;
import org.totalboumboum.ai.v200910.adapter.communication.AiActionName;
import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;

/**
 * 
 * classe principale de l'IA, qui définit son comportement.
 * n'hésitez pas à décomposer le traitement en plusieurs classes,
 * plus votre programme est modulaire et plus il sera facile à
 * débugger, modifier, relire, comprendre, etc.
 * 
 * @version 1
 * 
 * @author Cihan Aksoy
 * @author Necmi Murat Tangay
 *
 */
@SuppressWarnings("deprecation")
public class AksoyTangay extends ArtificialIntelligence
{	
	
	//dimensions de matrice
	@SuppressWarnings("unused")
	private int col;
	@SuppressWarnings("unused")
	private int line;
	@SuppressWarnings("unused")
	private double time = 0;
	
	
	/** méthode appelée par le moteur du jeu pour obtenir une action de votre IA */
	public AiAction processAction() throws StopRequestException
	{	// avant tout : test d'interruption
		checkInterruption();
		
		AiZone percepts = getPercepts();
		col = percepts.getHeight();
		line = percepts.getWidth();
		
		// a faire...
		
		
		
		AiAction result = new AiAction(AiActionName.NONE);
		uneMethode();
		return result;
	}
	
	/** une méthode bidon pour l'exemple */
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
