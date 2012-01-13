package org.totalboumboum.ai.v201112.ais.unluyildirim.v2;

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
 *
 * @author Merve Ünlü
 * @author Gülay Yıldırım
 */
public class UnluYildirim extends ArtificialIntelligence
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
		
		//  à compléter si vous utilisez d'autres gestionnaires
		// (bien sûr ils doivent aussi être déclarés ci-dessus)
		
		// cf. la java doc dans ArtificialIntelligence pour une description de la méthode
	}

	@Override
	protected AiModeHandler<UnluYildirim> getModeHandler() throws StopRequestException
	{	checkInterruption();
		return modeHandler;
	}

	@Override
	protected AiUtilityHandler<UnluYildirim> getUtilityHandler() throws StopRequestException
	{	checkInterruption();
		return utilityHandler;
	}

	@Override
	protected AiBombHandler<UnluYildirim> getBombHandler() throws StopRequestException
	{	checkInterruption();
		return bombHandler;
	}

	@Override
	protected AiMoveHandler<UnluYildirim> getMoveHandler() throws StopRequestException
	{	checkInterruption();
		return moveHandler;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void updateOutput() throws StopRequestException
	{	checkInterruption();

		//  à compléter si vous voulez modifier l'affichage
		// ici, par défaut on affiche :
			// les chemins et destinations courants
			moveHandler.updateOutput();
			// les utilités courantes
			utilityHandler.updateOutput();
	
		// cf. la java doc dans ArtificialIntelligence pour une description de la méthode
	}
}
