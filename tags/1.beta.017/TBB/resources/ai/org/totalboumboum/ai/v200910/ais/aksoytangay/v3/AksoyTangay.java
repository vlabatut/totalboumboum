package org.totalboumboum.ai.v200910.ais.aksoytangay.v3;

import org.totalboumboum.ai.v200910.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200910.adapter.communication.AiAction;
import org.totalboumboum.ai.v200910.adapter.communication.AiActionName;
import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;

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
	
	private Zone zone;
	
	@SuppressWarnings("unused")
	private AiHero ownHero;
	
	private AiAction result;
	
	private AiZone percepts;
	
	
	/** m�thode appel�e par le moteur du jeu pour obtenir une action d'IA */
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
	@SuppressWarnings("unused")
	private boolean isDangerous(AiTile tile) throws StopRequestException
	{	// avant tout : test d'interruption
		checkInterruption();
		if(tile==null)
			return false;
		State state = zone.getMatrix()[tile.getCol()][tile.getLine()];
		return (state==State.FLAMME || state==State.DANGER || state==State.BOMBE);
		
	}
}