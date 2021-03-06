package org.totalboumboum.ai.v201011.ais.buyuktopacturak.v6;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiFire;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Les méthodes qui se trouvent dans ces classes servent aux autres classes dans le cas de besoins. 
 * Comme ces méthodes sont appelé par les autre classes mais elle n'appartient pas aux eux, 
 * on làappelle comme l'étranger alors le nom de cette classe est PerfectSrangers. 
 * @author Onur Büyüktopaç
 * @author Yiğit Turak
 */
@SuppressWarnings("deprecation")
public class PerfectStrangers {

	/** */
	BuyuktopacTurak bt;
	/** */
	AiZone zone;
	/** */
	AiHero deepPurple;
	
	/** */
	private Direction[] dirTable = {Direction.DOWN, Direction.RIGHT, Direction.UP, Direction.LEFT};

	/**
	 * C'est un constructeur qui obtient des percepts de la zone et qui crée làobjet BuyuktopacTurak.
	 * @param bt
	 * 		description manquante !
	 * @param zone
	 * 		description manquante !
	 * @throws StopRequestException
	 * 		description manquante !
	 */
	public PerfectStrangers(BuyuktopacTurak bt, AiZone zone) throws StopRequestException{
		bt.checkInterruption();
		this.bt=bt;
		this.zone = zone;
		deepPurple = zone.getOwnHero();
	}
	
	/**
	 * Trouver tous les cases accessibles actuellement par notre hero recursivement. 
	 * @param tile
	 * 		description manquante !
	 * @param freeList
	 * 		description manquante !
	 * @return List<AiTile>
	 * 		description manquante !
	 * @throws StopRequestException
	 * 		description manquante !
	 */

	public List<AiTile> putFree(AiTile tile, List<AiTile> freeList) throws StopRequestException
	{
		bt.checkInterruption();
		
		double[][] matrix = new double[zone.getHeight()][zone.getWidth()];
		putBlast(matrix);
		
		List<AiTile> neigs = new ArrayList<AiTile>();
		AiTile tempoNeig;
		neigs = tile.getNeighbors();
		Iterator<AiTile> neigList = neigs.iterator();
		while (neigList.hasNext())
		{
			bt.checkInterruption();
			tempoNeig = neigList.next();
			if(!freeList.contains(tempoNeig)){
				if(tempoNeig.isCrossableBy(this.deepPurple) && matrix[tempoNeig.getLine()][tempoNeig.getCol()]>-800)
				{
					//Si portee de bombe plus de 5 on trouve les cases jusq'a la portee. 
					if(deepPurple.getBombRange()>7){
						if(Math.abs(tempoNeig.getLine()-deepPurple.getLine())<9){
							if(Math.abs(tempoNeig.getCol()-deepPurple.getCol())<9){
								freeList.add(tempoNeig);
								freeList = putFree(tempoNeig, freeList);
							}	
						}
					}
					else{
						freeList.add(tempoNeig);
						freeList = putFree(tempoNeig, freeList);
					}
				}
			}
		}
		return freeList;
	}
	
	/**
	 * Remplir le matrice par la distance de l'adversaire plus proche de la case.
	 * @param freeList
	 * 		description manquante !
	 * @param matrix
	 * 		description manquante !
	 * @throws StopRequestException
	 * 		description manquante !
	 */
	public void putDistanceEnnemy(List<AiTile> freeList, double[][] matrix) throws StopRequestException
	{
		bt.checkInterruption();
		int totalDistance=zone.getHeight()+zone.getWidth(),distance=0;
		for(AiTile tile:freeList){
			bt.checkInterruption();
			distance=getMinDistance(tile);
			matrix[tile.getLine()][tile.getCol()]+=(totalDistance-distance)*5;
		}
	}
	
	/** Butun matrisi doldurur
	 * 
	 * @param freeList
	 * 		description manquante !
	 * @param matrix
	 * 		description manquante !
	 * @throws StopRequestException
	 * 		description manquante !
	 */
	public void putDistanceHero(List<AiTile> freeList, double[][] matrix) throws StopRequestException
	{
		bt.checkInterruption();
		int totalDistance=zone.getHeight()+zone.getWidth();
		for(AiTile tile:freeList){
			bt.checkInterruption();
			matrix[tile.getLine()][tile.getCol()]+=totalDistance-Math.abs((deepPurple.getLine()-tile.getLine())+(deepPurple.getCol()-tile.getCol()));
		}
	}
	
