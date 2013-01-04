package org.totalboumboum.ai.v201213.ais.guneysharef.v4;



import org.totalboumboum.ai.v201213.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.path.AiLocation;
import org.totalboumboum.ai.v201213.adapter.path.AiPath;
import org.totalboumboum.ai.v201213.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201213.adapter.path.cost.CostCalculator;
import org.totalboumboum.ai.v201213.adapter.path.cost.TimeCostCalculator;
import org.totalboumboum.ai.v201213.adapter.path.heuristic.HeuristicCalculator;
import org.totalboumboum.ai.v201213.adapter.path.heuristic.TimeHeuristicCalculator;
import org.totalboumboum.ai.v201213.adapter.path.search.Astar;
import org.totalboumboum.ai.v201213.adapter.path.successor.SuccessorCalculator;
import org.totalboumboum.ai.v201213.adapter.path.successor.TimePartialSuccessorCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * @author Melis Güney
 * @author Seli Sharef
 */
public class MoveHandler extends AiMoveHandler<GuneySharef>
{	
	/**
	 * direction
	 */
	Direction direction = Direction.NONE;
	/**
	 * case avec la valeur d'utilité la plus elevé
	 */
	AiTile biggestTile;
	/**
	 * le meilleur chemin
	 */
	AiPath globalAiPath = new AiPath();
	/**
	 * location
	 */
	AiLocation globalCurrentLoc;
	
	/**
	 * ennemycost
	 */
	int enemycost=0;
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected MoveHandler(GuneySharef ai) throws StopRequestException
    {	super(ai);
		ai.checkInterruption();
		
		// on règle la sortie texte pour ce gestionnaire
		verbose = false;
		
		
	}

	/////////////////////////////////////////////////////////////////
	// DESTINATION				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	/**
	 * Met à jour l'objectif courant de l'agent, c'est à dire la case dans laquelle il veut aller. 
	 */
	protected AiTile updateCurrentDestination() throws StopRequestException
	{	
		ai.checkInterruption();
		AiHero h = this.ai.getZone().getOwnHero();
		AiTile t=h.getTile();
		
		if (!this.ai.getZone().getRemainingOpponents().isEmpty()) {
			if (this.ai.getDanger(t)){
				enemycost=1000;
			}
		} else {
			enemycost = 0;
		}
		
		biggestTile = this.ai.getBiggestTile();
		return biggestTile;
	}

	/////////////////////////////////////////////////////////////////
	// PATH						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	/**
	 * Met à jour le chemin courant de l'agent, c'est à dire la séquence de cases à 
	 * parcourir pour atteindre (directement ou indirectement) la case objectif. 

	 */
	protected AiPath updateCurrentPath() throws StopRequestException
	{	ai.checkInterruption();
		
		

		
		
		AiHero h = this.ai.getZone().getOwnHero();
		AiTile t=h.getTile();
		AiLocation l=new AiLocation(t);
		
	
		
		globalCurrentLoc = l;
		

		
		CostCalculator cc =new TimeCostCalculator(this.ai, h);
		HeuristicCalculator hc=new TimeHeuristicCalculator(this.ai,h);
		SuccessorCalculator sc=new TimePartialSuccessorCalculator(this.ai, TimePartialSuccessorCalculator.MODE_NOTREE);
		
		Astar a=new Astar(this.ai, h, cc, hc, sc);

		if (this.ai.getBiggestTile() != null) {
			try {

				AiPath aipath = a.startProcess(l, this.ai.getBiggestTile());
				globalAiPath = aipath;
				
			} catch(LimitReachedException e) {
		}
		}
		return globalAiPath;

	}
	/////////////////////////////////////////////////////////////////
	// DIRECTION				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	/**
	 * Met à jour la direction courante suivie par l'agent. 
	 */
	protected Direction updateCurrentDirection() throws StopRequestException
	{	
		ai.checkInterruption();
		
				
		try{
			if (globalAiPath != null) {
				long wait = globalAiPath.getFirstPause();
				if (globalAiPath.getLength() < 2 || wait > 0) {

					direction = Direction.NONE;
					return direction;

				}

				AiLocation target = globalAiPath.getLocation(1);
				return direction = this.ai.getZone().getDirection(globalCurrentLoc, target);
			}
		} 
			catch(RuntimeException exc){ return Direction.NONE;}
		return direction;
	
	}
		
		

	



	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	/**
	 * Met à jour les sorties graphiques de l'agent 
	 * en considérant les données de ce gestionnaire.
	 */
	public void updateOutput() throws StopRequestException
	{	ai.checkInterruption();
		
		// ici on se contente de faire le traitement par défaut
		super.updateOutput();
		
	}
}
