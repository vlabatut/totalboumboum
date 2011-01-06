package org.totalboumboum.ai.v201011.ais.buyuktopacturak.v2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiItem;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;

public class Bonus {
	private BuyuktopacTurak bt;
	private AiZone zone;		
	private AiHero ownHero;
	private List<AiItem> bonusList;
	private List<AiItem> foundBonusList = new ArrayList<AiItem>();
	private List<AiItem> willBurnList;//patlayacak bonuslar
	private List<AiHero> heroesList;
	private List<AiTile> heroRange = new ArrayList<AiTile>();
	private List<AiBomb> bombList;
	private Direction[] dirTable = {Direction.DOWN, Direction.RIGHT, Direction.UP, Direction.LEFT};
	
	public Bonus(BuyuktopacTurak bt) throws StopRequestException{
		bt.checkInterruption();
		this.zone=bt.getPercepts();
		this.bt = bt;
		init();
	}
	private void init()throws StopRequestException{
		bt.checkInterruption();
		this.ownHero = zone.getOwnHero();
		getBonus();
		getBombs();
	}
	private void getBombs() throws StopRequestException{
		bt.checkInterruption();
		this.bombList = zone.getBombs();
	}
	private void getBonus() throws StopRequestException{
		bt.checkInterruption();
		this.bonusList = zone.getItems();
	}
	
	//HEP PATLAYACAK MI DÝYE KONTROL ETMENÝN KOLAY YOLUNU BUL
	//HEP PATLAYACAK MI KORKUSUYLA YAÞAMAK ÝSTEMÝYORUM!
	private void calculateWillBurnBonus() throws StopRequestException{
		bt.checkInterruption();
		
		willBurnList = new  ArrayList<AiItem>();
		Iterator<AiBomb> itBombs = this.bombList.iterator(); 
		AiBomb bomb;
		
		while(itBombs.hasNext()){
			bt.checkInterruption();
			bomb = itBombs.next();
			getWillBurn(bomb, bomb.getRange());
		}
		foundBonusList.removeAll(willBurnList);
	}
	
	private void getWillBurn(AiBomb bomb, int range) throws StopRequestException{
		bt.checkInterruption();
		int i;
		boolean crossable;
		//neighbourTile bombamýn range.i boyunca ilerlerken while döngüsünde aldýðým komþu tile
		//tempTile methoda giren tile parametresinin deðerini korumak için (for döngüsünün baþýnda)
		AiTile neighbourTile, tempTile=bomb.getTile(), tile=bomb.getTile(); 
		
		AiItem bonus;
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
				if(neighbourTile.isCrossableBy(bomb)){
					i++;
					tile = neighbourTile;
				}
				else{
					crossable = false;
					//her while döngüsüne girmeden önce duvar listesini iterator.a yeniden yüklemek gerekiyor.
					Iterator<AiItem> itBonus = this.foundBonusList.iterator();
					while(itBonus.hasNext() && control==0){
						bt.checkInterruption();
						bonus = itBonus.next();
						if(bonus.getTile()==neighbourTile){
							willBurnList.add(bonus);
							control=1;
						}
					}
				}
			}
		}
	}	
	
	//LÝSTEYE AYNI ELEMANI 3 KERE ALIYOR
	public boolean findBonus() throws StopRequestException{
		bt.checkInterruption();
		foundBonusList = new ArrayList<AiItem>();
		boolean hasBonus = false;
		
		for(Direction dir:dirTable){
			heroRange = new ArrayList<AiTile>();
			heroRange.addAll(getONeighbour(ownHero.getTile(), dir, ownHero.getBombRange()));
			
			Iterator<AiTile> itRange = heroRange.iterator();
			AiTile range;
			
			hasBonus = false;
			while(itRange.hasNext()&& hasBonus == false){
				bt.checkInterruption();
				range = itRange.next();
				
				Iterator<AiItem> itBonus = bonusList.iterator();
				AiItem bonus;
				while(itBonus.hasNext()&& hasBonus == false){
					bt.checkInterruption();
					bonus = itBonus.next();
					if(range == bonus.getTile()){
						foundBonusList.add(bonus);
						hasBonus = true;
					}
				}
			}
		}
		return hasBonus;
	}
	
	//ADAM RESMEN ÝNÝT GÝBÝ OLDU HER ÞEYÝ TANIMLAMAK GEREKÝYOR
	//BAÞINDA BONUS VE BOMBALARI LÝSTEYE ATMASI LAZIM
	public boolean destroyBonus()throws StopRequestException{
		getBonus();
		getBombs();
		bt.checkInterruption();
		boolean destroy = false;
		//BURADA FIND BONUS DÜZGÜNLEÞTÝRMEM LAZIM BOOL DÖNDÜRMEMELÝ
		//findBonus()'u çalýþtýrabilmek için gereksiz boolean yaratýyoruz.
		@SuppressWarnings("unused")
		boolean salla = findBonus();
		calculateWillBurnBonus();
		int disOwn, disEnemy;
		heroesList = zone.getHeroes();
		Iterator<AiHero> itHeroes;
		AiHero hero;
		Iterator<AiItem> itBonus = foundBonusList.iterator();;
		AiItem bonus;
		while(itBonus.hasNext() && destroy==false){
			bonus = itBonus.next();
			itHeroes = heroesList.iterator();
			while(itHeroes.hasNext() && destroy==false){
				bt.checkInterruption();
				hero = itHeroes.next();		
				//Biz daha uzaksak bomba koymalý
				if(hero!=ownHero){
					disOwn = bt.getDistance(ownHero.getTile(), bonus.getTile());
					disEnemy = bt.getDistance(hero.getTile(), bonus.getTile());
					if(disEnemy <= disOwn){
						destroy = true;
					}
				}
			}
		}
		return destroy;
	}

	private List<AiTile> getONeighbour(AiTile tile, Direction dir, int range) throws StopRequestException{
		bt.checkInterruption();
		int i = 0;
		boolean crossable = true;
		List<AiTile> resultList = new ArrayList<AiTile>();
		AiTile tempTile;
		while(i < range && crossable){
			tempTile = tile.getNeighbor(dir);
			if(tempTile.isCrossableBy(ownHero)){
				resultList.add(tempTile);
				i++;
				tile = tempTile;
			}
			else
				crossable = false;
		}
		return resultList;
	}
}
