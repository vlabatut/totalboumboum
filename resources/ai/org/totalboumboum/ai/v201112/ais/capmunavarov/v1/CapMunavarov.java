package org.totalboumboum.ai.v201112.ais.capmunavarov.v1;

import java.util.ArrayList;
import java.util.List;


import org.totalboumboum.ai.v201112.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201112.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201112.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCase;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityHandler;
import org.totalboumboum.ai.v201112.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiBomb;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;

/**
 * Classe principale de votre agent, que vous devez compléter.
 * Cf. la documentation de {@link ArtificialIntelligence} pour plus de détails.
 * 
 * @author Fahri Cap
 * @author Suhrob Munavarov
 */
@SuppressWarnings("deprecation")
public class CapMunavarov extends ArtificialIntelligence
{
	/** */
	List<AiUtilityCase> bombcases=new ArrayList<AiUtilityCase>(); //Les cases definie pour le bombage 
	/** */
	List<AiUtilityCase> moveCases=new ArrayList<AiUtilityCase>();//Les cases definie pour la deplacement  
	
	/** */
	static int zoneWidth=0;
	/** */
	static int zoneHeight =0;
	/** */
	static int bombeCount=0;
	
	@Override
	protected void init() throws StopRequestException
	{	checkInterruption();
		
		super.init();
		verbose = false;
		
		zoneHeight=getZone().getHeight();
		zoneWidth=getZone().getWidth();
		initHandlers();
		
		
//		System.out.println("girdim");
		
		// à compléter si vous voulez créer des objets 
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
	@SuppressWarnings("unused")
	@Override
	protected void initPercepts() throws StopRequestException
	{	checkInterruption();
	
	
		AiZone zone =getZone();
		List<AiHero >  allheros =zone.getHeroes();
		bombeCount=getZone().getOwnHero().getBombNumberCurrent();
		
//		System.out.println ("initpercepts ");
		// à compléter si vous voulez créer des objets 
		// particuliers pour réaliser votre traitement.
		// Ils peuvent être stockés dans cette classe ou dans
		// un gestionnaire quelconque. 
	
		// cf. la Javadoc dans ArtificialIntelligence pour une description de la méthode
	}
	
	@SuppressWarnings("unused")
	@Override
	protected void updatePercepts() throws StopRequestException
	{	checkInterruption();
	
	List<AiBomb> bombes =getZone().getBombs();	
	bombeCount=getZone().getOwnHero().getBombNumberMax();
	
	
	if (bombeCount>0){
		bombHandler.considerBombing();
		bombHandler.updateOutput();
//		System.out.println("Putting bombes..");
	}
	
		// à compléter si vous avez des objets 
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
	
	@Override
	protected void initHandlers() throws StopRequestException
	{	checkInterruption();
		
		// création des gestionnaires standard (obligatoires)
		modeHandler = new ModeHandler(this);
		utilityHandler = new UtilityHandler(this);
		bombHandler = new BombHandler(this);
		moveHandler = new MoveHandler(this);
		
		// à compléter si vous utilisez d'autres gestionnaires
		// (bien sûr ils doivent aussi être déclarés ci-dessus)
		
		// cf. la Javadoc dans ArtificialIntelligence pour une description de la méthode
	}

	@Override
	protected AiModeHandler<CapMunavarov> getModeHandler() throws StopRequestException
	{	checkInterruption();
		return modeHandler;
	}

	@Override
	protected AiUtilityHandler<CapMunavarov> getUtilityHandler() throws StopRequestException
	{	checkInterruption();
		return utilityHandler;
	}

	@Override
	protected AiBombHandler<CapMunavarov> getBombHandler() throws StopRequestException
	{	checkInterruption();
		return bombHandler;
	}

	@Override
	protected AiMoveHandler<CapMunavarov> getMoveHandler() throws StopRequestException
	{	checkInterruption();
		
		return moveHandler;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void updateOutput() throws StopRequestException
	{	checkInterruption();

		// à compléter si vous voulez modifier l'affichage
		// ici, par défaut on affiche :
			// les chemins et destinations courants
			moveHandler.updateOutput();
			// les utilités courantes
			utilityHandler.updateOutput();
			bombHandler.updateOutput();
	
		// cf. la Javadoc dans ArtificialIntelligence pour une description de la méthode
	}
}
