package org.totalboumboum.ai.v200910.ais.aksoytangay.v5_2c;

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
import org.totalboumboum.engine.content.feature.Direction;

/**
 * cette class sert a diriger les chemins.
 * 
 * @version 5.2.c
 * 
 * @author Cihan Aksoy
 * @author Necmi Murat Tangay
 *
 */
public class PathManager {
	
	//variable pour faire de l'appel
	private AksoyTangay myAI;
	
	private Zone zone;
	
	private AiZone percepts;
	
	public boolean temp = false;
			
	public PathManager(AksoyTangay myAI, AiZone percepts) throws StopRequestException
	{
		myAI.checkInterruption();
		
		this.myAI = myAI;
		this.percepts = percepts;
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
			List<AiTile> endTiles) throws StopRequestException{
		
		myAI.checkInterruption();
		
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
		
//		System.out.println("getshortestpathTOESCAPE.patman");
//		System.out.println(shortestPath.toString());
		
		return shortestPath;		
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
	public AiPath getShortestPathToOneTile(AiHero ownHero, AiTile startTile, 
			AiTile endTile) throws StopRequestException{
		
		myAI.checkInterruption();
		
		//variable de le plus court chemin
		AiPath shortestPath;
		//Objet de Astar
		Astar aStar;
		//calcul du cout et de heurustique
		CostCalculator costCalculator = new BasicCostCalculator();
		HeuristicCalculator heuristicCalculator = new BasicHeuristicCalculator();
		//on cree une instance de Astar qui va nous donner le chemin plus court
		aStar = new Astar(myAI,ownHero, costCalculator, heuristicCalculator);
		shortestPath = aStar.processShortestPath(startTile, endTile);
		
//		System.out.println("getshortestpathTOESCAPE.patman");
//		System.out.println(shortestPath.toString());
		
		return shortestPath;		
	}
	
	/**
	 * 
	 * Methode qui fait tenir dans une liste auxquelles cases on peut aller.
	 * 
	 * @return la liste des possible cases a aller
	 * @throws StopRequestException
	 */
	public List<AiTile> getAvailibleTilesToEscape() throws StopRequestException
	{
		myAI.checkInterruption();
		List<AiTile> availibleTiles = new ArrayList<AiTile>();
				
		zone = new Zone(percepts, myAI);
			
		for(int i=0;i<percepts.getHeigh();i++)
		{
			myAI.checkInterruption();
			for(int j=0;j<percepts.getWidth();j++)
			{
				myAI.checkInterruption();

				if(zone.getMatrix()[i][j] == State.SURE)
					availibleTiles.add(percepts.getTile(i, j));
//				else
//				{
//					System.out.println("i ve j:  "+i+" "+j);
//					System.out.println("height : "+zone.getMatrix()[i][j]);
//				}
				
				
			}
		}
		
//		System.out.println("heigh : "+percepts.getHeigh()+"width : "+percepts.getWidth());
		//system.out.println("getavailibletilesTOESCAPE.patman");
		//system.out.println(availibleTiles.toString());
		
		return availibleTiles;
	}
	
	/**
	 * 
	 * Methode qui renvoie le meilleur chemin sure.
	 * 
	 * @return meilleur chemin
	 * @throws StopRequestException
	 */
	public AiPath getBestPathToEscape(List<AiTile> availibleTiles) throws StopRequestException
	{
		myAI.checkInterruption();
		
		AiHero tempHero = percepts.getOwnHero();
//		List<AiTile> tempTiles = getAvailibleTilesToEscape();
		AiPath result = getShortestPath(tempHero, tempHero.getTile(), availibleTiles);
		
		
		//system.out.println("getbestpat.patman");
		//system.out.println(result.toString());
		//system.out.println("current tile: "+ tempHero.getTile());
		
		return result;
	}
	
	public List<AiTile> getAvailibleTilesDirectToCollectBonus() throws StopRequestException
	{
		myAI.checkInterruption();
		List<AiTile> availibleTiles = new ArrayList<AiTile>();
				
		zone = new Zone(percepts, myAI);
		
		for(int i=0;i<percepts.getHeigh();i++)
		{
			myAI.checkInterruption();
			for(int j=0;j<percepts.getWidth();j++)
			{
				myAI.checkInterruption();

				if(zone.getMatrix()[i][j] == State.EXTRA_BOMBE || zone.getMatrix()[i][j] == State.EXTRA_FLAMME)
					availibleTiles.add(percepts.getTile(i, j));
				
				
			}
		}
		
		//system.out.println("getavailibletilesDirectTOCOLLECT.patman");
		//system.out.println(availibleTiles.toString());
		
		return availibleTiles;
	}
	
	public List<AiTile> getAvailibleTilesIndirectToCollectBonus() throws StopRequestException
	{
		myAI.checkInterruption();
		List<AiTile> availibleTiles = new ArrayList<AiTile>();
				
		zone = new Zone(percepts, myAI);
		
		for(int i=0;i<percepts.getHeigh();i++)
		{
			myAI.checkInterruption();
			for(int j=0;j<percepts.getWidth();j++)
			{
				myAI.checkInterruption();
			
				if(zone.getMatrix()[i][j] == State.DESTRUCTIBLE)
				{
					AiTile tempTileUp = percepts.getTile(i, j).getNeighbor(Direction.UP);
					AiTile tempTileDown = percepts.getTile(i, j).getNeighbor(Direction.DOWN);
					AiTile tempTileLeft = percepts.getTile(i, j).getNeighbor(Direction.LEFT);
					AiTile tempTileRight = percepts.getTile(i, j).getNeighbor(Direction.RIGHT);
									
					if(zone.getMatrix()[tempTileUp.getLine()][tempTileUp.getCol()] != State.INDESTRUCTIBLE)
						availibleTiles.add(tempTileUp);
					if(zone.getMatrix()[tempTileDown.getLine()][tempTileDown.getCol()] != State.INDESTRUCTIBLE)
						availibleTiles.add(tempTileDown);
					if(zone.getMatrix()[tempTileRight.getLine()][tempTileRight.getCol()] != State.INDESTRUCTIBLE)
						availibleTiles.add(tempTileRight);
					if(zone.getMatrix()[tempTileLeft.getLine()][tempTileUp.getCol()] != State.INDESTRUCTIBLE)
						availibleTiles.add(tempTileLeft);
					
				}
			}
		}
		
		
		
		//system.out.println("getavailibletilesIndirectTOCOLLECT.patman");
		//system.out.println(availibleTiles.toString());
		
		return availibleTiles;
	}
	
		
	
	public AiPath getBestPathToCollectBonus(List<AiTile> availibleTilesDirect, List<AiTile> availibleTilesIndirect) throws StopRequestException
	{
		myAI.checkInterruption();
		
		AiHero tempHero = percepts.getOwnHero();
//		List<AiTile> tempTiles = getAvailibleTilesToCollectBonus();
		AiPath resultDirect = getShortestPath(tempHero, tempHero.getTile(), availibleTilesDirect);
		AiPath resultIndirect = getShortestPath(tempHero, tempHero.getTile(), availibleTilesIndirect);
		AiPath result = null;
		
		//system.out.println("direct path : "+resultDirect.toString());
		
		if(!resultDirect.isEmpty())				
		{
			if(resultDirect.isShorterThan(resultIndirect))
				result = resultDirect;
			else if((resultDirect.getDuration(tempHero)) < (resultIndirect.getDuration(tempHero))+2400)
				result = resultDirect;
			
			temp = true;
		}
		else
		{
			result = resultIndirect;
			temp = false;
		}
		
				
		//system.out.println("getbestpatTOCOLLECT.patman");
		//system.out.println(result.toString());
		//system.out.println("current tile: "+ tempHero.getTile());
		
		return result;
	}
	
	
}
