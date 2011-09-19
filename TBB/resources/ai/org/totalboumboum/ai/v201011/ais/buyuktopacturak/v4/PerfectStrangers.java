package org.totalboumboum.ai.v201011.ais.buyuktopacturak.v4;

import java.util.ArrayList;
import java.util.List;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;

public class PerfectStrangers {

	BuyuktopacTurak bt;
	AiZone zone;
	private Direction[] dirTable = {Direction.DOWN, Direction.RIGHT, Direction.UP, Direction.LEFT};

	/**
	 * C�est un constructeur qui obtient des percepts de la zone et qui crée l�objet BuyuktopacTurak.
	 * @param bt
	 * @throws StopRequestException
	 */
	public PerfectStrangers(BuyuktopacTurak bt) throws StopRequestException{
		bt.checkInterruption();
		this.bt=bt;
		this.zone=bt.getPercepts();
	}
	
	/**
	 * On remplit toutes les cases de la matrice avec le constant FREE.
	 * @param matrix
	 * @throws StopRequestException
	 */
	public void putFree(double[][] matrix)throws StopRequestException{
		bt.checkInterruption();
		int line, col;
		for (line = 0; line < zone.getHeight(); line++) {
			bt.checkInterruption();
			for (col = 0; col < zone.getWidth(); col++) {
				bt.checkInterruption();
				matrix[line][col] = Constant.FREE;
			}
	   	}
	}
	
	/**
	 * On remplit toutes les cases dangers.
	 * @param matrix
	 * @throws StopRequestException
	 */
	public void putBlast(double[][] matrix) throws StopRequestException{
		bt.checkInterruption();
		 List<AiBomb> bombsList = zone.getBombs();
		double time;
		int line,col;
		for(AiBomb bomb:bombsList){
			bt.checkInterruption();
			
			List<AiTile> blasts = bomb.getBlast();
			blasts.add(bomb.getTile());
			matrix[bomb.getTile().getLine()][bomb.getTile().getCol()]=0;

			for(AiTile blast:blasts){
				col = blast.getCol();
				line = blast.getLine();
				time = bomb.getNormalDuration()-bomb.getTime();
				if(time < 0){
					time = 0.1;
				}
				matrix[line][col] += (Constant.FIRE/(time/1000));
			}
		}
	}
	
	/**
	 * On renvoie la liste des cases voisinages.
	 * @param tile
	 * @param dir
	 * @param range
	 * @return
	 * @throws StopRequestException
	 */
	public List<AiTile> getONeighbour(AiTile tile, Direction dir, int range) throws StopRequestException{
		bt.checkInterruption();
		int i = 0;
		boolean crossable = true;
		List<AiTile> result = new ArrayList<AiTile>();
		AiTile tempTile;
		while(i < range && crossable){
			tempTile = tile.getNeighbor(dir);
			if(tempTile.isCrossableBy(zone.getOwnHero())){
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
	 * On renvoie la liste des case qui définie la portée virtuelle.
	 * @param tile
	 * @param range
	 * @return
	 * @throws StopRequestException
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
				
				if(neighbourTile.isCrossableBy(zone.getOwnHero()) && crossableItem){
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
	 * @return
	 * @throws StopRequestException
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
	 * On renvoie la liste des murs destructibles qui vont exploser.
	 * @param willBurnWallsList
	 * @throws StopRequestException
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
	 * Si on pose une bombe, on trouve une/plusieurs case(s) sûre.
	 * @param bomb
	 * @return
	 * @throws StopRequestException
	 */	
	public boolean isRunnable(AiTile bomb) throws StopRequestException{
		bt.checkInterruption();
		boolean canIRun = false;
		boolean bool = false;
		
		List<AiTile> trySafeList = new ArrayList<AiTile>();
		List<AiTile> allRangeList = new ArrayList<AiTile>();
		allRangeList.addAll(getAllRangeList());
		
		for(Direction dir:dirTable){
			bt.checkInterruption();
			allRangeList.addAll(getONeighbour(bomb, dir, zone.getOwnHero().getBombRange()));
		}
		allRangeList.add(bomb);
		
		for(Direction dir:dirTable){
			bt.checkInterruption();
			trySafeList.addAll(getONeighbour(bomb, dir, 2));
		}

		for(AiTile s1:trySafeList){
			bt.checkInterruption();
			for(AiTile s2:s1.getNeighbors()){
				bt.checkInterruption();
				if(s2.isCrossableBy(zone.getOwnHero())){
					bool = false;
					for(AiTile d:allRangeList){
						bt.checkInterruption();
						if(s2 == d){
							bool = true;							
						}
					}
					if(bool == false){
						canIRun = true;
					}
				}
			}
		}
		
		return canIRun;
	}
}
