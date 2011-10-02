package org.totalboumboum.ai.v200910.ais.demirciduzokergok.v5_2c;
import java.util.ArrayList;

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

/**
 * 
 * @author Osman Demirci
 * @author Mustafa Göktuğ Düzok
 * @author Hatice Esra Ergök
 * 
 */
@SuppressWarnings("deprecation")
public class Can_escape_Manager {

	
	public Can_escape_Manager(DemirciDuzokErgok ai) throws StopRequestException{
	ai.checkInterruption(); //APPEL OBLIGATOIRE
		
		this.ai = ai;
		zone = ai.getPercepts();
		//safe_map=new Safety_Map(zone);
		
		// init A*
		double costMatrix[][] = new double[zone.getHeight()][zone.getWidth()];
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
	
	


	
	public int getPathLength() throws StopRequestException{
		ai.checkInterruption();
		return path_b.getLength();		
		
	}
	

	
	
	
	public void updatePath_b() throws StopRequestException {


		ai.checkInterruption(); //APPEL OBLIGATOIRE
		
		
		path_b = star_b.processShortestPath(ai.getPercepts().getOwnHero().getTile(),possibleDest_b);
		//System.out.println(path_b.getLength());
		arrived_tile_b = path_b.getLastTile();
		
		

		
	}

	
	public List<AiTile> destinations_possibles_b(AiTile tile) throws StopRequestException {


		ai.checkInterruption(); //APPEL OBLIGATOIRE
		safe_map=new Can_escape(zone,ai);
		AiTile tile_dest_b;
		List<AiTile> result_b = new ArrayList<AiTile>();
		
		for(int pos_y=0;pos_y<zone.getHeight();pos_y++)
		{	ai.checkInterruption(); //APPEL OBLIGATOIRE
			
			for(int pos_x=0;pos_x<zone.getWidth();pos_x++)
			{	ai.checkInterruption(); //APPEL OBLIGATOIRE
				
				
				if(safe_map.returnMatrix()[pos_y][pos_x]==safe_map.SAFE_CASE|| safe_map.returnMatrix()[pos_y][pos_x]==safe_map.BONUS){
					tile_dest_b=zone.getTile(pos_y, pos_x);
					result_b.add(tile_dest_b);
				}
				
		
			}
		
			
		
		}
				
		return result_b;
		
	}

	private DemirciDuzokErgok ai;
	private AiZone zone;
	private Can_escape safe_map;
	@SuppressWarnings("unused")
	private AiTile arrived_tile_b;
	private List<AiTile> possibleDest_b;
	@SuppressWarnings("unused")
	private boolean arrived_b;
	private AiPath path_b;
	
	private Astar star_b;
	private HeuristicCalculator hcalcul_b;
	private MatrixCostCalculator costCalculator_b;
	
	
	
	
	
	
	
	
	
}

