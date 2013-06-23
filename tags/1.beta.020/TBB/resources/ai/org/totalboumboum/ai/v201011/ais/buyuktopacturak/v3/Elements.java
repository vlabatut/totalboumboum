package org.totalboumboum.ai.v201011.ais.buyuktopacturak.v3;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiItem;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;

public class Elements {
	private BuyuktopacTurak bt;
	private AiZone zone;		
	private AiHero deepPurple;  
	private List<AiHero> heroesList;//tüm herolar
	private List<AiBlock> willBurnWallsList;//patlayacak duvarlar
	private List<AiItem> willBurnItemsList;//patlayacak itemler
	private List<AiBomb> bombList;//Tüm bombalarýn listesi
	private Direction[] dirTable = {Direction.DOWN, Direction.RIGHT, Direction.UP, Direction.LEFT};
	
	/**
	 * C'est le constructeur
	 * @param bt
	 * @throws StopRequestException
	 */
	public Elements(BuyuktopacTurak bt) throws StopRequestException{
		bt.checkInterruption();
		this.bt = bt;
		this.zone=bt.getPercepts();
		init();
	}
	/////////////////////////////////////////////////////////////////
	// INITIALISATION			/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Cette method est initialisée notre Héro, murs et bombs
	 * @throws StopRequestException
	 */
	private void init()throws StopRequestException{
		bt.checkInterruption();
		this.deepPurple = zone.getOwnHero();
		getBombs();
	}
	
	private void getBombs() throws StopRequestException{
		bt.checkInterruption();
		bombList = new ArrayList<AiBomb>();
		this.bombList = zone.getBombs();
	}
	
	///////////////////////////////////
	//Bombanýn etki listesini verir. //
	///////////////////////////////////
	private List<AiTile> getBombRangeList(AiTile tile, int range) throws StopRequestException{
		bt.checkInterruption();
		List<AiTile> bombRangeList;//Bomba alaný listesi
		bombRangeList = new ArrayList<AiTile>();	
		
		AiTile neighbourTile, tempTile = tile;
		int i=0;
		boolean crossableItem;//bombe Itemlere deðmiþ mi (sana deðmezmiþ bebeðim)
		boolean crossable;//Tamamen geçilebilirlik.
		
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
					crossableItem = true; //yani item yok
				}
				else{
					crossableItem = false;
				}
				//eðer neighbourTile crossable ise i.yi 1 artýrýp o anki tile'ýmý neighbourTile yaparým.
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
	
	//////////////////////////////////////
	//Tüm bombalarýn Range listesini ver //
	//////////////////////////////////////
	private List<AiTile> getAllRangeList()throws StopRequestException{
		bt.checkInterruption();
		List<AiTile> allRangeList;//Tüm bombalarýn Alan listesi
		allRangeList = new ArrayList<AiTile>();
				
		Iterator<AiBomb> itBombs = this.bombList.iterator(); 
		AiBomb bomb;
		
		while(itBombs.hasNext()){
			bt.checkInterruption();
			bomb = itBombs.next();
			allRangeList.addAll(getBombRangeList(bomb.getTile(), bomb.getRange()));
		}
		return allRangeList;
	}
	
	//////////////////////////////////////
	//Patlayacak duvarlar listesini ver //
	//////////////////////////////////////
	private List<AiBlock> getWillBurnWalls()throws StopRequestException{
		bt.checkInterruption();
		
		willBurnWallsList = new ArrayList<AiBlock>();
		List<AiTile> allRangeList;//Tüm bombalarýn Alan listesi
		allRangeList = getAllRangeList();
		Iterator<AiTile> itRange = allRangeList.iterator();
		AiTile range;
		
		while(itRange.hasNext()){
			bt.checkInterruption();
			range = itRange.next();
			//Listede eleman varsa
			if(range.getBlocks().size() != 0){
				//Duvar batlatýlabilirse
				if(range.getBlocks().get(0).isDestructible()){
					willBurnWallsList.add(range.getBlocks().get(0));
				} 
			} 
		}
		return willBurnWallsList; 
	}
	
	//////////////////////////////////////
	//Patlayacak bonuslar listesini ver //
	//////////////////////////////////////
	private List<AiItem> getWillBurnItems()throws StopRequestException{
		bt.checkInterruption();
		
		willBurnItemsList = new ArrayList<AiItem>();
		List<AiTile> allRangeList;//Tüm bombalarýn Alan listesi
		allRangeList = getAllRangeList();
		
		Iterator<AiTile> itRange = allRangeList.iterator();
		AiTile range;
		
		while(itRange.hasNext()){
			bt.checkInterruption();
			range = itRange.next();
			//Listede eleman varsa
			if(range.getItems().size() != 0){
				willBurnItemsList.addAll(range.getItems());
			} 
		}
		return willBurnItemsList; 
	}
	
