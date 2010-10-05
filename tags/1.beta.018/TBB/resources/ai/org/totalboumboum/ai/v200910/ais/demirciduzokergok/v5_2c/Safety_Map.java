
package org.totalboumboum.ai.v200910.ais.demirciduzokergok.v5_2c;



import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v200910.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiBlock;
import org.totalboumboum.ai.v200910.adapter.data.AiBomb;
import org.totalboumboum.ai.v200910.adapter.data.AiFire;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiItem;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;






/**
 * This class is for expriming the zone of the game with a matrix placing different danger
 * levels to each case.
 * 
 * @author MGoktug
 *
 */

public class Safety_Map {
	private AiZone our_zone;
	@SuppressWarnings("unused")
	private AiHero our_hero;
	private Collection<AiHero> enemies;
	
	private Collection<AiBlock> blocks;
	private Collection<AiItem> bonus;
	private Collection<AiFire> fires;
	private Collection<AiBomb> bombs;
	
	public int width;
	public int height;
	private int pos_x;
	private int pos_y;
	//Different exprimes for the cases possible. The more secured places will be
	//exprimed with negative numbers and the dangerous cases will be exprimed with positive ones.
	//enemies will be exprimed with zero.
	public  double BONUS=-300;
	public  double DEST_WALL=-50;
	public  double INDEST_WALL=-10;
	public double SAFE_CASE=-1000;
	public  double FIRE= 1000000;
	public  double BOMB=500000;
	public double ENEMIE=0;
	

	//matrix stocking the danger levels:
	public double security_matrix[][];
	ArtificialIntelligence ai;
	
	/** Constructer of the class Safety_Map
	 * @throws StopRequestException */
	public Safety_Map(AiZone zone, ArtificialIntelligence ai) throws StopRequestException{
		ai.checkInterruption();
		this.ai = ai;
		this.our_zone=zone;
		this.blocks=zone.getBlocks();
		this.bonus=zone.getItems();
		this.fires=zone.getFires();
		this.bombs=zone.getBombs();
		//All the heros in the zone:
		this.enemies=zone.getRemainingHeroes();
		
		this.our_hero=zone.getOwnHero();
		
		this.width=zone.getWidth();
		this.height=zone.getHeigh();
		//Method for Filling the matrix:
		Fill_The_Matrix();
	}

