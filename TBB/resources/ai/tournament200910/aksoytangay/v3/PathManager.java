package tournament200910.aksoytangay.v3;

import java.util.ArrayList;
import java.util.List;

import fr.free.totalboumboum.ai.adapter200910.communication.StopRequestException;
import fr.free.totalboumboum.ai.adapter200910.data.AiHero;
import fr.free.totalboumboum.ai.adapter200910.data.AiTile;
import fr.free.totalboumboum.ai.adapter200910.data.AiZone;
import fr.free.totalboumboum.ai.adapter200910.path.AiPath;
import fr.free.totalboumboum.ai.adapter200910.path.astar.Astar;
import fr.free.totalboumboum.ai.adapter200910.path.astar.cost.BasicCostCalculator;
import fr.free.totalboumboum.ai.adapter200910.path.astar.cost.CostCalculator;
import fr.free.totalboumboum.ai.adapter200910.path.astar.heuristic.BasicHeuristicCalculator;
import fr.free.totalboumboum.ai.adapter200910.path.astar.heuristic.HeuristicCalculator;

/**
 * >> cette class sert a diriger les chemins.
 */
public class PathManager {
	
	//variable pour faire de l'appel
	@SuppressWarnings("unused")
	private AksoyTangay myAI;
	
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
		aStar = new Astar(ownHero, costCalculator, heuristicCalculator);
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
		List<AiTile> tempTiles = new ArrayList<AiTile>();
		
		
		for(int i=0;i<zone.getHeigh();i++)
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
