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
 * @author Mustafa Göktuğ Düzok
 *
 */
@SuppressWarnings("deprecation")
public class SafetyMap {
	/** */
	private AiZone ourZone;
	/** */
	@SuppressWarnings("unused")
	private AiHero ourHero;
	/** */
	private Collection<AiHero> enemies;
	
	/** */
	private Collection<AiBlock> blocks;
	/** */
	private Collection<AiItem> bonus;
	/** */
	private Collection<AiFire> fires;
	/** */
	private Collection<AiBomb> bombs;
	
	/** */
	public int width;
	/** */
	public int height;
	/** */
	private int posX;
	/** */
	private int posY;
	//Different exprimes for the cases possible. The more secured places will be
	//exprimed with negative numbers and the dangerous cases will be exprimed with positive ones.
	//enemies will be exprimed with zero.
	/** */
	public  double BONUS=-300;
	/** */
	public  double DEST_WALL=-50;
	/** */
	public  double INDEST_WALL=-10;
	/** */
	public double SAFE_CASE=-1000;
	/** */
	public  double FIRE= 1000000;
	/** */
	public  double BOMB=500000;
	/** */
	public double ENEMIE=0;
	

	//matrix stocking the danger levels:
	/** */
	public double securityMatrix[][];
	/** */
	ArtificialIntelligence ai;
	
	/** 
	 * Constructer of the class Safety_Map
	 * @param zone 
	 * 		Description manquante !
	 * @param ai 
	 * 		Description manquante !
	 * @throws StopRequestException 
	 * 		Description manquante !
	 */
	public SafetyMap(AiZone zone, ArtificialIntelligence ai) throws StopRequestException{
		ai.checkInterruption();
		this.ai = ai;
		this.ourZone=zone;
		this.blocks=zone.getBlocks();
		this.bonus=zone.getItems();
		this.fires=zone.getFires();
		this.bombs=zone.getBombs();
		//All the heros in the zone:
		this.enemies=zone.getRemainingHeroes();
		
		this.ourHero=zone.getOwnHero();
		
		this.width=zone.getWidth();
		this.height=zone.getHeight();
		//Method for Filling the matrix:
		Fill_The_Matrix();
	}

