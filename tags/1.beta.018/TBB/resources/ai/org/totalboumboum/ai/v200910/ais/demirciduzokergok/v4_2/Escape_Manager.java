package org.totalboumboum.ai.v200910.ais.demirciduzokergok.v4_2;

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


public class Escape_Manager {

	
	public Escape_Manager(DemirciDuzokErgok ai) throws StopRequestException{
		
	ai.checkInterruption(); //APPEL OBLIGATOIRE
		
		this.ai = ai;
		zone = ai.getPercepts();
		AiHero our_bomberman = ai.getPercepts().getOwnHero();
		//safe_map=new Safety_Map(zone);
		// init A*
		double costMatrix[][] = new double[zone.getHeigh()][zone.getWidth()];
		costCalculator = new MatrixCostCalculator(costMatrix);
		heuristicCalculator = new BasicHeuristicCalculator();
		star = new Astar(ai,our_bomberman,costCalculator,heuristicCalculator);
		
		arrived = false;

		possibleDest=destinations_possibles(our_bomberman.getTile());
		path_init();
	
		
		
		
	}
	


	Direction direcition_updt() throws StopRequestException {
		ai.checkInterruption(); //APPEL OBLIGATOIRE
		Direction result = Direction.NONE;

		cost_calculator_updt();
		//Direction result = Direction.NONE;
		if(arrive_or_not()==false){
			check_on_path();


		if(path.isEmpty() || !checkPathValidity())
			path_init();
		
		AiTile tile = null;
		if(path.getLength()==2){
			tile=path.getTile(1);
		}
		else if(path.getLength()>2)
		{
			if(100000/(safe_map.returnMatrix()[path.getTile(1).getLine()][path.getTile(1).getCol()])>479)
				tile = path.getTile(1);
			
		}
		else if(path.getLength()==1)
			tile = path.getTile(0);
	
		if(tile!=null)
			result = zone.getDirection(zone.getOwnHero(),tile);

			
		}
		
		if(path.getLength()==0)
		{
		//	System.out.print(safe_map.returnMatrix()[our_bomberman.getLine()][our_bomberman.getCol()]);
			
			AiTile my_position=zone.getOwnHero().getTile();

			int k=1;
			//	System.out.print(k);
			int stop=0;
			List<Double> t=new ArrayList<Double>();
			List<AiTile> at=new ArrayList<AiTile>();
			
			//iteration on right of our position;
			while(stop==0&&my_position.getCol()+k<zone.getWidth()){
				if(safe_map.returnMatrix()[my_position.getLine()][my_position.getCol()+k]==safe_map.BOMB ||safe_map.returnMatrix()[my_position.getLine()][my_position.getCol()+k]==safe_map.DEST_WALL || safe_map.returnMatrix()[my_position.getLine()][my_position.getCol()+k]==safe_map.INDEST_WALL|| safe_map.returnMatrix()[my_position.getLine()][my_position.getCol()+k]==safe_map.FIRE)
					stop=1;
				else
				{
					double x3=safe_map.returnMatrix()[my_position.getLine()][my_position.getCol()+k];
					t.add(x3);
					at.add(zone.getTile(my_position.getLine(),my_position.getCol()+k));
					
				}
				k++;
			}
			//iteration on the left side of our position:
			k=1;
			stop=0;
			while(stop==0&&my_position.getCol()-k>0 ){
				if(safe_map.returnMatrix()[my_position.getLine()][my_position.getCol()-k]==safe_map.BOMB ||safe_map.returnMatrix()[my_position.getLine()][my_position.getCol()-k]==safe_map.DEST_WALL || safe_map.returnMatrix()[my_position.getLine()][my_position.getCol()-k]==safe_map.INDEST_WALL|| safe_map.returnMatrix()[my_position.getLine()][my_position.getCol()-k]==safe_map.FIRE)
					stop=1;
				else
				{
					double x2=safe_map.returnMatrix()[my_position.getLine()][my_position.getCol()-k];
					t.add(x2);
					at.add(zone.getTile(my_position.getLine(),my_position.getCol()-k));
				}
				k++;
			}
			//iteration on the down side of our position:
			k=1;
			stop=0;
			while(stop==0&&my_position.getLine()+k<zone.getHeigh()){
				if(safe_map.returnMatrix()[my_position.getLine()+k][my_position.getCol()]==safe_map.BOMB ||safe_map.returnMatrix()[my_position.getLine()+k][my_position.getCol()]==safe_map.DEST_WALL || safe_map.returnMatrix()[my_position.getLine()+k][my_position.getCol()]==safe_map.INDEST_WALL|| safe_map.returnMatrix()[my_position.getLine()+k][my_position.getCol()]==safe_map.FIRE)
					stop=1;
				else
				{
					double x= (safe_map.returnMatrix()[my_position.getLine()+k][my_position.getCol()]);
					t.add(x);
					at.add(zone.getTile(my_position.getLine()+k,my_position.getCol()));
				}
				k++;
			}
			//iteration on the up side of our position:
			
			k=1;
			stop=0;
			while(stop==0&&my_position.getLine()-k>0){
				if(safe_map.returnMatrix()[my_position.getLine()-k][my_position.getCol()]==safe_map.BOMB ||safe_map.returnMatrix()[my_position.getLine()-k][my_position.getCol()]==safe_map.DEST_WALL || safe_map.returnMatrix()[my_position.getLine()-k][my_position.getCol()]==safe_map.INDEST_WALL|| safe_map.returnMatrix()[my_position.getLine()-k][my_position.getCol()]==safe_map.FIRE)
				stop=1;
				else
				{
					double x4=safe_map.returnMatrix()[my_position.getLine()-k][my_position.getCol()];
						t.add(x4);
					at.add(zone.getTile(my_position.getLine()-k,my_position.getCol()));
				}
				k++;
			}
		
			t.add(safe_map.returnMatrix()[my_position.getLine()][my_position.getCol()]);
			at.add(zone.getTile(my_position.getLine(),my_position.getCol()));
			int m=0;
			int temp=0;
		
			
			if(t!=null)
			{
				double min=t.get(0);
				while(m<t.size())
				{
					if(t.get(m)<=min){
						min=t.get(m);
						temp=m;
						
				}
				
					m++;
					
				}
				
				AiTile tile_2=at.get(temp);
				//System.out.println(t.size());
			
				result = zone.getDirection(zone.getOwnHero(),tile_2);
				
		//	System.out.println(1);
			
			
			}

		
		}
		
		

		return result;
	}

