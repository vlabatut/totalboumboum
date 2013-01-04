package org.totalboumboum.ai.v201213.ais.guneysharef.v1;



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
	{	ai.checkInterruption();
		
		
		
		return null;
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
		
		
		
		return null;
	}

	/////////////////////////////////////////////////////////////////
	// DIRECTION				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	/**
	 * Met à jour la direction courante suivie par l'agent. 
	 */
	protected Direction updateCurrentDirection() throws StopRequestException
	{	ai.checkInterruption();
	AiHero h=this.ai.getZone().getOwnHero();
	AiTile t=h.getTile();
	Direction direction= Direction.NONE;
	AiLocation l=new AiLocation(t);
	float valeur = 10;
	
	
	CostCalculator cc =new TimeCostCalculator(this.ai, h);
	HeuristicCalculator hc=new TimeHeuristicCalculator(this.ai,h);
	SuccessorCalculator sc=new TimePartialSuccessorCalculator(this.ai, TimePartialSuccessorCalculator.MODE_NOTREE);
	
	Astar a=new Astar(this.ai, h, cc, hc, sc);
	
	if(this.ai.getBiggestTile() !=null){
		try{
			AiPath aipath=a.startProcess(l, this.ai.getBiggestTile());
			if(aipath!=null){
				if(this.ai.getDist(t, this.ai.getBiggestTile())>valeur){
					direction = Direction.NONE;
					return direction;
				}
				else {
					AiLocation l2=aipath.getLocation(1);
					return direction = this.ai.getZone().getDirection(l,l2);
				}
			}
			else{
				
			}
		
		
		}catch(LimitReachedException e){
			
		}

	}
	return Direction.NONE;
	
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