	/**
	 * On remplit toutes les cases dangereuses.
	 * @param matrix
	 * 		description manquante !
	 * @throws StopRequestException
	 * 		description manquante !
	 */
	public void putBlast(double[][] matrix) throws StopRequestException{
		bt.checkInterruption();
		 List<AiBomb> bombsList = zone.getBombs();
		 List<AiFire> fireList = zone.getFires();
		double time;
		int line,col;
		int blastRange;
		
		for(AiFire fire:fireList){
			bt.checkInterruption();
			col = fire.getCol();
			line = fire.getLine();
			matrix[line][col] = Constant.FIRE*100;
		}
		
		for(AiBomb bomb:bombsList){
			bt.checkInterruption();	
			blastRange = bomb.getRange();
			List<AiTile> blasts = bomb.getBlast();
			blasts.add(bomb.getTile());
			matrix[bomb.getTile().getLine()][bomb.getTile().getCol()]=0;

			for(AiTile blast:blasts){
				bt.checkInterruption();
				col = blast.getCol();
				line = blast.getLine();
				time = bomb.getNormalDuration()-bomb.getTime();
				if(time < 0){
					time = 0.1;
				}
				if(matrix[line][col]>0)
					matrix[line][col] = ((Constant.FIRE*blastRange)/(time/1000));
				else
					matrix[line][col] += ((Constant.FIRE*blastRange)/(time/1000));
			}
		}
	}
	
	
	/**
	 * On renvoie la liste des cases voisinages accessible par hero. 
	 * @param tile
	 * 		description manquante !
	 * @param dir
	 * 		description manquante !
	 * @param range
	 * 		description manquante !
	 * @return List<AiTile>
	 * 		description manquante !
	 * @throws StopRequestException
	 * 		description manquante !
	 */
	public List<AiTile> getONeighbour(AiTile tile, Direction dir, int range) throws StopRequestException{
		bt.checkInterruption();
		int i = 0;
		boolean crossable = true;
		List<AiTile> result = new ArrayList<AiTile>();
		AiTile tempTile;
		while(i < range && crossable){
			bt.checkInterruption();
			tempTile = tile.getNeighbor(dir);
			if(tempTile.isCrossableBy(deepPurple)){
				result.add(tempTile);
				i++;
				tile = tempTile;
			}
			else
				crossable = false;
		}
		return result;
	}	
	
	/**
	 * On renvoie la liste des case qui définie la portée virtuelle de la bombe.
	 * @param tile
	 * 		description manquante !
	 * @param range
	 * 		description manquante !
	 * @return List<AiTile>
	 * 		description manquante !
	 * @throws StopRequestException
	 * 		description manquante !
	 */
	public List<AiTile> getBombRangeList(AiTile tile, int range) throws StopRequestException{
		bt.checkInterruption();
		List<AiTile> bombRangeList= new ArrayList<AiTile>();	
		
		AiTile neighbourTile, tempTile = tile;
		int i=0;
		boolean crossableItem;
		boolean crossable;
		
		for(Direction dir:this.dirTable){
			bt.checkInterruption();
			crossableItem = true;
			crossable = true;
			i = 0;
			tile=tempTile;
			while(i < range && crossable==true){
				bt.checkInterruption();
				neighbourTile = tile.getNeighbor(dir);
				if(neighbourTile.getItems().size() == 0){
					crossableItem = true;
				}
				else{
					crossableItem = false;
				}
				
				if(neighbourTile.isCrossableBy(deepPurple) && crossableItem){
					i++;
					tile = neighbourTile;
					bombRangeList.add(neighbourTile);
				}
				else{
					crossable = false;
					bombRangeList.add(neighbourTile);
				}
			}
		}
		return bombRangeList;
	}
	
	/**
	 * On renvoie la liste des portées de toutes les bombes.
	 * @return List<AiTile>
	 * 		description manquante !
	 * @throws StopRequestException
	 * 		description manquante !
	 */
	public List<AiTile> getAllRangeList()throws StopRequestException{
		bt.checkInterruption();
		List<AiTile> allRangeList;
		allRangeList = new ArrayList<AiTile>();
		List<AiBomb> bombsList = zone.getBombs();
		for(AiBomb bomb:bombsList){
			bt.checkInterruption();
			allRangeList.addAll(bomb.getBlast());
		}
		return allRangeList;
	}
	
