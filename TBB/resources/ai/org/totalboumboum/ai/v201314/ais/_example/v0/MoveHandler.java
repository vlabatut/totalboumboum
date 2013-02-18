package org.totalboumboum.ai.v201314.ais._example.v0;

import org.totalboumboum.ai.v201314.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201314.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.path.AiPath;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe gérant le déplacement de l'agent.
 * Cf. la documentation de {@link AiMoveHandler} pour plus de détails.
 * 
 * TODO Effacez ces commentaires et remplacez-les par votre propre Javadoc.
 * 
 * @author Xxxxxx
 * @author Yyyyyy
 */
public class MoveHandler extends AiMoveHandler<Example>
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
	protected MoveHandler(Example ai) throws StopRequestException
    {	super(ai);
		ai.checkInterruption();
		
		// on règle la sortie texte pour ce gestionnaire
		verbose = true;
		
		/*
		 *  TODO à compléter.
		 *  
		 *  Remarque : ce commentaire est à effacer, comme tous les autres marqueurs TODO
		 */
	}

	/////////////////////////////////////////////////////////////////
	// DESTINATION				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected AiTile processCurrentDestination() throws StopRequestException
	{	ai.checkInterruption();
		
		/*
		 *  TODO à compléter.
		 *  
		 *  Remarque : ce commentaire est à effacer, comme tous les autres marqueurs TODO
		 */
		
		return null;
	}

	/////////////////////////////////////////////////////////////////
	// PATH						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected AiPath processCurrentPath() throws StopRequestException
	{	ai.checkInterruption();
		
		/*
		 *  TODO à compléter.
		 *  
		 *  Remarque : ce commentaire est à effacer, comme tous les autres marqueurs TODO
		 */
		
		return null;
	}

	/////////////////////////////////////////////////////////////////
	// DIRECTION				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Direction processCurrentDirection() throws StopRequestException
	{	ai.checkInterruption();
		
		/*
		 *  TODO à compléter.
		 *  
		 *  Remarque : ce commentaire est à effacer, comme tous les autres marqueurs TODO
		 */
		
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
		
		/*
		 *  TODO à redéfinir, si vous voulez afficher d'autres informations.
		 *  
		 *  Remarque : ce commentaire est à effacer, comme tous les autres marqueurs TODO
		 */
	}
}
