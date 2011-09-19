package org.totalboumboum.ai.v201011.ais.buyuktopacturak.v4;

import java.util.ArrayList;
import java.util.List;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiItem;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;

public class AttackMatrix{
	private BuyuktopacTurak bt;
	private PerfectStrangers ps;
	private AiZone zone;		
	private AiHero deepPurple;
	
	private List<AiTile> safesList;
	private List<AiItem> itemsList;
	private List<AiTile> bonusList; 
	private List<AiBlock> wallsList;
	private List<AiTile> rivalsList;
	private List<AiHero> heroesList;
	private List<AiBlock> willBurnWallsList;//Les murs qui vont exploser
	
	private int col;
	private int line;	
	private double[][] matrix; 
	private int width;
	private int heigh;
	public Direction[] dirTable = {Direction.DOWN, Direction.RIGHT, Direction.UP, Direction.LEFT};
	
	/**
	 * C�est le constructeur qui obtient des percepts dans la classe BuyuktopacTurak.
	 * @param bt
	 * @throws StopRequestException
	 */
	public AttackMatrix(BuyuktopacTurak bt) throws StopRequestException{	
		bt.checkInterruption();
		this.zone = bt.getPercepts();
		this.bt = bt;
		init();
	}
	/**
	 * On obtient notre h�ro, la dimension de la zone et crée les objets listes.
	 * @throws StopRequestException
	 */
	private void init()throws StopRequestException{
		bt.checkInterruption();
		width = zone.getWidth(); 
		heigh = zone.getHeight();
		deepPurple = zone.getOwnHero();
		ps=new PerfectStrangers(this.bt);
		matrix = new double[heigh][width];
		willBurnWallsList = new ArrayList<AiBlock>();
		heroesList = new ArrayList<AiHero>();
		rivalsList = new ArrayList<AiTile>();
		itemsList = new ArrayList<AiItem>();
		bonusList = new ArrayList<AiTile>();
		safesList = new ArrayList<AiTile>();
		wallsList = new ArrayList<AiBlock>();
		heroesList = zone.getHeroes();
		heroesList.remove(deepPurple);
	}
	
	/**
	 * On crée et remplit la matrice. 
	 * @throws StopRequestException
	 */
	public void createMatrix()throws StopRequestException{
		init();
		ps.putFree(matrix);
		putHero();
		boolean chemin=false;
		for(AiHero h:heroesList){
			bt.checkInterruption();
			if(bt.AstarDistance(deepPurple.getTile(),h.getTile())!=-1)
				chemin=true;
		}
		
		if(!chemin){
			putWalls();
			putBonus();
		}
		ps.putBlast(matrix);
	}
	
	/**
	 * on trouve tous les murs et puis on remplit les cases des entours 
	 * des murs avec le constant DESTRUCTIBLE ou INDESTRUCTIBLE. 
	 * On utilise l�algorithme A* et la méthode isRunnable().
	 * @throws StopRequestException
	 */
	private void putWalls() throws StopRequestException{
		bt.checkInterruption();
		wallsList = zone.getBlocks();
		ps.getWillBurnWalls(willBurnWallsList);
		wallsList.removeAll(willBurnWallsList);
		int dist;

		
		//appeler les murs de la liste
		for(AiBlock wall:wallsList){
			bt.checkInterruption();
			col = wall.getCol();
			line = wall.getLine();
			//Si c'est destructible calculer la zone et ajouter la matrice
			if(wall.isDestructible()){
				
				List<AiTile> wallRange =  new ArrayList<AiTile>();
				matrix[line][col] = Constant.DESTMUR;
				for (Direction dir : dirTable){
					bt.checkInterruption();
					wallRange.addAll(ps.getONeighbour(wall.getTile(), dir, deepPurple.getBombRange()));
				}
				
				for(AiTile tileRangeNext:wallRange){
					bt.checkInterruption();
					int line2,col2;
					col2 = tileRangeNext.getCol();
					line2 = tileRangeNext.getLine();
					dist = bt.AstarDistance(deepPurple.getTile(),tileRangeNext);
					if(dist!=-1){
						matrix[line2][col2] += 1;
						if(ps.isRunnable(tileRangeNext))
							matrix[line2][col2] += ((Constant.DESTRUCTIBLE)/(dist+1));
					}
				}
			}
			//Si ce n'est pas destructible 
			else{
				matrix[line][col] = Constant.INDESTRUCTIBLE;
			}
		}
	}
	
