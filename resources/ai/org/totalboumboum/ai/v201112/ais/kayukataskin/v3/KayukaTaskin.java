package org.totalboumboum.ai.v201112.ais.kayukataskin.v3;

import org.totalboumboum.ai.v201112.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201112.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201112.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityHandler;
import org.totalboumboum.ai.v201112.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.ais._simplet.v1.CommonTools;

/**
 * Classe principale de votre agent, que vous devez compléter.
 * Cf. la documentation de {@link ArtificialIntelligence} pour plus de détails.
 * 
 * @author Pol Kayuka
 * @author Ayça Taşkın
 */
@SuppressWarnings("deprecation")
public class KayukaTaskin extends ArtificialIntelligence
{
	@Override
	protected void init() throws StopRequestException
	{	checkInterruption();
		
		super.init();
		verbose = false;
		
		//  à compléter si vous voulez créer des objets 
		// particuliers pour réaliser votre traitement, et qui
		// ne sont ni des gestionnaires (initialisés dans initHandlers)
		// ni des percepts (initialisés dans initPercepts).
		// Par exemple, ici on surcharge init() pour initialiser
		// verbose, qui est la variable controlant la sortie 
		// texte de l'agent (true -> debug, false -> pas de sortie)
	
		// cf. la Javadoc dans ArtificialIntelligence pour une description de la méthode
	}
	
	/////////////////////////////////////////////////////////////////
	// PERCEPTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void initPercepts() throws StopRequestException
	{	checkInterruption();
	
		//  à compléter si vous voulez créer des objets 
		// particuliers pour réaliser votre traitement.
		// Ils peuvent être stockés dans cette classe ou dans
		// un gestionnaire quelconque. 
	
		// cf. la Javadoc dans ArtificialIntelligence pour une description de la méthode
	}
	
	@Override
	protected void updatePercepts() throws StopRequestException
	{	checkInterruption();
		
		//  à compléter si vous avez des objets 
		// à mettre à jour à chaque itération, e.g.
		// des objets créés par la méthode initPercepts().
	
		// cf. la Javadoc dans ArtificialIntelligence pour une description de la méthode
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
	/** */
	public CommonTools commonTools;
	
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
		
		// cf. la Javadoc dans ArtificialIntelligence pour une description de la méthode
	}

	@Override
	protected AiModeHandler<KayukaTaskin> getModeHandler() throws StopRequestException
	{	checkInterruption();
		return modeHandler;
	}

	@Override
	protected AiUtilityHandler<KayukaTaskin> getUtilityHandler() throws StopRequestException
	{	checkInterruption();
		return utilityHandler;
	}

	@Override
	protected AiBombHandler<KayukaTaskin> getBombHandler() throws StopRequestException
	{	checkInterruption();
		return bombHandler;
	}

	@Override
	protected AiMoveHandler<KayukaTaskin> getMoveHandler() throws StopRequestException
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
	
		// cf. la Javadoc dans ArtificialIntelligence pour une description de la méthode
	}
}
