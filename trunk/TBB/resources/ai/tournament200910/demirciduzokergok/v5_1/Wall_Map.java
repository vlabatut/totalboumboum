
package tournament200910.demirciduzokergok.v5_1;



import java.util.Collection;
import java.util.Iterator;
import java.util.List;




import fr.free.totalboumboum.ai.adapter200910.data.AiBlock;
import fr.free.totalboumboum.ai.adapter200910.data.AiBomb;
import fr.free.totalboumboum.ai.adapter200910.data.AiFire;
import fr.free.totalboumboum.ai.adapter200910.data.AiHero;
import fr.free.totalboumboum.ai.adapter200910.data.AiItem;
import fr.free.totalboumboum.ai.adapter200910.data.AiTile;
import fr.free.totalboumboum.ai.adapter200910.data.AiZone;
import fr.free.totalboumboum.engine.content.feature.Direction;

public class Wall_Map {
	@SuppressWarnings("unused")
	private AiZone our_zone;
	@SuppressWarnings("unused")
	private AiHero our_hero;
	@SuppressWarnings("unused")
	private Collection<AiHero> enemies;
	
	private Collection<AiBlock> blocks;
	private Collection<AiItem> bonus;
	private Collection<AiFire> fires;
	private Collection<AiBomb> bombs;
	
	public int width;
	public int height;
	private int pos_x;
	private int pos_y;
	
	public  double BONUS=-30;
	public  double DEST_WALL=-100;
	public  double INDEST_WALL=-200;
	public double SAFE_CASE=-1000;
	public  double FIRE= 1000000;
	public  double BOMB=500000;

	

	//la matrice qui stocke les niveaux de danger:
	public double security_matrix[][];
	
	/** Constructeur de classe Safety_Map*/
	public Wall_Map(AiZone zone){
		this.our_zone=zone;
		this.blocks=zone.getBlocks();
		this.bonus=zone.getItems();
		this.fires=zone.getFires();
		this.bombs=zone.getBombs();
		//Notre bomberman est incluce aussi:
		this.enemies=zone.getHeroes();
		
		this.our_hero=zone.getOwnHero();
		
		this.width=zone.getWidth();
		this.height=zone.getHeigh();
		/**Avec la fonction Fill_The_Matrix,on remplir la matrice */
		Fill_The_Matrix();
	}

	public void Fill_The_Matrix() {
		/** Premierement on rempli la matrice avec la valeur de securite:
		 *  (Avec SAFE_CASE)
		 */
		int x,y;
		security_matrix=new double[height][width];
		for(y=0;y<height;y++){
			for(x=0;x<width;x++){
				security_matrix[y][x]=SAFE_CASE;
			}
		}
	
		/** Placer les murs destructibles et indestructibles */
		Iterator<AiBlock> block_iterator=blocks.iterator();
		AiBlock blck;
		while(block_iterator.hasNext()==true){
			blck=block_iterator.next();
			pos_x=blck.getCol();
			pos_y=blck.getLine();
			if(blck.isDestructible()==true)
				security_matrix[pos_y][pos_x]=DEST_WALL;
			else
				security_matrix[pos_y][pos_x]=INDEST_WALL;
		}
	
	
		Iterator<AiItem> bonus_iterator = bonus.iterator();
		AiItem item_bonus;

		while (bonus_iterator.hasNext()) {
			item_bonus = bonus_iterator.next();

			pos_x = item_bonus.getCol();
			pos_y = item_bonus.getLine();

			security_matrix[pos_y][pos_x]=BONUS;

		}		
					
		
			/** Placer les feu*/
		Iterator<AiFire> fire_iterator=fires.iterator();
		AiFire fr;
		while(fire_iterator.hasNext()){
			fr=fire_iterator.next();
			pos_x=fr.getCol();
			pos_y=fr.getLine();
			security_matrix[pos_y][pos_x]=FIRE;
		}
	
		
		
		
		
	
		/** Placer les differentes niveua de danger*/
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
			bmb=iterate_bombs.next();
			
			pos_x=bmb.getCol();
			pos_y=bmb.getLine();
			//temps restant avant l'exlosion et calcule le niveau de danger:
			
				
			if(bmb.getNormalDuration()-bmb.getTime()>0){		
			time_left=bmb.getNormalDuration()-bmb.getTime();
	
			danger_level=(1/(time_left))*100000;
			}
			else if(bmb.getNormalDuration()-bmb.getTime()==0)
				time_left=1000000;
			else {		
				time_left=1500+(bmb.getNormalDuration()-bmb.getTime());
				if(time_left<0)
					time_left=500;
				danger_level=(1/(time_left))*100000;
					
			}	
					
					
			
				
				
			Block_Tile=bmb.getTile();
			
			blocks_right =Block_Tile.getNeighbor(Direction.RIGHT).getBlocks();
			blocks_left = Block_Tile.getNeighbor(Direction.LEFT).getBlocks();
			blocks_down = Block_Tile.getNeighbor(Direction.DOWN).getBlocks();
			blocks_up = Block_Tile.getNeighbor(Direction.UP).getBlocks();
			
			if(blocks_right.isEmpty()==true&&danger_level>0){
				for(int k=1;k<=bmb.getRange();k++){
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
	
		
		Iterator<AiBomb> it_bmb = bombs.iterator();
		AiBomb bm;

		while (it_bmb.hasNext()) {
			bm = it_bmb.next();
			pos_x = bm.getCol();
			pos_y = bm.getLine();
			if(security_matrix[pos_y][pos_x]!= FIRE)
			security_matrix[pos_y][pos_x] = BOMB;	
	}
		
		
		
		
	
	}
	

	
	public double[][] returnMatrix() {
		return security_matrix;
	}
		
}
