package tournament200910.findiksirin.v1_2;

import org.totalboumboum.ai.adapter200910.ArtificialIntelligence;
import org.totalboumboum.ai.adapter200910.communication.AiAction;
import org.totalboumboum.ai.adapter200910.communication.AiActionName;
import org.totalboumboum.ai.adapter200910.communication.StopRequestException;
import org.totalboumboum.ai.adapter200910.data.AiHero;
import org.totalboumboum.ai.adapter200910.data.AiTile;
import org.totalboumboum.ai.adapter200910.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * >> ce texte est � remplacer par votre propre description de votre IA
 * 
 * classe principale de l'IA, qui d�finit son comportement.
 * n'h�sitez pas � d�composer le traitement en plusieurs classes,
 * plus votre programme est modulaire et plus il sera facile �
 * d�bugger, modifier, relire, comprendre, etc.
 */
public class FindikSirin extends ArtificialIntelligence
{	
	/** m�thode appel�e par le moteur du jeu pour obtenir une action de votre IA */
	public AiAction processAction() throws StopRequestException
	{	// avant tout : test d'interruption
		checkInterruption();
		if(ownHero==null){
			initialise();
		}
		AiAction result = new AiAction(AiActionName.NONE);
		Direction moveDir = Direction.NONE;
		
		if(!ownHero.hasEnded()){
			updateLocation();
			safetyManager.update();
			moveDir = safetyManager.safestNeighbour(currentTile);
		}
		result= new AiAction(AiActionName.MOVE,moveDir);
		return result;
	}
		
	/////own hero et la zone du jeu
	private AiHero ownHero = null;
	private AiZone zone = null;

	////methodes d'acces
	public AiHero getOurHero() throws StopRequestException{
		checkInterruption();
		return ownHero;
	}
	public AiZone getZone() throws StopRequestException{
		checkInterruption();
		return zone;
	}
	
	///initialisation
	private void initialise() throws StopRequestException{
	checkInterruption();
	zone = getPercepts();
	ownHero = zone.getOwnHero();
	updateLocation();
	safetyManager = new SafetyManager(this);
	}
	
	///Safety Manager
	private SafetyManager safetyManager = null;
	
	public SafetyManager getSafetyManager() throws StopRequestException{
		checkInterruption();
		return safetyManager;
	}
	
	// tile a ou on se situe et la position
	private AiTile currentTile = null;
	/*
	private double currentX;
	private double currentY;
	private double currentZ;
	*/
	
	public AiTile getCurrentTile() throws StopRequestException{
		checkInterruption();
		return currentTile;
	}
	private void updateLocation () throws StopRequestException{
		checkInterruption();
		currentTile=ownHero.getTile();
		/*
		currentX = ownHero.getPosX();
		currentY = ownHero.getPosY();
		currentZ = ownHero.getPosZ();
	 */
	}
}
