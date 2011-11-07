package org.totalboumboum.ai.v201112.ais.sakaryasar.v1;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityHandler;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;

/**
 * Classe gérant le calcul des valeurs d'utilité de l'agent.
 * Cf. la documentation de {@link AiUtilityHandler} pour plus de détails.
 * 
 * TODO Effacez ces commentaires et remplacez-les par votre propre Javadoc.
 * 
 * @author Cahide Sakar
 * @author Abdurrahman Yaşar
 */
public class UtilityHandler extends AiUtilityHandler<SakarYasar>
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
	protected UtilityHandler(SakarYasar ai) throws StopRequestException
    {	super(ai);
		ai.checkInterruption();
	
		// TODO à compléter
	}

	/////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override 
	protected void update() throws StopRequestException
	{	ai.checkInterruption();
		
		// TODO à compléter
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
