package tournament200809.gunalpyurtsever.v1;


import fr.free.totalboumboum.ai.adapter200809.AiTile;
import fr.free.totalboumboum.engine.content.feature.Direction;

public class CostTile  {
	
	private AiTile MyAiTile;
	private int cost;
	private int heuristic;
	private Direction firstDrctn;
	private boolean markVisited;
	private boolean inDanger;
	
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
	
	public int getCost(){
		
		return cost;
		
	}
	
	public int getHeuristic(){
		
		return heuristic;
		
	}
	
	public AiTile getAiTile(){
		
		return MyAiTile;
	}
	
	public String toString(){
		
		return MyAiTile.toString()+","+cost+","+heuristic+","+firstDrctn.toString();
		
	}
	
	public Direction getFirstDirection(){
		
		return firstDrctn;
		
	}
	
	public void setmarkVisited(boolean a){
	
		this.markVisited = a;
		
	}
	
	public boolean getmarkVisited(){
		
		return this.markVisited;
		
	}
	
	public void setinDanger(boolean danger){
		
		this.inDanger = danger;
		
	}
	
	public boolean getinDanger(){
		
		return inDanger;
		
	}
}
