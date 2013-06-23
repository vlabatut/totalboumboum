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

public class AttackMatrix{
	private BuyuktopacTurak bt;
	private AiZone zone;		
	private AiHero ownHero;
	private List<AiTile> safesList;
	private List<AiItem> itemsList;
	private List<AiTile> bonusList; 
	private List<AiBlock> wallsList;
	private List<AiTile> rivalsList;
	private List<AiBomb> bombsList;
	private List<AiHero> heroesList;
	
	private List<AiBlock> willBurnWallsList;//patlayacak duvarlar
	
	private int col;
	private int line;	
	private double[][] matrix; 
	private int width;
	private int heigh;
	public Direction[] dirTable = {Direction.DOWN, Direction.RIGHT, Direction.UP, Direction.LEFT};
	
	public AttackMatrix(BuyuktopacTurak bt) throws StopRequestException{	
		bt.checkInterruption();
		this.zone = bt.getPercepts();
		this.bt = bt;
		init();
	}
	private void init()throws StopRequestException{
		bt.checkInterruption();
		width = zone.getWidth(); 
		heigh = zone.getHeight();
		ownHero = zone.getOwnHero();
		matrix = new double[heigh][width];
		
		willBurnWallsList = new ArrayList<AiBlock>();
		
		heroesList = new ArrayList<AiHero>();
		rivalsList = new ArrayList<AiTile>();
		itemsList = new ArrayList<AiItem>();
		bonusList = new ArrayList<AiTile>();
		safesList = new ArrayList<AiTile>();
		wallsList = new ArrayList<AiBlock>();
		bombsList = new ArrayList<AiBomb>();
		heroesList = zone.getHeroes();
		heroesList.remove(ownHero);
	}
	
	public void createMatrix()throws StopRequestException{
		init();
		putFree();	//öncelikle her yer Free olarak doldurulur.
		putWalls();
		putHero();
		putBonus();
		putBomb();
		putBlast(bombsList);
	}
	
	//her yeri Free olarak dolduruyor.
	private void putFree()throws StopRequestException{
		for (line = 0; line < zone.getHeight(); line++) {
			bt.checkInterruption();
			for (col = 0; col < zone.getWidth(); col++) {
				bt.checkInterruption();
				matrix[line][col] = Constant.FREE;
			}
	   	}
	}
	
	private void putWalls() throws StopRequestException{
		bt.checkInterruption();
		wallsList = zone.getBlocks();
		
		willBurnWallsList = getWillBurnWalls();
		wallsList.removeAll(willBurnWallsList);
		
		Iterator<AiBlock> itWalls = wallsList.iterator();
		AiBlock wall;
		int dist;
		boolean chemin=false;
		List<AiTile> wallRange =  new ArrayList<AiTile>();
		//Duvarlari sira ile lsiteden cagiriyor.
		for(AiHero h:heroesList){
			if(bt.AstarDistance(ownHero.getTile(),h.getTile())!=-1)
				chemin=true;
		}
		if(!chemin){
			while(itWalls.hasNext()){
				bt.checkInterruption();
				wall = itWalls.next();
				col = wall.getCol();
				line = wall.getLine();
				//patalatilabilir ise alanini hesapla matrise ekle
				if(wall.isDestructible()){
					matrix[line][col] = Constant.DESTMUR;
					for (Direction dir : dirTable){
						wallRange.addAll(getONeighbour(wall.getTile(), dir, ownHero.getBombRange()));
					}
					Iterator<AiTile> itWallRange = wallRange.iterator();
					AiTile tileRangeNext;
			
					while(itWallRange.hasNext()){
						bt.checkInterruption();
						tileRangeNext = itWallRange.next();
						col = tileRangeNext.getCol();
						line = tileRangeNext.getLine();
						dist = bt.AstarDistance(ownHero.getTile(),tileRangeNext);
						if(dist!=-1)
							matrix[line][col] += ((Constant.DESTRUCTIBLE)/(dist+1));
						else
							matrix[line][col]=0;
					}
				}
				//patlatilamaz ise
				else{
					matrix[line][col] = Constant.INDESTRUCTIBLE;
				}
			}
		}
		else{
			while(itWalls.hasNext()){
				bt.checkInterruption();
				wall = itWalls.next();
				col = wall.getCol();
				line = wall.getLine();
				//patalatilabilir ise alanini hesapla matrise ekle
				if(wall.isDestructible()){
					matrix[line][col] = Constant.DESTMUR;
				}
				else{
					matrix[line][col] = Constant.INDESTRUCTIBLE;
				}
			}
		}
	}

