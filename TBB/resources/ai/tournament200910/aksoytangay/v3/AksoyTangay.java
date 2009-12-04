package tournament200910.aksoytangay.v3;

import com.sun.org.apache.bcel.internal.generic.RETURN;

import fr.free.totalboumboum.ai.adapter200910.data.AiHero;
import fr.free.totalboumboum.ai.adapter200910.data.AiTile;
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
	
	private Zone zone;
	
	private AiHero ownHero;
	
	private AiAction result;
	
	private AiZone percepts;
	
	
	/** méthode appelée par le moteur du jeu pour obtenir une action d'IA */
	public AiAction processAction() throws StopRequestException
	{	// avant tout : test d'interruption
		checkInterruption();
		
		percepts = getPercepts();
		ownHero = percepts.getOwnHero();
		result = new AiAction(AiActionName.NONE);
		zone = new Zone(percepts, this);
		
		
		// a faire...
			
				
		return result;
	}
	
		
	/** methode qui teste si le case est en danger ou pas
	 * 
	 *  @param tile : AiTile
	 *  
	 *  @return la situation de danger de case
	 *  
	 *  @throws StopRequestException
	 */
	private boolean isDangerous(AiTile tile) throws StopRequestException
	{	// avant tout : test d'interruption
		checkInterruption();
		if(tile==null)
			return false;
		State state = zone.getMatrix()[tile.getCol()][tile.getLine()];
		return (state==State.FLAMME || state==State.DANGER || state==State.BOMBE);
		
	}
}
