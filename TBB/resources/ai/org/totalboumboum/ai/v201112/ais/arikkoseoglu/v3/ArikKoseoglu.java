package org.totalboumboum.ai.v201112.ais.arikkoseoglu.v3;

import org.totalboumboum.ai.v201112.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201112.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201112.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityHandler;
import org.totalboumboum.ai.v201112.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;

/**
 * Classe principale de votre agent, que vous devez compléter.
 * Cf. la documentation de {@link ArtificialIntelligence} pour plus de détails.
 * 
 *  Effacez ces commentaires et remplacez-les par votre propre Javadoc.
 *
 * @author Furkan Arık
 * @author Aksel Köseoğlu
 */
public class ArikKoseoglu extends ArtificialIntelligence
{
	@Override
	protected void init() throws StopRequestException
	{	checkInterruption();
		
		super.init();
		verbose = false;
		
		
	}
	
	/////////////////////////////////////////////////////////////////
	// PERCEPTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void initPercepts() throws StopRequestException
	{	checkInterruption();
	
		
	}
	
	@Override
	protected void updatePercepts() throws StopRequestException
	{	checkInterruption();
		
		
	}
	
	/////////////////////////////////////////////////////////////////
	// HANDLERS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** gestionnaire chargé de calculer le mode de l'agent */
	protected ModeHandler modeHandler;
	/** gestionnaire chargé de calculer les valeurs d'utilité de l'agent */
	protected UtilityHandler utilityHandler;
	/** gestionnaire chargé de décider si l'agent doit poser une bombe ou pas */
	protected BombHandler bombHandler;
	/** gestionnaire chargé de décidé de la direction de déplacement de l'agent */
	protected MoveHandler moveHandler;
	
	@Override
	protected void initHandlers() throws StopRequestException
	{	checkInterruption();
		
		// création des gestionnaires standard (obligatoires)
		modeHandler = new ModeHandler(this);
		utilityHandler = new UtilityHandler(this);
		bombHandler = new BombHandler(this);
		moveHandler = new MoveHandler(this);
	}

	@Override
	protected AiModeHandler<ArikKoseoglu> getModeHandler() throws StopRequestException
	{	checkInterruption();
		return modeHandler;
	}

	@Override
	protected AiUtilityHandler<ArikKoseoglu> getUtilityHandler() throws StopRequestException
	{	checkInterruption();
		return utilityHandler;
	}

	@Override
	protected AiBombHandler<ArikKoseoglu> getBombHandler() throws StopRequestException
	{	checkInterruption();
		return bombHandler;
	}

	@Override
	protected AiMoveHandler<ArikKoseoglu> getMoveHandler() throws StopRequestException
	{	checkInterruption();
		return moveHandler;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void updateOutput() throws StopRequestException
	{	checkInterruption();

			// les chemins et destinations courants
			moveHandler.updateOutput();
			// les utilités courantes
			//utilityHandler.updateOutput();
			//AiOutput ecran = new AiOutput(getZone());
			
		// cf. la java doc dans ArtificialIntelligence pour une description de la méthode
	}
}
