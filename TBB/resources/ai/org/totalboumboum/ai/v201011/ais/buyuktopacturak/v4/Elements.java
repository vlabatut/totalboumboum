package org.totalboumboum.ai.v201011.ais.buyuktopacturak.v4;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiItem;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;

public class Elements {
	private BuyuktopacTurak bt;
	private PerfectStrangers ps;
	private AiZone zone;		
	private AiHero deepPurple;  
	private List<AiHero> heroesList;//tous les heros
	private List<AiBlock> willBurnWallsList;//Les murs qui vont exploser
	private List<AiItem> willBurnItemsList;//Les items qui vont exploser

	
	/**
	 * C�est le constructeur qui obtient des percepts dans la classe BuyuktopacTurak.
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
	 * On obtient notre h�ro et on crée l�objet PerfectStrangers.
	 * @throws StopRequestException
	 */
	private void init()throws StopRequestException{
		bt.checkInterruption();
		this.deepPurple = zone.getOwnHero();
		this.ps=new PerfectStrangers(this.bt);
	
	}

	/**
	 * On renvoie la liste des murs destructibles qui vont exploser.
	 * @return
	 * @throws StopRequestException
	 */
	private List<AiBlock> getWillBurnWalls()throws StopRequestException{
		bt.checkInterruption();
		
		willBurnWallsList = new ArrayList<AiBlock>();
		List<AiTile> allRangeList;//Les portees de toutes les bombes.
		allRangeList = ps.getAllRangeList();
		Iterator<AiTile> itRange = allRangeList.iterator();
		AiTile range;
		
		while(itRange.hasNext()){
			bt.checkInterruption();
			range = itRange.next();
			//S'il y a d'element 
			if(range.getBlocks().size() != 0){
				//Si le mur destructible
				if(range.getBlocks().get(0).isDestructible()){
					willBurnWallsList.add(range.getBlocks().get(0));
				} 
			} 
		}
		return willBurnWallsList; 
	}
	
	/**
	 * On renvoie la liste des bonus qui vont exploser.
	 * @return
	 * @throws StopRequestException
	 */
	private List<AiItem> getWillBurnItems()throws StopRequestException{
		bt.checkInterruption();
		
		willBurnItemsList = new ArrayList<AiItem>();
		List<AiTile> allRangeList;//Les portees de toutes les bombes.
		allRangeList = ps.getAllRangeList();
		
		Iterator<AiTile> itRange = allRangeList.iterator();
		AiTile range;
		
		while(itRange.hasNext()){
			bt.checkInterruption();
			range = itRange.next();
			//S'il y a d'element 
			if(range.getItems().size() != 0){
				willBurnItemsList.addAll(range.getItems());
			} 
		}
		return willBurnItemsList; 
	}
	
	/**
	 * On renvoie la liste des murs destructibles qui ne vont pas d�exploser 
	 * dans notre port�e virtuelle.
	 * @param tile
	 * @param range
	 * @return
	 * @throws StopRequestException
	 */
	private List<AiBlock> getRangeBombBlock(AiTile tile, int range) throws StopRequestException{
		bt.checkInterruption();
		
		boolean willBurn;//le mur dans notre portee va exploser 
		List<AiTile> myBombRange = new ArrayList<AiTile>();
		List<AiBlock> inRangeWalls = new ArrayList<AiBlock>();//tenir les murs qui ne vont pas exploser dans ma portee 
		
		myBombRange = ps.getBombRangeList(tile, range);
		willBurnWallsList = getWillBurnWalls();
		
		for(AiTile r : myBombRange){
			bt.checkInterruption();
			//S'il y a des blocks
			if(r.getBlocks().size() != 0){
				//Si le mur destructible
				if(r.getBlocks().get(0).isDestructible()){
					//Si le mur va deja exploser
					willBurn = false;
					for(AiBlock w : willBurnWallsList){
						bt.checkInterruption();
						if(r == w.getTile()){
							willBurn = true;
						}
					}
					//Patlamayacaksa biz patlatal�m (bence)
					if(willBurn == false){
						inRangeWalls.add(r.getBlocks().get(0));
					}
				}
			}
		}
		return inRangeWalls;
	}
	
