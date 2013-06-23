package org.totalboumboum.ai.v201011.ais.buyuktopacturak.v2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;

public class Bomb {

	private BuyuktopacTurak bt;
	private AiZone zone;		
	private AiHero ownHero;
	private List<AiBlock> wallsList;
	private List<AiBlock> willBurnList;//patlayacak duvarlar
	private Direction[] dirTable = {Direction.DOWN, Direction.RIGHT, Direction.UP, Direction.LEFT};
	private List<AiBomb> bombList;
	
	public Bomb(BuyuktopacTurak bt) throws StopRequestException{
		bt.checkInterruption();
		this.zone=bt.getPercepts();
		this.bt = bt;
		init();
	}
	private void init()throws StopRequestException{
		bt.checkInterruption();
		this.ownHero = zone.getOwnHero();
		getWalls();
		getBombs();
	}
	private void getBombs() throws StopRequestException{
		bt.checkInterruption();
		this.bombList = zone.getBombs();
	}
	private void getWalls() throws StopRequestException{
		bt.checkInterruption();
		this.wallsList = zone.getBlocks();
	}

	//ÇALIÞIYOR AMA KOD ÇOK HANTAL 
	//ÝNSAN OLUP AZICIK LÝSTE KULLANALIM
	private void calculateWillBurnWalls() throws StopRequestException{
		bt.checkInterruption();
		
		willBurnList = new  ArrayList<AiBlock>();
		Iterator<AiBomb> itBombs = this.bombList.iterator(); 
		AiBomb bomb;
		
		while(itBombs.hasNext()){
			bt.checkInterruption();
			bomb = itBombs.next();
			getWillBurnBlock(bomb.getTile(), bomb.getRange());
		}
		wallsList.removeAll(willBurnList);
	}
	
	private void getWillBurnBlock(AiTile tile, int range) throws StopRequestException{
		bt.checkInterruption();
		int i;
		boolean crossable;
		//neighbourTile bombamýn range.i boyunca ilerlerken while döngüsünde aldýðým komþu tile
		//tempTile methoda giren tile parametresinin deðerini korumak için (for döngüsünün baþýnda)
		AiTile neighbourTile, tempTile=tile; 
		
		AiBlock wall;
		int control;
		for(Direction dir:this.dirTable){
			bt.checkInterruption();
			crossable = true;
			i = 0;
			control=0;
			tile=tempTile;
			//her while döngüsüne girmeden önce i ve control sýfýrlanýp, tile'ým baþlangýç noktasýna alýnýr.
			while(i < range && crossable==true){
				bt.checkInterruption();
				neighbourTile = tile.getNeighbor(dir);
				//eðer neighbourTile crossable ise i.yi 1 artýrýp o anki tile'ýmý neighbourTile yaparým.
				if(neighbourTile.isCrossableBy(ownHero)){
					i++;
					tile = neighbourTile;
				}
				else{
					crossable = false;
					//her while döngüsüne girmeden önce duvar listesini iterator.a yeniden yüklemek gerekiyor.
					Iterator<AiBlock> itWalls = this.wallsList.iterator();
					while(itWalls.hasNext() && control==0){
						bt.checkInterruption();
						wall = itWalls.next();
						if(wall.isDestructible() && wall.getTile()==neighbourTile){
							willBurnList.add(wall);
							control=1;
						}
					}
				}
			}
		}
	}	
	//range'imdeki duvarlarý hesaplar.
	public int getRangeBombBlock(AiTile tile, int range) throws StopRequestException{
		bt.checkInterruption();
		calculateWillBurnWalls();//Her þeyi deðiþtiren fonk
		int i;
		boolean crossable;
		//neighbourTile bombamýn range.i boyunca ilerlerken while döngüsünde aldýðým komþu tile
		//tempTile methoda giren tile parametresinin deðerini korumak için (for döngüsünün baþýnda)
		AiTile neighbourTile, tempTile=tile; 
		
		AiBlock wall;
		int counter=0, control;
		for(Direction dir:this.dirTable){
			bt.checkInterruption();
			crossable = true;
			i = 0;
			control=0;
			tile=tempTile;
			//her while döngüsüne girmeden önce i ve control sýfýrlanýp, tile'ým baþlangýç noktasýna alýnýr.
			while(i < range && crossable==true){
				bt.checkInterruption();
				neighbourTile = tile.getNeighbor(dir);
				//eðer neighbourTile crossable ise i.yi 1 artýrýp o anki tile'ýmý neighbourTile yaparým.
				if(neighbourTile.isCrossableBy(ownHero)){
					i++;
					tile = neighbourTile;
				}
				else{
					crossable = false;
					//her while döngüsüne girmeden önce duvar listesini iterator.a yeniden yüklemek gerekiyor.
					Iterator<AiBlock> itWalls = this.wallsList.iterator();
					while(itWalls.hasNext() && control==0){
						bt.checkInterruption();
						wall = itWalls.next();
						if(wall.isDestructible() && wall.getTile()==neighbourTile){
							counter++;
							control=1;
						}
					}
				}
			}
		}
		return counter;
	}	
}
