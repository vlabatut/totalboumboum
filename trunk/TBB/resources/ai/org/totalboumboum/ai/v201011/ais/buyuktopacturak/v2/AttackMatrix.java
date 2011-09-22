package org.totalboumboum.ai.v201011.ais.buyuktopacturak.v2;

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

/**
 * @author Onur Büyüktopaç
 * @author Yiğit Turak
 */
public class AttackMatrix{
	private BuyuktopacTurak bt;
	private AiZone zone;		
	private AiHero ownHero;
	private Bonus myBonus;
	private List<AiTile> safesList;
	private List<AiItem> itemsList;
	private List<AiTile> bonusList; 
	private List<AiBlock> wallsList;
	private List<AiTile> rivalsList;
	private List<AiBomb> bombsList;
	private List<AiHero> heroesList;
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
		
		heroesList = new ArrayList<AiHero>();
		rivalsList = new ArrayList<AiTile>();
		itemsList = new ArrayList<AiItem>();
		bonusList = new ArrayList<AiTile>();
		safesList = new ArrayList<AiTile>();
		wallsList = new ArrayList<AiBlock>();
		bombsList = new ArrayList<AiBomb>();
	}
	
	public void createMatrix()throws StopRequestException{
		init();
		putFree();	//ncelikle her yer Free olarak doldurulur.
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
		Iterator<AiBlock> itWalls = wallsList.iterator();
		AiBlock wall;
		myBonus=new Bonus(bt);
		int dist;
		List<AiTile> wallRange =  new ArrayList<AiTile>();
		//Duvarlari sira ile lsiteden cagiriyor.
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
					dist = bt.getDistance(ownHero.getTile(), tileRangeNext);
					if(this.myBonus.destroyBonus())
						matrix[line][col] += ((Constant.DESTRUCTIBLE)/(dist+1))*calculIsRunnable(tileRangeNext);

				}
			}
			//patlatilamaz ise
			else{
				matrix[line][col] = Constant.INDESTRUCTIBLE;
			}
		}
	}

	//Distance vs. hesaplarn yaptr. if-else iinde
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
			disOwn = bt.getDistance(ownHero.getTile(), item.getTile());
			
			matrix[line][col] += ((Constant.BONUS+(min-disOwn)*2)/(disOwn+1));

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
			
			List<AiTile> blasts = bomb.getBlast();;
			Iterator<AiTile> itBlasts = blasts.iterator();
			AiTile blast;
			//zaman hesaplamalarn yaptr.
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
		
		heroesList = zone.getHeroes();
		Iterator<AiHero> itHeroes = heroesList.iterator();
		AiHero hero;
		Iterator<AiTile> itHeroRange;
		AiTile tileRangeNext;
		
		int distOwn, distAdv;
		List<AiTile> heroRange = new ArrayList<AiTile>();
		while(itHeroes.hasNext()){
			bt.checkInterruption();
			hero = itHeroes.next();
			
			if(hero!=ownHero){
				for(Direction dir : dirTable){
					heroRange.addAll(getONeighbour(hero.getTile(), dir, ownHero.getBombRange()));
				}
			}
			
			itHeroRange = heroRange.iterator();
			
			while(itHeroRange.hasNext()){
				bt.checkInterruption();
				tileRangeNext = itHeroRange.next();
				col = tileRangeNext.getCol();
				line = tileRangeNext.getLine();
				distOwn = bt.getDistance(ownHero.getTile(), tileRangeNext);
				distAdv = bt.getDistance(hero.getTile(), tileRangeNext);

				matrix[line][col] += (Constant.RIVALATTACK/(distOwn*distAdv+1))*calculIsRunnable(tileRangeNext);
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
	public int calculIsRunnable(AiTile tile) throws StopRequestException
	{
        bt.checkInterruption(); 
        int safe = 0;
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
  }
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
			tempMin = bt.getDistance(t,tile);
			if(min > tempMin){
				min = tempMin;
			}
		}
		return min;
	}
}