	/**
	 * On renvoie la liste des bonus qui ne vont pas d�exploser dans notre port�e virtuelle.
	 * @param tile
	 * @param range
	 * @return
	 * @throws StopRequestException
	 */
	private List<AiItem> getRangeBombItem(AiTile tile, int range) throws StopRequestException{
		bt.checkInterruption();
		boolean willBurn; //l'item dans notre portee va exploser 
		
		List<AiTile> myBombRange = new ArrayList<AiTile>();
		List<AiItem> inRangeItems = new ArrayList<AiItem>();//tenir les items qui ne vont pas exploser dans ma portee 
		
		myBombRange = ps.getBombRangeList(tile, range);
		willBurnItemsList = getWillBurnItems();
		
		for(AiTile r : myBombRange){
			bt.checkInterruption();
			//S'il y a des items
			if(r.getItems().size() != 0){
				//Si le mur va deja exploser
				willBurn = false;
				for(AiItem i : willBurnItemsList){
					bt.checkInterruption();
					if(r == i.getTile()){
						willBurn = true;
					}
				}

				if(willBurn == false){
					inRangeItems.add(r.getItems().get(0));
				}
			}
		}
		return inRangeItems;
	}
	
	/**
	 * On renvoie la liste des adversaires qui sont dans notre port�e virtuelle.
	 * @param tile
	 * @param range
	 * @return
	 * @throws StopRequestException
	 */
	private List<AiHero> getRangeBombHero(AiTile tile, int range) throws StopRequestException{
		bt.checkInterruption();
		
		List<AiTile> myBombRange = new ArrayList<AiTile>();
		List<AiHero> inRangeHeroes = new ArrayList<AiHero>();//rangeimdeki d��manlar� al�r
		
		myBombRange = ps.getBombRangeList(tile, range);
		
		for(AiTile r : myBombRange){
			bt.checkInterruption();
			//S'il y a des heros
			if(r.getHeroes().size() != 0){
				if(r.getHeroes().get(0)!=deepPurple){
					inRangeHeroes.add(r.getHeroes().get(0));
				}
			}
		}
		return inRangeHeroes;
	}
	
	/**
	 * On renvoie la taille de la liste des murs destructibles qui ne vont pas d�exploser dans notre port�e virtuelle.
	 * @param tile
	 * @param range
	 * @return
	 * @throws StopRequestException
	 */
	public int getRangeBombBlockCounter(AiTile tile, int range) throws StopRequestException{
		bt.checkInterruption();
		return getRangeBombBlock(tile, range).size();
	}

	/**
	 * On renvoie la taille de la liste des bonus qui ne vont pas d�exploser dans notre port�e virtuelle.
	 * @param tile
	 * @param range
	 * @return
	 * @throws StopRequestException
	 */
	public int getRangeBombItemCounter(AiTile tile, int range) throws StopRequestException{
		bt.checkInterruption();
		return getRangeBombItem(tile, range).size();
	}
	
	/**
	 * On renvoie la taille de la liste des adversaires qui sont dans notre port�e virtuelle.
	 * @param tile
	 * @param range
	 * @return
	 * @throws StopRequestException
	 */
	public int getRangeBombHeroCounter(AiTile tile, int range) throws StopRequestException{
		bt.checkInterruption();
		return getRangeBombHero(tile, range).size();
	}
	
	/**
	 * On contient la control de la distance au bonus entre l�ennemie plus proche et notre h�ro.
	 * @param tile
	 * @param range
	 * @return
	 * @throws StopRequestException
	 */
	public boolean destroyBonus(AiTile tile, int range)throws StopRequestException{
		bt.checkInterruption();
		boolean destroy = false;
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
				
				if(hero!=deepPurple){
					disOwn = zone.getTileDistance(deepPurple.getTile(), bonus.getTile());
					disEnemy = zone.getTileDistance(hero.getTile(), bonus.getTile());
					if(disEnemy <= disOwn){
						destroy = true;
					}
				}
			}
		}
		return destroy;
	}
}