	public void Fill_The_Matrix() throws StopRequestException {
		ai.checkInterruption();
		//firstly we place all the matrice with SAFE_CASE:
		int x,y;
		security_matrix=new double[height][width];
		for(y=0;y<height;y++){
			ai.checkInterruption();
			for(x=0;x<width;x++){
				ai.checkInterruption();
				security_matrix[y][x]=SAFE_CASE;
			}
		}
	
		//placing the walls:
		Iterator<AiBlock> block_iterator=blocks.iterator();
		AiBlock blck;
		while(block_iterator.hasNext()==true){
			ai.checkInterruption();
			blck=block_iterator.next();
			pos_x=blck.getCol();
			pos_y=blck.getLine();
			if(blck.isDestructible())
				security_matrix[pos_y][pos_x]=DEST_WALL;
			else
				security_matrix[pos_y][pos_x]=INDEST_WALL;
		}
	
	
		
		//placing the bonus:
		Iterator<AiItem> bonus_iterator = bonus.iterator();
		AiItem item_bonus;

		while (bonus_iterator.hasNext()) {
			ai.checkInterruption();
			item_bonus = bonus_iterator.next();

			pos_x = item_bonus.getCol();
			pos_y = item_bonus.getLine();

			security_matrix[pos_y][pos_x]=BONUS;

		}		
					
		
		//placing the fire:
		Iterator<AiFire> fire_iterator=fires.iterator();
		AiFire fr;
		while(fire_iterator.hasNext()){
			ai.checkInterruption();
			fr=fire_iterator.next();
			pos_x=fr.getCol();
			pos_y=fr.getLine();
			security_matrix[pos_y][pos_x]=FIRE;
		}
	
		
		
		double time_left;
		double danger_level=-2.2;	
		AiTile Block_Tile;
		List<AiBlock> blocks_up;
		List<AiBlock> blocks_down;
		List<AiBlock> blocks_right;
		List<AiBlock> blocks_left;
		Iterator<AiBomb> iterate_bombs=bombs.iterator();
		AiBomb bmb;
		
		
		
		
		//placing the different danger levels:
		
		while(iterate_bombs.hasNext()==true){
			ai.checkInterruption();
			bmb=iterate_bombs.next();
			
			pos_x=bmb.getCol();
			pos_y=bmb.getLine();
			//calculating the time left before the explosion of the bomb and placing this result(
			// we want to express the less time left with a bigger level so we used:(1/timeleft)*100000)
			
				
			if(bmb.getNormalDuration()-bmb.getTime()>0){		
			time_left=bmb.getNormalDuration()-bmb.getTime();
	
			danger_level=(1/(time_left))*100000;
			}
			//bomb should explore if it is working:
			else if(bmb.getNormalDuration()-bmb.getTime()==0)
				time_left=1000000;
			
			//if not, we are making a countdown and calculating a danger level for it:
			else {		
				time_left=1500+(bmb.getNormalDuration()-bmb.getTime());
				if(time_left<0)
					time_left=800;
				danger_level=(1/(time_left))*100000;
					
			}	
					
					
			
				//placing the danger levels to the matrix:
				
			Block_Tile=bmb.getTile();
			
			blocks_right =Block_Tile.getNeighbor(Direction.RIGHT).getBlocks();
			blocks_left = Block_Tile.getNeighbor(Direction.LEFT).getBlocks();
			blocks_down = Block_Tile.getNeighbor(Direction.DOWN).getBlocks();
			blocks_up = Block_Tile.getNeighbor(Direction.UP).getBlocks();
			
			if(blocks_right.isEmpty()==true&&danger_level>0){
				for(int k=1;k<=bmb.getRange();k++){
					ai.checkInterruption();
					if( (pos_x+k<width) && ((security_matrix[pos_y][pos_x+k]==SAFE_CASE))){
						security_matrix[pos_y][pos_x+k]=danger_level;
					}
					else{
						if( (pos_x+k<width) && (security_matrix[pos_y][pos_x+k]==FIRE))
							security_matrix[pos_y][pos_x+k]=FIRE;
						else if(pos_x+k<width && security_matrix[pos_y][pos_x+k]==BOMB)
							security_matrix[pos_y][pos_x+k]=BOMB;
						else{
							if((pos_x+k<width) && security_matrix[pos_y][pos_x+k]< danger_level)
								security_matrix[pos_y][pos_x+k]=danger_level;
						}		
					}	
				}
			}
			
			if(blocks_left.isEmpty()==true&&danger_level>0){
				for(int k=1;k<=bmb.getRange();k++){
					ai.checkInterruption();
					if( (pos_x-k>0) && ((security_matrix[pos_y][pos_x-k]==SAFE_CASE)||(security_matrix[pos_y][pos_x-k]==BONUS))){
						security_matrix[pos_y][pos_x-k]=danger_level;
					}
					else{
						if( (pos_x-k>0) && (security_matrix[pos_y][pos_x-k]==FIRE))
							security_matrix[pos_y][pos_x-k]=FIRE;
						else if(pos_x-k>0 && security_matrix[pos_y][pos_x-k]==BOMB)
							security_matrix[pos_y][pos_x-k]=BOMB;
						else{
							if((pos_x-k>0) && security_matrix[pos_y][pos_x-k]< danger_level)
								security_matrix[pos_y][pos_x-k]=danger_level;
						}		
					}	
				}
			}
			
			
			if(blocks_down.isEmpty()==true&&danger_level>0){
				for(int k=1;k<=bmb.getRange();k++){
					ai.checkInterruption();
					if( (pos_y+k<height) && ((security_matrix[pos_y+k][pos_x]==SAFE_CASE))){
						security_matrix[pos_y+k][pos_x]=danger_level;
					}
					else{
						if( (pos_y+k<height) && (security_matrix[pos_y+k][pos_x]==FIRE))
							security_matrix[pos_y+k][pos_x]=FIRE;
						else if(pos_y+k<height && security_matrix[pos_y+k][pos_x]==BOMB)
							security_matrix[pos_y+k][pos_x]=BOMB;
						else{
							if((pos_y+k<height) && security_matrix[pos_y+k][pos_x]< danger_level)
								security_matrix[pos_y+k][pos_x]=danger_level;
						}		
					}	
				}
			}
			
			if(blocks_up.isEmpty()==true&&danger_level>0){
				for(int k=1;k<=bmb.getRange();k++){
					ai.checkInterruption();
					if( (pos_y-k>0) && ((security_matrix[pos_y-k][pos_x]==SAFE_CASE))){
						security_matrix[pos_y-k][pos_x]=danger_level;
					}
					else{
						if( (pos_y-k>0) && (security_matrix[pos_y-k][pos_x]==FIRE))
							security_matrix[pos_y-k][pos_x]=FIRE;
						else if(pos_y-k>0 && security_matrix[pos_y-k][pos_x]==BOMB)
							security_matrix[pos_y-k][pos_x]=BOMB;
						else{
							if((pos_y-k>0) && security_matrix[pos_y-k][pos_x]< danger_level)
								security_matrix[pos_y-k][pos_x]=danger_level;
						}		
					}	
				}
			}
		}
	
		//placing the bombs:
		
		Iterator<AiBomb> it_bmb = bombs.iterator();
		AiBomb bm;

		while (it_bmb.hasNext()) {
			ai.checkInterruption();
			bm = it_bmb.next();
			pos_x = bm.getCol();
			pos_y = bm.getLine();
			if(security_matrix[pos_y][pos_x]!= FIRE)
			security_matrix[pos_y][pos_x] = BOMB;	
	}
	
		
		//placing the heros
		Iterator<AiHero> it_hero=enemies.iterator();
		AiHero hr;
		while(it_hero.hasNext()){
			ai.checkInterruption();
			hr=it_hero.next();
			pos_x=hr.getCol();
			pos_y=hr.getLine();
			if(hr!=our_zone.getOwnHero()){
				if(security_matrix[pos_y][pos_x] <=0)
					security_matrix[pos_y][pos_x]=ENEMIE;
				
			}
		}
		
		
	
	}
	

	
	public double[][] returnMatrix() throws StopRequestException {
		ai.checkInterruption();
		return security_matrix;
	}
		
}
