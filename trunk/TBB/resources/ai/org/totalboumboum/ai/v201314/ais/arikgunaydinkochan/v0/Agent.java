package org.totalboumboum.ai.v201314.ais.arikgunaydinkochan.v0;

import org.totalboumboum.ai.v201314.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.agent.ArtificialIntelligence;

/**
 * Classe principale de votre agent, que vous devez compléter.
 * Cf. la documentation de {@link ArtificialIntelligence} pour plus de détails.
 * 
 * TODO Effacez ces commentaires et remplacez-les par votre propre Javadoc.
 *
 * @author İsmail Arık
 * @author Tuğba Günaydın
 * @author Çağdaş Kochan
 */
public class Agent extends ArtificialIntelligence
{
	/**
	 * Instancie la classe principale de l'agent.
	 */
	public Agent()
	{	checkInterruption();
		
		// active/désactive la sortie texte
		verbose = true;
	}
	
	@Override
	protected void initOthers()
	{	checkInterruption();
		
		/*
		 *  TODO à compléter si vous voulez créer des objets 
		 *  particuliers pour réaliser votre traitement, et qui
		 *  ne sont ni des gestionnaires (initialisés dans initHandlers)
		 *  ni des percepts (initialisés dans initPercepts).
		 *  
		 *  Remarque : ce commentaire est à effacer, comme tous les autres marqueurs TODO
		 */
	
		// cf. la Javadoc dans ArtificialIntelligence pour une description de la méthode
	}
	
	/////////////////////////////////////////////////////////////////
	// PERCEPTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void initPercepts()
	{	checkInterruption();
	
		/*
		 *  TODO à compléter si vous voulez créer des objets 
		 *  particuliers pour réaliser votre traitement.
		 *  Ils peuvent être stockés dans cette classe ou dans
		 *  un gestionnaire quelconque. 
		 *  
		 *  Remarque : ce commentaire est à effacer, comme tous les autres marqueurs TODO
		 */
	
		// cf. la Javadoc dans ArtificialIntelligence pour une description de la méthode
	}
	
	@Override
	protected void updatePercepts()
	{	checkInterruption();
		
		/*
		 *  TODO à compléter si vous avez des objets 
		 *  à mettre à jour à chaque itération, e.g.
		 *  des objets créés par la méthode initPercepts().
		 *  
		 *  Remarque : ce commentaire est à effacer, comme tous les autres marqueurs TODO
		 */
	
		// cf. la Javadoc dans ArtificialIntelligence pour une description de la méthode
	}
	
	/////////////////////////////////////////////////////////////////
	// HANDLERS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Gestionnaire chargé de calculer le mode de l'agent */
	protected ModeHandler modeHandler;
	/** Gestionnaire chargé de calculer les valeurs de préférence de l'agent */
	protected PreferenceHandler preferenceHandler;
	/** Gestionnaire chargé de décider si l'agent doit poser une bombe ou pas */
	protected BombHandler bombHandler;
	/** Gestionnaire chargé de décidé de la direction de déplacement de l'agent */
	protected MoveHandler moveHandler;
	
	@Override
	protected void initHandlers()
	{	checkInterruption();
		
		// création des gestionnaires standard (obligatoires)
		modeHandler = new ModeHandler(this);
		preferenceHandler = new PreferenceHandler(this);
		bombHandler = new BombHandler(this);
		moveHandler = new MoveHandler(this);
		
		/*
		 *  TODO à compléter si vous utilisez d'autres gestionnaires
		 *  (bien sûr ils doivent aussi être déclarés ci-dessus)
		 *  
		 *  Remarque : ce commentaire est à effacer, comme tous les autres marqueurs TODO
		 */
		
		// cf. la Javadoc dans ArtificialIntelligence pour une description de la méthode
	}

	@Override
	protected AiModeHandler<Agent> getModeHandler()
	{	checkInterruption();
		return modeHandler;
	}

	@Override
	protected AiPreferenceHandler<Agent> getPreferenceHandler()
	{	checkInterruption();
		return preferenceHandler;
	}

	@Override
	protected AiBombHandler<Agent> getBombHandler()
	{	checkInterruption();
		return bombHandler;
	}

	@Override
	protected AiMoveHandler<Agent> getMoveHandler()
	{	checkInterruption();
		return moveHandler;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void updateOutput()
	{	checkInterruption();

		/*
		 *  TODO à compléter si vous voulez modifier l'affichage.
		 *  
		 *  Remarque : ce commentaire est à effacer, comme tous les autres marqueurs TODO
		 */

		// ici, par défaut on affiche :
			// les chemins et destinations courants
			moveHandler.updateOutput();
			// les preferences courantes
			preferenceHandler.updateOutput();
	
		// cf. la Javadoc dans ArtificialIntelligence pour une description de la méthode
	}
}
