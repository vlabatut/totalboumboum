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
	 * @param currentcost
	 * @param ComparedAiTile
	 * @param FirstDirection
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
	 * 		?
	 */
	public int getCost(){
		
		return cost;
		
	}
	
	/**
	 * 
	 * @return
	 * 		?
	 */
	public int getHeuristic(){
		
		return heuristic;
		
	}
	
	/**
	 * 
	 * @return
	 * 		?
	 */
	public AiTile getAiTile(){
		
		return MyAiTile;
	}
	
	/**
	 * 
	 */
	public String toString(){
		
		return MyAiTile.toString()+","+cost+","+heuristic+","+firstDrctn.toString();
		
	}
	
	/**
	 * 
	 * @return
	 * 		?
	 */
	public Direction getFirstDirection(){
		
		return firstDrctn;
		
	}
	
	/**
	 * 
	 * @param a
	 */
	public void setmarkVisited(boolean a){
	
		this.markVisited = a;
		
	}
	
	/**
	 * 
	 * @return
	 * 		?
	 */
	public boolean getmarkVisited(){
		
		return this.markVisited;
		
	}
	
	/**
	 * 
	 * @param danger
	 */
	public void setinDanger(boolean danger){
		
		this.inDanger = danger;
		
	}
	
	/**
	 * 
	 * @return
	 * 		?
	 */
	public boolean getinDanger(){
		
		return inDanger;
		
	}
}