	/**
	 * 
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public void Fill_The_Matrix() throws StopRequestException {
		ai.checkInterruption();
		//firstly we place all the matrice with SAFE_CASE:
		int x,y;
		securityMatrix=new double[height][width];
		for(y=0;y<height;y++){
			ai.checkInterruption();
			for(x=0;x<width;x++){
				ai.checkInterruption();
				securityMatrix[y][x]=SAFE_CASE;
			}
		}
	
		//placing the walls:
		Iterator<AiBlock> block_iterator=blocks.iterator();
		AiBlock blck;
		while(block_iterator.hasNext()==true){
			ai.checkInterruption();
			blck=block_iterator.next();
			posX=blck.getCol();
			posY=blck.getLine();
			if(blck.isDestructible())
				securityMatrix[posY][posX]=DEST_WALL;
			else
				securityMatrix[posY][posX]=INDEST_WALL;
		}
	
	
		
		//placing the bonus:
		Iterator<AiItem> bonus_iterator = bonus.iterator();
		AiItem item_bonus;

		while (bonus_iterator.hasNext()) {
			ai.checkInterruption();
			item_bonus = bonus_iterator.next();

			posX = item_bonus.getCol();
			posY = item_bonus.getLine();

			securityMatrix[posY][posX]=BONUS;

		}		
					
		
		//placing the fire:
		Iterator<AiFire> fire_iterator=fires.iterator();
		AiFire fr;
		while(fire_iterator.hasNext()){
			ai.checkInterruption();
			fr=fire_iterator.next();
			posX=fr.getCol();
			posY=fr.getLine();
			securityMatrix[posY][posX]=FIRE;
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
			
			posX=bmb.getCol();
			posY=bmb.getLine();
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
					if( (posX+k<width) && ((securityMatrix[posY][posX+k]==SAFE_CASE))){
						securityMatrix[posY][posX+k]=danger_level;
					}
					else{
						if( (posX+k<width) && (securityMatrix[posY][posX+k]==FIRE))
							securityMatrix[posY][posX+k]=FIRE;
						else if(posX+k<width && securityMatrix[posY][posX+k]==BOMB)
							securityMatrix[posY][posX+k]=BOMB;
						else{
							if((posX+k<width) && securityMatrix[posY][posX+k]< danger_level)
								securityMatrix[posY][posX+k]=danger_level;
						}		
					}	
				}
			}
			
			if(blocks_left.isEmpty()==true&&danger_level>0){
				for(int k=1;k<=bmb.getRange();k++){
					ai.checkInterruption();
					if( (posX-k>0) && ((securityMatrix[posY][posX-k]==SAFE_CASE)||(securityMatrix[posY][posX-k]==BONUS))){
						securityMatrix[posY][posX-k]=danger_level;
					}
					else{
						if( (posX-k>0) && (securityMatrix[posY][posX-k]==FIRE))
							securityMatrix[posY][posX-k]=FIRE;
						else if(posX-k>0 && securityMatrix[posY][posX-k]==BOMB)
							securityMatrix[posY][posX-k]=BOMB;
						else{
							if((posX-k>0) && securityMatrix[posY][posX-k]< danger_level)
								securityMatrix[posY][posX-k]=danger_level;
						}		
					}	
				}
			}
			
			
			if(blocks_down.isEmpty()==true&&danger_level>0){
				for(int k=1;k<=bmb.getRange();k++){
					ai.checkInterruption();
					if( (posY+k<height) && ((securityMatrix[posY+k][posX]==SAFE_CASE))){
						securityMatrix[posY+k][posX]=danger_level;
					}
					else{
						if( (posY+k<height) && (securityMatrix[posY+k][posX]==FIRE))
							securityMatrix[posY+k][posX]=FIRE;
						else if(posY+k<height && securityMatrix[posY+k][posX]==BOMB)
							securityMatrix[posY+k][posX]=BOMB;
						else{
							if((posY+k<height) && securityMatrix[posY+k][posX]< danger_level)
								securityMatrix[posY+k][posX]=danger_level;
						}		
					}	
				}
			}
			
			if(blocks_up.isEmpty()==true&&danger_level>0){
				for(int k=1;k<=bmb.getRange();k++){
					ai.checkInterruption();
					if( (posY-k>0) && ((securityMatrix[posY-k][posX]==SAFE_CASE))){
						securityMatrix[posY-k][posX]=danger_level;
					}
					else{
						if( (posY-k>0) && (securityMatrix[posY-k][posX]==FIRE))
							securityMatrix[posY-k][posX]=FIRE;
						else if(posY-k>0 && securityMatrix[posY-k][posX]==BOMB)
							securityMatrix[posY-k][posX]=BOMB;
						else{
							if((posY-k>0) && securityMatrix[posY-k][posX]< danger_level)
								securityMatrix[posY-k][posX]=danger_level;
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
			posX = bm.getCol();
			posY = bm.getLine();
			if(securityMatrix[posY][posX]!= FIRE)
			securityMatrix[posY][posX] = BOMB;	
	}
	
		
		//placing the heros
		Iterator<AiHero> it_hero=enemies.iterator();
		AiHero hr;
		while(it_hero.hasNext()){
			ai.checkInterruption();
			hr=it_hero.next();
			posX=hr.getCol();
			posY=hr.getLine();
			if(hr!=ourZone.getOwnHero()){
				if(securityMatrix[posY][posX] <=0)
					securityMatrix[posY][posX]=ENEMIE;
				
			}
		}
		
		
	
	}
	

	/**
	 * 
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public double[][] returnMatrix() throws StopRequestException {
		ai.checkInterruption();
		return securityMatrix;
	}
		
}
