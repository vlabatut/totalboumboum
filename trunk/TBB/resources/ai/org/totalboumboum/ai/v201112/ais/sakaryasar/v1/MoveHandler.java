package org.totalboumboum.ai.v201112.ais.sakaryasar.v1;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.totalboumboum.ai.v201112.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.path.AiLocation;
import org.totalboumboum.ai.v201112.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201112.adapter.path.search.Astar;
import org.totalboumboum.ai.v201112.adapter.path.cost.TimeCostCalculator;
import org.totalboumboum.ai.v201112.adapter.path.heuristic.NoHeuristicCalculator;
import org.totalboumboum.ai.v201112.adapter.path.successor.BasicSuccessorCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe gérant le déplacement de l'agent.
 * Cf. la documentation de {@link AiMoveHandler} pour plus de détails.
 * 
 * 
 * @author Cahide Sakar
 * @author Abdurrahman Yaşar
 */
public class MoveHandler extends AiMoveHandler<SakarYasar>
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
	protected MoveHandler(SakarYasar ai) throws StopRequestException
    {	super(ai);
		ai.checkInterruption();
		
		// on règle la sortie texte pour ce gestionnaire
		verbose = false;
		
		// 
	}

	/////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Direction considerMoving() throws StopRequestException
	{	ai.checkInterruption();
		
		// 
	
		HashMap<AiTile, Float> hm = ai.getUtilityHandler().getUtilitiesByTile();
		HashMap<Float, List<AiTile>> hm2 = ai.getUtilityHandler().getUtilitiesByValue();
		AiLocation myHero = new AiLocation(ai.getZone().getOwnHero().getTile());
		TimeCostCalculator cc = new TimeCostCalculator(ai,ai.getZone().getOwnHero());
	
		NoHeuristicCalculator hc = new NoHeuristicCalculator(ai); 	
		BasicSuccessorCalculator  sc = new BasicSuccessorCalculator(ai);
		Astar path = new Astar(ai, ai.getZone().getOwnHero(),cc, hc, sc);

		Float max = Collections.max(hm.values());
		this.currentDestination=hm2.get(max).get((int)((100*Math.random())) % hm2.get(max).size());

		try {
			this.currentPath= path.processShortestPath(myHero, this.currentDestination);
		} catch (LimitReachedException e) {
			e.printStackTrace();
		}
				
		Direction d = ai.getZone().getDirection(ai.getZone().getOwnHero().getTile(), this.currentDestination);

		return d;
		
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void updateOutput() throws StopRequestException
	{	ai.checkInterruption();
		
		// ici on se contente de faire le traitement par défaut
		super.updateOutput();
		//  à redéfinir, si vous voulez afficher d'autres informations
	}
}
