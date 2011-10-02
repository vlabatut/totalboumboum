package org.totalboumboum.ai.v200809.ais.gunalpyurtsever.v2c;

import org.totalboumboum.ai.v200809.adapter.AiTile;
import org.totalboumboum.ai.v200809.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200809.adapter.StopRequestException;
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
	
	private AiTile MyAiTile;
	private int cost;
	private int heuristic;
	private Direction firstDrctn;
	private boolean markVisited;
	private boolean inDanger;
	ArtificialIntelligence ai;
	
	public CostTile(AiTile CurrentAiTile, int currentcost, AiTile ComparedAiTile,Direction FirstDirection, ArtificialIntelligence ai) throws StopRequestException{
		ai.checkInterruption();
		this.ai = ai;
		MyAiTile = CurrentAiTile;
		cost = currentcost;
	
		double a = Math.pow((ComparedAiTile.getLine()-CurrentAiTile.getLine()),2);
		double b = Math.pow((ComparedAiTile.getCol()-CurrentAiTile.getCol()),2);
		heuristic =  (int) (a+b);
		firstDrctn = FirstDirection;
		markVisited = false;
		inDanger = false;
	}
	
	public int getCost() throws StopRequestException{
		ai.checkInterruption();
		return cost;
		
	}
	
	public int getHeuristic() throws StopRequestException{
		ai.checkInterruption();
		return heuristic;
		
	}
	
	public AiTile getAiTile() throws StopRequestException{
		ai.checkInterruption();
		return MyAiTile;
	}
	
	public String toString(){
		
		return MyAiTile.toString()+","+cost+","+heuristic+","+firstDrctn.toString();
		
	}
	
	public Direction getFirstDirection() throws StopRequestException{
		ai.checkInterruption();
		return firstDrctn;
		
	}
	
	public void setmarkVisited(boolean a) throws StopRequestException{
		ai.checkInterruption();
		this.markVisited = a;
		
	}
	
	public boolean getmarkVisited() throws StopRequestException{
		ai.checkInterruption();
		return this.markVisited;
		
	}
	
	public void setinDanger(boolean danger) throws StopRequestException{
		ai.checkInterruption();
		this.inDanger = danger;
		
	}
	
	public boolean getinDanger() throws StopRequestException{
		ai.checkInterruption();
		return inDanger;
		
	}
}
