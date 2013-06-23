package org.totalboumboum.ai.v201011.ais.buyuktopacturak.v5;

import java.util.ArrayList;
import java.util.List;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiItem;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Cette classe calcule la matrice de la mode collecte.
 * @author Onur Büyüktopaç & Yigit Turak
 */
public class CollectionMatrix{	
	private BuyuktopacTurak bt;
	private PerfectStrangers ps;
	private AiZone zone;		
	private AiHero deepPurple;
	
	private List<AiTile> freeList =new ArrayList<AiTile>(); //ulaþýlabilir tile.lar	
	private List<AiItem> itemsList;
	private List<AiTile> bonusList; 
	private List<AiBlock> destWallsList;
	private List<AiBlock> hardWallsList;
	private List<AiHero> heroesList;
	private List<AiBlock> willBurnWallsList;//Les murs qui vont exploser
	
	private double[][] matrix; 
	private int width;
	private int heigh;
	
	private Direction[] dirTable = {Direction.DOWN, Direction.RIGHT, Direction.UP, Direction.LEFT};

	/**
	 * C’est le constructeur qui obtient des percepts dans la classe BuyuktopacTurak.
	 * @param bt
	 * @param zone
	 * @throws StopRequestException
	 */
	public CollectionMatrix(BuyuktopacTurak bt, AiZone zone) throws StopRequestException{
		bt.checkInterruption();
		this.zone = zone;
		this.bt = bt;
		init();
	}
	
	/**
	 * On obtient notre héro, la dimension de la zone et crée les objets listes.
	 * @throws StopRequestException
	 */
	private void init()throws StopRequestException{
		bt.checkInterruption();
		
		width=zone.getWidth(); 
		heigh=zone.getHeight();
		deepPurple = zone.getOwnHero();
		
		ps=new PerfectStrangers(this.bt,zone);
		matrix = new double[heigh][width];
		freeList = new ArrayList<AiTile>();
		willBurnWallsList = new ArrayList<AiBlock>();
		heroesList = new ArrayList<AiHero>();
		itemsList = new ArrayList<AiItem>();
		bonusList = new ArrayList<AiTile>();
		destWallsList = new ArrayList<AiBlock>();
		hardWallsList = new ArrayList<AiBlock>();
	}
	
	/**
	 * On crée et remplit la matrice.
	 * @throws StopRequestException
	 */
	public void createMatrix()throws StopRequestException{
		bt.checkInterruption();

		freeList=ps.putFree(this.deepPurple.getTile(),freeList);
		ps.putDistance(freeList, matrix);
		putWalls();
		putHero();
		putBonus();
		ps.putBlast(matrix);
	}
		
	/**
	 * On trouve tous les murs et puis on remplit les cases des entours des murs 
	 * avec le constant DESTRUCTIBLE ou INDESTRUCTIBLE. 
	 * On utilise la méthode isRunnable().
	 * @throws StopRequestException
	 */
	private void putWalls() throws StopRequestException{
		bt.checkInterruption();
		int numberBonus;
		AiTile neighbour;
		//pour trouver les murs autour de cases accessible.
		for(AiTile tile:freeList){
			bt.checkInterruption();
			for(Direction dir:dirTable){
				bt.checkInterruption();
				neighbour = tile.getNeighbor(dir);
				if(neighbour.getBlocks().size()!= 0){
					if(neighbour.getBlocks().get(0).isDestructible()){
						destWallsList.addAll(neighbour.getBlocks()); 
					}
					else{
						hardWallsList.addAll(neighbour.getBlocks());
					}
				}
			}
		}
		//On supprime les murs qui vont exploser
		ps.getWillBurnWalls(willBurnWallsList);
		destWallsList.removeAll(willBurnWallsList);
		
		//Si c'est destructible calculer la zone et ajouter la matrice
		for(AiBlock wall:destWallsList){
			bt.checkInterruption();
			
			List<AiTile> wallRange =  new ArrayList<AiTile>();
			matrix[wall.getLine()][wall.getCol()] = Constant.DESTMUR;
			for (Direction dir : dirTable){
				bt.checkInterruption();
				wallRange.addAll(ps.getONeighbour(wall.getTile(), dir, deepPurple.getBombRange()));
			}
			
			for(AiTile tileRangeNext:wallRange){
				bt.checkInterruption();
				int line,col;
				col = tileRangeNext.getCol();
				line = tileRangeNext.getLine();
				numberBonus = zone.getHiddenItemsCount();
				//On ajouter constant de mur destructible et des nombre des bonus cahcees. 
				if(ps.isRunnable(tileRangeNext)){
					matrix[line][col] += (Constant.DESTRUCTIBLE+numberBonus);
				}
			}
		}
		for(AiBlock wall:hardWallsList){
			bt.checkInterruption();

			matrix[wall.getLine()][wall.getCol()] = Constant.INDESTRUCTIBLE;
		}
	}

	/**
	 * On trouve tous les bonus et puis on les remplit avec le constant 
	 * BONUSBOMB ou BONUSFLAME.
	 * @throws StopRequestException
	 */
	private void putBonus() throws StopRequestException{
		bt.checkInterruption();
		for(AiTile tile:freeList){
			bt.checkInterruption();
			if(tile.getItems().size()!= 0){
				itemsList.addAll(tile.getItems());
			}
		}
		for(AiItem item:itemsList){
			bt.checkInterruption();

			if(item.getType().name() == "EXTRA_BOMB"){
				matrix[item.getLine()][item.getCol()] += (Constant.BONUSBOMB);
			}
			else{
				matrix[item.getLine()][item.getCol()] += (Constant.BONUSFLAME);
			}
			bonusList.add(item.getTile());
		}
	}
	
	/**
	 * On trouve toutes les adversaires et puis on remplit les cases des entours des ennemies avec le constant RIVAL. 
	 * @throws StopRequestException
	 */
	private void putHero() throws StopRequestException{
		bt.checkInterruption();
		
		heroesList = zone.getRemainingHeroes();
		heroesList.remove(deepPurple);
		
		List<AiTile> heroRange = new ArrayList<AiTile>();
		for(AiHero hero:heroesList){
			bt.checkInterruption();
			for(Direction dir : dirTable){
				bt.checkInterruption();
				heroRange.addAll(ps.getONeighbour(hero.getTile(), dir, deepPurple.getBombRange()));
			}
			
			for(AiTile tileRangeNext:heroRange){
				bt.checkInterruption();
				if(freeList.contains(tileRangeNext))
					matrix[tileRangeNext.getLine()][tileRangeNext.getCol()] += (Constant.RIVAL);					
			}
		}
	}
		
	/**
	 * renvoi matrix
	 * @return double[][]
	 * @throws StopRequestException
	 */
	public double[][] getMatrix()throws StopRequestException{
		bt.checkInterruption();
		return matrix;
	}
}
