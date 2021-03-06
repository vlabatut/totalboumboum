package org.totalboumboum.ai.v201112.ais.ilterkaya.v0;

import org.totalboumboum.ai.v201112.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe gérant le déplacement de l'agent.
 * Cf. la documentation de {@link AiMoveHandler} pour plus de détails.
 * 
 * Effacez ces commentaires et remplacez-les par votre propre Javadoc.
 * 
 * @author Mustafa Kaan İlter
 * @author Önder Kaya
 */
@SuppressWarnings("deprecation")
public class MoveHandler extends AiMoveHandler<IlterKaya>
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
	protected MoveHandler(IlterKaya ai) throws StopRequestException
    {	super(ai);
		ai.checkInterruption();
		
		// on règle la sortie texte pour ce gestionnaire
		verbose = false;
		
		// à compléter
	}

	/////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Direction considerMoving() throws StopRequestException
	{	ai.checkInterruption();
		
		// à compléter
		
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
		// à redéfinir, si vous voulez afficher d'autres informations
	}
}
