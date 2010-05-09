package tournament200910.aksoytangay.v1;

import fr.free.totalboumboum.ai.adapter200910.data.AiZone;
import fr.free.totalboumboum.ai.adapter200910.ArtificialIntelligence;
import fr.free.totalboumboum.ai.adapter200910.communication.AiAction;
import fr.free.totalboumboum.ai.adapter200910.communication.AiActionName;
import fr.free.totalboumboum.ai.adapter200910.communication.StopRequestException;

/**
 * >> ce texte est à remplacer par votre propre description de votre IA
 * 
 * classe principale de l'IA, qui définit son comportement.
 * n'hésitez pas à décomposer le traitement en plusieurs classes,
 * plus votre programme est modulaire et plus il sera facile à
 * débugger, modifier, relire, comprendre, etc.
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
	
	
	/** méthode appelée par le moteur du jeu pour obtenir une action de votre IA */
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
