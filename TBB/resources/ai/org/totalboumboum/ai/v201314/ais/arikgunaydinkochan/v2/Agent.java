package org.totalboumboum.ai.v201314.ais.arikgunaydinkochan.v2;


import java.util.ArrayList;

import org.totalboumboum.ai.v201314.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201314.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;


/**
 * Classe principale de votre agent, que vous devez compléter.
 * Cf. la documentation de {@link ArtificialIntelligence} pour plus de détails.
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
	
	}
	
	@Override
	protected void initOthers()
	{	checkInterruption();
		
		
	
		// cf. la Javadoc dans ArtificialIntelligence pour une description de la méthode
	}
	
	/////////////////////////////////////////////////////////////////
	// PERCEPTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void initPercepts()
	{	checkInterruption();
	
		
	
		// cf. la Javadoc dans ArtificialIntelligence pour une description de la méthode
	}
	
	@Override
	protected void updatePercepts()
	{	checkInterruption();
		
		// active/désactive la sortie texte
		verbose = false;
		modeHandler.verbose = false;
		preferenceHandler.verbose = false;
		bombHandler.verbose = false;
		moveHandler.verbose = false;
	
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

	

	//Les codes qu'on a ajoutée
	
	/** La liste de tile accessible
	 */
	public ArrayList<AiTile> accessibleTiles;
	
	/** La liste de tile control
	 */
	 public ArrayList<AiTile> controlTiles;
	 
	/**
	 *  La liste de tile qui est accessible dans loop
	 */
	 public ArrayList<AiTile> accessible;

	/**
	 * Calculer les tiles accessible par notre agent
	 */
	public void accessibleTiles(){
		checkInterruption();
		accessibleTiles = new ArrayList<AiTile>();
		controlTiles = new ArrayList<AiTile>();
		accessible = new ArrayList<AiTile>();
		
		AiZone zone = this.getZone();
		AiHero myHero = zone.getOwnHero();
		AiTile mytile = myHero.getTile();
		
		accessibleTiles.add(mytile);
		controlTiles.add(mytile);
		
		int control;
		
		// selection de tile accessible
		do{	
			checkInterruption();
			control=1;		
			 for(AiTile controlTile : controlTiles){
				 checkInterruption();									
				 	
				 	for(AiTile neighbours: controlTile.getNeighbors()){
				 		checkInterruption();
				 		AiTile current = neighbours;			 	
				 		if(current.isCrossableBy(myHero)){
				 			if(!accessibleTiles.contains(current))
				 					accessible.add(current);
				 		}
				 	}				 	
				}
			 
			 	if(accessible.isEmpty())
			 		control=0;
			 	//clear control list
			 	controlTiles.clear();
			 	//add accesbible list to control list
			 	controlTiles.addAll(accessible);
			 	//add accessible list to accessibletiles liste
			 	accessibleTiles.addAll(accessible);
			 	//clear accessible list
			 	accessible.clear();
				
		}while(control==1);
	}
	
	
	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void updateOutput()
	{	checkInterruption();
	
		// vous pouvez changer la taille du texte affiché, si nécessaire
		// attention: il s'agit d'un coefficient multiplicateur
		AiOutput output = getOutput();
		output.setTextSize(2);

		// ici, par défaut on affiche :
			// les chemins et destinations courants
			moveHandler.updateOutput();
			// les preferences courantes
			preferenceHandler.updateOutput();
	
		// cf. la Javadoc dans ArtificialIntelligence pour une description de la méthode
	}	
}




