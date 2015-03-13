package org.totalboumboum.ai.v200809.ais.gunalpyurtsever.v2;

import org.totalboumboum.ai.v200809.adapter.AiTile;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * objet specifique qui garde les infos de cost et heuristique pour des tiles
 * 
 * @author Ozan Günalp
 * @author Sinan Yürtsever
 *
 */
@SuppressWarnings("deprecation")
public class CostTile  {
	
	/** */
	private AiTile MyAiTile;
	/** */
	private int cost;
	/** */
	private int heuristic;
	/** */
	private Direction firstDrctn;
	/** */
	private boolean markVisited;
	/** */
	private boolean inDanger;
	
	/**
	 * 
	 * @param CurrentAiTile
	 * 		Description manquante !
	 * @param currentcost
	 * 		Description manquante !
	 * @param ComparedAiTile
	 * 		Description manquante !
	 * @param FirstDirection
	 * 		Description manquante !
	 */
	public CostTile(AiTile CurrentAiTile, int currentcost, AiTile ComparedAiTile,Direction FirstDirection){
		
		MyAiTile = CurrentAiTile;
		cost = currentcost;
	
		double a = Math.pow((ComparedAiTile.getLine()-CurrentAiTile.getLine()),2);
		double b = Math.pow((ComparedAiTile.getCol()-CurrentAiTile.getCol()),2);
		heuristic =  (int) (a+b);
		firstDrctn = FirstDirection;
		markVisited = false;
		inDanger = false;
	}
	
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 */
	public int getCost(){
		
		return cost;
		
	}
	
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 */
	public int getHeuristic(){
		
		return heuristic;
		
	}
	
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 */
	public AiTile getAiTile(){
		
		return MyAiTile;
	}
	
	@Override
	public String toString(){
		
		return MyAiTile.toString()+","+cost+","+heuristic+","+firstDrctn.toString();
		
	}
	
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 */
	public Direction getFirstDirection(){
		
		return firstDrctn;
		
	}
	
	/**
	 * 
	 * @param a
	 * 		Description manquante !
	 */
	public void setmarkVisited(boolean a){
	
		this.markVisited = a;
		
	}
	
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 */
	public boolean getmarkVisited(){
		
		return this.markVisited;
		
	}
	
	/**
	 * 
	 * @param danger
	 * 		Description manquante !
	 */
	public void setinDanger(boolean danger){
		
		this.inDanger = danger;
		
	}
	
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 */
	public boolean getinDanger(){
		
		return inDanger;
		
	}
}
