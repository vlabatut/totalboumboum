package org.totalboumboum.ai.v200910.ais.demirciduzokergok.v5_2;
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

/**
 * Our attacking strategies is based on 3 strategies:
 * 
 * 1.If we are 3 cases near the enemy(3 cases on line or colon) we drop a bomb.Here we don't control
 * if the bomb is on near the walls or not. The reason for it is that:
 * if we already posed a bomb above and enemy and we approached near it and then posed another one.
 * By this way, we can take the enemy near the danger levels of the bombs which we posed early.

 * 2.If the path to the enemy is in danger level,we don't make any deplacements and wait the
 * enemy to get closer to us. By this way, when the enemy makes movement to us and when we 
 * escape,we can drive the enemy by posing bombs to the cases containing danger levels.
 * 
 * 3. If we are breaking walls and when the path is not safe to approach to the enemy,we complete
 * breaking the which is chosen before and then make the movement to the enemy. The purpose of this
 * action is to make the enemy to make movement to us and then by posing bombs taking it to dangerous
 * cases.
 * 
 * @author Mustafa Göktuğ Düzok
 * 
 */
@SuppressWarnings("deprecation")
public class Enemie_Manager {

	
	public Enemie_Manager(DemirciDuzokErgok ai) throws StopRequestException{
	ai.checkInterruption(); //APPEL OBLIGATOIRE
		
		this.ai = ai;
		zone = ai.getPercepts();
		safe_map=new Safety_Map(zone);
		esc=new Can_escape_Manager(ai);
		
		// init A*:
		double costMatrix[][] = new double[zone.getHeight()][zone.getWidth()];
		costCalculator_b = new MatrixCostCalculator(costMatrix);
		hcalcul_b = new BasicHeuristicCalculator();
		star_b = new Astar(ai,ai.getPercepts().getOwnHero(),costCalculator_b,hcalcul_b);
		
		// init destinations:
		arrived_b = false;
		AiHero our_bomberman = zone.getOwnHero();
		possibleDest_b=destinations_possibles_b(our_bomberman.getTile());
		updatePath_b();	
	}
	
	
	
	/**
	 * This method explains the direction to go if we didn't arrived the final case:
	 * Like in Escape Manger,here we control the security level of the neighbor cases too.
	 * 
	 */
	
	
	public Direction direcition_updt_b() throws StopRequestException {
		ai.checkInterruption(); //APPEL OBLIGATOIRE
		
		
		updateCostCalculator_b();

		Direction result = Direction.NONE;
		if(hasArrived_b()==false)
		{	
			checkIsOnPath_b();
		
			if(path_b.isEmpty() || !checkPathValidity_b() ){
				updatePath_b();
			}
			else{
			
			AiTile tile = null;
		
			
			if(path_b.getLength()>1){
			if(safe_map.returnMatrix()[path_b.getTile(1).getLine()][path_b.getTile(1).getCol()]<=0)
				tile = path_b.getTile(1);
		
			}
			else if(path_b.getLength()>0){
				tile = path_b.getTile(0);
			
			}
		
			if(tile!=null)
				result = zone.getDirection(zone.getOwnHero(),tile);			
				
			
			
			}
		}		
		return result;

		
		
		
		
	}
	
	/**
	 *  There may be obstacles like fire,walls or bombs on the path which we want to make the deplacement.
	 * This method verifies it.
	 * 
	 */
		
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


	/**
	 * Checks if we are on the path which we decided to make the movement.
	 * 
	 */
	
	public void checkIsOnPath_b() throws StopRequestException {

		ai.checkInterruption(); //APPEL OBLIGATOIRE

		
		AiTile currentTile =zone.getOwnHero().getTile();
		while(!path_b.isEmpty() && path_b.getTile(0)!=currentTile)
		{	ai.checkInterruption(); //APPEL OBLIGATOIRE
			
			path_b.removeTile(0);
		}
	}

	
/**
 * Verifies if we arrived the destination case or not.(Here if we are 3 cases near the enemy,we 
 * suppose that we arrived because of the attacking strategie explained above.)
 */