	/**
	 * Trouver des murs destructibles qui vont exploser et remplir la liste willBurnList avec ces cases.
	 * @param willBurnWallsList
	 * 		description manquante !
	 * @throws StopRequestException
	 * 		description manquante !
	 */
	public void getWillBurnWalls(List<AiBlock> willBurnWallsList)throws StopRequestException{
		bt.checkInterruption();
		
		willBurnWallsList = new ArrayList<AiBlock>();
		List<AiTile> allRangeList= getAllRangeList();

		for(AiTile range:allRangeList){
			bt.checkInterruption();

			if(range.getBlocks().size() != 0){
	
				if(range.getBlocks().get(0).isDestructible()){
					willBurnWallsList.add(range.getBlocks().get(0));
				} 
			} 
		}
	}
	
	/**
	 * Si on pose une bombe, notre hero peut trouve une/plusieurs case(s) sûre?
	 * @param bomb
	 * 		description manquante !
	 * @param freeList 
	 * 		description manquante !
	 * @return boolean
	 * 		description manquante !
	 * @throws StopRequestException
	 * 		description manquante !
	 */	
	public boolean isRunnable(AiTile bomb, List<AiTile> freeList) throws StopRequestException{
		bt.checkInterruption();
		boolean canIRun = false;
		
		List<AiTile> trySafeList = new ArrayList<AiTile>();
		List<AiTile> allRangeList = new ArrayList<AiTile>();
		allRangeList.addAll(getAllRangeList());
		
		for(Direction dir:dirTable){
			bt.checkInterruption();
			allRangeList.addAll(getONeighbour(bomb, dir, deepPurple.getBombRange()));
		}
		allRangeList.add(bomb);
		
		for(Direction dir:dirTable){
			bt.checkInterruption();
			trySafeList.addAll(getONeighbour(bomb, dir, 3));
		}

		for(AiTile s1:trySafeList){
			bt.checkInterruption();
			for(AiTile s2:s1.getNeighbors()){
				bt.checkInterruption();
				if(s2.isCrossableBy(deepPurple)){
					if(!allRangeList.contains(s2)&&freeList.contains(s2)){
						canIRun = true;
					}
				}
			}
		}		
		return canIRun;
	}
	
	/**
	 * Si on pose une bombe, adversaire peut trouve une/plusieurs case(s) sûre?
	 * @param hero
	 * 		description manquante !
	 * @param bomb
	 * 		description manquante !
	 * @return boolean
	 * 		description manquante !
	 * @throws StopRequestException
	 * 		description manquante !
	 */
	public boolean isRunnableEnemy(AiHero hero, AiTile bomb) throws StopRequestException{
		bt.checkInterruption();
		boolean canHeRun = false;
		
		List<AiTile> trySafeList = new ArrayList<AiTile>();
		List<AiTile> allRangeList = new ArrayList<AiTile>();
		allRangeList.addAll(getAllRangeList());
		
		for(Direction dir:dirTable){
			bt.checkInterruption();
			allRangeList.addAll(getONeighbour(bomb, dir, deepPurple.getBombRange()));
		}
		allRangeList.add(bomb);
		for(Direction dir:dirTable){
			bt.checkInterruption();
			trySafeList.addAll(getONeighbour(hero.getTile(), dir, 6));
		}

		for(AiTile s1:trySafeList){
			bt.checkInterruption();
			for(AiTile s2:s1.getNeighbors()){
				bt.checkInterruption();
				if(s2.isCrossableBy(deepPurple)){
					if(!allRangeList.contains(s2)){
						canHeRun = true;
					}
				}
			}
		}
		return canHeRun;
	}
	
	/**
	 * Trouver l'adversaire plus proche de la case. Retourne la distance de cette case.
	 * @param tile
	 * 		description manquante !
	 * @return int
	 * 		description manquante !
	 * @throws StopRequestException
	 * 		description manquante !
	 */
	public int getMinDistance(AiTile tile) throws StopRequestException{
		bt.checkInterruption();
		int min = 100;
		int tempMin;
		List<AiHero> hero = zone.getHeroes();
		hero.remove(deepPurple);
		List<AiTile> tileList = new ArrayList<AiTile>();
		for(AiHero h:hero){
			bt.checkInterruption();
			tileList.add(h.getTile());
		}
		for(AiTile t:tileList){
			bt.checkInterruption();
			tempMin = Math.abs(t.getCol()-tile.getCol()+t.getLine()-tile.getLine());
			if(min > tempMin){
				min = tempMin;
			}
		}
		return min;
	}
	
}
