package tournament200910.aksoytangay.v1;

import fr.free.totalboumboum.ai.adapter200910.data.AiZone;
import fr.free.totalboumboum.ai.adapter200910.ArtificialIntelligence;
import fr.free.totalboumboum.ai.adapter200910.communication.AiAction;
import fr.free.totalboumboum.ai.adapter200910.communication.AiActionName;
import fr.free.totalboumboum.ai.adapter200910.communication.StopRequestException;

/**
 * >> ce texte est � remplacer par votre propre description de votre IA
 * 
 * classe principale de l'IA, qui d�finit son comportement.
 * n'h�sitez pas � d�composer le traitement en plusieurs classes,
 * plus votre programme est modulaire et plus il sera facile �
 * d�bugger, modifier, relire, comprendre, etc.
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
		col = percepts.getHeigh();
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