	public boolean checkPathValidity() throws StopRequestException {
		ai.checkInterruption(); //APPEL OBLIGATOIRE
		
		boolean result = true;
		Iterator<AiTile> it = path.getTiles().iterator();
		while(it.hasNext() && result)
		{	ai.checkInterruption(); //APPEL OBLIGATOIRE
			
			AiTile tile = it.next();
			result = tile.isCrossableBy(zone.getOwnHero());			
		}
		return result;
	}


	public void check_on_path() throws StopRequestException {
		ai.checkInterruption(); //APPEL OBLIGATOIRE

		
		AiTile currentTile = ai.getPercepts().getOwnHero().getTile();
		while(!path.isEmpty() && path.getTile(0)!=currentTile)
		{	ai.checkInterruption(); //APPEL OBLIGATOIRE
			
			path.removeTile(0);
		}
		
		
		
	}


	public boolean arrive_or_not() throws StopRequestException {
		ai.checkInterruption(); //APPEL OBLIGATOIRE
		
		if(arrived==false)
		{	if(arrived_tile==null)
				arrived = true;
			else
			{	AiTile cur_Tile = ai.getPercepts().getOwnHero().getTile();
				arrived = (cur_Tile==arrived_tile);			
			}
		
		}
		
		

		return arrived;
		
	}


	public void cost_calculator_updt() throws StopRequestException {
		ai.checkInterruption(); //APPEL OBLIGATOIRE
		
		safe_map=new Safety_Map(zone);		
		double safetyMatrix1[][] = safe_map.returnMatrix();
		for(int line=0;line<zone.getHeigh();line++)
		{	ai.checkInterruption(); //APPEL OBLIGATOIRE
			
			for(int col=0;col<zone.getWidth();col++)
			{	ai.checkInterruption(); //APPEL OBLIGATOIRE
				
			double cost_1 = safetyMatrix1[line][col];
			costCalculator.setCost(line,col,cost_1);
			
			}
		}
		
	}


	public void path_init() throws StopRequestException {
		ai.checkInterruption(); //APPEL OBLIGATOIRE
		
		
		path = star.processShortestPath(ai.getPercepts().getOwnHero().getTile(),possibleDest);
		
		
		
		arrived_tile = path.getLastTile();
		
	}


	public List<AiTile> destinations_possibles(AiTile tile) throws StopRequestException {
		ai.checkInterruption(); //APPEL OBLIGATOIRE
		safe_map=new Safety_Map(zone);
		
		AiTile tile_dest;
		List<AiTile> result = new ArrayList<AiTile>();
		for(int pos_y=0;pos_y<zone.getHeigh();pos_y++)
		{	ai.checkInterruption(); //APPEL OBLIGATOIRE
			
			for(int pos_x=0;pos_x<zone.getWidth();pos_x++)
			{	ai.checkInterruption(); //APPEL OBLIGATOIRE
				
				
				if(safe_map.returnMatrix()[pos_y][pos_x]==safe_map.SAFE_CASE ||safe_map.returnMatrix()[pos_y][pos_x]==safe_map.BONUS ){
					tile_dest=zone.getTile(pos_y, pos_x);
					result.add(tile_dest);
				}
				
			}
		
			
		
		}
		
		return result;
		
	}

	
	
	
	
	
	
	
	private DemirciDuzokErgok ai;
	private AiZone zone;
	private Safety_Map safe_map;
	private AiTile arrived_tile;
	private List<AiTile> possibleDest;
	private boolean arrived;
	private AiPath path;
	
	
	private Astar star;
	private HeuristicCalculator heuristicCalculator;
	private MatrixCostCalculator costCalculator;
	
	
	
}
