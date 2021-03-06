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
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * This class is for calculating if we can find a secured place if we pose a bomb.
 * 
 * @author Mustafa Göktuğ Düzok
 * 
 */
@SuppressWarnings("deprecation")
public class CanEscape {
	/** */
	@SuppressWarnings("unused")
	private AiZone ourZone;
	/** */
	private AiHero ourHero;
	/** */
	@SuppressWarnings("unused")
	private Collection<AiHero> enemies;
	
	/** */
	private Collection<AiBlock> blocks;

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
	
	/** */
	public  double BONUS=-300;
	/** */
	public  double WALL=-10;
	/** */
	public double SAFE_CASE=-1000;
	/** */
	public  double FIRE= 1000000;
	/** */
	public  double BOMB=500000;
	/** */
	public double ENEMIE=0;
	

	/** */
	public double security_matrix[][];
	/** */
	ArtificialIntelligence ai;
	
	/** Constructeur of the Can_escape
	 * 
	 * @param zone
	 * 		Description manquante !
	 * @param ai
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public CanEscape(AiZone zone, ArtificialIntelligence ai) throws StopRequestException{
		ai.checkInterruption();
		this.ai = ai;
		this.ourZone=zone;
		this.blocks=zone.getBlocks();

		this.fires=zone.getFires();
		this.bombs=zone.getBombs();
		//Notre bomberman est incluce aussi:
		this.enemies=zone.getHeroes();
		
		this.ourHero=zone.getOwnHero();
		
		this.width=zone.getWidth();
		this.height=zone.getHeight();
		/*Avec la fonction Fill_The_Matrix,on remplir la matrice */
		Fill_The_Matrix();
	}

	/**
	 * 
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public void Fill_The_Matrix() throws StopRequestException {
		ai.checkInterruption();
		//First of all, we fill the matrce with safe cases.
		int x,y;
		security_matrix=new double[height][width];
		for(y=0;y<height;y++){
			ai.checkInterruption();
			for(x=0;x<width;x++){
				ai.checkInterruption();
				security_matrix[y][x]=SAFE_CASE;
			}
		}
	
		/*Placing the walls */
		Iterator<AiBlock> block_iterator=blocks.iterator();
		AiBlock blck;
		while(block_iterator.hasNext()==true){
			ai.checkInterruption();
			blck=block_iterator.next();
			posX=blck.getCol();
			posY=blck.getLine();

			
				security_matrix[posY][posX]=WALL;
		}
	
	
				
		
			/*Placing fire*/
		Iterator<AiFire> fire_iterator=fires.iterator();
		AiFire fr;
		while(fire_iterator.hasNext()){
			ai.checkInterruption();
			fr=fire_iterator.next();
			posX=fr.getCol();
			posY=fr.getLine();
			security_matrix[posY][posX]=FIRE;
		}
		
		/*Placing the different danger levels according the time left before explosion
		 * More time left is much secure and it is exprimed in smaller numbers than the bombs
		 * which has less time left before its explosion */
		
		
		double time_left;
		double danger_level=-2.2;	
		AiTile Block_Tile;
		List<AiBlock> blocks_up;
		List<AiBlock> blocks_down;
		List<AiBlock> blocks_right;
		List<AiBlock> blocks_left;
		Iterator<AiBomb> iterate_bombs=bombs.iterator();
		AiBomb bmb;
		
		
		
		while(iterate_bombs.hasNext()==true){
			ai.checkInterruption();
			bmb=iterate_bombs.next();
			
			posX=bmb.getCol();
			posY=bmb.getLine();
			//calculating the time left before the explosion of the bombs:
			
				
			if(bmb.getNormalDuration()-bmb.getTime()>0){		
			time_left=bmb.getNormalDuration()-bmb.getTime();
	
			danger_level=(1/(time_left))*100000;
			}
			//Bomb should explore if it is working
			else if(bmb.getNormalDuration()-bmb.getTime()==0)
				time_left=1000000;
		//If it is not working, we make count down from 1500 ms.
			else {		
				time_left=1500+(bmb.getNormalDuration()-bmb.getTime());
				if(time_left<0)
					time_left=800;
				danger_level=(1/(time_left))*100000;
					
			}	
					
					
			
		//Here we place the danger levels according the calculations made:		
				
			Block_Tile=bmb.getTile();
			
			blocks_right =Block_Tile.getNeighbor(Direction.RIGHT).getBlocks();
			blocks_left = Block_Tile.getNeighbor(Direction.LEFT).getBlocks();
			blocks_down = Block_Tile.getNeighbor(Direction.DOWN).getBlocks();
			blocks_up = Block_Tile.getNeighbor(Direction.UP).getBlocks();
			
			if(blocks_right.isEmpty()==true&&danger_level>0){
				for(int k=1;k<=bmb.getRange();k++){
					ai.checkInterruption();
					if( (posX+k<width) && ((security_matrix[posY][posX+k]==SAFE_CASE))){
						security_matrix[posY][posX+k]=danger_level;
					}
					else{
						if( (posX+k<width) && (security_matrix[posY][posX+k]==FIRE))
							security_matrix[posY][posX+k]=FIRE;
						else if(posX+k<width && security_matrix[posY][posX+k]==BOMB)
							security_matrix[posY][posX+k]=BOMB;
						else{
							if((posX+k<width) && security_matrix[posY][posX+k]< danger_level)
								security_matrix[posY][posX+k]=danger_level;
						}		
					}	
				}
			}
			
			if(blocks_left.isEmpty()==true&&danger_level>0){
				for(int k=1;k<=bmb.getRange();k++){
					ai.checkInterruption();
					if( (posX-k>0) && ((security_matrix[posY][posX-k]==SAFE_CASE)||(security_matrix[posY][posX-k]==BONUS))){
						security_matrix[posY][posX-k]=danger_level;
					}
					else{
						if( (posX-k>0) && (security_matrix[posY][posX-k]==FIRE))
							security_matrix[posY][posX-k]=FIRE;
						else if(posX-k>0 && security_matrix[posY][posX-k]==BOMB)
							security_matrix[posY][posX-k]=BOMB;
						else{
							if((posX-k>0) && security_matrix[posY][posX-k]< danger_level)
								security_matrix[posY][posX-k]=danger_level;
						}		
					}	
				}
			}
			
			
			if(blocks_down.isEmpty()==true&&danger_level>0){
				for(int k=1;k<=bmb.getRange();k++){
					ai.checkInterruption();
					if( (posY+k<height) && ((security_matrix[posY+k][posX]==SAFE_CASE))){
						security_matrix[posY+k][posX]=danger_level;
					}
					else{
						if( (posY+k<height) && (security_matrix[posY+k][posX]==FIRE))
							security_matrix[posY+k][posX]=FIRE;
						else if(posY+k<height && security_matrix[posY+k][posX]==BOMB)
							security_matrix[posY+k][posX]=BOMB;
						else{
							if((posY+k<height) && security_matrix[posY+k][posX]< danger_level)
								security_matrix[posY+k][posX]=danger_level;
						}		
					}	
				}
			}
			
			if(blocks_up.isEmpty()==true&&danger_level>0){
				for(int k=1;k<=bmb.getRange();k++){
					ai.checkInterruption();
					if( (posY-k>0) && ((security_matrix[posY-k][posX]==SAFE_CASE))){
						security_matrix[posY-k][posX]=danger_level;
					}
					else{
						if( (posY-k>0) && (security_matrix[posY-k][posX]==FIRE))
							security_matrix[posY-k][posX]=FIRE;
						else if(posY-k>0 && security_matrix[posY-k][posX]==BOMB)
							security_matrix[posY-k][posX]=BOMB;
						else{
							if((posY-k>0) && security_matrix[posY-k][posX]< danger_level)
								security_matrix[posY-k][posX]=danger_level;
						}		
					}	
				}
			}
		}
	

		
		//Placing the bombs
		
		Iterator<AiBomb> it_bmb = bombs.iterator();
		AiBomb bm;

		while (it_bmb.hasNext()) {
			ai.checkInterruption();
			bm = it_bmb.next();
			posX = bm.getCol();
			posY = bm.getLine();
			if(security_matrix[posY][posX]!= FIRE)
			security_matrix[posY][posX] = BOMB;	
	}
	
		int i=0;
		
		//Here we suppose that our bomberman is a bomb and we get the range of the bomb
		//We calculate the possible places which will be affected when the bomb is posed
		//and in the Can_escape_Manager we will find the safe cases to escape(if they exist)
		//according to this part of this class.
		
		while(ourHero.getBombRange()>i){
			ai.checkInterruption();
			posY=ourHero.getLine();
			posX=ourHero.getCol();
			if(i==0){
				security_matrix[posY][posX]=BOMB;
			
			}
			else{
			if(posX+i<width)
			{
				if(security_matrix[posY][posX+i]!=WALL)
					security_matrix[posY][posX+i]=FIRE;
			}
			if(posX-i>0)
			{
				if(security_matrix[posY][posX-i]!=WALL)
					security_matrix[posY][posX-i]=FIRE;
			}
			if(posY-i>0)
			{
				if(security_matrix[posY-i][posX]!=WALL)
					security_matrix[posY-i][posX]=FIRE;
			}
			if(posY+i<height)
			{
				if(security_matrix[posY+i][posX]!=WALL)
					security_matrix[posY+i][posX]=FIRE;
			}
			}
			i++;
			
			
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
		return security_matrix;
	}
		
}
