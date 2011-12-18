package org.totalboumboum.ai.v200910.ais.aksoytangay.v1;

import org.totalboumboum.ai.v200910.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200910.adapter.communication.AiAction;
import org.totalboumboum.ai.v200910.adapter.communication.AiActionName;
import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;

/**
 * 
 * classe principale de l'IA, qui d�finit son comportement.
 * n'h�sitez pas � d�composer le traitement en plusieurs classes,
 * plus votre programme est modulaire et plus il sera facile �
 * d�bugger, modifier, relire, comprendre, etc.
 * 
 * @version 1
 * 
 * @author Cihan Aksoy
 * @author Necmi Murat Tangay
 *
 */
public class AksoyTangay extends ArtificialIntelligence
{	
	
	//dimensions de matrice
	@SuppressWarnings("unused")
	private int col;
	@SuppressWarnings("unused")
	private int line;
	@SuppressWarnings("unused")
	private double time = 0;
	
	
	/** m�thode appel�e par le moteur du jeu pour obtenir une action de votre IA */
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
	
	/** une m�thode bidon pour l'exemple */
	private AiAction uneMethode() throws StopRequestException
	{	// avant tout : test d'interruption
		checkInterruption();
		
		// je cr�e un objet impl�mentant mon traitement
		MonTraitement mt = new MonTraitement(this);
		// j'appelle la m�thode qui effectue le traitement
		AiAction result = mt.gagneRound();
		return result;
	}
}