	public boolean hasArrived_b() throws StopRequestException {

		ai.checkInterruption(); //APPEL OBLIGATOIRE
		int x,y;
		safe_map=new Safety_Map(zone);
		if(arrived_tile_b!=null){
			x=arrived_tile_b.getCol()-zone.getOwnHero().getCol();
			y=arrived_tile_b.getLine()-zone.getOwnHero().getLine();
			if(x<0)
				x=-x;
			if(y<0)
				y=-y;
			if(x==0){
				if(y<=3){
					arrived_b=true;
				}
		
			}
			else if(y==0){
				if(x<=3){
					arrived_b=true;
				}
	
			}
			
		}
		else if(safe_map.returnMatrix()[zone.getOwnHero().getLine()][zone.getOwnHero().getCol()]==safe_map.ENEMIE)
			arrived_b=true;
		
		
		return arrived_b;
		
		}
		
	/**
	 * This method verifies that if there are any danger levels on the path which we will make a 
	 * movement to approch the enemie(like different danger levels,bombs and fire). If yes, we stop to make the movement.
	 * @return
	 */
	public boolean canPass(){
		int m=0;
		safe_map=new Safety_Map(zone);
		int stop=1;
		while(m<path_b.getLength() && path_b.isEmpty()==false && stop==1){
			if(safe_map.returnMatrix()[path_b.getTile(m).getLine()][path_b.getTile(m).getCol()]!=safe_map.SAFE_CASE &&safe_map.returnMatrix()[path_b.getTile(m).getLine()][path_b.getTile(m).getCol()]!=safe_map.ENEMIE){
				stop=0;
			}
		m++;
		}
		if(stop==0)
			return false;
		else
			return true;
		
	}
	
	
	/**
	 * Returns the lenght of the path.
	 * 
	 */
	public int getPathLength(){
		return path_b.getLength();		
		
	}
	
	
	/**
	 * Method for calculating the cost
	 *
	 */
	
	public void updateCostCalculator_b() throws StopRequestException{

		ai.checkInterruption(); //APPEL OBLIGATOIRE
		
		
		double safetyMatrix_b[][] = safe_map.returnMatrix();
		for(int line=0;line<zone.getHeight();line++)
		{	ai.checkInterruption(); //APPEL OBLIGATOIRE
			
			for(int col=0;col<zone.getWidth();col++)
			{	ai.checkInterruption(); //APPEL OBLIGATOIRE
				
			double cost_b = safetyMatrix_b[line][col];
			costCalculator_b.setCost(line, col, cost_b);
			
			}
		}
		

		
	
		
		
	}	
	
	
	
	/**
	 * 
	 * This method takes the path to make the movement using the astar algorithme.
	 */
	
	public void updatePath_b() throws StopRequestException {


		ai.checkInterruption(); //APPEL OBLIGATOIRE
		
		
		path_b = star_b.processShortestPath(zone.getOwnHero().getTile(),possibleDest_b);
		arrived_tile_b = path_b.getLastTile();
	
		

		

		
	}
	
	/**
	 * This method verifies if we can directly go to the enemie or not:
	 *
	 */
	public boolean accessiblePath(){
	
		safe_map=new Safety_Map(zone);
		if(safe_map.returnMatrix()[zone.getOwnHero().getLine()][zone.getOwnHero().getCol()]==safe_map.ENEMIE || path_b.isEmpty()==false)
			return true;
		else
			return false;
	}
	
	/**
	 * This method verifies that if we have enough space to escape if we place a bomb.
	 */
	
	
	public boolean canesc() throws StopRequestException{
		boolean res=false;
		esc=new Can_escape_Manager(ai);
		//We choose the numbers here as 3 and 7 because the normal explosion of the bomb is 6 cases of movement and when we are stucked by both
		//sides of walls,we need at least 3 safe cases to escape.
		if(esc.getPathLength()<3 || esc.getPathLength()>7){
		
			res=false;
		}
		else
			res=true;
		return res;
	}
	
	
	/**
	 * The possible destinations to make the deplacement(Gets the positions of the enemies and puts
	 * them into the list)
	 */
	
	public List<AiTile> destinations_possibles_b(AiTile tile) throws StopRequestException {


		ai.checkInterruption(); //APPEL OBLIGATOIRE
		//safe_map=new Safety_Map(zone);
		
		AiTile tile_dest_b;
		List<AiTile> result_b = new ArrayList<AiTile>();
				
		for(int pos_y=0;pos_y<zone.getHeight();pos_y++)
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
	private Can_escape_Manager esc;
	private AiTile arrived_tile_b;
	private List<AiTile> possibleDest_b;
	private boolean arrived_b;
	private AiPath path_b;
	
	private Astar star_b;
	private HeuristicCalculator hcalcul_b;
	private MatrixCostCalculator costCalculator_b;
	
	
	
	
	
	
	
	
	
}

