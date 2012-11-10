package org.totalboumboum.ai.v201213.ais.caliskangeckalan.v0;

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
 * @author Mustafa Çalışkan
 * @author Gökhan Geçkalan
 */
public class MoveHandler extends AiMoveHandler<CaliskanGeckalan>
{	
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected MoveHandler(CaliskanGeckalan ai) throws StopRequestException
    {	super(ai);
		ai.checkInterruption();
		
		// on règle la sortie texte pour ce gestionnaire
		verbose = true;
		
		// TODO à compléter
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
		
		// TODO à compléter
		
		return Direction.NONE;
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
