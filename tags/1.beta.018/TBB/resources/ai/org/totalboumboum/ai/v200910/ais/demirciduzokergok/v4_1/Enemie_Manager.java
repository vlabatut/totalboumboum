package org.totalboumboum.ai.v200910.ais.demirciduzokergok.v4_1;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.ai.v200910.adapter.path.AiPath;
import org.totalboumboum.ai.v200910.adapter.path.astar.Astar;
import org.totalboumboum.ai.v200910.adapter.path.astar.cost.MatrixCostCalculator;
import org.totalboumboum.ai.v200910.adapter.path.astar.heuristic.BasicHeuristicCalculator;
import org.totalboumboum.ai.v200910.adapter.path.astar.heuristic.HeuristicCalculator;
import org.totalboumboum.engine.content.feature.Direction;











public class Enemie_Manager {

	
	public Enemie_Manager(DemirciDuzokErgok ai) throws StopRequestException{
	ai.checkInterruption(); //APPEL OBLIGATOIRE
		
		this.ai = ai;
		zone = ai.getPercepts();
		//safe_map=new Safety_Map(zone);
		//safe_map=new Safety_Map(zone);
		// init A*
		double costMatrix[][] = new double[zone.getHeigh()][zone.getWidth()];
		costCalculator_b = new MatrixCostCalculator(costMatrix);
		hcalcul_b = new BasicHeuristicCalculator();
		star_b = new Astar(ai,ai.getPercepts().getOwnHero(),costCalculator_b,hcalcul_b);
		
		// init destinations
		arrived_b = false;
		//safe_map=new Safety_Map(zone);
		AiHero our_bomberman = zone.getOwnHero();
		possibleDest_b=destinations_possibles_b(our_bomberman.getTile());
		updatePath_b();	
	}
	
	public Direction direcition_updt_b() throws StopRequestException {
		ai.checkInterruption(); //APPEL OBLIGATOIRE
		
		
		updateCostCalculator_b();

		Direction result = Direction.NONE;
		if(hasArrived_b()==false)
		{	
			checkIsOnPath_b();
		
			if(path_b.isEmpty() || !checkPathValidity_b()){
				updatePath_b();
			}
			

			else{
			
			AiTile tile = null;
			if(path_b.getLength()>1)
				tile = path_b.getTile(1);
		
			else if(path_b.getLength()>0)
				tile = path_b.getTile(0);
		
			if(tile!=null)
				result = zone.getDirection(zone.getOwnHero(),tile);			
				
			
			
			}
		}
		return result;

		
		
		
		
	}



	public boolean checkPathValidity_b() throws StopRequestException {

		ai.checkInterruption(); //APPEL OBLIGATOIRE
		
		boolean result = true;
		Iterator<AiTile> it = path_b.getTiles().iterator();
		while(it.hasNext() && result)
		{	ai.checkInterruption(); //APPEL OBLIGATOIRE
			
			AiTile tile = it.next();
			result = tile.isCrossableBy(zone.getOwnHero());			
		}
		return result;
		
	}


	public void checkIsOnPath_b() throws StopRequestException {

		ai.checkInterruption(); //APPEL OBLIGATOIRE

		
		AiTile currentTile =zone.getOwnHero().getTile();
		while(!path_b.isEmpty() && path_b.getTile(0)!=currentTile)
		{	ai.checkInterruption(); //APPEL OBLIGATOIRE
			
			path_b.removeTile(0);
		}
		
		
		

		
	}


	public boolean hasArrived_b() throws StopRequestException {

		ai.checkInterruption(); //APPEL OBLIGATOIRE
		int x,y;
		if(arrived_tile_b!=null){
			x=arrived_tile_b.getCol()-zone.getOwnHero().getCol();
			y=arrived_tile_b.getLine()-zone.getOwnHero().getLine();
			if(x<0)
				x=-x;
			if(y<0)
				y=-y;
			
			
		if(x<=zone.getOwnHero().getBombRange() || y<=zone.getOwnHero().getBombRange()){
			arrived_b=true;
		}
		else
			arrived_b=false;
		}
		else {
			arrived_b=true;
		}
	//	else if(arrived_tile_b==null)
		//	arrived_b=false;
		/*
		boolean k=true;
		int m=0;
		while(m<path_b.getLength()-1 && path_b.isEmpty()==false){
			if(safe_map.returnMatrix()[path_b.getTile(m).getLine()][path_b.getTile(m).getCol()]!=safe_map.SAFE_CASE)
				k=false;
				
		m++;
		}
		
		if(k==false)
			arrived_b=true;
*/		
		return arrived_b;
		
		}
		
	public void updateCostCalculator_b() throws StopRequestException{

		ai.checkInterruption(); //APPEL OBLIGATOIRE
		
		safe_map=new Safety_Map(zone);
		double safetyMatrix_b[][] = safe_map.returnMatrix();
		for(int line=0;line<zone.getHeigh();line++)
		{	ai.checkInterruption(); //APPEL OBLIGATOIRE
			
			for(int col=0;col<zone.getWidth();col++)
			{	ai.checkInterruption(); //APPEL OBLIGATOIRE
				
			double cost_b = safetyMatrix_b[line][col];
			costCalculator_b.setCost(line, col, cost_b);
			
			}
		}
		

		
	
		
		
	}	
	
	public void updatePath_b() throws StopRequestException {


		ai.checkInterruption(); //APPEL OBLIGATOIRE
		
		
		path_b = star_b.processShortestPath(ai.getPercepts().getOwnHero().getTile(),possibleDest_b);
		arrived_tile_b = path_b.getLastTile();
		

		

		
	}
	
	public boolean accessiblePath(){
		if(path_b.isEmpty()==false)
			return true;
		else
			return false;
	}
	
	
	public List<AiTile> destinations_possibles_b(AiTile tile) throws StopRequestException {


		ai.checkInterruption(); //APPEL OBLIGATOIRE
		safe_map=new Safety_Map(zone);
		AiTile tile_dest_b;
		List<AiTile> result_b = new ArrayList<AiTile>();
		
		for(int pos_y=0;pos_y<zone.getHeigh();pos_y++)
		{	ai.checkInterruption(); //APPEL OBLIGATOIRE
			
			for(int pos_x=0;pos_x<zone.getWidth();pos_x++)
			{	ai.checkInterruption(); //APPEL OBLIGATOIRE
				
				
				if(safe_map.returnMatrix()[pos_y][pos_x]==safe_map.ENEMIE){
					tile_dest_b=zone.getTile(pos_y, pos_x);
					result_b.add(tile_dest_b);
				}
				
		
			}
		
			
		
		}
				
		return result_b;
		
	}

	private DemirciDuzokErgok ai;
	private AiZone zone;
	private Safety_Map safe_map;
	private AiTile arrived_tile_b;
	private List<AiTile> possibleDest_b;
	private boolean arrived_b;
	private AiPath path_b;
	
	private Astar star_b;
	private HeuristicCalculator hcalcul_b;
	private MatrixCostCalculator costCalculator_b;
	
	
	
	
	
	
	
	
	
}