	////////////////////////////////////////////////////////////////
	//range'imdeki Patlamayacak duvarlarý hesaplar. LÝSTEYE ATAR  //
	////////////////////////////////////////////////////////////////
	private List<AiBlock> getRangeBombBlock(AiTile tile, int range) throws StopRequestException{
		bt.checkInterruption();
		
		boolean willBurn;//bizim rangeimizdeki duvar patlayacak mý?
		List<AiTile> myBombRange = new ArrayList<AiTile>();
		List<AiBlock> inRangeWalls = new ArrayList<AiBlock>();//rangeimdeki PATLAMAYACAK duvarlarý alýr 
		
		myBombRange = getBombRangeList(tile, range);
		willBurnWallsList = getWillBurnWalls();
		
		for(AiTile r : myBombRange){
			bt.checkInterruption();
			//Listede block var mý
			if(r.getBlocks().size() != 0){
				//Bu block kýrýlabilir mi
				if(r.getBlocks().get(0).isDestructible()){
					//bu duvar zaten patlayacak mý
					willBurn = false;
					for(AiBlock w : willBurnWallsList){
						bt.checkInterruption();
						if(r == w.getTile()){
							willBurn = true;
						}
					}
					//Patlamayacaksa biz patlatalým (bence)
					if(willBurn == false){
						inRangeWalls.add(r.getBlocks().get(0));
					}
				}
			}
		}
		return inRangeWalls;
	}
	
	//////////////////////////////////////////////////
	//range'imdeki itemlarý hesaplar. LÝSTEYE ATAR  //
	//////////////////////////////////////////////////
	private List<AiItem> getRangeBombItem(AiTile tile, int range) throws StopRequestException{
		bt.checkInterruption();
		boolean willBurn; //bizim rangeimizdeki item patlayacak mý?
		
		List<AiTile> myBombRange = new ArrayList<AiTile>();
		List<AiItem> inRangeItems = new ArrayList<AiItem>();//rangeimdeki PATLAMAYACAK bonuslarý alýr 
		
		myBombRange = getBombRangeList(tile, range);
		willBurnItemsList = getWillBurnItems();
		
		for(AiTile r : myBombRange){
			bt.checkInterruption();
			//Listede item var mý
			if(r.getItems().size() != 0){
				//bu item zaten patlayacak mý
				willBurn = false;
				for(AiItem i : willBurnItemsList){
					bt.checkInterruption();
					if(r == i.getTile()){
						willBurn = true;
					}
				}
				//Patlamayacaksa biz alalým (bence)
				if(willBurn == false){
					inRangeItems.add(r.getItems().get(0));
				}
			}
		}
		return inRangeItems;
	}
	
	//////////////////////////////////////////////////
	//range'imdeki herolarý hesaplar. LÝSTEYE ATAR  //
	//////////////////////////////////////////////////
	private List<AiHero> getRangeBombHero(AiTile tile, int range) throws StopRequestException{
		bt.checkInterruption();
		
		List<AiTile> myBombRange = new ArrayList<AiTile>();
		List<AiHero> inRangeHeroes = new ArrayList<AiHero>();//rangeimdeki düþmanlarý alýr
		
		myBombRange = getBombRangeList(tile, range);
		
		for(AiTile r : myBombRange){
			bt.checkInterruption();
			//Listede hero var mý
			if(r.getHeroes().size() != 0){
				//bu hero biz miyiz
				if(r.getHeroes().get(0)!=deepPurple){
					inRangeHeroes.add(r.getHeroes().get(0));
				}
			}
		}
		return inRangeHeroes;
	}
	
	/////////////////////////////////////////////////////
	//Rangeimde kaç tane PATLAMAYACAK kýrýlýr duvar var//
	/////////////////////////////////////////////////////
	public int getRangeBombBlockCounter(AiTile tile, int range) throws StopRequestException{
		bt.checkInterruption();
		return getRangeBombBlock(tile, range).size();
	}
	/////////////////////////////////////////////
	//Rangeimde kaç tane PATLAMAYACAK bonus var//
	/////////////////////////////////////////////
	public int getRangeBombItemCounter(AiTile tile, int range) throws StopRequestException{
		bt.checkInterruption();
		return getRangeBombItem(tile, range).size();
	}
	
	/////////////////////////////////
	//Rangeimde kaç tane düþman var//
	/////////////////////////////////
	public int getRangeBombHeroCounter(AiTile tile, int range) throws StopRequestException{
		bt.checkInterruption();
		return getRangeBombHero(tile, range).size();
	}
	
	////////////////////////////////////
	//Bonusu Patlatak mý Bro          //
	////////////////////////////////////
	public boolean destroyBonus(AiTile tile, int range)throws StopRequestException{
		bt.checkInterruption();
		boolean destroy = false;//Patlatak mý?(þimdilik: yok abi, günah nimete)
		int disOwn, disEnemy;
		
		heroesList = zone.getHeroes();
		
		Iterator<AiHero> itHeroes;
		AiHero hero;
		
		Iterator<AiItem> itBonus = getRangeBombItem(tile, range).iterator();;
		AiItem bonus;
		
		while(itBonus.hasNext() && destroy==false){
			bt.checkInterruption();
			bonus = itBonus.next();
			itHeroes = heroesList.iterator();
			while(itHeroes.hasNext() && destroy==false){
				bt.checkInterruption();
				hero = itHeroes.next();		
				//Biz daha uzaksak bomba koymalý
				if(hero!=deepPurple){
					disOwn = zone.getTileDistance(deepPurple.getTile(), bonus.getTile());
					disEnemy = zone.getTileDistance(hero.getTile(), bonus.getTile());
					if(disEnemy <= disOwn){
						destroy = true;//PATLATAK
					}
				}
			}
		}
		return destroy;
	}
}