	//Distance vs. hesaplarýný yaptýr. if-else içinde
	private void putBonus() throws StopRequestException{
		bt.checkInterruption();
		itemsList = zone.getItems();
		Iterator<AiItem> itItems = itemsList.iterator();
		AiItem item;
		int min,disOwn;
		while(itItems.hasNext()){
			bt.checkInterruption();
			item = itItems.next();
			col = item.getCol();
			line = item.getLine();
			min = getMinDistance(heroesList,item.getTile());
			disOwn = bt.AstarDistance(ownHero.getTile(),item.getTile());
			if(disOwn!=-1)
				matrix[line][col] += ((Constant.BONUS+(min-disOwn)*2)/(disOwn+1));
			else
				matrix[line][col] = 0;
			bonusList.add(item.getTile());
		}
	}
	
	//Listeyi doldurmak icin kullaniliyor. Asil isi putBlast yapiyor.
	private void putBomb() throws StopRequestException{
		bt.checkInterruption();
		bombsList = zone.getBombs();
	}
	
	private void putBlast(List<AiBomb> bombs) throws StopRequestException{
		bt.checkInterruption();
		
		Iterator<AiBomb> itBombs = bombs.iterator();
		AiBomb bomb;
		double time;
		while(itBombs.hasNext()){
			bt.checkInterruption();
			bomb = itBombs.next();
			
			List<AiTile> blasts = bomb.getBlast();
			blasts.add(bomb.getTile());
			matrix[bomb.getTile().getLine()][bomb.getTile().getCol()]=0;
			Iterator<AiTile> itBlasts = blasts.iterator();
			AiTile blast;
			//zaman hesaplamalarýný yaptýr.
			while(itBlasts.hasNext()){
				blast = itBlasts.next();
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
	
	//distance vs hesapla.
	private void putHero() throws StopRequestException{
		bt.checkInterruption();

		Iterator<AiHero> itHeroes = heroesList.iterator();
		AiHero hero;
		Iterator<AiTile> itHeroRange;
		AiTile tileRangeNext;
		
		int distOwn, distAdv;
		List<AiTile> heroRange = new ArrayList<AiTile>();
		while(itHeroes.hasNext()){
			bt.checkInterruption();
			hero = itHeroes.next();

			for(Direction dir : dirTable){
				heroRange.addAll(getONeighbour(hero.getTile(), dir, ownHero.getBombRange()));
			}

			itHeroRange = heroRange.iterator();
			
			while(itHeroRange.hasNext()){
				bt.checkInterruption();
				tileRangeNext = itHeroRange.next();
				col = tileRangeNext.getCol();
				line = tileRangeNext.getLine();
				distOwn = bt.AstarDistance(ownHero.getTile(),tileRangeNext);
				distAdv = zone.getTileDistance(hero.getTile(), tileRangeNext);
				if(distOwn!=-1)
					matrix[line][col] += (Constant.RIVALATTACK/(distOwn*distAdv+1));
				else
					matrix[line][col] = 0;
					
			}
		}
	}
	/**
	 * ->Bomba koyulacak karenin 2lik + sini bir listeye ekliyor
	 * ->Bomba koyulacak karenin gercek patlama alanini baska bir listeye ekliyor
	 * ->2 karelik listenin komsularini alir alirken gercek alanin icinde mi diye kontrol eder 
	 * alanin disindaysa komsu listesine ekler.
	 * ->Bomba alaninin disindaki komsulardan olusan listeyi kontrol eder
	 * ->AMA CALISMIYOR
	 * @param tile
	 * @return
	 * @throws StopRequestException
	 */
	/*private int calculIsRunnable(AiTile tile) throws StopRequestException
	{
        bt.checkInterruption(); 
        int safe = 1;
        boolean add;
        double tempMatVal;
        List<AiTile> rangeList = new ArrayList<AiTile>(); //yalnizca 2 karelik alani tarar
        List<AiTile> blastList = new ArrayList<AiTile>();//bombamizin toplam etki alani
        List<AiTile> neighbourList = new ArrayList<AiTile>();//hesaplanacak komsular
        Iterator<AiTile> itRange;
        Iterator<AiTile> itBlast;
        Iterator<AiTile> itNeighbour;
        AiTile bombTile = tile;
        AiTile tileRange;
        AiTile tileNeighbour;
        AiTile tileBlast; 
        //olasi bomba noktasinin 2lik '+' listeye aldi
        for (Direction dir : this.dirTable){
              rangeList.addAll(getONeighbour(bombTile, dir, 2));
        } 
        //bombamizin etki alanini tutan liste
        for (Direction dir : this.dirTable){
              blastList.addAll(getONeighbour(bombTile, dir, ownHero.getBombRange()));
        }
        blastList.add(bombTile); 
        itRange = rangeList.iterator();
        while(itRange.hasNext()){
              tileRange = itRange.next();
              for (Direction dir : this.dirTable){
                    tileNeighbour = tileRange.getNeighbor(dir); 
                    add = true;
                    itBlast = blastList.iterator();
                    while(itBlast.hasNext()){
                          tileBlast = itBlast.next();
                          if(tileBlast == tileNeighbour){
                                add = false;
                          }
                    }
                    if (add){
                          neighbourList.add(tileNeighbour);
                    }
              }
        } 
        itNeighbour = neighbourList.iterator();
        while(itNeighbour.hasNext() && safe == 0){
              tileNeighbour = itNeighbour.next();
              tempMatVal = matrix[tileNeighbour.getLine()][tileNeighbour.getCol()]; 
              if(tempMatVal >= 0){
                    safe = 1;
              }              
              else{
                    safe = 0;
              }
        }
        return safe;
  	}*/
	public List<AiTile> getONeighbour(AiTile tile, Direction dir, int range) throws StopRequestException{
		bt.checkInterruption();
		int i = 0;
		boolean crossable = true;
		List<AiTile> result = new ArrayList<AiTile>();
		AiTile tempTile;
		while(i < range && crossable){
			tempTile = tile.getNeighbor(dir);
			if(tempTile.isCrossableBy(ownHero)){
				result.add(tempTile);
				i++;
				tile = tempTile;
			}
			else
				crossable = false;
		}
		return result;
	}
	
	public double[][] getMatrix()throws StopRequestException{
		bt.checkInterruption();
		return matrix;
	}
	
	public List<AiHero> getHeroesList()throws StopRequestException{
		bt.checkInterruption();
		return heroesList;
	}

	public List<AiTile> getRivalsList()throws StopRequestException{
		bt.checkInterruption();
		return rivalsList;
	}
	public List<AiItem> getItemsList()throws StopRequestException{
		bt.checkInterruption();
		return itemsList;
	}

	public List<AiTile> getBonusList()throws StopRequestException{
		bt.checkInterruption();
		return bonusList;
	}
	
	public List<AiTile> getSafesList()throws StopRequestException{
		bt.checkInterruption();
		return safesList;
	}
	
	public List<AiBlock> getWallsList()throws StopRequestException{
		bt.checkInterruption();
		return wallsList;
	}

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
	
	//////////////////////////////
	//BURAYI ACELE EKLÝYORUM    //
	//////////////////////////////
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
				if(neighbourTile.isCrossableBy(ownHero) && crossableItem){
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
	private List<AiTile> getAllRangeList()throws StopRequestException{
		bt.checkInterruption();
		List<AiTile> allRangeList;//Tüm bombalarýn Alan listesi
		allRangeList = new ArrayList<AiTile>();
				
		Iterator<AiBomb> itBombs = this.bombsList.iterator(); 
		AiBomb bomb;
		
		while(itBombs.hasNext()){
			bt.checkInterruption();
			bomb = itBombs.next();
			allRangeList.addAll(getBombRangeList(bomb.getTile(), bomb.getRange()));
		}
		return allRangeList;
	}
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
}