	/**
	 * on trouve tous les bonus et puis on les remplit avec le constant BONUS. On utilise l�algorithme A*.
	 * @throws StopRequestException
	 */
	private void putBonus() throws StopRequestException{
		bt.checkInterruption();
		itemsList = zone.getItems();
		int min,disOwn;
		for(AiItem item:itemsList){
			bt.checkInterruption();
			col = item.getCol();
			line = item.getLine();
			min = getMinDistance(heroesList,item.getTile());
			disOwn = bt.AstarDistance(deepPurple.getTile(),item.getTile());
			if(disOwn!=-1)
				matrix[line][col] += ((Constant.BONUS+(min-disOwn)*2)/(disOwn+1));
			bonusList.add(item.getTile());
		}
	}
	
	/**
	 * on trouve toutes les adversaires et puis 
	 * on remplit les cases des entours des ennemies avec le constant RIVALATTACK. 
	 * On utilise l�algorithme A*.
	 * @throws StopRequestException
	 */
	private void putHero() throws StopRequestException{
		bt.checkInterruption();

		
		int distOwn, distAdv;
		List<AiTile> heroRange = new ArrayList<AiTile>();
		for(AiHero hero:heroesList){
			bt.checkInterruption();

			for(Direction dir : dirTable){
				heroRange.addAll(ps.getONeighbour(hero.getTile(), dir, deepPurple.getBombRange()));
			}

			
			for(AiTile tileRangeNext:heroRange){
				bt.checkInterruption();
				if(ps.isRunnable(tileRangeNext))
				{
					col = tileRangeNext.getCol();
					line = tileRangeNext.getLine();
					distOwn = bt.AstarDistance(deepPurple.getTile(),tileRangeNext);
					distAdv = zone.getTileDistance(hero.getTile(), tileRangeNext);
					if(distOwn!=-1){
						if(distOwn == 0)
							matrix[line][col] += Constant.RIVALATTACK;
						else
							matrix[line][col] += (Constant.RIVALATTACK/(distOwn*distAdv+1));
					}
					else
						matrix[line][col] = 0;
				}	
			}
		}
	}

	/**
	 * renvoi matrix
	 * @return
	 * @throws StopRequestException
	 */
	public double[][] getMatrix()throws StopRequestException{
		bt.checkInterruption();
		return matrix;
	}
	
	/**
	 * renvoi heroesList
	 * @return
	 * @throws StopRequestException
	 */
	public List<AiHero> getHeroesList()throws StopRequestException{
		bt.checkInterruption();
		return heroesList;
	}

	/**
	 * renvoi rivalsList
	 * @return
	 * @throws StopRequestException
	 */
	public List<AiTile> getRivalsList()throws StopRequestException{
		bt.checkInterruption();
		return rivalsList;
	}
	
	/**
	 * renvoi itemsList
	 * @return
	 * @throws StopRequestException
	 */
	public List<AiItem> getItemsList()throws StopRequestException{
		bt.checkInterruption();
		return itemsList;
	}

	/**
	 * renvoi bonusList
	 * @return
	 * @throws StopRequestException
	 */
	public List<AiTile> getBonusList()throws StopRequestException{
		bt.checkInterruption();
		return bonusList;
	}
	
	/**
	 * renvoi safesList
	 * @return
	 * @throws StopRequestException
	 */
	public List<AiTile> getSafesList()throws StopRequestException{
		bt.checkInterruption();
		return safesList;
	}
	
	/**
	 * renvoi wallsList
	 * @return
	 * @throws StopRequestException
	 */
	public List<AiBlock> getWallsList()throws StopRequestException{
		bt.checkInterruption();
		return wallsList;
	}

	/**
	 * on renvoie la plus proche distance à l�iA des adversaires.
	 * @param hero
	 * @param tile
	 * @return
	 * @throws StopRequestException
	 */
	public int getMinDistance(List<AiHero> hero, AiTile tile) throws StopRequestException{
		bt.checkInterruption();
		int min = 100;
		int tempMin;
		List<AiTile> tileList = new ArrayList<AiTile>();
		for(AiHero h:hero){
			bt.checkInterruption();
			tileList.add(h.getTile());
		}
		for(AiTile t:tileList){
			bt.checkInterruption();
			tempMin = bt.AstarDistance(t, tile);
			if(tempMin!=-1){
				if(min > tempMin){
					min = tempMin;
				}	
			}
		}
		return min;
	}
}