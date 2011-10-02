package org.totalboumboum.ai.v200910.ais.aksoytangay.v3;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.ai.v200910.adapter.path.AiPath;
import org.totalboumboum.ai.v200910.adapter.path.astar.Astar;
import org.totalboumboum.ai.v200910.adapter.path.astar.cost.BasicCostCalculator;
import org.totalboumboum.ai.v200910.adapter.path.astar.cost.CostCalculator;
import org.totalboumboum.ai.v200910.adapter.path.astar.heuristic.BasicHeuristicCalculator;
import org.totalboumboum.ai.v200910.adapter.path.astar.heuristic.HeuristicCalculator;


/**
 * 
 * @version 3
 * 
 * @author Cihan Aksoy
 * @author Necmi Murat Tangay
 *
 */
@SuppressWarnings("deprecation")
public class PathManager {
	
	//variable pour faire de l'appel
	@SuppressWarnings("unused")
	private AksoyTangay myAI;
	
	@SuppressWarnings("unused")
	private AiZone percepts;
	
	public PathManager() {
		
	}
	
	/**
	 * 
	 * Methode qui calcule le chemin le plus court.
	 * 
	 * @param ownHero
	 *           
	 * @param startPoint
	 *            
	 * @param endPoints
	 *            
	 * @param myAI
	 * 
	 * @return le chemin le plus court
	 * @throws StopRequestException
	 */
	public AiPath getShortestPath(AiHero ownHero, AiTile startTile, 
			List<AiTile> endTiles, AksoyTangay myAI) throws StopRequestException{
		
		myAI.checkInterruption();
		this.myAI = myAI;
		//variable de le plus court chemin
		AiPath shortestPath;
		//Objet de Astar
		Astar aStar;
		//calcul du cout et de heurustique
		CostCalculator costCalculator = new BasicCostCalculator();
		HeuristicCalculator heuristicCalculator = new BasicHeuristicCalculator();
		//on cree une instance de Astar qui va nous donner le chemin plus court
		aStar = new Astar(myAI,ownHero, costCalculator, heuristicCalculator);
		shortestPath = aStar.processShortestPath(startTile, endTiles);
		
		return shortestPath;		
	}
	
	/**
	 * 
	 * Methode qui fait tenir dans une liste auxquelles cases on peut aller.
	 * 
	 * @return la liste des possible cases a aller
	 * @throws StopRequestException
	 */
	public List<AiTile> getAvailibleTiles(AiZone zone, AksoyTangay myAI) throws StopRequestException
	{
		myAI.checkInterruption();
		List<AiTile> availibleTiles = new ArrayList<AiTile>();
		@SuppressWarnings("unused")
		List<AiTile> tempTiles = new ArrayList<AiTile>();
		
		
		for(int i=0;i<zone.getHeight();i++)
		{
			myAI.checkInterruption();
			for(int j=0;j<zone.getWidth();j++)
			{
				myAI.checkInterruption();
				//a faire
			}
		}
		
		return availibleTiles;
	}

}
