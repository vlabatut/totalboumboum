package org.totalboumboum.ai.v201213.ais.ciplakerakyol.v1;

import java.util.List;

import org.totalboumboum.ai.v201213.adapter.data.AiBomb;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.path.AiPath;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe gérant le déplacement de l'agent.
 * Cf. la documentation de {@link AiMoveHandler} pour plus de détails.
 * 
 * TODO Effacez ces commentaires et remplacez-les par votre propre Javadoc.
 * 
 * @author Hazal Çıplak
 * @author Şebnem Erakyol
 */
public class MoveHandler extends AiMoveHandler<CiplakErakyol>
{	/** */
	AiTile hedef = null;
	/** */
	Boolean isBombing = false;
	/** */
	int wallBombing = 0;
	/** */
	boolean canRun = true;
	
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected MoveHandler(CiplakErakyol ai) throws StopRequestException
    {	super(ai);
		ai.checkInterruption();
		
		// on règle la sortie texte pour ce gestionnaire
		verbose = false;

	}

	/////////////////////////////////////////////////////////////////
	// DESTINATION				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected AiTile updateCurrentDestination() throws StopRequestException
	{	ai.checkInterruption();
		
		// TODO à compléter
		
		return null;
	}

	/////////////////////////////////////////////////////////////////
	// PATH						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected AiPath updateCurrentPath() throws StopRequestException
	{	ai.checkInterruption();
		
		// TODO à compléter
		
		return null;
	}

	/////////////////////////////////////////////////////////////////
	// DIRECTION				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Direction updateCurrentDirection() throws StopRequestException
	{	ai.checkInterruption();
	
	
	
	
	
	if(hedef!=null)
	{
		
		if(!tehlike(hedef))
		{
			//left return Direction.DOWN;
		}
		
		
	}
	return Direction.NONE;
	//return Direction.LEFT;
	//return Direction.RIGHT;
	//return Direction.DOWN;
	
	}
	
	/**
	 * @param tile 
	 * @return ?
	 * @throws StopRequestException  */
	public boolean tehlike(AiTile tile) throws StopRequestException {
		ai.checkInterruption();
		AiZone zone = ai.getZone();
		// print("isDangerous");
		List<AiBomb> bombalar= zone.getBombs();
		for (AiBomb bomba : bombalar) {
			ai.checkInterruption();
			if (bomba.getBlast().contains(tile))
				return true;
		}
		return false;

	}
	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void updateOutput() throws StopRequestException
	{	ai.checkInterruption();
		
		// ici on se contente de faire le traitement par défaut
		super.updateOutput();
		// TODO à redéfinir, si vous voulez afficher d'autres informations
	}
}
